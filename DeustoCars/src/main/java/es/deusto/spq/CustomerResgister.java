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
        add(new JLabel()); // Espacio en blanco
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

        // Validar que la fecha de nacimiento sea válida
        LocalDate birth;
        try {
        	birth = LocalDate.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Fecha de nacimiento inválida. Formato esperado: AAAA-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // Crear el usuario
        Customer newUser = new Customer(0, name, surname, birth);
        
       
        System.out.println("Customer registered: " + newUser);

        // Limpiar los campos del formulario
        nameField.setText("");
        surnameField.setText("");
        BithDate.setText("");
        
        updateDatabase(newUser);
    }
    
    private void updateDatabase(Customer customer) {
        // Datos de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/spq";
        String username = "spq";
        String password = "spq";

        try {
            // Establecer la conexión con la base de datos
            Connection connection = DriverManager.getConnection(url, username, password);

            // Consulta SQL para insertar el vehículo en la base de datos
            String query = "INSERT INTO customers (name, surname, birth_date) " +
                           "VALUES (?, ?, ?)";

            // Preparar la declaración SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getSurname());
            //No acepta LocalDate SQL por lo que lo pasamos a java.sql.Date
            java.sql.Date sqlDate = java.sql.Date.valueOf(customer.getDateOfBirth());
            preparedStatement.setDate(3, sqlDate);
            
            

            // Ejecutar la consulta SQL
            preparedStatement.executeUpdate();

            // Cerrar la conexión y liberar recursos
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
