package es.deusto.spq.client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JTabbedPane;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
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
        // You can either update the values of existing categories or add new ones.
        dataset.setValue("Category A", Math.random() * 100);  // Existing category, updating value
        dataset.setValue("Category B", Math.random() * 100);  // New category
        dataset.setValue("Category C", Math.random() * 100);  // Another new category
    }


    private void updateDatasetFromServer(DefaultCategoryDataset dataset) {
        // Directly manipulate barDataset
        dataset.setValue(Math.random() * 100, "Row 1", "Column 1");
        dataset.setValue(Math.random() * 100, "Row 1", "Column 2");
        dataset.setValue(Math.random() * 100, "Row 1", "Column 3");
    }

    private void updateDatasetFromServer(XYSeriesCollection dataset) {
        // Clear the dataset first to prevent old data accumulation if needed
        dataset.removeAllSeries();

        // Simulate adding random data to 3 different series
        for (int seriesId = 1; seriesId <= 3; seriesId++) {
            XYSeries series = new XYSeries("Series " + seriesId);

            // Adding 5 points to each series for illustration
            for (int i = 1; i <= 5; i++) {
                series.add(i, Math.random() * 100);
            }
            
            dataset.addSeries(series);
        }
    }


    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            AdminClient ex = new AdminClient();
            ex.setVisible(true);
        });
    }
}