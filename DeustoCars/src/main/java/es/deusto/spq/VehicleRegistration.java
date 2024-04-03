package software;

import javax.swing.*;
import java.awt.*;
import java.sql.*;


/**
 * Clase para el registro de vehículos utilizando una interfaz gráfica de usuario.
 */

public class VehicleRegistration extends JFrame {
    private JTextField brandField;
    private JTextField numberPlateField;
    private JTextField modelField;
    private JCheckBox readyToBorrowCheckbox;
    private JCheckBox onRepairCheckbox;
    private JButton submitButton;

    /**
     * Constructor para la ventana de registro de vehículos.
     */
    
    public VehicleRegistration() {
        setTitle("Registro de Vehículo");
        setSize(600, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(6, 2));

        JLabel brandLabel = new JLabel("Marca:");
        brandField = new JTextField();
        JLabel numberPlateLabel = new JLabel("Matrícula:");
        numberPlateField = new JTextField();
        JLabel modelLabel = new JLabel("Modelo:");
        modelField = new JTextField();
        readyToBorrowCheckbox = new JCheckBox("Listo para prestar", true);
        onRepairCheckbox = new JCheckBox("En reparación", false);
        submitButton = new JButton("Registrar Vehículo");

        add(brandLabel);
        add(brandField);
        add(numberPlateLabel);
        add(numberPlateField);
        add(modelLabel);
        add(modelField);
        add(readyToBorrowCheckbox);
        add(onRepairCheckbox);
        add(new JLabel());
        add(submitButton);

        submitButton.addActionListener(e -> registerVehicle());

        setVisible(true);

      
    }

    /**
     * Registra un nuevo vehículo en la base de datos y lo agrega a la lista de vehículos.
     */
    
    private void registerVehicle() {
        String brand = brandField.getText();
        String numberPlate = numberPlateField.getText();
        String model = modelField.getText();
        boolean readyToBorrow = readyToBorrowCheckbox.isSelected();
        boolean onRepair = onRepairCheckbox.isSelected();

        // Crear el nuevo vehículo
        Vehicle newVehicle = new Vehicle(brand, numberPlate, model);
        newVehicle.setReadyToBorrow(readyToBorrow);
        newVehicle.setOnRepair(onRepair);

        // Limpiar los campos de entrada
        brandField.setText("");
        numberPlateField.setText("");
        modelField.setText("");
        readyToBorrowCheckbox.setSelected(true);
        onRepairCheckbox.setSelected(false);

        // Conectar y actualizar la base de datos
        updateDatabase(newVehicle);
    }

    /**
     * Conecta a la base de datos MySQL y actualiza la información del nuevo vehículo.
     *
     */
    
    private void updateDatabase(Vehicle vehicle) {
        // Datos de conexión a la base de datos
        String url = "jdbc:mysql://localhost:3306/PONER_DATABASE_NAME";
        String username = "PONER_USERNAME";
        String password = "PONER_PASSWORD";

        try {
            // Establecer la conexión con la base de datos
            Connection connection = DriverManager.getConnection(url, username, password);

            // Consulta SQL para insertar el vehículo en la base de datos
            String query = "INSERT INTO vehicles (brand, number_plate, model, ready_to_borrow, on_repair) " +
                           "VALUES (?, ?, ?, ?, ?)";

            // Preparar la declaración SQL
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, vehicle.getBrand());
            preparedStatement.setString(2, vehicle.getNumberPlate());
            preparedStatement.setString(3, vehicle.getModel());
            preparedStatement.setBoolean(4, vehicle.isReadyToBorrow());
            preparedStatement.setBoolean(5, vehicle.isOnRepair());

            // Ejecutar la consulta SQL
            preparedStatement.executeUpdate();

            // Cerrar la conexión y liberar recursos
            preparedStatement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Método principal para iniciar la aplicación.
     *
     */
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(VehicleRegistration::new);
    }
}
