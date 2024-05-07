package es.deusto.spq;

import javax.swing.*;
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;
import java.awt.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Locale;
import java.util.ResourceBundle;

import es.deusto.spq.pojo.VehicleData;

import es.deusto.spq.client.ClientManager;
import es.deusto.spq.client.MainClient;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
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

    private static final String SYSTEM_MESSAGES = "SystemMessages";
    private ResourceBundle resourceBundle;

    private static final long serialVersionUID = 1L;
    private JTextField brandField;
    private JTextField numberPlateField;
    private JTextField modelField;
    private JCheckBox readyToBorrowCheckbox;
    private JCheckBox onRepairCheckbox;
    private JButton submitButton;

    private Database database;

    /**
     * Sets the database instance for this vehicle registration.
     * 
     * @param database The database instance to set.
     */
    public void setDatabase(Database database) {
        this.database = database;
    }

    /**
     * Constructs a new vehicle registration window.
     */
    public VehicleRegistration() {
        resourceBundle = MainClient.getResourceBundle();

        submitButton = new JButton(resourceBundle.getString("register_vehicle_label"));
        setupUI(resourceBundle.getString("register_vehicle_label"));

        brandField = new JTextField();
        numberPlateField = new JTextField();
        modelField = new JTextField();
        readyToBorrowCheckbox = new JCheckBox("Ready to lend", true);
        onRepairCheckbox = new JCheckBox("In repair", false);

        addComponentsToForm(false);

        setVisible(true);
    }

    /**
     * Constructs a new vehicle modification window.
     * 
     * @param numberPlate The number plate of the vehicle to modify.
     */
    public VehicleRegistration(String numberPlate) {

        submitButton = new JButton(resourceBundle.getString("register_vehicle_label"));
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
                readyToBorrowCheckbox = new JCheckBox("Ready to lend", true);
                onRepairCheckbox = new JCheckBox("In repair", false);        
            } else {
                JOptionPane.showMessageDialog(this, resourceBundle.getString("vehicle_not_found_message"), "Error", JOptionPane.ERROR_MESSAGE);

            }
        } catch (SQLException e) {
            logger.info("Error loading vehicle data: " + e.getMessage());
        }

        addComponentsToForm(true);

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        submitButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    registerVehicle();
                    return null;
                }
            };
            worker.execute();
        });

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm(boolean isModification) {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns layout

        formPanel.add(new JLabel(resourceBundle.getString("number_plate_label")));
        formPanel.add(numberPlateField);
        formPanel.add(new JLabel(resourceBundle.getString("brand_label")));
        formPanel.add(brandField);
        formPanel.add(new JLabel(resourceBundle.getString("model_label")));
        formPanel.add(modelField);
        if (isModification) {
            formPanel.add(readyToBorrowCheckbox);
            formPanel.add(onRepairCheckbox);
        } else {
            formPanel.add(new JPanel()); // Empty panel for alignment
            formPanel.add(new JPanel()); // Empty panel for alignment
        }
        formPanel.add(new JPanel()); // Empty panel for alignment

        brandField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        numberPlateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        modelField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
    }

    /**
     * Registers a new vehicle.
     */
    private void registerVehicle() {
        // Create the new vehicle
        VehicleData newVehicle = new VehicleData(numberPlateField.getText(), brandField.getText(), modelField.getText());

        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/addvehicle");
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(newVehicle, MediaType.APPLICATION_JSON));
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Error connecting with the server. Code: {}", response.getStatus());
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
            JOptionPane.showMessageDialog(this, resourceBundle.getString("vehicle_registered_successfully"), "Success", JOptionPane.INFORMATION_MESSAGE);

        } else {
            JOptionPane.showMessageDialog(this, resourceBundle.getString("error_registering_vehicle_message"), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Updates the database with the new vehicle information.
     * 
     * @param vehicle The vehicle data to update in the database.
     * @return True if the database was successfully updated, false otherwise.
     */
    boolean updateDatabase(VehicleData vehicle) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO vehicles (brand, number_plate, model, ready_to_borrow, on_repair) VALUES (?, ?, ?, ?, ?)",
                new Parameter(vehicle.getBrand(), DataType.STRING),
                new Parameter(vehicle.getNumberPlate(), DataType.STRING),
                new Parameter(vehicle.getModel(), DataType.STRING),
                new Parameter(vehicle.isReadyToBorrow(), DataType.BOOLEAN),
                new Parameter(vehicle.isOnRepair(), DataType.BOOLEAN)
        );
    }

    /**
     * Main method to launch the VehicleRegistration window for vehicle registration.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        VehicleRegistration vehicleRegistrationWindow = new VehicleRegistration();
        vehicleRegistrationWindow.setVisible(true);
    }
}
