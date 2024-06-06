package app.dashboard;

import app.database.DatabaseAccess;
import app.models.PartyCandidate;
import app.models.Voter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.List;

public class VoterDashboard extends JFrame {
    private static Voter currentVoter;
    public VoterDashboard(Voter voter) {
        JFrame voterDashboard = new JFrame();
        voterDashboard.setTitle("E-Voting System | Voter Dashboard");
        voterDashboard.setVisible(true);
        voterDashboard.setSize(1080, 720);
        voterDashboard.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JLabel screenTitle = new JLabel("Voter's Dashboard", JLabel.LEFT);
        JLabel screenSubTitle = new JLabel("Cast your vote", JLabel.LEFT);
        screenTitle.setFont(new Font("Verdana", Font.BOLD, 32));
        screenSubTitle.setFont(new Font("Verdana", Font.ITALIC, 16));
        voterDashboard.add(screenTitle, BorderLayout.NORTH);
        voterDashboard.add(screenSubTitle, BorderLayout.NORTH);

        currentVoter = voter;

        renderAllCandidates(voterDashboard);
    }

    private static JPanel generateCandidateWidget(String cnic, String name, String logo, String partyName, JFrame mainFrame) {
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font("Verdana", Font.BOLD, 14));
        JLabel logoLabel = new JLabel(logo);
        logoLabel.setFont(new Font("Verdana", Font.BOLD, 20));
        JLabel partyNameLabel = new JLabel(partyName);
        JButton voteCandidateButton = new JButton("Vote");
        voteCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int response = JOptionPane.showConfirmDialog(
                            mainFrame,
                            "Are you sure you want to vote for " + name + "?",
                            "Confirm Vote",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE
                    );
                    if (response == JOptionPane.YES_OPTION) {
                        String result = DatabaseAccess.setVote(cnic, currentVoter.getCnic());
                        JOptionPane.showMessageDialog(mainFrame, result);
                        mainFrame.dispose();
                    } else {
                        System.out.println("Vote canceled");
                    }

                } catch (SQLException ex) {
                    JOptionPane.showMessageDialog(mainFrame, ex.getMessage());
                }
            }
        });

        JPanel logoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        logoPanel.setPreferredSize(new Dimension(100, 50));
        logoPanel.setMaximumSize(new Dimension(100, 50));
        logoPanel.add(logoLabel);

        JPanel namePartyPanel = new JPanel();
        namePartyPanel.setLayout(new BoxLayout(namePartyPanel, BoxLayout.Y_AXIS));
        namePartyPanel.add(nameLabel);
        namePartyPanel.add(partyNameLabel);

        JPanel voteButtonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        voteButtonPanel.add(voteCandidateButton);

        JPanel candidateWidget = new JPanel(new BorderLayout());
        candidateWidget.add(logoPanel, BorderLayout.WEST);
        candidateWidget.add(namePartyPanel, BorderLayout.CENTER);
        candidateWidget.add(voteButtonPanel, BorderLayout.EAST);

        candidateWidget.setPreferredSize(new Dimension(400, 50));
        candidateWidget.setMaximumSize(new Dimension(400, 50));
        candidateWidget.setBorder(BorderFactory.createLineBorder(Color.BLACK));

        return candidateWidget;
    }

    public static void renderAllCandidates(JFrame mainFrame) {
        try {
            JPanel candidatesPanel = new JPanel();
            candidatesPanel.setLayout(new GridLayout(0, 2));

            List<PartyCandidate> allCandidates = DatabaseAccess.getAllCandidates();

            for (int i = 0; i < allCandidates.size(); i++) {
                JPanel candidateWidget = generateCandidateWidget(
                        allCandidates.get(i).getCnic(),
                        allCandidates.get(i).getName(),
                        allCandidates.get(i).getLogo(),
                        allCandidates.get(i).getPartyName(),
                        mainFrame
                );
                candidatesPanel.add(candidateWidget);
            }
            JScrollPane scrollableCandidatePanel = new JScrollPane(candidatesPanel);
            scrollableCandidatePanel.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

            JScrollBar scrollBar = scrollableCandidatePanel.getVerticalScrollBar();
            scrollBar.setUnitIncrement(20);

            mainFrame.add(scrollableCandidatePanel, BorderLayout.CENTER);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
