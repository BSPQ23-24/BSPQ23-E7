package es.deusto.spq;

import javax.swing.*;

import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;

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
    
    boolean updateDatabase(Vehicle vehicle) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO customers (name, surname, birth_date)",
        	    new Parameter(vehicle.getBrand(), DataType.STRING),
        	    new Parameter(vehicle.getNumberPlate(), DataType.STRING),
        	    new Parameter(vehicle.getModel(), DataType.STRING),
        	    new Parameter(vehicle.isReadyToBorrow(), DataType.BOOLEAN),
        	    new Parameter(vehicle.isOnRepair(), DataType.BOOLEAN)
        	);
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
