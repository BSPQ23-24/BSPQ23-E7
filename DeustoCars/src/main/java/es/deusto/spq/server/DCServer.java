/**
 * @file DCServer.java
 * @brief Contains the implementation of the DCServer class.
 */

package es.deusto.spq.server;

import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.Transaction;

import es.deusto.spq.server.jdo.VehicleJDO;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.RentingJDO;
import es.deusto.spq.server.jdo.User;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.DELETE;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.CustomerAssembler;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleAssembler;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.RentingAssembler;

/**
 * @class DCServer
 * @brief Represents the server-side implementation for managing customers and vehicles.
 */


@Path("/server")
@Produces(MediaType.APPLICATION_JSON)
public class DCServer {
    protected static final Logger logger = LogManager.getLogger();

    private int cont = 0;
	private PersistenceManager pm=null;
	private Transaction tx=null;
    
	/**
     * Constructor for the DCServer class.
     */
	
	
    public DCServer() {	
        PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
        this.pm = pmf.getPersistenceManager();
        this.tx = pm.currentTransaction();
    }

    /**
     * Adds a new customer to the database.
     * @param customerData The data of the customer to be added.
     * @return Response indicating success or failure of the operation.
     */
    
    @POST
    @Path("/addcustomer")
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
                customer = CustomerAssembler.getInstance().CustomerDataToJDO(customerData);
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
    
    /**
     * Adds a new vehicle to the database.
     * @param vehicleData The data of the vehicle to be added.
     * @return Response indicating success or failure of the operation.
     */

    @POST
    @Path("/addvehicle")
    public Response addVehicle(VehicleData vehicleData) {   
        try {
            logger.info("Adding vehicle...");
            tx.begin(); // Begin transaction
            VehicleJDO vehicle = null;
            try {
                vehicle = pm.getObjectById(VehicleJDO.class, vehicleData.getNumberPlate());
            } catch (Exception e) {
                logger.info("New vehicle will be added to database");
            }
            logger.info("Vehicle: {}", vehicle);
            if (vehicle != null) {
                logger.info("Vehicle with that number plate already exists");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Vehicle with that number plate already exists.").build();
            } else {
                logger.info("Creating vehicle: {}", vehicle);
                vehicle = VehicleAssembler.getInstance().VehicleDataToJDO(vehicleData);
                
                pm.makePersistent(vehicle); // Persist the vehicle object
                logger.info("Vehicle added: {}", vehicle);
            }
            tx.commit(); // Commit the transaction
            return Response.ok().build();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback the transaction in case of an exception
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while adding vehicle.").build();
        } finally {
            logger.info("Closing");
            pm.close(); // Close the PersistenceManager
        }
    }


    /**
     * Adds a new Renting to the database.
     * @param renting The data of the Renting to be added.
     * @return Response indicating success or failure of the operation.
     */

    @POST
    @Path("/addrenting")
    public Response addRenting(Renting renting) {   
        try {
            logger.info("Searching for vehicle to rent...");
            tx.begin(); // Begin transaction
            VehicleJDO vehicle = null;
            try {
                vehicle = pm.getObjectById(VehicleJDO.class, renting.getVehicle().getNumberPlate());
            } catch (Exception e) {
                logger.info("Vehicle not available to rent");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Vehicle with that number plate does not exists.").build();
            }
            logger.info("Vehicle: {}", vehicle);
            if (vehicle == null) {
                logger.info("Vehicle not available to rent");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Vehicle with that number plate does not exists.").build();
            } else {
                logger.info("Retrieved vehicle for Renting: {}", vehicle);
                if (!vehicle.isReadyToBorrow()) {
                    logger.info("Retrieved vehicle is not Ready to borrow: {}", vehicle);
                    return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Vehicle with that number plate is not ready to borrow.").build();
                }
            }
            CustomerJDO customer = null;
            try {
                customer = pm.getObjectById(CustomerJDO.class, renting.getCustomer().geteMail());
            } catch (Exception e) {
                logger.info("Customer not available to rent");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Customer with that eMail does not exist.").build();
            }
            logger.info("Customer: {}", customer);
            if (customer == null) {
                logger.info("Customer not available to rent");
                return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Customer with that eMail does not exist.").build();
            } else {
                logger.info("Retrieved customer for Renting: {}", customer);
            }

            vehicle.setReadyToBorrow(false);
            pm.makePersistent(vehicle);
            RentingJDO rentToSave = new RentingJDO(customer, vehicle, renting.getStartDate(), renting.getEndDate());
            pm.makePersistent(rentToSave);

            tx.commit(); // Commit the transaction
            return Response.ok().build();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback the transaction in case of an exception
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while adding a renting.").build();
        } finally {
            logger.info("Closing");
            pm.close(); // Close the PersistenceManager
        }
    }


