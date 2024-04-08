package es.deusto.spq.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;

import es.deusto.spq.CustomerRegister;
import es.deusto.spq.VehicleRegistration;
import es.deusto.spq.db.Database;


public class ClientGUI extends JFrame{
	private static final long serialVersionUID = 1L;
	private JButton editClient;
	private JButton addClient;
	private JButton editVehicle;
	private JButton addVehicle;
	
	public ClientGUI() {
        setTitle("Client");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(3, 2));

        JLabel client = new JLabel("Client:");
        addClient = new JButton("Add Client");
        editClient = new JButton("Edit Client");
        JLabel vehicle = new JLabel("Vehicle:");
        addVehicle = new JButton("Add Vehicle");
        editVehicle = new JButton("Edit Vehicle");

        add(client);
        add(vehicle);
        add(addClient);
        add(addVehicle);
        add(editClient);
        add(editVehicle);

        addClient.addActionListener(e -> new CustomerRegister());
        addVehicle.addActionListener(e -> new VehicleRegistration());
        editClient.addActionListener(e -> TableCustomersWindow());
        editVehicle.addActionListener(e -> TableVehicleWindow());

        setVisible(true);
    }
	
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
	                int Id = (int) table.getValueAt(selectedRow, 0);
	                new CustomerRegister(Id); 
	            }
	        });
	        editPane.add(editButton);
	    } catch (SQLException e) {
	        System.out.println("Error accessing database: " + e.getMessage());
	        e.printStackTrace();
	    }


		tableFrame.setVisible(true);
	}
	
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
	        System.out.println("Error accessing database: " + e.getMessage());
	        e.printStackTrace();
	    }


		tableFrame.setVisible(true);
	}
}
