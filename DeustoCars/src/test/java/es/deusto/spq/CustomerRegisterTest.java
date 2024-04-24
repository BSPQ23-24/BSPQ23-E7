package es.deusto.spq;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import es.deusto.spq.pojo.CustomerData;

import java.util.Calendar;
import java.util.Date;

/**
 * Unit test for CustomerRegister class.
 */
public class CustomerRegisterTest {

    /**
     * Test for updateDatabase method.
     */
    @Test
    public void testUpdateDatabase() {
        // Crear una instancia de CustomerData para simular los datos de un cliente
        CustomerData customer = new CustomerData("test@example.com", "Test", "User", getCurrentDate());

        // Crear una instancia de CustomerRegister
        CustomerRegister customerRegister = new CustomerRegister();

        // Llamar a la función updateDatabase con los datos del cliente
        boolean result = customerRegister.updateDatabase(customer);

        // Verificar si la actualización de la base de datos fue exitosa
        assertTrue(result);
    }

    /**
     * Obtener la fecha actual.
     */
    private Date getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        return calendar.getTime();
    }
}