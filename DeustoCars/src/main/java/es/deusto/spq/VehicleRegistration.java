package es.deusto.spq;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


/**
 * Class for vehicle registration using a graphical user interface.
 */

public class VehicleRegistration extends JFrame {
    private JTextField brandField;
    private JTextField numberPlateField;
    private JTextField modelField;
    private JCheckBox readyToBorrowCheckbox;
    private JCheckBox onRepairCheckbox;
    private JButton submitButton;

    /**
     * Constructor for vehicle registration window.
     */
    
    public VehicleRegistration() {
        setTitle("Vehicle Registration");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel brandLabel = new JLabel("Brand:");
        brandField = new JTextField();
        JLabel numberPlateLabel = new JLabel("Number Plate:");
        numberPlateField = new JTextField();
        JLabel modelLabel = new JLabel("Model:");
        modelField = new JTextField();
        readyToBorrowCheckbox = new JCheckBox("Ready to lend", true);
        onRepairCheckbox = new JCheckBox("In repair", false);
        submitButton = new JButton("Register Vehicle");

        add(brandLabel);
        add(brandField);
        add(numberPlateLabel);
        add(numberPlateField);
        add(modelLabel);
        add(modelField);
        add(readyToBorrowCheckbox);
        add(onRepairCheckbox);
        add(new JLabel());
        add(submitButton);

        submitButton.addActionListener(e -> registerVehicle());

        setVisible(true);

      
    }

    /**
     * Registers a new vehicle in the database and adds it to the vehicle list.
     */
    
    private void registerVehicle() {
        String brand = brandField.getText();
        String numberPlate = numberPlateField.getText();
        String model = modelField.getText();
        boolean readyToBorrow = readyToBorrowCheckbox.isSelected();
        boolean onRepair = onRepairCheckbox.isSelected();

        // Create the new vehicle
        Vehicle newVehicle = new Vehicle(brand, numberPlate, model);
        newVehicle.setReadyToBorrow(readyToBorrow);
        newVehicle.setOnRepair(onRepair);

        // Clear input fields
        brandField.setText("");
        numberPlateField.setText("");
        modelField.setText("");
        readyToBorrowCheckbox.setSelected(true);
        onRepairCheckbox.setSelected(false);

        // Connect and update the database
        updateDatabase(newVehicle);
    }

    /**
     * Connect to the MySQL database and update the information of the new vehicle.
     *
     */
    
    private void updateDatabase(Vehicle vehicle) {
        // Database connection details
        String url = "jdbc:mysql://localhost:3306/deustoCarsDB";
        String username = "spq";
        String password = "spq";

        try {
            // Establish the connection to the database
            Connection connection = DriverManager.getConnection(url, username, password);

            // SQL query to insert the vehicle into the database
            String query = "INSERT INTO vehicles (brand, number_plate, model, ready_to_borrow, on_repair) " +
                           "VALUES (?, ?, ?, ?, ?)";

            // Prepare the SQL statement
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vehicle.getBrand());
            preparedStatement.setString(2, vehicle.getNumberPlate());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setBoolean(4, vehicle.isReadyToBorrow());
            preparedStatement.setBoolean(5, vehicle.isOnRepair());

            // Run the SQL query
            preparedStatement.executeUpdate();

            // Close the connection and release resources
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method to start the application.
     *
     */
    
     public static void main(String[] args) {
        // Instantiate the customer registration window
        VehicleRegistration CustomerRegistrationWindow = new VehicleRegistration();

        // Display the customer registration window
        CustomerRegistrationWindow.setVisible(true);
    }
}
