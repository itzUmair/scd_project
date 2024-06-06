package app.forms;

import app.database.DatabaseAccess;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RemoveCandidateForm extends JFrame {
    public RemoveCandidateForm() {
        JFrame candidateFormWindow = new JFrame();
        candidateFormWindow.setTitle("Remove candidate");
        candidateFormWindow.setSize(480, 400);
        candidateFormWindow.setVisible(true);
        candidateFormWindow.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        candidateFormWindow.add(generateRemovalForm(candidateFormWindow));
    }

    private JPanel generateRemovalForm(JFrame mainFrame) {
        JPanel candidateRemovalForm = new JPanel();
        candidateRemovalForm.setLayout(new BoxLayout(candidateRemovalForm, BoxLayout.Y_AXIS));
        candidateRemovalForm.setBorder(BorderFactory.createEmptyBorder(50, 50, 50, 50));

        JLabel formTitleLabel = new JLabel("Remove candidate");
        formTitleLabel.setFont(new Font("Verdana", Font.BOLD, 32));

        JLabel cnicLabel = new JLabel("Enter CNIC (14 digits): ");
        JTextField cnicInput = new JTextField();

        cnicInput.setMaximumSize(new Dimension(400, cnicInput.getPreferredSize().height));

        JPanel actionButtonContainer = generateActionButtonContainer(mainFrame, cnicInput);

        candidateRemovalForm.add(formTitleLabel);
        candidateRemovalForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateRemovalForm.add(cnicLabel);
        candidateRemovalForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateRemovalForm.add(cnicInput);
        candidateRemovalForm.add(Box.createRigidArea(new Dimension(0, 10)));
        candidateRemovalForm.add(actionButtonContainer);

        return candidateRemovalForm;
    }

    private static JPanel generateActionButtonContainer(JFrame mainFrame, JTextField cnicInput) {
        JButton removeCandidateButton = new JButton("Remove");
        removeCandidateButton.setBackground(Color.RED);
        JButton cancelActionButton = new JButton("Cancel");

        removeCandidateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String validity = validateForm(cnicInput.getText());
                if (validity.equals("valid")) {
                    String candidateCnic = cnicInput.getText();
                    String result = DatabaseAccess.removeCandidate(candidateCnic);
                    JOptionPane.showMessageDialog(mainFrame, result);
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
        actionButtonContainer.add(removeCandidateButton);
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

}
