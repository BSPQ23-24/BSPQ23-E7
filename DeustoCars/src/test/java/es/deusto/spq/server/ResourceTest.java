package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
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
import es.deusto.spq.server.jdo.RentingJDO;
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
    public void testDeleteVehicleNotFound() {
        String licensePlate = "1234ABC";
        when(persistenceManager.getObjectById(VehicleJDO.class, licensePlate)).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.deleteVehicle(licensePlate);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteCustomerNotFound() {
        String email = "z@gmail.com";
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
        when(persistenceManager.getObjectById(VehicleJDO.class, "123ABCD")).thenReturn(null);
        Response response = dcserver.getVehicle("123ABCD");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    }
    
    @Test
    public void getCustomer() {
    	CustomerJDO customer= new CustomerJDO("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.getCustomer("x@gmail.com");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    @Test
    public void testGetCustomerRents() {
        // Mock the customer and renting objects
        String email = "x@gmail.com";
        CustomerJDO customer = new CustomerJDO(email, "First", "Last", new Date());
        VehicleJDO vehicle1 = new VehicleJDO("123AB", "Toyota", "Corolla");
        VehicleJDO vehicle2 = new VehicleJDO("321YZA", "Kia", "Sportage");
        RentingJDO renting1 = new RentingJDO(customer, vehicle1, new Date(), new Date());
        RentingJDO renting2 = new RentingJDO(customer, vehicle2, new Date(), new Date());

        // Mock the PersistenceManager behavior
        when(persistenceManager.getObjectById(CustomerJDO.class, email)).thenReturn(customer);

        // Mock the Query behavior
        Query mockQuery = mock(Query.class);
        when(persistenceManager.newQuery(RentingJDO.class, "customer == :customer")).thenReturn(mockQuery);
        when(mockQuery.execute(customer)).thenReturn(Arrays.asList(renting1, renting2));

        // Call the method to be tested
        Response response = dcserver.getCustomerRents(email);

        // Verify the results
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Renting> rentingDataList = (List<Renting>) response.getEntity();
        assertNotNull(rentingDataList);
        assertEquals(3, rentingDataList.size());

        // Verify the content of the rentingDataList
        Renting rentingData1 = rentingDataList.get(0);
        assertEquals(renting1.getCustomer().geteMail(), rentingData1.getCustomer().geteMail());
        assertEquals(renting1.getVehicle().getNumberPlate(), rentingData1.getVehicle().getNumberPlate());

        Renting rentingData2 = rentingDataList.get(1);
        assertEquals(renting2.getCustomer().geteMail(), rentingData2.getCustomer().geteMail());
        assertEquals(renting2.getVehicle().getNumberPlate(), rentingData2.getVehicle().getNumberPlate());
    }
    
    @Test
    public void testGetVehicleRents() {
        // Mock the vehicle and renting objects
        String numberPlate = "123ABC";
        VehicleJDO vehicle = new VehicleJDO(numberPlate, "Toyota", "Camry");
        CustomerJDO customer1 = new CustomerJDO("juan@gmail.com", "First", "Last", new Date());
        CustomerJDO customer2 = new CustomerJDO("x@gmail.com", "Second", "Last", new Date());
        RentingJDO renting1 = new RentingJDO(customer1, vehicle, new Date(), new Date());
        RentingJDO renting2 = new RentingJDO(customer2, vehicle, new Date(), new Date());

        // Mock the PersistenceManager behavior
        when(persistenceManager.getObjectById(VehicleJDO.class, numberPlate)).thenReturn(vehicle);

        // Mock the Query behavior
        Query mockQuery = mock(Query.class);
        when(persistenceManager.newQuery(RentingJDO.class, "vehicle == :vehicle")).thenReturn(mockQuery);
        when(mockQuery.execute(vehicle)).thenReturn(Arrays.asList(renting1, renting2));

        // Call the method to be tested
        Response response = dcserver.getVehicleRents(numberPlate);

        // Verify the results
        assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

        List<Renting> rentingDataList = (List<Renting>) response.getEntity();
        assertNotNull(rentingDataList);
        assertEquals(2, rentingDataList.size());

        // Verify the content of the rentingDataList
        Renting rentingData1 = rentingDataList.get(0);
        assertEquals(renting1.getCustomer().geteMail(), rentingData1.getCustomer().geteMail());
        assertEquals(renting1.getVehicle().getNumberPlate(), rentingData1.getVehicle().getNumberPlate());

        Renting rentingData2 = rentingDataList.get(1);
        assertEquals(renting2.getCustomer().geteMail(), rentingData2.getCustomer().geteMail());
        assertEquals(renting2.getVehicle().getNumberPlate(), rentingData2.getVehicle().getNumberPlate());
    }




    
    @Test
    public void testNoGetCustomer() {
        when(persistenceManager.getObjectById(CustomerJDO.class, "y@gmail.com")).thenReturn(null);
        Response response = dcserver.getCustomer("y@gmail.com");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
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
//        when(persistenceManager.getObjectById(VehicleData.class, existingVehicle.getNumberPlate())).thenThrow(JDOObjectNotFoundException.class);
//        Response response = dcserver.addVehicle(existingVehicle);
//        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatusInfo());
//    }
    
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

    @Test
    public void testDeleteCustomer() {
    	CustomerJDO customer= new CustomerJDO("x@gmail.com", "xx", "xxxx");
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.deleteCustomer("x@gmail.com");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    @Test
    public void testDeleteVehicle() {
    	VehicleJDO vehicle= new VehicleJDO("123ABC", "Toyota", "Corolla");
    	when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.deleteVehicle("123ABC");
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
     
}
