package es.deusto.spq.server.services;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.RentingAssembler;
import es.deusto.spq.server.jdo.CustomerJDO;
import es.deusto.spq.server.jdo.RentingJDO;
import es.deusto.spq.server.jdo.VehicleJDO;

public class RentingService {
    private static RentingService instance;

    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManager pm;
	private Transaction tx;
    private PersistenceManagerFactory pmf;

    public RentingService() {	
        pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static  RentingService getInstance() {
        if (instance == null) {
            instance = new RentingService();
        }
        return instance;
    }
    
    /**
     * Adds a new Renting to the database.
     * @param renting The data of the Renting to be added.
     * @return Response indicating success or failure of the operation.
     */
    public Response addRenting(Renting renting) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
    public Response returnVehicle(String numberPlate, String email) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
    public Response getCustomerRents(String eMail) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
    public Response getVehicleRents(String numberPlate) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
}
