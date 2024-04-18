package es.deusto.spq;

import javax.swing.*;
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import es.deusto.spq.serialization.Customer;

/**
 * Class for customer registration and modification using a graphical user interface.
 */
public class CustomerRegister extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField birthDateField;
    private JTextField emailField;
    private JButton submitButton;

    public CustomerRegister() {
        submitButton = new JButton("Register User");
        setupUI("User Registration");

        nameField = new JTextField();
        surnameField = new JTextField();
        birthDateField = new JTextField();
        emailField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    public CustomerRegister(String eMail) {
        submitButton = new JButton("Modify User");
        setupUI("User Modification");

        ResultSet rs = Database.getInstance().ejecutarConsulta(
                "SELECT email, name, surname, birth_date FROM customers WHERE email = ?",
                new Parameter(eMail, DataType.STRING)
        );
        try {
            if (rs != null && rs.next()) {
                nameField = new JTextField(rs.getString("name"));
                surnameField = new JTextField(rs.getString("surname"));
                birthDateField = new JTextField(rs.getDate("birth_date").toString());
                emailField = new JTextField(rs.getString("email"));
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("Error loading customer data: " + e.getMessage());
        }

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        submitButton.addActionListener(e -> registerUser());

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);

        formPanel.add(createFormField("Name", nameField));
        formPanel.add(createFormField("Surname", surnameField));
        formPanel.add(createFormField("Date of Birth (YYYY-MM-DD)", birthDateField));
        formPanel.add(createFormField("Email", emailField));
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

    private void registerUser() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = birthDateField.getText();
        String email = emailField.getText();

        LocalDate birth;
        try {
            birth = LocalDate.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth. Expected format: YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Customer newUser = new Customer(email, name, surname, birth);

        nameField.setText("");
        surnameField.setText("");
        birthDateField.setText("");
        emailField.setText("");

        if (updateDatabase(newUser)) {
            System.out.println("Customer registered: " + newUser);
        } else {
            System.out.println("Error registering user");
        }
    }

    boolean updateDatabase(Customer customer) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO customers (email, name, surname, birth_date)",
                new Parameter(customer.geteMail(), DataType.STRING),
                new Parameter(customer.getName(), DataType.STRING),
                new Parameter(customer.getSurname(), DataType.STRING),
                new Parameter(java.sql.Date.valueOf(customer.getDateOfBirth()), DataType.DATE)
        );
    }

    public static void main(String[] args) {
        CustomerRegister customerRegistrationWindow = new CustomerRegister();
        customerRegistrationWindow.setVisible(true);
    }
}