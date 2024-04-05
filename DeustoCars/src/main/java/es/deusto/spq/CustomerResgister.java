import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.sql.*;

/*
 * Class for customer registration using a graphical user interface.
 * */

public class CustomerResgister extends JFrame {
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField BithDate;
    private JButton submitButton;
    
    /*
     * Constructor for the registration of customers
     * */

    public CustomerResgister() {
        setTitle("Registro de Usuario");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(4, 2));

        JLabel nameLabel = new JLabel("Name:");
        nameField = new JTextField();
        JLabel surnameLabel = new JLabel("Surname:");
        surnameField = new JTextField();
        JLabel BirthLabel = new JLabel("Fecha de nacimiento (AAAA-MM-DD):");
        BithDate = new JTextField();
        submitButton = new JButton("Registrar Usuario");

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
            JOptionPane.showMessageDialog(this, "Fecha de nacimiento inv�lida. Formato esperado: AAAA-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
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
    
    private void updateDatabase(Customer customer) {
        // Connection to the database data
        String url = "jdbc:mysql://localhost:3306/deustoCarsDB";
        String username = "spq";
        String password = "spq";

        try {
            // Connection establishment with the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // SQL query to insert the vehicle into the database
            String query = "INSERT INTO customers (name, surname, birth_date) " +
                           "VALUES (?, ?, ?)";

            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            // It does not accept LocalDate SQL so we pass it to java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(customer.getDateOfBirth());
            preparedStatement.setDate(3, sqlDate);
            
            

            // Run the SQL query
            preparedStatement.executeUpdate();

            // Close the connection and release resources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    /*
     * Main method to start the application. 
     * */

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CustomerResgister::new);
    }
}
