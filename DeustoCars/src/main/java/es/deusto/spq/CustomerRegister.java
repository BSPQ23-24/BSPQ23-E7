package es.deusto.spq;

import javax.swing.*;
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;

import java.awt.*;
import java.sql.*;
import java.time.LocalDate;

/**
 * Class for customer registration and modification using a graphical user interface.
 */
public class CustomerRegister extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
    private JTextField surnameField;
    private JTextField birthDateField;
    private JButton submitButton;

    public CustomerRegister() {
        submitButton = new JButton("Register user");
        setupUI("User Registration");

        nameField = new JTextField();
        surnameField = new JTextField();
        birthDateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    public CustomerRegister(int customerId) {
        submitButton = new JButton("Modify user");
        setupUI("User Modification");

        ResultSet rs = Database.getInstance().ejecutarConsulta(
            "SELECT name, surname, birth_date FROM customers WHERE id = ?",
            new Parameter(Integer.toString(customerId), DataType.STRING)
        );
        try {
            if (rs != null && rs.next()) {
                nameField = new JTextField(rs.getString("name"));
                surnameField = new JTextField(rs.getString("surname"));
                birthDateField = new JTextField(rs.getDate("birth_date").toString());
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
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        submitButton.addActionListener(e -> registerUser());
    }

    private void addComponentsToForm() {
        add(new JLabel("Name:"));
        add(nameField);
        add(new JLabel("Surname:"));
        add(surnameField);
        add(new JLabel("Date of Birth (AAAA-MM-DD):"));
        add(birthDateField);
        add(new JLabel("")); // Espacio en blanco
        add(submitButton);
    }

    private void registerUser() {
    	String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = birthDateField.getText();

        // Check that the birth date is valid
        LocalDate birth;
        try {
        	birth = LocalDate.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth. Expected format: YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the user
        Customer newUser = new Customer(name, surname, birth);

        // Clean the form fields
        nameField.setText("");
        surnameField.setText("");
        birthDateField.setText("");
        
        if (updateDatabase(newUser)) {
        	System.out.println("Customer registered: " + newUser);
        } else {
        	System.out.println("Error registering user");
        }
    }
    
    boolean updateDatabase(Customer customer) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO customers (name, surname, birth_date)",
        	    new Parameter(customer.getName(), DataType.STRING),
        	    new Parameter(customer.getSurname(), DataType.STRING),
        	    new Parameter(java.sql.Date.valueOf(customer.getDateOfBirth()), DataType.DATE)
        	);
    }

    public static void main(String[] args) {
        // Para pruebas
        CustomerRegister customerRegistrationWindow = new CustomerRegister();
        customerRegistrationWindow.setVisible(true);
    }
}
