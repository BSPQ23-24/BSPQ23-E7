/**
 * This class represents the login
 */
package es.deusto.spq.client;

import es.deusto.spq.client.MainClient;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class DeustoCarsLogin extends JFrame {
    private JTextField emailField;
    private JPasswordField passwordField;
    private JCheckBox policyCheckbox;

    public DeustoCarsLogin(String hostname, String port) {
        setTitle("DeustoCars Login");
        setSize(650, 350);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        ImageIcon img = new ImageIcon("src/resources/bottom-bar-icon.png");
        setIconImage(img.getImage());

        ImageIcon imgIcon = new ImageIcon("src/resources/loginBackground.png");
        Image imgScaled = imgIcon.getImage().getScaledInstance(600, 350, java.awt.Image.SCALE_SMOOTH);
        
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(imgScaled, 0, 0, this.getWidth(), this.getHeight(), null);
            }
        };
        backgroundPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc =  new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        JLabel emailLabel = new JLabel("Email:");
        gbc.gridx = 0;
        gbc.gridy = 0;       
        backgroundPanel.add(emailLabel, gbc);
 
        emailField = new JTextField();
        emailField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 0;
        backgroundPanel.add(emailField, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;   
        backgroundPanel.add(passwordLabel, gbc);
        
        passwordField = new JPasswordField();
        passwordField.setPreferredSize(new Dimension(200, 30));
        gbc.gridx = 1;
        gbc.gridy = 1;         
        backgroundPanel.add(passwordField, gbc);

        policyCheckbox = new JCheckBox("I accept the DeustoCars Data Treatment policy");
        policyCheckbox.setOpaque(false);
        gbc.gridx = 0;
        gbc.gridy = 2;   
        backgroundPanel.add(policyCheckbox, gbc);

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
                    ClientManager.getInstance().setWebTarget(hostname, port);
                    MainClient mainClient = new MainClient(hostname, port);
                    mainClient.setVisible(true);
                    dispose();
                } else if (email.equals("admin@admin.com") && password.equals("admin")) {
                	ClientManager.getInstance().setWebTarget(hostname, port);
                    MainClient mainClient = new MainClient(hostname, port);
                    mainClient.setVisible(true);
                    AdminClient adminClient = new AdminClient();
                    adminClient.setVisible(true);
                    dispose();
                } else if (email.equals("user@user.com") || email.equals("admin@admin.com")) {
                    JOptionPane.showMessageDialog(null, "Wrong password");
                } else {
                    JOptionPane.showMessageDialog(null, "This email is not registered");
                }
            }
        });
        gbc.gridx = 1;
        gbc.gridy = 2;
        backgroundPanel.add(enterButton, gbc);

        add(backgroundPanel);
        validate();
        setVisible(true);

    }

    public static void main(String[] args) {
        String hostname = args[0];
        String port = args[1];
        new DeustoCarsLogin(hostname, port);
        
    }
}
