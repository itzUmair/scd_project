package app.forms;
import app.database.DatabaseAccess;

import javax.swing.*;
import java.sql.SQLException;
import java.util.List;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddCandidateForm extends JFrame {
    public AddCandidateForm() {
        JFrame candidateFormWindow = new JFrame();
        candidateFormWindow.setTitle("Add new candidate");
        candidateFormWindow.setSize(480, 400);
        candidateFormWindow.setVisible(true);
        candidateFormWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        candidateFormWindow.add(generateEntryForm(candidateFormWindow));
    }

    private JPanel generateEntryForm(JFrame mainFrame) {
        JPanel candidateEntryForm = new JPanel();
        candidateEntryForm.setLayout(new BoxLayout(candidateEntryForm, BoxLayout.Y_AXIS));
        candidateEntryForm.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel formTitleLabel = new JLabel("Add candidate");
        formTitleLabel.setFont(new Font("Verdana", Font.BOLD, 32));

        JLabel cnicLabel = new JLabel("Enter CNIC (14 digits): ");
        JTextField cnicInput = new JTextField();

        cnicInput.setMaximumSize(new Dimension(400, cnicInput.getPreferredSize().height));

        JComboBox<String> partyDropDown = new JComboBox<>();
        partyDropDown.setMaximumSize(new Dimension(400, partyDropDown.getPreferredSize().height));

        try {
            List<String> parties = DatabaseAccess.getAllParties();
            for (String party : parties) {
                partyDropDown.addItem(party);
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Failed to load parties.", "Error", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }

        JButton addCandidateButton = new JButton("Add");
        JButton cancelActionButton = new JButton("Cancel");

        addCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String selectedPartyName = (String) partyDropDown.getSelectedItem();
                try {
                    String validity = validateForm(cnicInput.getText());
                    if (validity.equals("valid")) {
                        String result = DatabaseAccess.addCandidate(cnicInput.getText(), selectedPartyName);
                        JOptionPane.showMessageDialog(mainFrame, result);
                        mainFrame.dispose();
                    } else {
                        JOptionPane.showMessageDialog(mainFrame, validity);
                    }
                } catch (SQLException error) {
                    JOptionPane.showMessageDialog(mainFrame, error.getMessage());
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
        actionButtonContainer.add(addCandidateButton);
        actionButtonContainer.add(cancelActionButton);

        candidateEntryForm.add(formTitleLabel);
        candidateEntryForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateEntryForm.add(cnicLabel);
        candidateEntryForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateEntryForm.add(cnicInput);
        candidateEntryForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateEntryForm.add(partyDropDown);
        candidateEntryForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateEntryForm.add(actionButtonContainer);

        return candidateEntryForm;
    }

    private String validateForm(String cnic) {
        if (!cnic.matches("\\d+") ) {
            return "CNIC should only contain digits";
        }

        if (cnic.length() != 14) {
            return "CNIC should contain 14 digits";
        }
        return "valid";
    }

    private void closeForm() {
        this.dispose();
    }
}
