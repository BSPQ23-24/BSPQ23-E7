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
import javax.swing.JOptionPane;
import javax.ws.rs.core.Response;
import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.CustomerAssembler;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.RentingAssembler;
import es.deusto.spq.pojo.VehicleAssembler;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.RentingJDO;
import es.deusto.spq.server.jdo.VehicleJDO;



public class ResourceTest {

    private DCServer dcserver;
    private VehicleData testVehicle_1;
    private VehicleData testVehicle_2;
    private VehicleData testVehicle_to_rent;
    private VehicleData testVehicle_delete;
    private CustomerData testCustomer_1;
    private CustomerData testCustomer_2;
    private CustomerData testCustomer_to_rent;
    private CustomerData testCustomer_delete;
    private Renting testRenting_1;
    private Renting testRenting_2;

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
        testVehicle_1 = new VehicleData("test_plate_1", "test", "test", true, false);
        testVehicle_2 = new VehicleData("test_plate_2", "test", "test", true, false);
        testVehicle_to_rent = new VehicleData("test_plate_to_rent", "test", "test", true, false);
        testVehicle_delete = new VehicleData("test_plate_delete", "test", "test", true, false);;
        testCustomer_1 = new CustomerData("test_email_1", "test", "test");
        testCustomer_2 = new CustomerData("test_email_2", "test", "test");
        testCustomer_to_rent = new CustomerData("test_email_to_rent", "test", "test");
        testCustomer_delete = new CustomerData("test_email_delete", "test", "test");
        testRenting_1 = new Renting(testCustomer_1, testVehicle_1, new Date(), new Date());
        testRenting_2 = new Renting(testCustomer_1, testVehicle_2, new Date(), new Date());
        dcserver.addVehicle(testVehicle_1);
        dcserver.addVehicle(testVehicle_2);
        dcserver.addVehicle(testVehicle_to_rent);
        dcserver.addVehicle(testVehicle_delete);
        dcserver.addCustomer(testCustomer_1);
        dcserver.addCustomer(testCustomer_2);
        dcserver.addCustomer(testCustomer_to_rent);
        dcserver.addCustomer(testCustomer_delete);
        dcserver.addRenting(testRenting_1);
        dcserver.addRenting(testRenting_2);
    }
    
    
    @Test
    public void testDeleteVehicleNotFound() {
        String licensePlate = "not_found_plate";
        when(persistenceManager.getObjectById(VehicleJDO.class, licensePlate)).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.deleteVehicle(licensePlate);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    @Test
    public void testDeleteCustomerNotFound() {
        String email = "not_found_email";
        when(persistenceManager.getObjectById(CustomerJDO.class, email)).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.deleteCustomer(email);
        assertEquals(Response.Status.NOT_FOUND.getStatusCode(), response.getStatus());
    }
    
    
    @Test
    public void getVehicle() {
    	VehicleJDO vehicle= VehicleAssembler.getInstance().VehicleDataToJDO(testVehicle_1);
    	when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.getVehicle(vehicle.getNumberPlate());
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    @Test
    public void testNoGetVehicle() {
        when(persistenceManager.getObjectById(VehicleJDO.class, "not_found_plate")).thenReturn(null);
        Response response = dcserver.getVehicle("not_found_plate");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    }
    
    @Test
    public void getCustomer() {
    	CustomerJDO customer= CustomerAssembler.getInstance().CustomerDataToJDO(testCustomer_1);
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.getCustomer(customer.geteMail());
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    // @Test
    // public void testGetCustomerRents() {
    //     // Mock the customer and renting objects
    //     CustomerJDO customer = CustomerAssembler.getInstance().CustomerDataToJDO(testCustomer_1);
    //     RentingJDO renting1 = RentingAssembler.getInstance().RentingDatatoJDO(testRenting_1);
    //     RentingJDO renting2 = RentingAssembler.getInstance().RentingDatatoJDO(testRenting_2);

    //     // Mock the PersistenceManager behavior
    //     when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);

    //     // Mock the Query behavior
    //     Query mockQuery = mock(Query.class);
    //     when(persistenceManager.newQuery(RentingJDO.class, "customer == :customer")).thenReturn(mockQuery);
    //     when(mockQuery.execute(customer)).thenReturn(Arrays.asList(renting1, renting2));

    //     // Call the method to be tested
    //     Response response = dcserver.getCustomerRents(customer.geteMail());

    //     // Verify the results
    //     assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    //     List<Renting> rentingDataList = (List<Renting>) response.getEntity();
    //     assertNotNull(rentingDataList);
    //     assertEquals(2, rentingDataList.size());

    //     // Verify the content of the rentingDataList
    //     Renting rentingData1 = rentingDataList.get(0);
    //     assertEquals(renting1.getCustomer().geteMail(), rentingData1.getCustomer().geteMail());
    //     assertEquals(renting1.getVehicle().getNumberPlate(), rentingData1.getVehicle().getNumberPlate());

    //     Renting rentingData2 = rentingDataList.get(1);
    //     assertEquals(renting2.getCustomer().geteMail(), rentingData2.getCustomer().geteMail());
    //     assertEquals(renting2.getVehicle().getNumberPlate(), rentingData2.getVehicle().getNumberPlate());
    // }
    
    // @Test
    // public void testGetVehicleRents() {
    //     // Mock the vehicle and renting objects
    //     VehicleJDO vehicle = VehicleAssembler.getInstance().VehicleDataToJDO(testVehicle_1);
    //     RentingJDO renting1 = RentingAssembler.getInstance().RentingDatatoJDO(testRenting_1);

    //     // Mock the PersistenceManager behavior
    //     when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);

    //     // Mock the Query behavior
    //     Query mockQuery = mock(Query.class);
    //     when(persistenceManager.newQuery(RentingJDO.class, "vehicle == :vehicle")).thenReturn(mockQuery);
    //     when(mockQuery.execute(vehicle)).thenReturn(Arrays.asList(renting1));

    //     // Call the method to be tested
    //     Response response = dcserver.getVehicleRents(vehicle.getNumberPlate());

    //     // Verify the results
    //     assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    //     List<Renting> rentingDataList = (List<Renting>) response.getEntity();
    //     assertNotNull(rentingDataList);
    //     assertEquals(1, rentingDataList.size());

    //     // Verify the content of the rentingDataList
    //     Renting rentingData1 = rentingDataList.get(0);
    //     assertEquals(renting1.getCustomer().geteMail(), rentingData1.getCustomer().geteMail());
    //     assertEquals(renting1.getVehicle().getNumberPlate(), rentingData1.getVehicle().getNumberPlate());
    // }




    
    @Test
    public void testNoGetCustomer() {
        when(persistenceManager.getObjectById(CustomerJDO.class, "no_found_email")).thenReturn(null);
        Response response = dcserver.getCustomer("no_found_email");
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR, response.getStatusInfo());
    }
    
    @Test
    public void addVehicle() {
    	VehicleData vehicle= new VehicleData("test_plate_3", "test", "test", true, false);
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
    	CustomerData customer= new CustomerData("test_email_3", "test", "test");
    	when(persistenceManager.getObjectById(CustomerData.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.addCustomer(customer);
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
    
    // @Test
    // public void testAddRenting() {
    //     // Set up the data
    //     CustomerData customerData = testCustomer_to_rent;
    //     VehicleData vehicleData = testVehicle_to_rent;
    //     Renting renting = new Renting(customerData, vehicleData, new Date(), new Date());
    //     Response response = dcserver.addRenting(renting);
    //     assertEquals(Response.Status.OK.getStatusCode(), response.getStatus());

    // }
    
    
    @Test
    public void testAddRentingVehicleNotFound() {
        CustomerData customerData = new CustomerData("no_found_email", "test", "test");
        VehicleData vehicleData = new VehicleData("no_found_plate", "test", "test", true, false);
        Renting renting = new Renting(customerData, vehicleData, new Date(), new Date());
        when(persistenceManager.getObjectById(VehicleJDO.class, "no_found_plate")).thenThrow(new JDOObjectNotFoundException());
        Response response = dcserver.addRenting(renting);
        assertEquals(Response.Status.INTERNAL_SERVER_ERROR.getStatusCode(), response.getStatus());
    }

    @Test
    public void testDeleteCustomer() {
    	CustomerJDO customer= CustomerAssembler.getInstance().CustomerDataToJDO(testCustomer_delete);
    	when(persistenceManager.getObjectById(CustomerJDO.class, customer.geteMail())).thenReturn(customer);
        Response response = dcserver.deleteCustomer(customer.geteMail());
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    @Test
    public void testDeleteVehicle() {
    	VehicleJDO vehicle= VehicleAssembler.getInstance().VehicleDataToJDO(testVehicle_delete);
    	when(persistenceManager.getObjectById(VehicleJDO.class, vehicle.getNumberPlate())).thenReturn(vehicle);
        Response response = dcserver.deleteVehicle(vehicle.getNumberPlate());
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
    
     
}
