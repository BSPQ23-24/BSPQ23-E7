package es.deusto.spq;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.VehicleData;

public class VehicleRegisterTest {


    @Test
    public void testCreateVehicleWithConstructor() {
        // Crear un objeto Vehicle llamando al constructor
        VehicleData vehicle = new VehicleData("123ABC", "Toyota", "Corolla", true, false);

        // Verificar que los atributos se hayan establecido correctamente
        assertEquals("123ABC", vehicle.getNumberPlate());
        assertEquals("Toyota", vehicle.getBrand());
        assertEquals("Corolla", vehicle.getModel());
    }

}
