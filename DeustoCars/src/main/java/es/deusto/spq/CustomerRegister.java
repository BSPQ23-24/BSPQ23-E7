package es.deusto.spq;

import javax.swing.*;

import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.*;

import java.awt.*;
import java.time.LocalDate;
import java.sql.*;

/*
 * Class for customer registration using a graphical user interface.
 * */

public class CustomerRegister extends JFrame {
	private static final long serialVersionUID = 1L;
	private JTextField nameField;
    private JTextField surnameField;
    private JTextField BithDate;
    private JButton submitButton;
    
    /*
     * Constructor for the registration of customers
     * */

    public CustomerRegister() {
        setTitle("User Registration");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel surnameLabel = new JLabel("Surname:");
        surnameField = new JTextField();
        JLabel BirthLabel = new JLabel("Date of Birth (AAAA-MM-DD):");
        BithDate = new JTextField();
        submitButton = new JButton("Register user");

        add(nameLabel);
        add(nameField);
        add(surnameLabel);
        add(surnameField);
        add(BirthLabel);
        add(BithDate);
        add(new JLabel()); // Blank space
        add(submitButton);

        submitButton.addActionListener(e -> registerUser());

        setVisible(true);
    }
    
    /*
     * Registers a new customer in the database and adds it to the customer list.
     * */

    private void registerUser() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = BithDate.getText();

        // Check that the birth date is valid
        LocalDate birth;
        try {
        	birth = LocalDate.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date of birth. Expected format: YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Create the user
        Customer newUser = new Customer(0, name, surname, birth);
        
       
        System.out.println("Customer registered: " + newUser);

        // Clean the form fields
        nameField.setText("");
        surnameField.setText("");
        BithDate.setText("");
        
        updateDatabase(newUser);
    }
    
    boolean updateDatabase(Customer customer) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO customers (name, surname, birth_date)",
        	    new Parameter(customer.getName(), DataType.STRING),
        	    new Parameter(customer.getSurname(), DataType.STRING),
        	    new Parameter(java.sql.Date.valueOf(customer.getDateOfBirth()), DataType.DATE)
        	);
    }
    
    /*
     * Main method to start the application. 
     * */

    public static void main(String[] args) {
        // Instantiate the customer registration window
        CustomerRegister CustomerRegistrationWindow = new CustomerRegister();

        // Display the customer registration window
        CustomerRegistrationWindow.setVisible(true);
    }
    

}
