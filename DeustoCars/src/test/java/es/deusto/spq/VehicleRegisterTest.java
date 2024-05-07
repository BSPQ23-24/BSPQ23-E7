package es.deusto.spq;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.VehicleData;

public class VehicleRegisterTest {
    private VehicleRegistration vehicleRegistration;

    @Before
    public void setUp() {
        vehicleRegistration = new VehicleRegistration();
    }

    @Test
    public void testVehicleRegistrationWindowOpening() {
        // Verificar que la ventana esté visible después de crear la instancia
        assertTrue(vehicleRegistration.isVisible());
    }

    @Test
    public void testCreateVehicleWithConstructor() {
        // Crear un objeto Vehicle llamando al constructor
        VehicleData vehicle = new VehicleData("123ABC", "Toyota", "Corolla");

        // Verificar que los atributos se hayan establecido correctamente
        assertEquals("123ABC", vehicle.getNumberPlate());
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals("Corolla", vehicle.getModel());
    }

//    @Test
//    public void testUpdateDatabase() {
//        // Crear una instancia de VehicleData para simular los datos de un vehículo
//        VehicleData vehicle = new VehicleData("123ABC", "Toyota", "Corolla");
//
//        // Llamar a la función updateDatabase con los datos del vehículo
//        boolean result = vehicleRegistration.updateDatabase(vehicle);
//
//        // Verificar si la actualización de la base de datos fue exitosa
//        assertTrue(result);
//    }
}
