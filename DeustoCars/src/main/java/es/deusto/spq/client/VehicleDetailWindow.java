package es.deusto.spq.client;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.ResourceBundle;

import es.deusto.spq.client.controller.RentingController;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.VehicleData;


public class VehicleDetailWindow extends JFrame{
    private ResourceBundle resourceBundle = MainClient.getResourceBundle();
	
	public VehicleDetailWindow(VehicleData vehicle) {
        setTitle(resourceBundle.getString("vehicle_detail_title"));
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);
        

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        add(mainPanel);

        JPanel topPanel = new JPanel(new BorderLayout());
        mainPanel.add(topPanel, BorderLayout.NORTH);

        //Icon
        JPanel iconPanel = new JPanel();
        iconPanel.setPreferredSize(new Dimension(150, 150));
        ImageIcon img = new ImageIcon("src/resources/vehicle.png");
        setIconImage(img.getImage());
        JLabel iconLabel = new JLabel(resizeIcon("src/resources/vehicle.png", new Dimension(120, 120)));
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
        detailsPanel.add(new JLabel(resourceBundle.getString("number_plate_label")), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(vehicle.getNumberPlate()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        detailsPanel.add(new JLabel(resourceBundle.getString("brand_label")), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(vehicle.getBrand()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        detailsPanel.add(new JLabel(resourceBundle.getString("model_label")), gbc);

        gbc.gridx = 1;
        detailsPanel.add(new JLabel(vehicle.getModel()), gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        detailsPanel.add(new JLabel(resourceBundle.getString("ready_to_borrow_checkbox_label")), gbc);

        gbc.gridx = 1;
        JCheckBox readyToBorrowCheckBox = new JCheckBox();
        readyToBorrowCheckBox.setSelected(vehicle.isReadyToBorrow());
        readyToBorrowCheckBox.setEnabled(false);
        detailsPanel.add(readyToBorrowCheckBox, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        detailsPanel.add(new JLabel(resourceBundle.getString("on_repair_table_col")+":"), gbc);

        gbc.gridx = 1;
        JCheckBox onRepairCheckBox = new JCheckBox();
        onRepairCheckBox.setSelected(vehicle.isOnRepair());
        onRepairCheckBox.setEnabled(false);
        detailsPanel.add(onRepairCheckBox, gbc);

        // Table
        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        mainPanel.add(bottomPanel, BorderLayout.CENTER);

        JLabel tableLabel = new JLabel(resourceBundle.getString("renting_history_label"));
        tableLabel.setFont(new Font("Arial", Font.BOLD, 14));
        bottomPanel.add(tableLabel, BorderLayout.NORTH);

        String[] columnNames = {resourceBundle.getString("customer_table_col"), resourceBundle.getString("rent_date_table_col"), resourceBundle.getString("retrieval_date_table_col")};
        List<Renting> rentingList = RentingController.getVehicleRents(vehicle.getNumberPlate());
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Object[][] dataArray = new Object[rentingList.size()][3];
        for (int i = 0; i < rentingList.size(); i++) {
            Renting renting = rentingList.get(i);
            dataArray[i][0] = renting.getCustomer().geteMail();
            dataArray[i][1] = dateFormat.format(renting.getStartDate());
            dataArray[i][2] = dateFormat.format(renting.getEndDate());
        }
        JTable table = new JTable(dataArray, columnNames);
        JScrollPane tableScrollPane = new JScrollPane(table);
        bottomPanel.add(tableScrollPane, BorderLayout.CENTER);
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
	
}
