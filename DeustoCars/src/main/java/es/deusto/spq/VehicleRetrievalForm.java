package es.deusto.spq;

import javax.swing.*;
import java.awt.*;

public class VehicleRetrievalForm extends JFrame {
    private JTextField emailField;
    private JTextField plateField;
    private JButton submitButton;

    public VehicleRetrievalForm() {
        submitButton = new JButton("Retrieve Vehicle");
        setupUI("Vehicle Retrieval");

        emailField = new JTextField();
        plateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        // ActionListener to handle button click
        submitButton.addActionListener(e -> retrieveVehicle());

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);

        formPanel.add(createFormField("Email", emailField));
        formPanel.add(createFormField("License Plate", plateField));
    }

    private JPanel createFormField(String label, JTextField textField) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel jLabel = new JLabel(label);
        jLabel.setFont(new Font("Arial", Font.BOLD, 14));
        panel.add(jLabel, BorderLayout.WEST);
        panel.add(textField, BorderLayout.CENTER);
        textField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2)); // Add border
        return panel;
    }

    private void retrieveVehicle() {
        // Add functionality to retrieve the vehicle based on the entered information
        // This method will be called when the submitButton is clicked
        // Implement retrieval logic here
    }

    public static void main(String[] args) {
        VehicleRetrievalForm form = new VehicleRetrievalForm();
        form.setVisible(true);
    }
}