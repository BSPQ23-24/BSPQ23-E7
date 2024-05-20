/**
 * This class represents the main client application for managing customers and vehicles.
 */
package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.client.controller.CustomerController;
import es.deusto.spq.client.controller.RentingController;
import es.deusto.spq.client.controller.VehicleController;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.Renting;
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
    private JButton retrieveVehicle;
    private JButton makeRent;

    private JTextField numberPlate;
    private JTextField eMail;
    protected static final Logger logger = LogManager.getLogger();

    static {resourceBundle = ResourceBundle.getBundle("SystemMessages", Locale.getDefault());}
    static Locale currentLocale = Locale.forLanguageTag("en");;

    // Static method to access the ResourceBundle instance
    public static ResourceBundle getResourceBundle() {
        return resourceBundle;
    }

    /**
     * Constructs a new MainClient object.
     * @param hostname The hostname of the server.
     * @param port The port of the server.
     */
    
    
    public MainClient(String hostname, String port, Locale locale) {
        resourceBundle = ResourceBundle.getBundle("SystemMessages", locale);

        logger.info(resourceBundle.getString("starting_msg"));

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
        retrieveVehicle = new JButton(resourceBundle.getString("retrieve_vehicle_button"));
        retrieveVehicle.setBackground(new Color(255, 168, 54));
        retrieveVehicle.setForeground(Color.WHITE);

        JLabel getCustomerLabel = new JLabel(resourceBundle.getString("search_delete_by_email_label"));
        eMail = new JTextField();
        getCustomer = new JButton(resourceBundle.getString("get_customer_button"));
        getCustomer.setBackground(new Color(46, 204, 113));
        getCustomer.setForeground(Color.WHITE);
        deleteCustomer = new JButton(resourceBundle.getString("delete_customer_button"));
        deleteCustomer.setBackground(new Color(231, 76, 60));
        deleteCustomer.setForeground(Color.WHITE);
        makeRent = new JButton(resourceBundle.getString("make_a_rent_button"));
        makeRent.setBackground(new Color(255, 168, 54));
        makeRent.setForeground(Color.WHITE);

        searchDeletePanel.add(getVehicleLabel);
        searchDeletePanel.add(numberPlate);
        searchDeletePanel.add(makeRent);
        searchDeletePanel.add(getVehicle);
        searchDeletePanel.add(deleteVehicle);
        searchDeletePanel.add(retrieveVehicle);
        
        searchDeletePanel.add(getCustomerLabel);
        searchDeletePanel.add(eMail);
        searchDeletePanel.add(new JLabel()); // Empty label for spacing
        searchDeletePanel.add(getCustomer);
        searchDeletePanel.add(deleteCustomer);

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
        getVehicle.addActionListener(e -> {
            String nPlate_search = numberPlate.getText();
            VehicleData vehicle = getVehicle(nPlate_search); 
            if (vehicle != null) {
                new VehicleDetailWindow(vehicle).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Vehicle not found for plate: " + nPlate_search);
            }
        });
        getCustomer.addActionListener(e -> {
            String email_search = eMail.getText();
            CustomerData customer = getCustomer(email_search); 
            if (customer != null) {
                // Open CustomerDetailWindow with the retrieved customer
                new CustomerDetailWindow(customer).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(this, "Customer not found for email: " + email_search);
            }
        });
        deleteVehicle.addActionListener(e -> deleteVehicle(numberPlate.getText()));
        deleteCustomer.addActionListener(e -> deleteCustomer(eMail.getText()));
        retrieveVehicle.addActionListener(e -> new VehicleRetrievalForm());
        makeRent.addActionListener(e -> new VehicleRentingForm());


        // Language selection dropdown
        String[] languages = {"English", "Español", "Euskara"};
        JComboBox<String> languageDropdown = new JComboBox<>(languages);
        languageDropdown.setBackground(new Color(211, 126, 242));

        String localeString = currentLocale.toString();
        if (localeString.equals(Locale.forLanguageTag("es").toString())) {
            languageDropdown.setSelectedItem("Español");
        } else if (localeString.equals(Locale.forLanguageTag("en").toString())) {
            languageDropdown.setSelectedItem("English");
        } else if (localeString.equals(Locale.forLanguageTag("eu").toString())) {
            languageDropdown.setSelectedItem("Euskara");
        }       

        languageDropdown.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent e) {
                String selectedLanguage = (String) languageDropdown.getSelectedItem();
                switch (selectedLanguage) {
                    case "Español":
                        changeLanguage(hostname, port, Locale.forLanguageTag("es"));
                    break;
                    case "Euskara":
                        changeLanguage(hostname, port, Locale.forLanguageTag("eu"));
                        break;
                    default:
                        changeLanguage(hostname, port, Locale.forLanguageTag("en"));
                }
                revalidate();
                repaint();
            }
        });

        JPanel languagePanel = new JPanel(new BorderLayout());
        languagePanel.add(languageDropdown, BorderLayout.EAST);
        add(languagePanel, BorderLayout.SOUTH);


        setVisible(true);
    }

    private void changeLanguage(String hostname, String port, Locale locale) {
        setVisible(false);
        SwingUtilities.invokeLater(() -> {
            new MainClient(hostname, port, locale).setVisible(true);
        });

        currentLocale = locale;

        dispose();
    }



    /**
     * The main method to start the application.
     * @param args Command line arguments containing the hostname and port of the server.
     */
    public static void main(String[] args) {
    	String hostname = args[0];
        String port = args[1];
        ServiceLocator.getInstance().setWebTarget(hostname, port);
        new MainClient(hostname, port, Locale.forLanguageTag("en"));
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

        ImageIcon img = new ImageIcon("src/resources/customerListIcon.png");
        tableFrame.setIconImage(img.getImage());

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
        JTable table = new JTable(dataArray, column) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane pane = new JScrollPane(table);
        searchPanel.add(pane, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit Customer");
        editButton.setBackground(new Color(0, 153, 204));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String email = table.getValueAt(selectedRow, 0).toString();
                new CustomerRegister(email); 
            }
        });
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String email = table.getValueAt(row, 0).toString();
                        CustomerData customer = getCustomer(email); 
                        new CustomerDetailWindow(customer).setVisible(true);
                    }
                }
            }
        });
        searchPanel.add(editButton, BorderLayout.SOUTH);

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

        ImageIcon img = new ImageIcon("src/resources/vehicleListIcon.png");
        tableFrame.setIconImage(img.getImage());

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
        JTable table = new JTable(dataArray, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        JScrollPane pane = new JScrollPane(table);
        searchPanel.add(pane, BorderLayout.CENTER);

        JButton editButton = new JButton("Edit Vehicle");
        editButton.setBackground(new Color(0, 153, 204));
        editButton.setForeground(Color.WHITE);
        editButton.setFocusPainted(false);
        editButton.setFont(new Font("Arial", Font.BOLD, 16));
        editButton.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                String plate = (String) table.getValueAt(selectedRow, 0);
                new VehicleRegistration(plate);
            }
        });
        searchPanel.add(editButton, BorderLayout.SOUTH);
        
        table.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    int row = table.getSelectedRow();
                    if (row != -1) {
                        String nPlate = table.getValueAt(row, 0).toString();
                        VehicleData vehicle = getVehicle(nPlate); 
                        new VehicleDetailWindow(vehicle).setVisible(true);
                    }
                }
            }
        });
        
        tableFrame.setVisible(true);
    }

    /**
     * Retrieves a Customer object from the server based on the provided email.
     *
     * @param eMail The email of the customer to retrieve.
     * @return The Customer object retrieved from the server, or null if not found or an error occurred.
     */
    public static CustomerData getCustomer(String eMail) {
        return CustomerController.getCustomer(eMail);
    }

    /**
     * Retrieves a Vehicle object from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return The Vehicle object retrieved from the server, or null if not found or an error occurred.
     */
    public static VehicleData getVehicle(String numberPlate) {
        return VehicleController.getVehicle(numberPlate);
    }

    /**
     * Retrieves a list of Customer objects from the server.
     *
     * @return A list of Customer objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<CustomerData> getCustomers() {
        return CustomerController.getCustomers();
    }

    /**
     * Retrieves a list of Vehicle objects from the server.
     *
     * @return A list of Vehicle objects retrieved from the server, or an empty list if no vehicles found or an error occurred.
     */
    public static List<VehicleData> getVehicles() {
        return VehicleController.getVehicles();
    }

    /**
     * Deletes a vehicle from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to delete.
     */
    public static void deleteVehicle(String numberPlate) {
        VehicleController.deleteVehicle(numberPlate);
    }
    
    /**
     * Deletes a customer from the server based on the provided email.
     *
     * @param eMail The email of the customer to delete.
     */
    public static void deleteCustomer(String eMail) {
        CustomerController.deleteCustomer(eMail);
    }
    
    public void addCustomer(CustomerData customer) {
        CustomerController.addCustomer(customer);
    }
    
    
    public void addVehicle(VehicleData vehicle) {
        VehicleController.addVehicle(vehicle);
    }
    
    /**
     * Retrieves a list of Renting objects associated with a customer from the server.
     *
     * @return A list of Renting objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<Renting> getCustomerRents(String eMail) {
        return RentingController.getCustomerRents(eMail);
    }
    /**
     * Retrieves a list of Renting objects associated with a vehicle from the server.
     *
     * @return A list of Renting objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<Renting> getVehicleRents(String numberPlate) {
        return RentingController.getVehicleRents(numberPlate);
    }

}

