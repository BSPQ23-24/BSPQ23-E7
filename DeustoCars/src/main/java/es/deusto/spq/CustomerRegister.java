package es.deusto.spq;

import javax.swing.*;

import es.deusto.spq.client.ClientManager;
import es.deusto.spq.client.MainClient;
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;

import java.awt.*;
import java.sql.*;
import java.util.Date;
import java.util.Locale;
import java.text.SimpleDateFormat;
import es.deusto.spq.pojo.CustomerData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Class for customer registration and modification using a graphical user interface.
 */
public class CustomerRegister extends JFrame {
    protected static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField birthDateField;
    private JTextField emailField;
    private JButton submitButton;

    private static final String SYSTEM_MESSAGES = "SystemMessages";
    private ResourceBundle resourceBundle;
    

    /**
     * Constructs a new CustomerRegister window for user registration.
     */
    public CustomerRegister() {
        resourceBundle = MainClient.getResourceBundle();

        submitButton = new JButton(resourceBundle.getString("register_user_label"));
        setupUI(resourceBundle.getString("register_user_label"));

        nameField = new JTextField();
        surnameField = new JTextField();
        birthDateField = new JTextField();
        emailField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    /**
     * Constructs a new CustomerRegister window for user modification.
     * 
     * @param eMail The email of the customer to modify.
     */
    public CustomerRegister(String eMail) {
        submitButton = new JButton(resourceBundle.getString("register_user_label"));

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
                JOptionPane.showMessageDialog(this, resourceBundle.getString("customer_not_found_message"), "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            logger.info(resourceBundle.getString("customer_not_found_message") + e.getMessage());
            
        }

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
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
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns layout
        formPanel.add(new JLabel(resourceBundle.getString("name_label")));
        formPanel.add(nameField);
        formPanel.add(new JLabel(resourceBundle.getString("surname_label")));
        formPanel.add(surnameField);
        formPanel.add(new JLabel(resourceBundle.getString("birthdate_label")));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel(resourceBundle.getString("email_label")));
        formPanel.add(emailField);
    
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        surnameField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        birthDateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
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

    /**
     * Registers a new customer.
     */
    public void registerUser() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = birthDateField.getText();
        String email = emailField.getText();

        Date birth;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birth = dateFormat.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, resourceBundle.getString("invalid_date_message"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CustomerData newUser = new CustomerData(email, name, surname, birth);
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/customers");
		Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
		} else {
			logger.info("Customer Correctly Registered :)");
		}

        nameField.setText("");
        surnameField.setText("");
        birthDateField.setText("");
        emailField.setText("");

        if (updateDatabase(newUser)) {
            logger.info("Customer registered: " + newUser);
        } else {
            logger.info("Error registering user");
        }				
    }

    /**
     * Updates the database with the new customer information.
     * 
     * @param customer The customer data to update in the database.
     * @return True if the database was successfully updated, false otherwise.
     */
    boolean updateDatabase(CustomerData customer) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO customers (email, name, surname, birth_date)",
                new Parameter(customer.geteMail(), DataType.STRING),
                new Parameter(customer.getName(), DataType.STRING),
                new Parameter(customer.getSurname(), DataType.STRING),
                new Parameter(new java.sql.Date(customer.getDateOfBirth().getTime()), DataType.DATE)

        );
    }

    /**
     * Main method to launch the CustomerRegister window for user registration.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        CustomerRegister customerRegistrationWindow = new CustomerRegister();
        customerRegistrationWindow.setVisible(true);
    }
}
