package app.forms;

import CustomExceptions.NoUserFoundException;
import app.database.DatabaseAccess;
import app.models.Voter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class SearchCandidateForm extends JFrame {
    public SearchCandidateForm() {
        JFrame candidateFormWindow = new JFrame();
        candidateFormWindow.setTitle("Search candidate");
        candidateFormWindow.setSize(480, 400);
        candidateFormWindow.setVisible(true);
        candidateFormWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        candidateFormWindow.add(generateSearchForm(candidateFormWindow));
    }

    private JPanel generateSearchForm(JFrame mainFrame) {
        JPanel candidateSearchForm = new JPanel();
        candidateSearchForm.setLayout(new BoxLayout(candidateSearchForm, BoxLayout.Y_AXIS));
        candidateSearchForm.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel formTitleLabel = new JLabel("Search candidate");
        formTitleLabel.setFont(new Font("Verdana", Font.BOLD, 32));

        JLabel cnicLabel = new JLabel("Enter CNIC (14 digits): ");
        JTextField cnicInput = new JTextField();

        cnicInput.setMaximumSize(new Dimension(400, cnicInput.getPreferredSize().height));

        JPanel actionButtonContainer = generateActionButtonContainer(mainFrame, cnicInput);

        candidateSearchForm.add(formTitleLabel);
        candidateSearchForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateSearchForm.add(cnicLabel);
        candidateSearchForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateSearchForm.add(cnicInput);
        candidateSearchForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateSearchForm.add(actionButtonContainer);

        return candidateSearchForm;
    }

    private static JPanel generateActionButtonContainer(JFrame mainFrame, JTextField cnicInput) {
        JButton searchCandidateButton = new JButton("Search");
        JButton cancelActionButton = new JButton("Cancel");

        searchCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String validity = validateForm(cnicInput.getText());
                if (validity.equals("valid")) {
                    String candidateCnic = cnicInput.getText();
                    try {
                        Voter voter = DatabaseAccess.getVoterByCnic(candidateCnic);
                        if (voter != null) {
                            showVoterDetailsDialog(mainFrame, voter);
                        }
                    } catch (SQLException | NoUserFoundException ex) {
                        JOptionPane.showMessageDialog(mainFrame, ex.getMessage());
                    }
                } else {
                    JOptionPane.showMessageDialog(mainFrame, validity);
                }
            }
        });

        cancelActionButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
            }
        });

        JPanel actionButtonContainer = new JPanel(new FlowLayout());
        actionButtonContainer.add(searchCandidateButton);
        actionButtonContainer.add(cancelActionButton);
        return actionButtonContainer;
    }

    private static String validateForm(String cnic) {
        if (!cnic.matches("\\d+") ) {
            return "CNIC should only contain digits";
        }

        if (cnic.length() != 14) {
            return "CNIC should contain 14 digits";
        }
        return "valid";
    }

    private static void showVoterDetailsDialog(JFrame parentFrame, Voter voter) {
        JDialog voterDetailsDialog = new JDialog(parentFrame, "Voter Details", true);
        voterDetailsDialog.setLayout(new BorderLayout());
        voterDetailsDialog.setSize(300, 200);

        JPanel detailsPanel = new JPanel();
        detailsPanel.setLayout(new GridLayout(4, 2, 10, 10));
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        detailsPanel.add(new JLabel("CNIC:"));
        detailsPanel.add(new JLabel(voter.getCnic()));

        detailsPanel.add(new JLabel("Name:"));
        detailsPanel.add(new JLabel(voter.getName()));

        detailsPanel.add(new JLabel("Age:"));
        detailsPanel.add(new JLabel(String.valueOf(voter.getAge())));

        voterDetailsDialog.add(detailsPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> voterDetailsDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(closeButton);
        voterDetailsDialog.add(buttonPanel, BorderLayout.SOUTH);

        voterDetailsDialog.setLocationRelativeTo(parentFrame);
        voterDetailsDialog.setVisible(true);
    }
    
}
