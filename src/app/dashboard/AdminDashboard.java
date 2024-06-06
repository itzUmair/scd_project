package app.dashboard;

import app.forms.SearchCandidateForm;
import app.models.Admin;
import app.database.DatabaseAccess;
import app.models.PartyCandidate;
import app.forms.AddCandidateForm;
import app.forms.RemoveCandidateForm;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;
import java.text.DecimalFormat;

public class AdminDashboard extends JFrame {

    Timer timer;
    int REFRESH_TIME_IN_SECONDS = 5;
    private Admin currentAdmin;
    public AdminDashboard(Admin admin) {
        currentAdmin = admin;
        JFrame adminDashboard = new JFrame();
        adminDashboard.setTitle("E-Voting System | Admin Dashboard");
        adminDashboard.setVisible(true);
        adminDashboard.setSize(1080, 720);
        adminDashboard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel screenTitle = new JLabel("Admin Dashboard", JLabel.LEFT);
        screenTitle.setFont(new Font("Verdana", Font.BOLD, 32));
        adminDashboard.add(screenTitle, BorderLayout.NORTH);

//        initial load
        adminDashboard.setJMenuBar(generateMenuBar());
        adminDashboard.add(generateInfoPanel());
        adminDashboard.add(generateCandidateTable(), BorderLayout.SOUTH);

        timer = new Timer(REFRESH_TIME_IN_SECONDS * 1000, e -> {
//          Live reloading the dashboard
            adminDashboard.setJMenuBar(generateMenuBar());
            adminDashboard.add(generateInfoPanel());
            adminDashboard.add(generateCandidateTable(), BorderLayout.SOUTH);
        });
        timer.start();
    }

    private JMenuBar generateMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        JMenu candidatesMenu = new JMenu("Candidates");
        JMenuItem addCandidateItem = new JMenuItem("Add Candidate");
        JMenuItem removeCandidateItem = new JMenuItem("Remove Candidate");
        JMenuItem searchCandidate = new JMenuItem("Search Candidate");

        candidatesMenu.add(addCandidateItem);
        candidatesMenu.add(removeCandidateItem);
        candidatesMenu.add(searchCandidate);

        addCandidateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new AddCandidateForm();
            }
        });

        removeCandidateItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new RemoveCandidateForm();
            }
        });

        searchCandidate.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new SearchCandidateForm();
            }
        });

        menuBar.add(candidatesMenu);

        return menuBar;
    }

    private JPanel generateInfoPanel() {
        JPanel infoPanel = new JPanel();
        infoPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        DecimalFormat df = new DecimalFormat("#.##");

        String votesCasted = getVotesCasted();
        String votesRemainaing = getVotesRemaining();
        double votesCastingPercentage = (double) Integer.parseInt(votesCasted) / Integer.parseInt(votesRemainaing) * 100;

        JPanel votesCastedPanel = createInfoBox("Votes Casted", votesCasted);
        JPanel votesRemainingPanel = createInfoBox("Votes Remaining", votesRemainaing);
        JPanel castingPercentage = createInfoBox("Casting %", df.format(votesCastingPercentage) + "%");
//        JPanel topCandidatesPanel = createInfoBox("Top Candidates", getTopCandidates());

        infoPanel.add(votesCastedPanel);
        infoPanel.add(votesRemainingPanel);
        infoPanel.add(castingPercentage);

        return infoPanel;
    }

    private JPanel createInfoBox(String title, String content) {
        JPanel box = new JPanel();
        box.setLayout(new BorderLayout());
        box.setPreferredSize(new Dimension(150, 150));
        box.setSize(150, 150);
        box.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        JLabel titleLabel = new JLabel(title, JLabel.CENTER);
        titleLabel.setFont(new Font("Verdana", Font.BOLD, 16));
        box.add(titleLabel, BorderLayout.NORTH);

        JLabel contentLabel = new JLabel("<html>" + content.replaceAll("\n", "<br>") + "</html>");
        contentLabel.setVerticalAlignment(JLabel.CENTER);
        contentLabel.setHorizontalAlignment(JLabel.CENTER);
        contentLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        box.add(contentLabel, BorderLayout.CENTER);

        return box;
    }

    private JPanel generateCandidateTable() {
        JPanel tablePanel = new JPanel(new BorderLayout());

        List<PartyCandidate> topCandidates = null;
        try {
            topCandidates = DatabaseAccess.getTopCandidates();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        String[] columnNames = {"Name", "Party", "Logo", "Votes"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);
        if (topCandidates != null) {
            for (PartyCandidate candidate : topCandidates) {
                model.addRow(new Object[]{candidate.getName(), candidate.getPartyName(), candidate.getLogo(), candidate.getVotes()});
            }
        }

        JTable table = new JTable(model);
        tablePanel.add(new JScrollPane(table), BorderLayout.CENTER);

        return tablePanel;
    }


    private String getVotesCasted() {
        try {
            int votesCasted = DatabaseAccess.getVotesCasted();
            return String.valueOf(votesCasted);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching data";
        }
    }

    private String getVotesRemaining() {
        // Fetch votes remaining from the database
        try {
            int votesRemaining = DatabaseAccess.getVotesRemaining();
            return String.valueOf(votesRemaining);
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error fetching data";
        }
    }
}
