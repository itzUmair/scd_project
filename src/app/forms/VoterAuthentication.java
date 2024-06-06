package app.forms;

import CustomExceptions.NoUserFoundException;
import CustomExceptions.VoteAlreadyCastedException;
import app.database.DatabaseAccess;
import app.models.Voter;
import app.dashboard.VoterDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.SQLException;

public class VoterAuthentication extends JFrame {
    public VoterAuthentication() {
        JFrame voterAuthForm = new JFrame();
        voterAuthForm.setTitle("E-Voting System | Login");
        voterAuthForm.setSize(480, 400);
        voterAuthForm.setVisible(true);

        voterAuthForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel voterAuthFormPanel = generateForm(voterAuthForm);
        voterAuthForm.add(voterAuthFormPanel);

    }

    private JPanel generateForm(JFrame mainFrame) {
        JPanel voterAuthFormPanel = new JPanel();
        voterAuthFormPanel.setLayout(new BoxLayout(voterAuthFormPanel, BoxLayout.Y_AXIS));
        voterAuthFormPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel cnicLabel = new JLabel("Enter CNIC (14 digits):");
        JTextField cnicField = new JTextField();
        cnicField.setMaximumSize(new Dimension(400, cnicField.getPreferredSize().height));

        JLabel pinLabel = new JLabel("Enter 6-digit Pin:");
        JPasswordField pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(400, cnicField.getPreferredSize().height));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cnic = cnicField.getText();
                String pin = new String(pinField.getPassword());

                String result = validateForm(cnic, pin);
                if (result.equals("valid")) {
                    try {
                        Voter voter = DatabaseAccess.getVoterByCredentials(cnic, pin);
                        assert voter != null;
                        new VoterDashboard(voter);
                        mainFrame.dispose();
                    } catch (SQLException sqlError) {
                        sqlError.printStackTrace();
                    } catch (NoUserFoundException | VoteAlreadyCastedException error) {
                        JOptionPane.showMessageDialog(mainFrame, error.getMessage());
                    }

                } else {
                    JOptionPane.showMessageDialog(mainFrame, result);
                }
            }
        });

        voterAuthFormPanel.add(cnicLabel);
        voterAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        voterAuthFormPanel.add(cnicField);
        voterAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        voterAuthFormPanel.add(pinLabel);
        voterAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        voterAuthFormPanel.add(pinField);
        voterAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        voterAuthFormPanel.add(submitButton);

        return voterAuthFormPanel;
    }

    private String validateForm(String cnic, String pin) {
        if (!cnic.matches("\\d+") ) {
            return "CNIC should only contain digits";
        }

        if (cnic.length() != 14) {
            return "CNIC should contain 14 digits";
        }

        if (!pin.matches("\\d+")) {
            return "PIN should only contain digits";
        }

        if (pin.length() != 6) {
            return "PIN should contain 6 digits";
        }

        return "valid";
    }
}
