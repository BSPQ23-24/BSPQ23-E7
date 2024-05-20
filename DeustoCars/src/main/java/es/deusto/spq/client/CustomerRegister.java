package es.deusto.spq.client;

import javax.swing.*;

import java.awt.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ResourceBundle;

/**
 * Class for customer registration and modification using a graphical user interface.
 */
public class CustomerRegister extends JFrame {
    protected static final Logger logger = LogManager.getLogger();

    private static final long serialVersionUID = 1L;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField birthDateField;
    private JTextField emailField;
    private JButton submitButton;

    private static final String SYSTEM_MESSAGES = "SystemMessages";
    private ResourceBundle resourceBundle;
    

    /**
     * Constructs a new CustomerRegister window for user registration.
     */
    public CustomerRegister() {
        resourceBundle = MainClient.getResourceBundle();
        resourceBundle = MainClient.getResourceBundle();

        submitButton = new JButton(resourceBundle.getString("register_user_label"));
        submitButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    registerUser();
                    return null;
                }
            };
            worker.execute();
        });
        setupUI(resourceBundle.getString("register_user_label"));

        nameField = new JTextField();
        surnameField = new JTextField();
        birthDateField = new JTextField();
        emailField = new JTextField();

        addComponentsToForm();

        setVisible(true);
    }

    /**
     * Constructs a new CustomerRegister window for user modification.
     * 
     * @param eMail The email of the customer to modify.
     */
    public CustomerRegister(String eMail) {
    	resourceBundle = MainClient.getResourceBundle();
    	submitButton = new JButton(resourceBundle.getString("edit_customer_label"));
    	submitButton.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    modifyUser();
                    return null;
                }
                @Override
                protected void done() {
                    dispose();
                }
            };
            worker.execute();
        });
        setupUI("Customer Modification");
        CustomerData customer = MainClient.getCustomer(eMail);
        if(customer!=null) {
        	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        	nameField = new JTextField(customer.getName());
            surnameField = new JTextField(customer.getSurname());
            birthDateField = new JTextField(dateFormat.format(customer.getDateOfBirth()));
            emailField = new JTextField(eMail);
        } else {
        	JOptionPane.showMessageDialog(this, resourceBundle.getString("customer_not_found_message"), "Error", JOptionPane.ERROR_MESSAGE);
            
        }

        addComponentsToForm();

        setVisible(true);
    }

    private void setupUI(String title) {
        setTitle(title);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window
        setLayout(new BorderLayout());

        ImageIcon img = new ImageIcon("src/resources/registerUserIcon.png");
        setIconImage(img.getImage());

        JPanel formPanel = new JPanel(new GridLayout(5, 1, 10, 10));
        formPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        submitButton.setBackground(new Color(0, 153, 204));
        submitButton.setForeground(Color.WHITE);
        submitButton.setFocusPainted(false);
        submitButton.setFont(new Font("Arial", Font.BOLD, 16));

        add(formPanel, BorderLayout.CENTER);
        add(submitButton, BorderLayout.SOUTH);
    }

    private void addComponentsToForm() {
        JPanel formPanel = (JPanel) getContentPane().getComponent(0);
        formPanel.setLayout(new GridLayout(0, 2, 10, 10)); // Two columns layout
        formPanel.add(new JLabel(resourceBundle.getString("name_label")));
        formPanel.add(nameField);
        formPanel.add(new JLabel(resourceBundle.getString("surname_label")));
        formPanel.add(surnameField);
        formPanel.add(new JLabel(resourceBundle.getString("birthdate_label")));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel(resourceBundle.getString("email_label")));
        formPanel.add(emailField);
    
        nameField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        surnameField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        birthDateField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        emailField.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
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

    /**
     * Registers a new customer.
     */
    public void registerUser() {
        String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = birthDateField.getText();
        String email = emailField.getText();

        Date birth;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birth = dateFormat.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, resourceBundle.getString("invalid_date_message"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CustomerData newUser = new CustomerData(email, name, surname, birth);
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/addcustomer");
		Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
		Response response = invocationBuilder.post(Entity.entity(newUser, MediaType.APPLICATION_JSON));
		if (response.getStatus() != Status.OK.getStatusCode()) {
			logger.error("Error connecting with the server. Code: {}",response.getStatus());
		} else {
			logger.info("Customer Correctly Registered :)");
            JOptionPane.showMessageDialog(this, "Customer Correctly Registered", "Success registering customer", JOptionPane.INFORMATION_MESSAGE);

		}

        nameField.setText("");
        surnameField.setText("");
        birthDateField.setText("");
        emailField.setText("");			
    }
    
    public void modifyUser() {
    	String name = nameField.getText();
        String surname = surnameField.getText();
        String birthDate = birthDateField.getText();
        String email = emailField.getText();

        Date birth;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            birth = dateFormat.parse(birthDate);
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, resourceBundle.getString("invalid_date_message"), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        CustomerData newCustomer = new CustomerData(email, name, surname, birth);
        WebTarget DeustoCarsWebTarget = ClientManager.getInstance().getWebTarget().path("server/addcustomer");
        Invocation.Builder invocationBuilder = DeustoCarsWebTarget.request(MediaType.APPLICATION_JSON);
        Response response = invocationBuilder.post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON));
        
        if (response.getStatus() != Status.OK.getStatusCode()) {
            logger.info("Customer to modify found.");
            
            MainClient.deleteCustomer(email);

            response = invocationBuilder.post(Entity.entity(newCustomer, MediaType.APPLICATION_JSON));
            if (response.getStatus() != Status.OK.getStatusCode()) {
    			logger.error("Error connecting with the server. Code: {}",response.getStatus());
    		} else {
    			logger.info("Customer Correctly Registered :)");
    		}

            // Clear input fields
            nameField.setText("");
            surnameField.setText("");
            birthDateField.setText("");
            emailField.setText("");	
        } else {
            logger.error("Customer doesnt exist. Code: {}", response.getStatus());
        }
    }
    
    /**
     * Main method to launch the CustomerRegister window for user registration.
     * 
     * @param args Command line arguments.
     */
    public static void main(String[] args) {
        CustomerRegister customerRegistrationWindow = new CustomerRegister();
        customerRegistrationWindow.setVisible(true);
    }
}
