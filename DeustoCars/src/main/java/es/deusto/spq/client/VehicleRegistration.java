package es.deusto.spq.client;

import javax.swing.*;
import java.awt.*;
import java.util.ResourceBundle;

import es.deusto.spq.pojo.VehicleData;

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

    private ResourceBundle resourceBundle;

    private static final long serialVersionUID = 1L;
    private JTextField brandField;
    private JTextField numberPlateField;
    private JTextField modelField;
    private JCheckBox readyToBorrowCheckbox;
    private JCheckBox onRepairCheckbox;
    private JButton submitButton;

    /**
     * Constructs a new vehicle registration window.
     */
    public VehicleRegistration() {
        resourceBundle = MainClient.getResourceBundle();

        submitButton = new JButton(resourceBundle.getString("register_vehicle_label"));
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
    	resourceBundle = MainClient.getResourceBundle();
    	
        submitButton = new JButton(resourceBundle.getString("register_vehicle_label"));
        submitButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    modifyVehicle();
                    return null;
                }
            };
            worker.execute();
        });
        setupUI("Vehicle Modification");

        VehicleData vehicle = MainClient.getVehicle(numberPlate);
        
        if (vehicle != null) {
	        brandField = new JTextField(vehicle.getBrand());
	        numberPlateField = new JTextField(vehicle.getNumberPlate());
	        modelField = new JTextField(vehicle.getModel());
	        readyToBorrowCheckbox = new JCheckBox("Ready to lend", vehicle.isReadyToBorrow());
	        onRepairCheckbox = new JCheckBox("In repair", vehicle.isOnRepair());
        } else {
        	JOptionPane.showMessageDialog(this, resourceBundle.getString("vehicle_not_found_message"), "Error", JOptionPane.ERROR_MESSAGE);
        }

        addComponentsToForm(true);

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon("src/resources/registerVehicleIcon.png");
        setIconImage(img.getImage());

        JPanel formPanel = new JPanel(new GridLayout(6, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

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
        VehicleData newVehicle = new VehicleData(numberPlateField.getText(), brandField.getText(), modelField.getText(), 
        		readyToBorrowCheckbox.isSelected(), onRepairCheckbox.isSelected());

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
        readyToBorrowCheckbox.setSelected(false);
        onRepairCheckbox.setSelected(false);
    }

    /**
     * Updates the database with the new vehicle information.
     * 
     * @param vehicle The vehicle data to update in the database.
     * @return True if the database was successfully updated, false otherwise.
     */
    public void modifyVehicle() {
    	VehicleData newVehicle = new VehicleData(numberPlateField.getText(), brandField.getText(), modelField.getText(), 
    			readyToBorrowCheckbox.isSelected(), onRepairCheckbox.isSelected());
    	
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/addvehicle");
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(newVehicle, MediaType.APPLICATION_JSON));
        
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.info("Vehicle to modify found.");
            
            //MainClient.deleteVehicle(numberPlateField.getText());

            response = invocationBuilder.post(Entity.entity(newVehicle, MediaType.APPLICATION_JSON));
            if (response.getStatus() != Status.OK.getStatusCode()) {
                logger.error("Error connecting with the server. Code: {}", response.getStatus());
            } else {
                logger.info("Vehicle Correctly Modified :)");
            }

            // Clear input fields
            brandField.setText("");
            numberPlateField.setText("");
            modelField.setText("");
            readyToBorrowCheckbox.setSelected(false);
            onRepairCheckbox.setSelected(false);
        } else {
            logger.error("Vehicle doesnt exist. Code: {}", response.getStatus());
        }
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
