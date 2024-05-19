package es.deusto.spq.client;

import java.util.Calendar;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import es.deusto.spq.pojo.CustomerData;

public class CustomerDetailWindow extends JFrame{

	public CustomerDetailWindow(CustomerData customer) {
        setTitle("Customer Detail");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        // Top panel
        JPanel topPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        // Icon
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(150, 150));
        ImageIcon img = new ImageIcon("src/resources/user.png");
        setIconImage(img.getImage());
        JLabel iconLabel = new JLabel(resizeIcon("src/resources/user.png", new Dimension(100, 100)));
        GridBagConstraints iconGbc = new GridBagConstraints();
        iconGbc.gridx = 1; 
        iconGbc.gridy = 0;
        iconGbc.insets = new Insets(0, 30, 0, 0); 
        iconPanel.add(iconLabel, iconGbc);
        topPanel.add(iconPanel, BorderLayout.WEST);

        // Details
        JPanel detailsPanel = new JPanel(new GridBagLayout());
        detailsPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        topPanel.add(detailsPanel, BorderLayout.CENTER);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 20); 
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0;
        gbc.gridy = 0;
        detailsPanel.add(new JLabel("Name:"), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(customer.getName()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel("Surname:"), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(customer.getSurname()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel("Email:"), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(customer.geteMail()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(new JLabel("Date of Birth:"), gbc);

        gbc.gridx = 1;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        detailsPanel.add(new JLabel(dateFormat.format(customer.getDateOfBirth())), gbc);

        // Table
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        JLabel tableLabel = new JLabel("Vehicles Rented");
        tableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(tableLabel, BorderLayout.NORTH);

        String[] columnNames = {"Plate Number", "Rent Date", "Retrieval Date"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane tableScrollPane = new JScrollPane(table);
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);
        // Examples to be replaced
        tableModel.addRow(new Object[]{"ABC123", "2023-05-01", "2023-05-10"});
        tableModel.addRow(new Object[]{"XYZ789", "2023-05-15", "2023-05-20"});
    }
	
	private ImageIcon resizeIcon(String path, Dimension size) {
        try {
            BufferedImage img = ImageIO.read(new File(path));
            Image scaledImage = img.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledImage);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
	
	public static void main(String[] args) {
		SwingUtilities.invokeLater(() -> {
            CustomerData customer = new CustomerData("John", "Doe", "john.doe@example.com", new Date(90, 5, 19)); // Example date
            new CustomerDetailWindow(customer).setVisible(true);
        });
    }
}