package es.deusto.spq;

import javax.swing.*;
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import es.deusto.spq.serialization.Vehicle;
import es.deusto.spq.pojo.VehicleData;

import es.deusto.spq.client.ClientManager;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Class for vehicle registration using a graphical user interface.
 */
public class VehicleRegistration extends JFrame {
    protected static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = 1L;
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
        submitButton = new JButton("Register Vehicle");
        setupUI("Vehicle Registration");

        brandField = new JTextField();
        numberPlateField = new JTextField();
        modelField = new JTextField();
        readyToBorrowCheckbox = new JCheckBox("Ready to lend", true);
        onRepairCheckbox = new JCheckBox("In repair", false);

        addComponentsToForm(false);

        setVisible(true);
    }

    public VehicleRegistration(String numberPlate) {
        submitButton = new JButton("Modify Vehicle");
        setupUI("Vehicle Modification");

        ResultSet rs = Database.getInstance().ejecutarConsulta(
            "SELECT brand, number_plate, model, ready_to_borrow, on_repair FROM vehicles WHERE number_plate = ?",
            new Parameter(numberPlate, DataType.STRING)
        );
        try {
            if (rs != null && rs.next()) {
                brandField = new JTextField(rs.getString("brand"));
                numberPlateField = new JTextField(rs.getString("number_plate"));
                modelField = new JTextField(rs.getString("model"));
                readyToBorrowCheckbox = new JCheckBox("Ready to lend", rs.getBoolean("ready_to_borrow"));
                onRepairCheckbox = new JCheckBox("In repair", rs.getBoolean("on_repair"));
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException e) {
            System.out.println("Error loading vehicle data: " + e.getMessage());
        }

        addComponentsToForm(true);

        setVisible(true);
    }
    
    private void setupUI(String title) {
        setTitle(title);
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        submitButton.addActionListener(e -> registerVehicle());
    }

    private void addComponentsToForm(boolean isModification) {
        add(new JLabel("Brand:"));
        add(brandField);
        add(new JLabel("Number Plate:"));
        add(numberPlateField);
        add(new JLabel("Model:"));
        add(modelField);
        if (isModification) {
            add(readyToBorrowCheckbox);
            add(onRepairCheckbox);
        } else {
            add(new JLabel(""));
            add(new JLabel(""));
        }
        add(new JLabel("")); // Espacio en blanco para alinear el botón
        add(submitButton);
    }

    private void registerVehicle() {

        // Create the new vehicle
        VehicleData newVehicle = new VehicleData(brandField.getText(), numberPlateField.getText(), modelField.getText());

        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/vehicles");
		Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(newVehicle, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
		} else {
			logger.info("Vehicle Correctly Registered :)");
		}

        // Clear input fields
        brandField.setText("");
        numberPlateField.setText("");
        modelField.setText("");
        readyToBorrowCheckbox.setSelected(true);
        onRepairCheckbox.setSelected(false);

        // Connect and update the database
        if (updateDatabase(newVehicle)) {
            JOptionPane.showMessageDialog(this, "Vehicle registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error registering vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    boolean updateDatabase(VehicleData vehicle) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO vehicles (brand, number_plate, model, ready_to_borrow, on_repair) VALUES (?, ?, ?, ?, ?)",
                new Parameter(vehicle.getBrand(), DataType.STRING),
                new Parameter(vehicle.getNumberPlate(), DataType.STRING),
                new Parameter(vehicle.getModel(), DataType.STRING),
                new Parameter(vehicle.isReadyToBorrow(), DataType.BOOLEAN),
                new Parameter(vehicle.isOnRepair(), DataType.BOOLEAN)
        );
    }

    public static void main(String[] args) {
        VehicleRegistration a = new VehicleRegistration();
        //VehicleRegistration a = new VehicleRegistration(new Vehicle("a", "a", "a"));
        a.setVisible(true);
    }
}


