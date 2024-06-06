package app.forms;

import CustomExceptions.NoUserFoundException;
import CustomExceptions.VoteAlreadyCastedException;
import app.models.Admin;
import app.database.DatabaseAccess;
import app.dashboard.AdminDashboard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class AdminAuthentication extends JFrame {
    public AdminAuthentication() {
        JFrame adminAuthForm = new JFrame();
        adminAuthForm.setTitle("E-Voting System | Login");
        adminAuthForm.setSize(480, 400);
        adminAuthForm.setVisible(true);

        adminAuthForm.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel adminAuthFormPanel = generateForm(adminAuthForm);
        adminAuthForm.add(adminAuthFormPanel);
    }

    private JPanel generateForm(JFrame mainFrame) {
        JPanel adminAuthFormPanel = new JPanel();

        JLabel formTitleLabel = new JLabel("Admin Login");
        formTitleLabel.setFont(new Font("Verdana", Font.BOLD, 32));

        adminAuthFormPanel.setLayout(new BoxLayout(adminAuthFormPanel, BoxLayout.Y_AXIS));
        adminAuthFormPanel.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel adminIDLabel = new JLabel("Enter Admin ID (6 digits):");
        JTextField adminIDField = new JTextField();
        adminIDField.setMaximumSize(new Dimension(400, adminIDField.getPreferredSize().height));

        JLabel pinLabel = new JLabel("Enter 6-digit Pin:");
        JPasswordField pinField = new JPasswordField();
        pinField.setMaximumSize(new Dimension(400, pinField.getPreferredSize().height));

        JButton submitButton = new JButton("Submit");
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String adminID = adminIDField.getText();
                String pin = new String(pinField.getPassword());

                String result = validateForm(adminID, pin);
                if (result.equals("valid")) {
                    try {
                        Admin admin = DatabaseAccess.getAdminByCredentials(adminID, pin);
                        assert admin != null;
                        new AdminDashboard(admin);
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
        adminAuthFormPanel.add(formTitleLabel);
        adminAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        adminAuthFormPanel.add(adminIDLabel);
        adminAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        adminAuthFormPanel.add(adminIDField);
        adminAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        adminAuthFormPanel.add(pinLabel);
        adminAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        adminAuthFormPanel.add(pinField);
        adminAuthFormPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        adminAuthFormPanel.add(submitButton);

        return adminAuthFormPanel;
    }
    private String validateForm(String adminID, String pin) {
        if (!adminID.matches("\\d+") ) {
            return "Admin ID should only contain digits";
        }

        if (adminID.length() != 6) {
            return "Admin ID should contain 6 digits";
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
