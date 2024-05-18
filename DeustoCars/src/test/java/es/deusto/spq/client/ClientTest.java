package es.deusto.spq.client;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Answers;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.DirectMessage;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.server.DCServerManager;


public class ClientTest {

    @Mock
    private Client client;
    

    @Mock(answer=Answers.RETURNS_DEEP_STUBS)
    private WebTarget webTarget;

    @Captor
    private ArgumentCaptor<Entity<UserData>> userDataEntityCaptor;

    @Captor
    private ArgumentCaptor<Entity<DirectMessage>> directMessageEntityCaptor;

    private TestForClient mainclient;
    
    //private DCServerManager cdservermanager;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // prepare static mock of ClientBuilder
        try (MockedStatic<ClientBuilder> clientBuilder = Mockito.mockStatic(ClientBuilder.class)) {
            clientBuilder.when(ClientBuilder::newClient).thenReturn(client);
            when(client.target("http://localhost:8080/deustocars")).thenReturn(webTarget);

            mainclient = new TestForClient("localhost", "8080");
        }
        
    }

    
 
    @Test
    public void testDeleteVehicle() {
        when(webTarget.path("server/deletevehicle")).thenReturn(webTarget);
        Response response = Response.ok().build();
        when(webTarget.queryParam(eq("numberPlate"), anyString())
                .request(MediaType.APPLICATION_JSON)
                .delete())
                .thenReturn(response);  
        mainclient.deleteVehicle("1234QWR");

    }
    
    @Test
    public void testDeleteCustomer() {
        when(webTarget.path("server/deletecustomer")).thenReturn(webTarget);
        Response response = Response.ok().build();
        when(webTarget.queryParam(eq("eMail"), anyString())
                .request(MediaType.APPLICATION_JSON)
                .delete())
                .thenReturn(response);        
        mainclient.deleteCustomer("test@gmail.com");

    }
    
    @Test
    public void testInsertCustomer() {
    	when(webTarget.path("server/addcustomer")).thenReturn(webTarget);
    	Response response = Response.ok().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        CustomerData customer = new CustomerData("a@gmail.com", "a", "a", new Date());
        mainclient.addCustomer(customer);

    }
    
    @Test
    public void testInsertVehicle() {
    	when(webTarget.path("server/addvehicle")).thenReturn(webTarget);
    	Response response = Response.ok().build();
        when(webTarget.request(MediaType.APPLICATION_JSON).post(any(Entity.class))).thenReturn(response);
        VehicleData vehicle = new VehicleData("a@gmail.com", "a", "a", true, false);
        mainclient.addVehicle(vehicle);

    }
    
  
    

}
