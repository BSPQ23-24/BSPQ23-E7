package es.deusto.spq.client.controller;

import java.util.Collections;
import java.util.List;

import javax.swing.JOptionPane;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.client.ServiceLocator;

public class VehicleController {
    protected static final Logger logger = LogManager.getLogger();
    /**
     * Retrieves a Vehicle object from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to retrieve.
     * @return The Vehicle object retrieved from the server, or null if not found or an error occurred.
     */
    public static VehicleData getVehicle(String numberPlate) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getvehicle")
                .queryParam("numberPlate", numberPlate)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Getting vehicle: " + response.toString());
            return response.readEntity(VehicleData.class);
        } else {
            logger.info("ERROR getting vehicle");
            return null;
        }
    }
    
    /**
     * Retrieves a list of Vehicle objects from the server.
     *
     * @return A list of Vehicle objects retrieved from the server, or an empty list if no vehicles found or an error occurred.
     */
    public static List<VehicleData> getVehicles() {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getvehicles")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<VehicleData>>() {});
        } else {
            logger.info("ERROR getting vehicles");
            return Collections.emptyList();
        }
    }

    /**
     * Deletes a vehicle from the server based on the provided number plate.
     *
     * @param numberPlate The number plate of the vehicle to delete.
     */
    public static void deleteVehicle(String numberPlate) {
        if (ServiceLocator.getInstance().getWebTarget() != null) {
            Response response = ServiceLocator.getInstance().getWebTarget()
                    .path("server/deletevehicle")
                    .queryParam("numberPlate", numberPlate)
                    .request(MediaType.APPLICATION_JSON)
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Deleting vehicle: " + response.toString());
                JOptionPane.showMessageDialog(null, "Vehicle Correctly Deleted", "Success deleting vehicle", JOptionPane.INFORMATION_MESSAGE);

            } else {
                logger.info("ERROR deleting vehicle: " + response.getStatus());
                JOptionPane.showMessageDialog(null, "Error deleting vehicle", "Error", JOptionPane.ERROR_MESSAGE);

            }
        } else {
            logger.error("WebTarget is null. Unable to delete vehicle.");
        }
    }

    public static void addVehicle(VehicleData vehicle) {
        if (ServiceLocator.getInstance().getWebTarget() != null) {
            Response response = ServiceLocator.getInstance().getWebTarget()
                    .path("server/addvehicle")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(vehicle, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Adding vehicle: " + response.toString());
            } else {
                logger.info("ERROR adding vehicle: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to add vehicle.");
        }
    }
}
