/**
 * This class represents the login
 */
package es.deusto.spq.client;

import javax.swing.*;

import es.deusto.spq.client.controller.UserController;
import es.deusto.spq.pojo.UserData;

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
                this.repaint();
    
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
        enterButton.setBackground(new Color(0, 153, 204));
        enterButton.setForeground(Color.WHITE);
        enterButton.setFocusPainted(false);
        enterButton.setFont(new Font("Arial", Font.BOLD, 16));
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String email = emailField.getText();
                String password = new String(passwordField.getPassword());
                UserData user = UserController.getUser(email, password);
                boolean policyAccepted = policyCheckbox.isSelected();

                if (!policyAccepted) {
                    JOptionPane.showMessageDialog(null, "To enter you need to accept our Data Treatment Policy");
                } else if (user!=null && password.equals(user.getPassword()) && user.getIsAdmin()==false) {
                    ServiceLocator.getInstance().setWebTarget(hostname, port);
                    MainClient mainClient = new MainClient(hostname, port, MainClient.currentLocale);
                    mainClient.setVisible(true);
                    dispose();
                } else if (user!=null && password.equals(user.getPassword()) && user.getIsAdmin()==true) {
                	ServiceLocator.getInstance().setWebTarget(hostname, port);
                    MainClient mainClient = new MainClient(hostname, port, MainClient.currentLocale);
                    mainClient.setVisible(true);
                    javax.swing.SwingUtilities.invokeLater(() -> {
                        AdminClient ex = new AdminClient();
                        ex.setVisible(true);
                    });
                    dispose();
                } else if (user!=null && !password.equals(user.getPassword())) {
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
        backgroundPanel.repaint();
        validate();
        setVisible(true);

    }

    public static void main(String[] args) {
        String hostname = args[0];
        String port = args[1];
        ServiceLocator.getInstance().setWebTarget(hostname, port);
        new DeustoCarsLogin(hostname, port);
        
    }
}
