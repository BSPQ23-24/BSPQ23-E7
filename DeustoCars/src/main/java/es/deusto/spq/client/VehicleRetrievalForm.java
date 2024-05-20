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

import java.awt.*;

/**
 * Class for retrieving a vehicle using a graphical user interface.
 */
public class VehicleRetrievalForm extends JFrame {
    private static final long serialVersionUID = 1L;

    protected static final Logger logger = LogManager.getLogger();
    
    private JTextField emailField;    
    private JTextField plateField;
    private JButton submitButton;

    /**
     * Constructs a new VehicleRetrievalForm window.
     */
    public VehicleRetrievalForm() {
        ImageIcon img = new ImageIcon("src/resources/retrieveFormIcon.png");
        setIconImage(img.getImage());
        submitButton = new JButton("Retrieve Vehicle");
        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        submitButton.addActionListener(e -> returnVehicle());

        setupUI("Vehicle Retrieval");

        emailField = new JTextField();
        plateField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setTitle(title);
        setSize(400, 200);
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

        emailField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        plateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
    }

    private void returnVehicle() {
        String email = emailField.getText();
        String plate = plateField.getText();
    
        // Validate email and plate inputs
        if (email.isEmpty() || plate.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Email and Number Plate must be filled out.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        // Build the target URL with query parameters
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/returnvehicle")
                .queryParam("numberPlate", plate)
                .queryParam("eMail", email);
    
        // Create a request and send it
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(null);
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.error("Error returning vehicle in the server. Code: {}", response.getStatus());
            JOptionPane.showMessageDialog(this, "Error returning vehicle.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            logger.info("Vehicle returned successfully!");
    
            // Clear input fields
            emailField.setText("");
            plateField.setText("");
    
            JOptionPane.showMessageDialog(this, "Vehicle returned successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
    }
    
    
    /**
     * Updates the database after vehicle retrieval.
     * 
     * @param plate The license plate of the retrieved vehicle.
     * @return True if the database was successfully updated, false otherwise.
     */
    boolean updateDatabase(String plate) {
        //return Database.getInstance().ejecutarActualizacion("UPDATE retrieval SET licensePlate = ?",
        //        new Parameter(plate, DataType.STRING)
        //);
        return true;
    }

    /**
     * Main method to launch the VehicleRetrievalForm window for vehicle retrieval.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        VehicleRetrievalForm form = new VehicleRetrievalForm();
        form.setVisible(true);
    }
}
