package es.deusto.spq;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.sql.Date;

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
        nameField = new JTextField();
        surnameField = new JTextField();
        birthDateField = new JTextField();
        emailField = new JTextField();
        webTarget = mock(WebTarget.class);
        invocationBuilder = mock(Invocation.Builder.class);
        response = mock(Response.class);
        when(ClientManager.getInstance().getWebTarget().path(anyString())).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
    }
    
    //DATABASE DOEST WORK
    @Test
    public void testRegisterUser() {
        nameField.setText("John");
        surnameField.setText("Doe");
        birthDateField.setText("1990-01-01");
        emailField.setText("john@example.com");
        form.registerUser();
        assertEquals("", nameField.getText());
        assertEquals("", surnameField.getText());
        assertEquals("", birthDateField.getText());
        assertEquals("", emailField.getText());
        verify(response, times(1)).getStatus();
    }

    @Test
    public void testUpdateDatabase() {
        CustomerData customer = new CustomerData("john@example.com", "John", "Doe", new Date(1990, 1, 1));
        assertTrue(form.updateDatabase(customer));
    }
}
