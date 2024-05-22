package es.deusto.spq.server.services;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.RentingAssembler;
import es.deusto.spq.pojo.Renting;
import es.deusto.spq.pojo.VehicleAssembler;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.server.jdo.RentingJDO;
import es.deusto.spq.server.jdo.VehicleJDO;

public class VehicleService {
    private static VehicleService instance;

    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManager pm;
	private Transaction tx;
    private PersistenceManagerFactory pmf;

    public VehicleService() {	
       pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static  VehicleService getInstance() {
        if (instance == null) {
            instance = new VehicleService();
        }
        return instance;
    }

    /**
     * Adds a new vehicle to the database.
     * @param vehicleData The data of the vehicle to be added.
     * @return Response indicating success or failure of the operation.
     */
    public Response addVehicle(VehicleData vehicleData) {  
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction(); 
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
                logger.info("Vehicle with that number plate already exists, editing entry...");
                vehicle.setBrand(vehicleData.getBrand());
                vehicle.setModel(vehicleData.getModel());
                vehicle.setOnRepair(vehicleData.isOnRepair());
                vehicle.setReadyToBorrow(vehicleData.isReadyToBorrow());
                pm.makePersistent(vehicle); // Persist the vehicle object
                tx.commit(); // Commit the transaction
                return Response.ok().entity("Vehicle with that number plate already exists, entry edited.").build();
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
     * Retrieves all vehicles from the database.
     * @return Response containing the list of vehicles or an error message if no vehicles are found.
     */
    @SuppressWarnings("unchecked")
    public Response getVehicles() {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
     * Retrieves a vehicle by number plate from the database.
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return Response containing the vehicle data or an error message if the vehicle is not found.
     */
    public Response getVehicle(String numberPlate) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
    @SuppressWarnings("unchecked")
    public Response deleteVehicle(String numberPlate) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            VehicleJDO vehicle = pm.getObjectById(VehicleJDO.class, numberPlate);
            if (vehicle != null) {
                try {
                    for(RentingJDO rent : (List<RentingJDO>) RentingAssembler.getInstance().RentingListToJDO((List<Renting>) RentingService.getInstance().getVehicleRents(vehicle.getNumberPlate()).getEntity())){
                        RentingService.getInstance().deleteRenting(rent.getVehicle().getNumberPlate(), rent.getCustomer().geteMail());
                    }  
                } catch (Exception e) {
                    logger.info("Vehicle has no rents asociated");
                }
                pm.deletePersistent(vehicle);
                return Response.ok("Vehicle deleted successfully").build();
            } else {
                return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
            }
        } catch (JDOObjectNotFoundException e) {
            logger.info("Vehicle not found: {}", numberPlate);
            return Response.status(Response.Status.NOT_FOUND).entity("Vehicle not found").build();
        } catch (Exception e) {
            logger.info(e);
            logger.info("Error occurred while deleting vehicle");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while deleting vehicle.").build();
        } finally {
            pm.close();
        }
    }
}
