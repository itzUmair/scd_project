package app.dashboard;

import app.forms.AdminAuthentication;
import app.forms.VoterAuthentication;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class EVotingApp extends JFrame {
    public EVotingApp() {
//      modern UI styles
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception e) {
            e.printStackTrace();
        }

        JFrame mainWindow = new JFrame();
        mainWindow.setTitle("E-Voting System");
        mainWindow.setSize(1080, 720);
        mainWindow.setVisible(true);
        mainWindow.setLayout(new BorderLayout());
        mainWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

//       Title of the application
        JLabel appNameLabel = new JLabel("E-Voting System", JLabel.CENTER);
        appNameLabel.setFont(new Font("Verdana", Font.BOLD, 32));
        mainWindow.add(appNameLabel, BorderLayout.CENTER);

//      Home screen options
        JPanel homeScreenOptions = getjPanel();

        mainWindow.add(homeScreenOptions, BorderLayout.SOUTH);

    }

    private JPanel getjPanel() {
        JPanel homeScreenOptions = new JPanel();
        homeScreenOptions.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        homeScreenOptions.setSize(100, 40);

//       Option 1: for users who are voting
        JButton votersOptionButton = new JButton("I am a voter");
        votersOptionButton.setPreferredSize(new Dimension(200, 40));
        votersOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redirectToUserDashboard();
            }
        });


//       Option 2: for admins
        JButton adminOptionButton = new JButton("I am an admin");
        adminOptionButton.setPreferredSize(new Dimension(200, 40));
        adminOptionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redirectToAdminDashboard();
            }
        });

        homeScreenOptions.add(votersOptionButton);
        homeScreenOptions.add(adminOptionButton);

        return homeScreenOptions;
    }

    private void redirectToUserDashboard() {
        new VoterAuthentication();
    }

    private void redirectToAdminDashboard() {
        new AdminAuthentication();
    }
}
