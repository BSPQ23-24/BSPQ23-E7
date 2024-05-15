/**
 * This class represents the main client application for managing customers and vehicles.
 */
package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingWorker;
import javax.swing.border.EmptyBorder;
//import javax.swing.text.html.parser.Entity;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.CustomerRegister;
import es.deusto.spq.VehicleRegistration;
import es.deusto.spq.db.Database;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.pojo.VehicleData;


/**
 * This class represents the main client application for managing customers and vehicles.
 */
public class MainClient extends JFrame {
    private static ResourceBundle resourceBundle;

    private JButton editClient;
    private JButton addClient;
    private JButton editVehicle;
    private JButton addVehicle;
    private JButton getCustomer;
    private JButton getVehicle;
    private JButton deleteCustomer;
    private JButton deleteVehicle;

    private JTextField numberPlate;
    private JTextField eMail;
    protected static final Logger logger = LogManager.getLogger();
	
	protected Client client;
	protected WebTarget webTarget;

    static {
        // English (default):
        //resourceBundle = ResourceBundle.getBundle("SystemMessages", Locale.getDefault());

        // Spanish:
        resourceBundle = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("es"));

        // Basque:
        //resourceBundle = ResourceBundle.getBundle("SystemMessages", Locale.forLanguageTag("eu"));
    }

    // Static method to access the ResourceBundle instance
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Constructs a new MainClient object.
     * @param hostname The hostname of the server.
     * @param port The port of the server.
     */
    
    
    public MainClient(String hostname, String port) {
        logger.info(resourceBundle.getString("starting_msg"));

    	
		client = ClientBuilder.newClient();
		//ClientManager.getInstance().setWebTarget(hostname, port);
		webTarget = client.target(String.format("http://%s:%s/deustocars", hostname, port));
    	
        setTitle(resourceBundle.getString("main_client_title"));
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JLabel banner = new JLabel("DEUSTOCARS", JLabel.CENTER);
        banner.setOpaque(true);
        banner.setBackground(new Color(180, 227, 240));
        banner.setFont(new Font("Verdana", Font.BOLD | Font.ITALIC, 28));
        banner.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        add(banner, BorderLayout.NORTH);
        

        JPanel contentPanel = new JPanel(new GridLayout(2, 1));
        add(contentPanel, BorderLayout.CENTER);

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        contentPanel.add(topPanel);

        JPanel clientPanel = new JPanel(new GridLayout(3, 1));
        clientPanel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("client_panel_label")));
        clientPanel.setBackground(new Color(240, 248, 255));
        topPanel.add(clientPanel);

        addClient = new JButton(resourceBundle.getString("add_customer_label"));
        addClient.setBackground(new Color(52, 103, 220));
        addClient.setForeground(Color.WHITE);
        editClient = new JButton(resourceBundle.getString("edit_customer_label"));
        editClient.setBackground(new Color(52, 152, 219));
        editClient.setForeground(Color.WHITE);
        clientPanel.add(new JLabel(resourceBundle.getString("actions_label")));
        clientPanel.add(addClient);
        clientPanel.add(editClient);

        ImageIcon img = new ImageIcon("src/resources/bottom-bar-icon.png");
        setIconImage(img.getImage());

        JPanel vehiclePanel = new JPanel(new GridLayout(3, 1));
        vehiclePanel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("vehicle_panel_label")));
        vehiclePanel.setBackground(new Color(240, 248, 255));
        topPanel.add(vehiclePanel);

        addVehicle = new JButton(resourceBundle.getString("add_vehicle_label"));
        addVehicle.setBackground(new Color(52, 103, 220));
        addVehicle.setForeground(Color.WHITE);
        editVehicle = new JButton(resourceBundle.getString("edit_vehicle_label"));
        editVehicle.setBackground(new Color(52, 152, 219));
        editVehicle.setForeground(Color.WHITE);
        vehiclePanel.add(new JLabel(resourceBundle.getString("actions_label")));
        vehiclePanel.add(addVehicle);
        vehiclePanel.add(editVehicle);

        JPanel searchDeletePanel = new JPanel(new GridLayout(4, 3));
        searchDeletePanel.setBorder(BorderFactory.createTitledBorder(resourceBundle.getString("search_delete_panel_label")));
        searchDeletePanel.setBackground(new Color(240, 248, 255));
        contentPanel.add(searchDeletePanel);

        JLabel getVehicleLabel = new JLabel(resourceBundle.getString("search_delete_by_plate_label"));
        numberPlate = new JTextField();
        getVehicle = new JButton(resourceBundle.getString("get_vehicle_button"));
        getVehicle.setBackground(new Color(46, 204, 113));
        getVehicle.setForeground(Color.WHITE);
        deleteVehicle = new JButton(resourceBundle.getString("delete_vehicle_button"));
        deleteVehicle.setBackground(new Color(231, 76, 60));
        deleteVehicle.setForeground(Color.WHITE);

        JLabel getCustomerLabel = new JLabel(resourceBundle.getString("search_delete_by_email_label"));
        eMail = new JTextField();
        getCustomer = new JButton(resourceBundle.getString("get_customer_button"));
        getCustomer.setBackground(new Color(46, 204, 113));
        getCustomer.setForeground(Color.WHITE);
        deleteCustomer = new JButton(resourceBundle.getString("delete_customer_button"));
        deleteCustomer.setBackground(new Color(231, 76, 60));
        deleteCustomer.setForeground(Color.WHITE);

        searchDeletePanel.add(getVehicleLabel);
        searchDeletePanel.add(numberPlate);
        searchDeletePanel.add(getVehicle);
        searchDeletePanel.add(new JLabel()); // Empty label for spacing
        searchDeletePanel.add(deleteVehicle);
        searchDeletePanel.add(new JLabel()); // Empty label for spacing

        searchDeletePanel.add(getCustomerLabel);
        searchDeletePanel.add(eMail);
        searchDeletePanel.add(getCustomer);
        searchDeletePanel.add(new JLabel()); // Empty label for spacing
        searchDeletePanel.add(deleteCustomer);
        searchDeletePanel.add(new JLabel()); // Empty label for spacing

        numberPlate.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        eMail.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));

        addClient.addActionListener(e -> new CustomerRegister());
        addVehicle.addActionListener(e -> new VehicleRegistration());
        editClient.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    TableCustomersWindow();
                    return null;
                }
            };
            worker.execute();
        });
        editVehicle.addActionListener(e -> {
            SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
                @Override
                protected Void doInBackground() throws Exception {
                    TableVehicleWindow();
                    return null;
                }
            };
            worker.execute();
        });
        getVehicle.addActionListener(e -> getVehicle(numberPlate.getText()));
        getCustomer.addActionListener(e -> getCustomer(eMail.getText()));
        deleteVehicle.addActionListener(e -> deleteVehicle(numberPlate.getText()));
        deleteCustomer.addActionListener(e -> deleteCustomer(eMail.getText()));

        setVisible(true);
    }

    /**
     * The main method to start the application.
     * @param args Command line arguments containing the hostname and port of the server.
     */
    public static void main(String[] args) {
    	String hostname = args[0];
        String port = args[1];
        ClientManager.getInstance().setWebTarget(hostname, port);
        new MainClient(hostname, port);
    }

    /**
     * Displays a window for editing customer information.
     */
    public void TableCustomersWindow() {
        JFrame tableFrame = new JFrame();
        tableFrame.setTitle("Customers Window");
        tableFrame.setSize(600, 600);
        tableFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        Toolkit screen = Toolkit.getDefaultToolkit();
        tableFrame.setLocation((screen.getScreenSize().width - tableFrame.getSize().width) / 2,
                (screen.getScreenSize().height - tableFrame.getSize().height) / 2);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        searchPanel.setBackground(Color.white);
        tableFrame.setContentPane(searchPanel);

        List<CustomerData> allCustomers = getCustomers();

        // Ensure dataArray is properly formatted
        Object[][] dataArray = new Object[allCustomers.size()][4];
    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for (int i = 0; i < allCustomers.size(); i++) {
            CustomerData customer = allCustomers.get(i);
            dataArray[i][0] = customer.geteMail();
            dataArray[i][1] = customer.getName();
            dataArray[i][2] = customer.getSurname();
            dataArray[i][3] = dateFormat.format(customer.getDateOfBirth());  // Ensure Date is converted to a suitable format if necessary
        }

        String[] column = {"eMail", "Name", "Surname", "Date of Birth"};
        JTable table = new JTable(dataArray, column);

        JScrollPane pane = new JScrollPane(table);
        searchPanel.add(pane, BorderLayout.CENTER);

        JPanel editPane = new JPanel(new GridLayout(2, 1, 0, 0));
        editPane.setBackground(Color.white);
        searchPanel.add(editPane, BorderLayout.SOUTH);

        JButton editButton = new JButton("Edit Customer");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String email = table.getValueAt(selectedRow, 0).toString();
                new CustomerRegister(email); 
            }
        });
        editPane.add(editButton);

        tableFrame.setVisible(true);
    }

    /**
     * Displays a window for editing vehicle information.
     */
    public void TableVehicleWindow() {
        JFrame tableFrame = new JFrame();
        tableFrame.setTitle("Vehicles Window");
        tableFrame.setSize(600, 600);
        tableFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Toolkit screen = Toolkit.getDefaultToolkit();
        tableFrame.setLocation((screen.getScreenSize().width - tableFrame.getSize().width) / 2,
                (screen.getScreenSize().height - tableFrame.getSize().height) / 2);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        searchPanel.setBackground(Color.white);
        tableFrame.setContentPane(searchPanel);

        List<VehicleData> allVehicles = getVehicles();
        
        Object[][] dataArray = new Object[allVehicles.size()][5];
        for (int i = 0; i < allVehicles.size(); i++) {
            VehicleData vehicle = allVehicles.get(i);
            dataArray[i][0] = vehicle.getNumberPlate();
            dataArray[i][1] = vehicle.getBrand();
            dataArray[i][2] = vehicle.getModel();
            dataArray[i][3] = vehicle.isReadyToBorrow();  
            dataArray[i][4] = vehicle.isOnRepair(); 
        }

        String[] columnNames = {"Plate", "Brand", "Model", "Ready To Borrow", "On Repair"};
        JTable table = new JTable(dataArray, columnNames);

        JScrollPane pane = new JScrollPane(table);
        searchPanel.add(pane, BorderLayout.CENTER);

        JPanel editPane = new JPanel(new GridLayout(2, 1, 0, 0));
        editPane.setBackground(Color.white);
        searchPanel.add(editPane, BorderLayout.SOUTH);

        JButton editButton = new JButton("Edit Vehicle");
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String plate = (String) table.getValueAt(selectedRow, 0);
                new VehicleRegistration(plate);
            }
        });
        editPane.add(editButton);

        tableFrame.setVisible(true);
    }

    /**
     * Retrieves a Customer object from the server based on the provided email.
     *
     * @param eMail The email of the customer to retrieve.
     * @return The Customer object retrieved from the server, or null if not found or an error occurred.
     */
    public static CustomerData getCustomer(String eMail) {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/getcustomer")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Getting customer: " + response.toString());
            return response.readEntity(CustomerData.class);
        } else {
            logger.info("ERROR getting customer");
            return null;
        }
    }

    /**
     * Retrieves a Vehicle object from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return The Vehicle object retrieved from the server, or null if not found or an error occurred.
     */
    public static VehicleData getVehicle(String numberPlate) {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/getvehicle")
                .queryParam("numberPlate", numberPlate)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Getting vehicle: " + response.toString());
            return response.readEntity(VehicleData.class);
        } else {
            logger.info("ERROR getting vehicle");
            return null;
        }
    }

    /**
     * Retrieves a list of Customer objects from the server.
     *
     * @return A list of Customer objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<CustomerData> getCustomers() {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/getcustomers")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<CustomerData>>() {});
        } else {
        	logger.info("ERROR getting customers Code: {}", response.getStatus());
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of Vehicle objects from the server.
     *
     * @return A list of Vehicle objects retrieved from the server, or an empty list if no vehicles found or an error occurred.
     */
    public static List<VehicleData> getVehicles() {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/getvehicles")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<VehicleData>>() {});
        } else {
            logger.info("ERROR getting vehicles");
            return Collections.emptyList();
        }
    }

    /**
     * Deletes a vehicle from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to delete.
     */
    public void deleteVehicle(String numberPlate) {
        //WebTarget webTarget = ClientManager.getInstance().getWebTarget();
        logger.info("NumberPlate: " + numberPlate);
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
                    .path("server/deletevehicle")
                    .queryParam("numberPlate", numberPlate)
                    .request(MediaType.APPLICATION_JSON)
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Deleting vehicle: " + response.toString());
            } else {
                logger.info("ERROR deleting vehicle: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to delete vehicle.");
        }
    }


    /**
     * Deletes a vehicle from the server based on the provided email.
     *
     * @param numberPlate The number plate of the vehicle to delete.
     */
    public boolean deleteVehicleBoolean(String numberPlate) {
        WebTarget deleteVehicleWebTarget = webTarget.path("deletevehicle");

        Response response = deleteVehicleWebTarget.queryParam("numberPlate", numberPlate)
                .request(MediaType.APPLICATION_JSON).delete();

        if (response.getStatus() != Response.Status.OK.getStatusCode()) {
            logger.info("Error connecting with the server. Code: {}", response.getStatus());
            return false;
        } else {
            logger.info("Vehicle correctly deleted");
            return true;
        }
    }
    
    
    /**
     * Deletes a customer from the server based on the provided email.
     *
     * @param eMail The email of the customer to delete.
     */
    public void deleteCustomer(String eMail) {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/deletecustomer")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Deleting customer: " + response.toString());
        } else {
            logger.info("ERROR deleting customer");
        }
    }
    
    public static void addCustomer(CustomerData customer) {
        WebTarget webTarget = ClientManager.getInstance().getWebTarget();
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
                    .path("server/addcustomer")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(customer, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Adding customer: " + response.toString());
            } else {
                logger.info("ERROR adding customer: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to add customer.");
        }
    }
    
    
    public static void addVehicle(VehicleData vehicle) {
        WebTarget webTarget = ClientManager.getInstance().getWebTarget();
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
                    .path("server/addvehicle")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(vehicle, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Adding vehicle: " + response.toString());
            } else {
                logger.info("ERROR adding vehicle: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to add vehicle.");
        }
    }
}
