package es.deusto.spq;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.VehicleData;

public class VehicleRentingFormTest {




    @Test
    public void testCreateRentingWithConstructor() {
        CustomerData customer = new CustomerData();
        customer.seteMail("@gmail.com");
        VehicleData vehicle = new VehicleData();
        vehicle.setNumberPlate("123ABC");
        // Crear un objeto Vehicle llamando al constructor
        Renting renting = new Renting(customer, vehicle, new Date(), new Date());

        // Verificar que los atributos se hayan establecido correctamente
        assertEquals("@gmail.com", renting.getCustomer().geteMail());
        assertEquals("123ABC", renting.getVehicle().getNumberPlate());
        assertEquals(new Date(), renting.getStartDate());
        assertEquals(new Date(), renting.getEndDate());
    }

}


