package es.deusto.spq.client.controller;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.UserData;
import es.deusto.spq.client.ServiceLocator;

public class UserController {
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Retrieves a Customer object from the server based on the provided email.
     *
     * @param eMail The email of the user to retrieve.
     * @param password The password of the user to retrieve.
     * @return The user object retrieved from the server, or null if not found or an error occurred.
     */
    public static UserData getUser(String eMail, String password) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getuser")
                .queryParam("eMail", eMail)
                .queryParam("password", password)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Getting user: " + response.toString());
            return response.readEntity(UserData.class);
        } else {
            logger.info("ERROR getting user");
            return null;
        }
    }
}
