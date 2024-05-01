package es.deusto.spq.server;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.VehicleJDO;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;

@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {
    protected static final Logger logger = LogManager.getLogger();

    private int cont = 0;
	private PersistenceManager pm=null;
	private Transaction tx=null;
    
    public DCServer() {	
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    @POST
    @Path("/customers")
    public Response addCustomer(CustomerData customerData) {
        try {
            logger.info("Adding customer...");
            tx.begin(); // Begin transaction
            CustomerJDO customer = null;
            try {
                customer = pm.getObjectById(CustomerJDO.class, customerData.geteMail());
            } catch (Exception e) {
                logger.info("New customer will be added to database");
            }
            logger.info("Customer: {}", customer);
            if (customer != null) {
                logger.info("Customer with that email already exists");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Customer with that email already exists.").build();
            } else {
                logger.info("Creating customer: {}", customer);
                customer = new CustomerJDO(
                    customerData.geteMail(), 
                    customerData.getName(), 
                    customerData.getSurname(), 
                    customerData.getDateOfBirth()
                );
                pm.makePersistent(customer); // Persist the customer object
                logger.info("Customer added: {}", customer);
            }
            tx.commit(); // Commit the transaction
            return Response.ok().build();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback the transaction in case of an exception
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while adding customer.").build();
        } finally {
            logger.info("Closing");
            pm.close(); // Close the PersistenceManager
        }
    }
    


    @POST
    @Path("/vehicles")
    public Response addVehicle(VehicleData vehicleData) {
        VehicleData vehicle = new VehicleData(
            vehicleData.getNumberPlate(), 
            vehicleData.getBrand(), 
            vehicleData.getModel()
        );
        
        logger.info("Adding vehicle: " + vehicle);
        return Response.ok().entity(vehicleData).build(); // Respond with the DTO for consistency
    }

    @GET
    @Path("/getcustomers")
    public Response getCustomers() {
        List<CustomerData> customers = new ArrayList<CustomerData>();
        customers.add(new CustomerData("test@gmail.com", "Billy", "Bob"));
        customers.add(new CustomerData("test2@gmail.com", "Johnny", "Jones"));
        customers.add(new CustomerData("test3@gmail.com", "Evelyn", "Easton"));
        return Response.ok(customers).build();
    }

    @GET
    @Path("/getvehicles")
    public Response getVehicles() {
        List<VehicleData> vehicles = new ArrayList<VehicleData>();
        vehicles.add(new VehicleData("9872SLY", "Toyota", "Corolla"));
        vehicles.add(new VehicleData("1234QWR", "Opel", "Corsa"));
        vehicles.add(new VehicleData("5678BNM", "Volkswagen", "Golf"));
        return Response.ok(vehicles).build();
    }

    @GET
    @Path("/getcustomer")
    public Response getCustomer(String eMail) {

        CustomerData customerData = new CustomerData(eMail, "Billy", "Bob");
        return Response.ok(customerData).build();

    }

    @GET
    @Path("/getvehicle")
    public Response getVehicle(String numberPlate) {

        VehicleData vehicleData = new VehicleData(numberPlate, "Toyota", "Corolla");
        return Response.ok(vehicleData).build();

    }

    @DELETE
    @Path("/deletevehicle")
    public Response deleteVehicle(String numberPlate) {
        logger.info("Deleting vehicle with number plate: " + numberPlate);
        return Response.ok().build();
    }

    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(String eMail) {
        logger.info("Deleting customer with eMail: " + eMail);
        return Response.ok().build();
    }
}
