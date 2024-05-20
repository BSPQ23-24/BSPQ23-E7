package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Date;
import java.util.List;


import javax.jdo.JDOHelper;
import javax.jdo.Query;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.VehicleJDO;



public class ResourceTest {

    private DCServer dcserver;

    @Mock
    private PersistenceManager persistenceManager;

    @Mock
    private Transaction transaction;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // initializing static mock object PersistenceManagerFactory
        try (MockedStatic<JDOHelper> jdoHelper = Mockito.mockStatic(JDOHelper.class)) {
            PersistenceManagerFactory pmf = mock(PersistenceManagerFactory.class);
            jdoHelper.when(() -> JDOHelper.getPersistenceManagerFactory("datanucleus.properties")).thenReturn(pmf);
            
            when(pmf.getPersistenceManager()).thenReturn(persistenceManager);
            when(persistenceManager.currentTransaction()).thenReturn(transaction);

            // instantiate tested object with mock dependencies
            dcserver = new DCServer();
        }
    }

   

    @Test
    public void testDeleteVehicle() {
    	VehicleJDO vehicle= new VehicleJDO("123ABC", "Toyota", "Corolla");
    	when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.deleteVehicle("123ABC");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    
    @Test
    public void testDeleteVehicleNotFound() {
        String licensePlate = "123ABC";
        when(persistenceManager.getObjectById(VehicleJDO.class, licensePlate)).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.deleteVehicle(licensePlate);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    
    @Test
    public void testDeleteCustomer() {
    	CustomerJDO customer= new CustomerJDO("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.deleteCustomer("x@gmail.com");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    @Test
    public void testDeleteCustomerNotFound() {
        String email = "x@gmail.com";
        when(persistenceManager.getObjectById(CustomerJDO.class, email)).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.deleteCustomer(email);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    
    @Test
    public void getVehicle() {
    	VehicleJDO vehicle= new VehicleJDO("123ABC", "Toyota", "Corolla");
    	when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.getVehicle("123ABC");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    @Test
    public void testNoGetVehicle() {
        when(persistenceManager.getObjectById(VehicleJDO.class, "123ABC")).thenReturn(null);
        Response response = dcserver.getVehicle("123ABC");
        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo());
    }
    
    @Test
    public void getCustomer() {
    	CustomerJDO customer= new CustomerJDO("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.getCustomer("x@gmail.com");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    @Test
    public void testNoGetCustomer() {
        when(persistenceManager.getObjectById(CustomerJDO.class, "x@gmail.com")).thenReturn(null);
        Response response = dcserver.getCustomer("x@gmail.com");
        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo());
    }
    
    @Test
    public void addVehicle() {
    	VehicleData vehicle= new VehicleData("123ABC", "Toyota", "Corolla", true, false);
    	when(persistenceManager.getObjectById(VehicleData.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.addVehicle(vehicle);
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    @Test
    public void testNoAddVehicle() {
        VehicleData existingVehicle = new VehicleData("123ABC", "Toyota", "Corolla", true, false);
        when(persistenceManager.getObjectById(VehicleData.class, existingVehicle.getNumberPlate())).thenThrow(JDOObjectNotFoundException.class);
        Response response = dcserver.addVehicle(existingVehicle);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusInfo());
    }
    
    @Test
    public void addCustomer() {
    	CustomerData customer= new CustomerData("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerData.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.addCustomer(customer);
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    
    @Test
    public void testAddRenting() {
        // Set up the data
        CustomerData customerData = new CustomerData("x@gmail.com", "xx", "xxxx");
        VehicleData vehicleData = new VehicleData("123ABC", "Toyota", "Corolla", true, false);
        Renting renting = new Renting(customerData, vehicleData, new Date(), new Date());
        CustomerJDO customerJDO = new CustomerJDO("x@gmail.com", "xx", "xxxx");
        VehicleJDO vehicleJDO = new VehicleJDO("123ABC", "Toyota", "Corolla");
        vehicleJDO.setReadyToBorrow(true);
        when(persistenceManager.getObjectById(VehicleJDO.class, "123ABC")).thenReturn(vehicleJDO);
        when(persistenceManager.getObjectById(CustomerJDO.class, "x@gmail.com")).thenReturn(customerJDO);
        Response response = dcserver.addRenting(renting);
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    }
    
    
    @Test
    public void testAddRentingVehicleNotFound() {
        CustomerData customerData = new CustomerData("x@gmail.com", "xx", "xxxx");
        VehicleData vehicleData = new VehicleData("123ABC", "Toyota", "Corolla", true, false);
        Renting renting = new Renting(customerData, vehicleData, new Date(), new Date());
        when(persistenceManager.getObjectById(VehicleJDO.class, "123ABC")).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.addRenting(renting);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }
    
     
}
