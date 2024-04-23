package es.deusto.spq;

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
import es.deusto.spq.db.Database;
import es.deusto.spq.db.resources.DataType;
import es.deusto.spq.db.resources.Parameter;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.serialization.Retrieval;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;


public class VehicleRetrievalForm extends JFrame {
	protected static final Logger logger = LogManager.getLogger();
	
    private JTextField emailField;	
    private JTextField plateField;
    private JButton submitButton;

    public VehicleRetrievalForm() {
        submitButton = new JButton("Retrieve Vehicle");
        setupUI("Vehicle Retrieval");

        emailField = new JTextField();
        plateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        JPanel formPanel = new JPanel(new GridLayout(3, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        // ActionListener to handle button click
        submitButton.addActionListener(e -> retrieveVehicle());

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);

        formPanel.add(createFormField("Email", emailField));
        formPanel.add(createFormField("License Plate", plateField));
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

    private void retrieveVehicle() {
        String email = emailField.getText();
        String plate = plateField.getText();


        // Create a VehicleRetrieval object
        Retrieval newRetrieval = new Retrieval(email, plate);

        	
        // Add logic to send retrieval request to the server
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/vehicles/retrieve");
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(newRetrieval, MediaType.APPLICATION_JSON));
        
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
		} else {
			logger.info("Vehicle Correctly Retrieved :)");
		}


        // Clear input fields
        emailField.setText("");
        plateField.setText("");
        
        // Connect and update the database
        if (updateDatabase(newRetrieval)) {
            JOptionPane.showMessageDialog(this, "Vehicle retrieved successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, "Error retrieving vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        }

 
    }
    
    boolean updateDatabase(Retrieval retrieval) {
        return Database.getInstance().ejecutarActualizacion("INSERT INTO retrieval (id, email, licensePlate) VALUES (?, ?, ?)",
                new Parameter(retrieval.getId(), DataType.INTEGER),
                new Parameter(retrieval.getEmail(), DataType.STRING),
                new Parameter(retrieval.getLicensePlate(), DataType.STRING)
 
        );
    }

    public static void main(String[] args) {
        VehicleRetrievalForm form = new VehicleRetrievalForm();
        form.setVisible(true);
    }
}