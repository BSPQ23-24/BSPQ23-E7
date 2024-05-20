package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import java.util.List;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

import org.jfree.chart.plot.PlotOrientation;

public class AdminClient extends JFrame {
	private static final long serialVersionUID = 1L;
	
	private JTabbedPane tabbedPane;
    private ChartPanel pieChartPanel, barChartPanel, lineChartPanel;
    private DefaultPieDataset<String> pieDataset;
    private DefaultCategoryDataset barDataset;
    private XYSeriesCollection lineDataset;


    public AdminClient() {
        ImageIcon img = new ImageIcon("src/resources/analystIcon.png");
        setIconImage(img.getImage());
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        initUI();
    }

    private void initUI() {
        tabbedPane = new JTabbedPane();
        
        initializeDatasets();
        
        // Panels for different charts
        pieChartPanel = createPieChartPanel();
        barChartPanel = createBarChartPanel();
        lineChartPanel = createLineChartPanel();

        // Adding to tabbed pane
        tabbedPane.add("Pie Chart", pieChartPanel);
        tabbedPane.add("Bar Chart", barChartPanel);
        tabbedPane.add("Line Chart", lineChartPanel);

        getContentPane().add(tabbedPane, BorderLayout.CENTER);

        // Refresh button
        JButton refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(this::refreshAction);
        getContentPane().add(refreshButton, BorderLayout.SOUTH);
    }

    private void refreshAction(ActionEvent event) {
        updateCharts();
    }
    
    private void initializeDatasets() {
        pieDataset = new DefaultPieDataset<String>();
        pieDataset.setValue("Initial Category", 0);

        barDataset = new DefaultCategoryDataset();
        barDataset.setValue(0, "Initial Row", "Initial Column");

        lineDataset = new XYSeriesCollection();
        XYSeries series = new XYSeries("Series 1");
        series.add(0, 0);  // Start with an initial point
        lineDataset.addSeries(series);
    }


    private void updateCharts() {
        updatePieChart();
        updateBarChart();
        updateLineChart();
    }

    private ChartPanel createPieChartPanel() {
        DefaultPieDataset<String> dataset = new DefaultPieDataset<String>();
        updateDatasetFromServer(dataset);
        JFreeChart chart = ChartFactory.createPieChart("Sample Pie Chart", dataset, true, true, false);
        return new ChartPanel(chart);
    }

    private ChartPanel createBarChartPanel() {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        updateDatasetFromServer(dataset);
        JFreeChart chart = ChartFactory.createBarChart(
                "Sample Bar Chart", 
                "Category", 
                "Value", 
                dataset, 
                PlotOrientation.VERTICAL, 
                true, true, false);
        return new ChartPanel(chart);
    }

    private ChartPanel createLineChartPanel() {
        XYSeriesCollection dataset = new XYSeriesCollection();
        updateDatasetFromServer(dataset);
        JFreeChart chart = ChartFactory.createXYLineChart(
                "Sample Line Chart",
                "X",
                "Y",
                dataset,
                PlotOrientation.VERTICAL, 
                true, true, false);
        return new ChartPanel(chart);
    }

    private void updatePieChart() {
        updateDatasetFromServer(pieDataset);
        tabbedPane.remove(pieChartPanel);
        pieChartPanel = createPieChartPanel();
        tabbedPane.add("Pie Chart", pieChartPanel);
        
    }

    private void updateBarChart() {
        updateDatasetFromServer(barDataset);
        tabbedPane.remove(barChartPanel);
        barChartPanel = createBarChartPanel();
        tabbedPane.add("Bar Chart", barChartPanel);
    }

    private void updateLineChart() {
        updateDatasetFromServer(lineDataset);
        tabbedPane.remove(lineChartPanel);
        lineChartPanel = createLineChartPanel();
        tabbedPane.add("Line Chart", lineChartPanel);
    }

    private void updateDatasetFromServer(DefaultPieDataset<String> dataset) {
        List<VehicleData> vehicles = MainClient.getVehicles();
        Map<String, Integer> brandCount = new HashMap<>();
        for (VehicleData vehicle : vehicles) {
            brandCount.merge(vehicle.getBrand(), 1, Integer::sum);
        }
        brandCount.forEach(dataset::setValue);
    }


    private void updateDatasetFromServer(DefaultCategoryDataset dataset) {
        List<VehicleData> vehicles =  MainClient.getVehicles();
        int readyToBorrow = 0, onRepair = 0;
        for (VehicleData vehicle : vehicles) {
            if (vehicle.isReadyToBorrow()) readyToBorrow++;
            if (vehicle.isOnRepair()) onRepair++;
        }
        dataset.setValue(readyToBorrow, "Status", "Ready to Borrow");
        dataset.setValue(onRepair, "Status", "On Repair");
    }

    private void updateDatasetFromServer(XYSeriesCollection dataset) {
        List<CustomerData> customers = MainClient.getCustomers();
        Map<Integer, Integer> registrationsPerYear = new TreeMap<>();
        SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy");

        for (CustomerData customer : customers) {
            int year = Integer.parseInt(yearFormat.format(customer.getDateOfBirth()));
            registrationsPerYear.merge(year, 1, Integer::sum);
        }

        XYSeries series = new XYSeries("Registrations");
        registrationsPerYear.forEach(series::add);
        dataset.addSeries(series);
    }



    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AdminClient ex = new AdminClient();
            ex.setVisible(true);
        });
    }
}