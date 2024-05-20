package es.deusto.spq.client;

import javax.swing.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
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
    private Client client;
	private WebTarget webTarget;

    /**
     * Constructs a new VehicleRentingForm window.
     */
    public VehicleRentingForm() {
        ImageIcon img = new ImageIcon("src/resources/rentFormIcon.png");
        setIconImage(img.getImage());

        submitButton = new JButton("Rent Vehicle");
        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        submitButton.addActionListener(e -> rentVehicle());

        setupUI("Vehicle Renting");

        emailField = new JTextField();
        plateField = new JTextField();
        startDateField = new JTextField();
        endDateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);

        formPanel.add(new JLabel("Email:"));
        formPanel.add(emailField);
        formPanel.add(new JLabel("License Plate:"));
        formPanel.add(plateField);
        formPanel.add(new JLabel("Start Date (YYYY-MM-DD):"));
        formPanel.add(startDateField);
        formPanel.add(new JLabel("End Date (YYYY-MM-DD):"));
        formPanel.add(endDateField);

        emailField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        plateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        startDateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        endDateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
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

        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/addrenting");
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(newRenting, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error creating Rent in the server. Code: {}",response.getStatus());
            JOptionPane.showMessageDialog(this, "Error creating Rent in the server", "Error", JOptionPane.ERROR_MESSAGE);
		} else {
			logger.info("Renting Correctly Registered :)");
            JOptionPane.showMessageDialog(this, "Renting Correctly Registered", "Success registering renting", JOptionPane.INFORMATION_MESSAGE);

		}

        // Clear input fields
		emailField.setText("");
		plateField.setText("");
		startDateField.setText("");
		endDateField.setText("");

        // Connect and update the database
        // if (updateDatabase(newRenting)) {
        //     JOptionPane.showMessageDialog(this, "Renting registered successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        // } else {
        //     JOptionPane.showMessageDialog(this, "Error registering renting.", "Error", JOptionPane.ERROR_MESSAGE);
        // }
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