    /**
     * Returns a rented vehicle.
     * @param numberPlate The number plate of the vehicle to be returned.
     * @param email The email of the customer returning the vehicle.
     * @return Response indicating success or failure of the operation.
     */
    @POST
    @Path("/returnvehicle")
    public Response returnVehicle(@QueryParam("numberPlate") String numberPlate, @QueryParam("eMail") String email) {
        try {
            logger.info("Returning vehicle...");
            tx.begin(); // Begin transaction

            VehicleJDO vehicle = null;
            try {
                vehicle = pm.getObjectById(VehicleJDO.class, numberPlate);
            } catch (Exception e) {
                logger.info("Vehicle not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle with that number plate does not exist.").build();
            }
            logger.info("Vehicle: {}", vehicle);
            
            if (vehicle == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
            }
            
            CustomerJDO customer = null;
            try {
                customer = pm.getObjectById(CustomerJDO.class, email);
            } catch (Exception e) {
                logger.info("Customer not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer with that eMail does not exist.").build();
            }
            logger.info("Customer: {}", customer);
            
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
            
            Query query = pm.newQuery(RentingJDO.class, "vehicle == :vehicle && customer == :customer");
            List<RentingJDO> rentingList = (List<RentingJDO>) query.execute(vehicle, customer);
            
            if (rentingList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No renting found for this vehicle and customer").build();
            }
            
            for (RentingJDO renting : rentingList) {
                pm.deletePersistent(renting);
            }
            
            vehicle.setReadyToBorrow(true);
            pm.makePersistent(vehicle);

            tx.commit(); // Commit the transaction
            return Response.ok("Vehicle returned successfully").build();
        } catch (Exception e) {
            if (tx.isActive()) {
                tx.rollback(); // Rollback the transaction in case of an exception
            }
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while returning the vehicle.").build();
        } finally {
            logger.info("Closing");
            pm.close(); // Close the PersistenceManager
        }
    }
    /**
     * Retrieves all rentings from a customer from the database.
     * @return Response containing the list of rentings of a customer or an error message if no rentings are found.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getcustomerrents")
    public Response getCustomerRents(@QueryParam("eMail") String eMail) {
        try {
        	CustomerJDO customer = null;
            try {
                customer = pm.getObjectById(CustomerJDO.class, eMail);
            } catch (Exception e) {
                logger.info("Customer not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer with that eMail does not exist.").build();
            }
            logger.info("Customer: {}", customer);
            if (customer == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        	Query query = pm.newQuery(RentingJDO.class, "customer == :customer");
            List<RentingJDO> rentingList = (List<RentingJDO>) query.execute(customer);
            if (rentingList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No renting found for this customer").build();
            } else {
            	return Response.ok(RentingAssembler.getInstance().RentingJDOListToData(rentingList)).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while extracting renting information.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Retrieves all rentings of a vehicle from the database.
     * @return Response containing the list of rentings and customers or an error message if no rentings are found.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getvehiclerents")
    public Response getVehicleRents(@QueryParam("numberPlate") String numberPlate) {
    	try {
        	VehicleJDO vehicle = null;
            try {
                vehicle = pm.getObjectById(VehicleJDO.class, numberPlate);
            } catch (Exception e) {
                logger.info("Vehicle not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle with that plate number does not exist.").build();
            }
            logger.info("vehicle: {}", vehicle);
            if (vehicle == null) {
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
            }
        	Query query = pm.newQuery(RentingJDO.class, "vehicle == :vehicle");
            List<RentingJDO> rentingList = (List<RentingJDO>) query.execute(vehicle);
            if (rentingList.isEmpty()) {
                return Response.status(Response.Status.NOT_FOUND).entity("No renting found for this vehicle").build();
            } else {
            	return Response.ok(RentingAssembler.getInstance().RentingJDOListToData(rentingList)).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while extracting renting information.").build();
        } finally {
            pm.close();
        }
    }
    
    
    /**
     * Retrieves all customers from the database.
     * @return Response containing the list of customers or an error message if no customers are found.
     */

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/getcustomers")
    public Response getCustomers() {
        try {
            Query query = pm.newQuery(CustomerJDO.class);
            List<CustomerJDO> customers = (List<CustomerJDO>) query.execute();
            if (!customers.isEmpty()) {
                return Response.ok(CustomerAssembler.getInstance().CustomerJDOListToData(customers)).build();
            } else {
            	logger.info("No customers found");
                return Response.status(Response.Status.NOT_FOUND).entity("No customers found").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while fetching customers.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Retrieves all vehicles from the database.
     * @return Response containing the list of vehicles or an error message if no vehicles are found.
     */
    
    @GET
    @Path("/getvehicles")
    public Response getVehicles() {
        try {
            Query query = pm.newQuery(VehicleJDO.class);
            List<VehicleJDO> vehicles = (List<VehicleJDO>) query.execute();
            if (!vehicles.isEmpty()) {
                return Response.ok(VehicleAssembler.getInstance().VehicleJDOListToData(vehicles)).build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("No vehicles found").build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while fetching vehicles.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Retrieves a customer by email from the database.
     * @param eMail The email of the customer to retrieve.
     * @return Response containing the customer data or an error message if the customer is not found.
     */

    @GET
    @Path("/getcustomer")
    public Response getCustomer(@QueryParam("eMail") String eMail) {
        try {
            CustomerJDO customer = pm.getObjectById(CustomerJDO.class, eMail);
            if (customer != null) {
                return Response.ok(CustomerAssembler.getInstance().CustomerJDOToData(customer)).build();
            } else {
                logger.info("Customer not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        } catch (Exception e) {
            logger.info(e);
            logger.info("Error occurred while fetching customer");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while fetching customer.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Retrieves a vehicle by number plate from the database.
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return Response containing the vehicle data or an error message if the vehicle is not found.
     */

    @GET
    @Path("/getvehicle")
    public Response getVehicle(@QueryParam("numberPlate") String numberPlate) {
        try {
            VehicleJDO vehicle = pm.getObjectById(VehicleJDO.class, numberPlate);
            if (vehicle != null) {
                return Response.ok(VehicleAssembler.getInstance().VehicleJDOToData(vehicle)).build();
            } else {
                logger.info("Vehicle not found");
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
            }
        } catch (Exception e) {
            logger.info(e);
            logger.info("Error occurred while fetching vehicle");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while fetching vehicle.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Deletes a vehicle from the database.
     * @param email The email of the vehicle to delete.
     * @return Response indicating success or failure of the operation.
     */

    @DELETE
    @Path("/deletevehicle")
    public Response deleteVehicle(@QueryParam("numberPlate") String email) {
        try {
            VehicleJDO vehicle = pm.getObjectById(VehicleJDO.class, email);
            if (vehicle != null) {
                pm.deletePersistent(vehicle);
                return Response.ok("Vehicle deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
            }
        } catch (JDOObjectNotFoundException e) {
            logger.info("Vehicle not found: {}", email);
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        } catch (Exception e) {
            logger.info("Error occurred while deleting vehicle");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while deleting vehicle.").build();
        } finally {
            pm.close();
        }
    }
    
    /**
     * Deletes a customer from the database.
     * @param email The email of the customer to delete.
     * @return Response indicating success or failure of the operation.
     */

    @DELETE
    @Path("/deletecustomer")
    public Response deleteCustomer(@QueryParam("eMail") String email) {
        try {
            CustomerJDO customer = pm.getObjectById(CustomerJDO.class, email);
            if (customer != null) {
                pm.deletePersistent(customer);
                return Response.ok("Customer deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
            }
        } catch (JDOObjectNotFoundException e) {
            logger.info("Customer not found: {}", email);
            return Response.status(Response.Status.NOT_FOUND).entity("Customer not found").build();
        } catch (Exception e) {
            logger.info("Error occurred while deleting customer");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while deleting customer.").build();
        } finally {
            pm.close();
        }
    }


}
