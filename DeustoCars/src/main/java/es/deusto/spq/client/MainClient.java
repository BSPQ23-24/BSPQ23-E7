/**
 * This class represents the main client application for managing customers and vehicles.
 */
package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.CustomerRegister;
import es.deusto.spq.VehicleRegistration;
import es.deusto.spq.db.Database;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

/**
 * This class represents the main client application for managing customers and vehicles.
 */
public class MainClient extends JFrame {
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

    /**
     * Constructs a new MainClient object.
     * @param hostname The hostname of the server.
     * @param port The port of the server.
     */
    public MainClient(String hostname, String port) {
        setTitle("Client");
        setSize(900, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(2, 1));

        JPanel topPanel = new JPanel(new GridLayout(1, 2));
        topPanel.setBackground(new Color(240, 240, 240)); 
        add(topPanel);

        JPanel clientPanel = new JPanel(new GridLayout(3, 1));
        clientPanel.setBorder(BorderFactory.createTitledBorder("Customer"));
        topPanel.add(clientPanel);

        addClient = new JButton("Add Customer");
        addClient.setBackground(new Color(52, 103, 220)); 
        addClient.setForeground(Color.WHITE); 
        editClient = new JButton("Edit Customer");
        editClient.setBackground(new Color(52, 152, 219)); 
        editClient.setForeground(Color.WHITE); 
        clientPanel.add(new JLabel("Actions:"));
        clientPanel.add(addClient);
        clientPanel.add(editClient);

        ImageIcon img = new ImageIcon("src/resources/bottom-bar-icon.png");
        setIconImage(img.getImage());

        JPanel vehiclePanel = new JPanel(new GridLayout(3, 1));
        vehiclePanel.setBorder(BorderFactory.createTitledBorder("Vehicle"));
        topPanel.add(vehiclePanel);

        addVehicle = new JButton("Add Vehicle");
        addVehicle.setBackground(new Color(52, 103, 220)); 
        addVehicle.setForeground(Color.WHITE); 
        editVehicle = new JButton("Edit Vehicle");
        editVehicle.setBackground(new Color(52, 152, 219)); 
        editVehicle.setForeground(Color.WHITE); 
        vehiclePanel.add(new JLabel("Actions:"));
        vehiclePanel.add(addVehicle);
        vehiclePanel.add(editVehicle);

        JPanel searchDeletePanel = new JPanel(new GridLayout(4, 3));
        searchDeletePanel.setBorder(BorderFactory.createTitledBorder("Search/Delete"));
        searchDeletePanel.setBackground(new Color(240, 240, 240)); 
        add(searchDeletePanel);

        JLabel getVehicleLabel = new JLabel("Search/Delete Vehicle using number plate:");
        numberPlate = new JTextField();
        getVehicle = new JButton("Get Vehicle");
        getVehicle.setBackground(new Color(46, 204, 113)); 
        getVehicle.setForeground(Color.WHITE); 
        deleteVehicle = new JButton("Delete Vehicle");
        deleteVehicle.setBackground(new Color(231, 76, 60)); 
        deleteVehicle.setForeground(Color.WHITE); 

        JLabel getCustomerLabel = new JLabel("Search/Delete Customer using eMail:");
        eMail = new JTextField();
        getCustomer = new JButton("Get Customer");
        getCustomer.setBackground(new Color(46, 204, 113)); 
        getCustomer.setForeground(Color.WHITE); 
        deleteCustomer = new JButton("Delete Customer");
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

        // Add blue border to text fields
        numberPlate.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));
        eMail.setBorder(BorderFactory.createLineBorder(new Color(0, 153, 204), 2));

        addClient.addActionListener(e -> new CustomerRegister());
        addVehicle.addActionListener(e -> new VehicleRegistration());
        editClient.addActionListener(e -> TableCustomersWindow());
        editVehicle.addActionListener(e -> TableVehicleWindow());
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
        tableFrame.setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        Toolkit screen = Toolkit.getDefaultToolkit();
        tableFrame.setLocation((screen.getScreenSize().width - tableFrame.getSize().width) / 2,
                (screen.getScreenSize().height - tableFrame.getSize().height) / 2);

        JPanel searchPanel = new JPanel(new BorderLayout());
        searchPanel.setBorder(new EmptyBorder(30, 30, 30, 30));
        searchPanel.setBackground(Color.white);
        tableFrame.setContentPane(searchPanel);

        try {
            ResultSet allCustomers = Database.getInstance().ejecutarConsulta("SELECT * FROM customers");
            java.sql.ResultSetMetaData metaData = allCustomers.getMetaData();

            List<Object[]> data = new ArrayList<>();
            while (allCustomers.next()) {
                Object[] row = new Object[metaData.getColumnCount()];
                for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                    row[columnIndex - 1] = allCustomers.getObject(columnIndex);
                }
                data.add(row);
            }
            Object[][] dataArray = new Object[data.size()][];
            data.toArray(dataArray);

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
        } catch (SQLException e) {
            logger.info("Error accessing database: " + e.getMessage());
            e.printStackTrace();
        }

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

        try {
            ResultSet allVehicles = Database.getInstance().ejecutarConsulta("SELECT * FROM vehicles");
            java.sql.ResultSetMetaData metaData = allVehicles.getMetaData();

            List<Object[]> data2 = new ArrayList<>();
            while (allVehicles.next()) {
                Object[] row2 = new Object[metaData.getColumnCount()];
                for (int columnIndex = 1; columnIndex <= metaData.getColumnCount(); columnIndex++) {
                    row2[columnIndex - 1] = allVehicles.getObject(columnIndex);
                }
                data2.add(row2);
            }
            Object[][] dataArray2 = new Object[data2.size()][];
            data2.toArray(dataArray2);

            String[] column2 = {"Plate", "Brand", "Model", "Ready To Borrow", "On Repair"};
            JTable table = new JTable(dataArray2, column2);

            JScrollPane pane = new JScrollPane(table);
            searchPanel.add(pane, BorderLayout.CENTER);

            JPanel editPane = new JPanel(new GridLayout(2, 1, 0, 0));
            editPane.setBackground(Color.white);
            searchPanel.add(editPane, BorderLayout.SOUTH);

            JButton editButton = new JButton("Edit Vehicle");
            editButton.addActionListener(e -> {
                int selectedRow2 = table.getSelectedRow();
                if (selectedRow2 >= 0) {
                    String Plate2 = (String) table.getValueAt(selectedRow2, 0);
                    new VehicleRegistration(Plate2); 
                }
            });
            editPane.add(editButton);
        } catch (SQLException e) {
            logger.info("Error accessing database: " + e.getMessage());
            e.printStackTrace();
        }

        tableFrame.setVisible(true);
    }

    /**
     * Retrieves a Customer object from the server based on the provided email.
     *
     * @param eMail The email of the customer to retrieve.
     * @return The Customer object retrieved from the server, or null if not found or an error occurred.
     */
    public CustomerData getCustomer(String eMail) {
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
    public VehicleData getVehicle(String numberPlate) {
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
    public List<CustomerData> getCustomers() {
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/getcustomers")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<CustomerData>>() {});
        } else {
            logger.info("ERROR getting customers");
            return Collections.emptyList();
        }
    }

    /**
     * Retrieves a list of Vehicle objects from the server.
     *
     * @return A list of Vehicle objects retrieved from the server, or an empty list if no vehicles found or an error occurred.
     */
    public List<VehicleData> getVehicles() {
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
        Response response = ClientManager.getInstance().getWebTarget()
                .path("server/deletevehicle")
                .queryParam("numberPlate", numberPlate)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Deleting vehicle: " + response.toString());
        } else {
            logger.info("ERROR deleting vehicle");
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
}
