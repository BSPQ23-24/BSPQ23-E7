package es.deusto.spq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;

import javax.swing.JTextField;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

import es.deusto.spq.client.ClientManager;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

public class CustomerRegisterTest {
    private CustomerRegister form;
    private JTextField nameField;
    private JTextField surnameField;
    private JTextField birthDateField;
    private JTextField emailField;
    private WebTarget webTarget;
    private Invocation.Builder invocationBuilder;
    private Response response;

    @Before
    public void setUp() {
        form = new CustomerRegister();

    }
    
    @Test
    public void testCustomerRegistrationWindowOpening() {
        // Verificar que la ventana esté visible después de crear la instancia
        assertTrue(form.isVisible());
    }
 

	@Test
    public void testCreateCustomerWithConstructor() {
        // Crear un objeto Customer llamando al constructor

        CustomerData customer = new CustomerData("asier@gmail.com", "Asier", "Iturriotz", new Date(2002, 10, 31));
        
        // Verificar que los atributos se hayan establecido correctamente
        assertEquals("asier@gmail.com", customer.geteMail());
        assertEquals("Asier", customer.getName());
        assertEquals("Iturriotz", customer.getSurname());
        assertEquals(new Date(2002, 10, 31), customer.getDateOfBirth());
    }
    
    //DATABASE DOEST WORK

    @Test
    public void testUpdateDatabase() {
        CustomerData customer = new CustomerData("john@example.com", "John", "Doe", new Date(1990, 1, 1));
        assertTrue(form.updateDatabase(customer));
    }
}
