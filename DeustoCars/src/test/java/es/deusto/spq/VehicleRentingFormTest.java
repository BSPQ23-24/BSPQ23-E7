package es.deusto.spq;

import static org.junit.Assert.*;

import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.VehicleData;

public class VehicleRentingFormTest {




    @Test
    public void testCreateRentingWithConstructor() {
        // Crear un objeto Vehicle llamando al constructor
        Renting renting = new Renting("@gmail.com", "123ABC", new Date(), new Date());

        // Verificar que los atributos se hayan establecido correctamente
        assertEquals("@gmail.com", renting.getEmail());
        assertEquals("123ABC", renting.getLicensePlate());
        assertEquals(new Date(), renting.getStartDate());
        assertEquals(new Date(), renting.getEndDate());
    }

}


