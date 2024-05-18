package es.deusto.spq.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.DirectMessage;
import es.deusto.spq.pojo.MessageData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.pojo.VehicleData;

public class TestForClient {

	private static final Logger logger = LogManager.getLogger();

	private static final String USER = "dipina";
	private static final String PASSWORD = "dipina";

	private Client client;
	private WebTarget webTarget;

	public TestForClient(String hostname, String port) {
		client = ClientBuilder.newClient();
		webTarget = client.target(String.format("http://%s:%s/deustocars", hostname, port));
	}

	public void addVehicle(VehicleData vehicle) {
     
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
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
	
	public void deleteVehicle(String numberPlate) {
        logger.info("NumberPlate: " + numberPlate);
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
                    .path("server/deletevehicle")
                    .queryParam("numberPlate", numberPlate)
                    .request(MediaType.APPLICATION_JSON)
                    .delete();

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Deleting vehicle: " + response.toString());
            } else {
                logger.info("ERROR deleting vehicle: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to delete vehicle.");
        }
    }
	
	public void deleteCustomer(String eMail) {
        Response response = webTarget
                .path("server/deletecustomer")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Deleting customer: " + response.toString());
        } else {
            logger.info("ERROR deleting customer");
        }
    }
	
	public void addCustomer(CustomerData customer) {
        logger.info("WebTarget: " + webTarget);
        if (webTarget != null) {
            Response response = webTarget
                    .path("server/addcustomer")
                    .request(MediaType.APPLICATION_JSON)
                    .post(Entity.entity(customer, MediaType.APPLICATION_JSON));

            if (response.getStatus() == Response.Status.OK.getStatusCode()) {
                logger.info("Adding customer: " + response.toString());
            } else {
                logger.info("ERROR adding customer: " + response.getStatus());
            }
        } else {
            logger.error("WebTarget is null. Unable to add customer.");
        }
    }

	
}