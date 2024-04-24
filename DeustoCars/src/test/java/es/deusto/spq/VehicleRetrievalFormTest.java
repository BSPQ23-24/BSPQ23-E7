package es.deusto.spq;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import javax.swing.JTextField;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;

public class VehicleRetrievalFormTest {
    private VehicleRetrievalForm form;
    private JTextField plateField;
    private WebTarget webTarget;
    private Invocation.Builder invocationBuilder;
    private Response response;

    @Before
    public void setUp() {
        form = new VehicleRetrievalForm();
        plateField = new JTextField();
        form.setPlateField(plateField);

        // Mocking the web target and response
        webTarget = mock(WebTarget.class);
        invocationBuilder = mock(Invocation.Builder.class);
        response = mock(Response.class);
        when(ClientManager.getInstance().getWebTarget().path(anyString())).thenReturn(webTarget);
        when(webTarget.request(MediaType.APPLICATION_JSON)).thenReturn(invocationBuilder);
        when(invocationBuilder.post(any(Entity.class))).thenReturn(response);
    }

    @Test
    public void testRetrieveVehicle() {
        plateField.setText("ABC123");
        form.retrieveVehicle();
        assertEquals("", plateField.getText());
        verify(response, times(1)).getStatus();
    }

    @Test
    public void testUpdateDatabase() {
        assertTrue(form.updateDatabase("ABC123"));
    }
}
