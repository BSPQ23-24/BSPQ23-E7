/**
 * This class represents the login
 */
package es.deusto.spq.client;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeustoCarsLogin extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox policyCheckbox;

    public DeustoCarsLogin() {
        setTitle("DeustoCars Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1));

        JLabel emailLabel = new JLabel("Email:");
        emailField = new JTextField();
        panel.add(emailLabel);
        panel.add(emailField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField();
        panel.add(passwordLabel);
        panel.add(passwordField);

        policyCheckbox = new JCheckBox("I accept the DeustoCars Data Treatment policy");
        panel.add(policyCheckbox);

        JButton enterButton = new JButton("Enter");
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                boolean policyAccepted = policyCheckbox.isSelected();

                if (!policyAccepted) {
                    JOptionPane.showMessageDialog(null, "To enter you need to accept our Data Treatment Policy");
                } else if (email.equals("user@user.com") && password.equals("user")) {
                    MainClient mainClient = new MainClient();
                    mainClient.setVisible(true);
                    dispose();
                } else if (email.equals("admin@admin.com") && password.equals("admin")) {
                    // Do nothing for admin for now
                } else if (email.equals("user@user.com") || email.equals("admin@admin.com")) {
                    JOptionPane.showMessageDialog(null, "Wrong password");
                } else {
                    JOptionPane.showMessageDialog(null, "This email is not registered");
                }
            }
        });
        panel.add(enterButton);

        add(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        new DeustoCarsLogin();
    }
}

class MainClient extends JWindow {
    public MainClient() {
        setSize(400, 300);
        setLocationRelativeTo(null);
        setBackground(new Color(0, 0, 0, 0));

        JLabel label = new JLabel("Welcome to DeustoCars!");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(label, BorderLayout.CENTER);
    }
}
