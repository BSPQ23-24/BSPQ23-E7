package es.deusto.spq.client.controller;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.client.ServiceLocator;
import es.deusto.spq.pojo.Renting;

public class RentingController {

    protected static final Logger logger = LogManager.getLogger();

     /**
     * Retrieves a list of Renting objects associated with a customer from the server.
     *
     * @return A list of Renting objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<Renting> getCustomerRents(String eMail) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getcustomerrents")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Renting>>() {});
        } else {
        	logger.info("ERROR getting rentings Code: {}", response.getStatus());
            return Collections.emptyList();
        }
    }
    /**
     * Retrieves a list of Renting objects associated with a vehicle from the server.
     *
     * @return A list of Renting objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<Renting> getVehicleRents(String numberPlate) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getvehiclerents")
                .queryParam("numberPlate", numberPlate)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<Renting>>() {});
        } else {
        	logger.info("ERROR getting rentings (There might be no rentings for this vehicle) Code: {}", response.getStatus());
            return Collections.emptyList();
        }
    }


}
