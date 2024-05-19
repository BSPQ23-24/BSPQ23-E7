package es.deusto.spq.client;

import javax.swing.*;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.client.ClientManager;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Class for renting a vehicle using a graphical user interface.
 */
public class VehicleRentingForm extends JFrame {
    protected static final Logger logger = LogManager.getLogger();
	
    private JTextField emailField;
    private JTextField plateField;
    private JTextField startDateField;
    private JTextField endDateField;
    private JButton submitButton;

    /**
     * Constructs a new VehicleRentingForm window.
     */
    public VehicleRentingForm() {
        submitButton = new JButton("Rent Vehicle");
        setupUI("Vehicle Renting");

        emailField = new JTextField();
        plateField = new JTextField();
        startDateField = new JTextField();
        endDateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(4, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        // ActionListener to handle button click
        submitButton.addActionListener(e -> rentVehicle());

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);

        formPanel.add(createFormField("Email", emailField));
        formPanel.add(createFormField("License Plate", plateField));
        formPanel.add(createFormField("Start Date (YYYY-MM-DD)", startDateField));
        formPanel.add(createFormField("End Date (YYYY-MM-DD)", endDateField));
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

    private void rentVehicle() {
    	String email = emailField.getText();
        String plate = plateField.getText();
        String startDate = startDateField.getText();
        String endDate = endDateField.getText();

        // Validating dates
        Date start, end;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            start = dateFormat.parse(startDate);
            end = dateFormat.parse(endDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Invalid date format. Expected: YYYY-MM-DD", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        CustomerData customer = MainClient.getCustomer(email);
        VehicleData vehicle = MainClient.getVehicle(plate);
        Renting newRenting = new Renting(customer, vehicle, start, end);

        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/rent");
		Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(newRenting, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error creating Rent in the server. Code: {}",response.getStatus());
		} else {
			logger.info("Renting Correctly Registered :)");
		}

        // Clear input fields
		emailField.setText("");
		plateField.setText("");
		startDateField.setText("");
		endDateField.setText("");
        

        // Connect and update the database
        if (updateDatabase(newRenting)) {
            JOptionPane.showMessageDialog(this, "Vehicle registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error registering vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    /**
     * Updates the database with the new renting information.
     * 
     * @param renting The renting data to update in the database.
     * @return True if the database was successfully updated, false otherwise.
     */
    boolean updateDatabase(Renting renting) {
        //return Database.getInstance().ejecutarActualizacion("INSERT INTO renting (id, email, licensePlate, startDate, endDate) VALUES (?, ?, ?, ?, ?)",
                //new Parameter(renting.getId(), DataType.INTEGER),
                //new Parameter(renting.getEmail(), DataType.STRING),
                //new Parameter(renting.getLicensePlate(), DataType.STRING),
                //new Parameter(new java.sql.Date(renting.getStartDate().getTime()), DataType.DATE),
                //new Parameter(new java.sql.Date(renting.getEndDate().getTime()), DataType.DATE)
        //);
        return true;
    }

    /**
     * Main method to launch the VehicleRentingForm window for vehicle renting.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        VehicleRentingForm form = new VehicleRentingForm();
        form.setVisible(true);
    }
}
