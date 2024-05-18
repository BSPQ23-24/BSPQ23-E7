package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
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
    
    
//    @Test
//    public void testNoDeleteVehicle() {
//        String numberPlate = "123ABC";
//        when(persistenceManager.getObjectById(VehicleJDO.class, numberPlate))
//                .thenThrow(new JDOObjectNotFoundException()); // Simula una excepción JDOObjectNotFoundException
//
//        Response response = dcserver.deleteVehicle(numberPlate);
//        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
//    }
    
    @Test
    public void testDeleteCustomer() {
    	CustomerJDO customer= new CustomerJDO("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.deleteCustomer("x@gmail.com");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
//    @Test
//    public void testNoDeleteCustomer() {
//        when(persistenceManager.getObjectById(CustomerJDO.class, "x@gmail.com")).thenThrow(new JDOObjectNotFoundException());
//        Response response = dcserver.deleteCustomer("x@gmail.com");
//        assertEquals(Response.Status.NOT_FOUND, response.getStatusInfo());
//    }
    
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
    
//    @Test
//    public void testNoAddVehicle() {
//        VehicleData existingVehicle = new VehicleData("123ABC", "Toyota", "Corolla", true, false);
//        when(persistenceManager.getObjectById(VehicleData.class, existingVehicle.getNumberPlate())).thenReturn(existingVehicle);
//        Response response = dcserver.addVehicle(existingVehicle);
//        assertEquals(Response.Status.CONFLICT, response.getStatusInfo());
//    }
    
    @Test
    public void addCustomer() {
    	CustomerData customer= new CustomerData("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerData.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.addCustomer(customer);
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
//    @Test
//    public void testNoAddCustomer() {
//        CustomerData existingCustomer = new CustomerData("x@gmail.com", "xx", "xxxx");
//        when(persistenceManager.getObjectById(CustomerData.class, existingCustomer.geteMail())).thenReturn(existingCustomer);
//        Response response = dcserver.addCustomer(existingCustomer);
//        assertEquals(Response.Status.CONFLICT, response.getStatusInfo());
//    }
    
     
}
