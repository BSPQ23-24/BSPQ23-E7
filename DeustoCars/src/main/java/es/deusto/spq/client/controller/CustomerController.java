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

import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.client.ServiceLocator;

public class CustomerController {
    protected static final Logger logger = LogManager.getLogger();

    /**
     * Retrieves a Customer object from the server based on the provided email.
     *
     * @param eMail The email of the customer to retrieve.
     * @return The Customer object retrieved from the server, or null if not found or an error occurred.
     */
    public static CustomerData getCustomer(String eMail) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getcustomer")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Getting customer: " + response.toString());
            return response.readEntity(CustomerData.class);
        } else {
            logger.info("ERROR getting customer");
            return null;
        }
    }
    /**
     * Retrieves a list of Customer objects from the server.
     *
     * @return A list of Customer objects retrieved from the server, or an empty list if no customers found or an error occurred.
     */
    public static List<CustomerData> getCustomers() {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/getcustomers")
                .request(MediaType.APPLICATION_JSON)
                .get();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            return response.readEntity(new GenericType<List<CustomerData>>() {});
        } else {
        	logger.info("ERROR getting customers Code: {}", response.getStatus());
            return Collections.emptyList();
        }
    }

    /**
     * Deletes a customer from the server based on the provided email.
     *
     * @param eMail The email of the customer to delete.
     */
    public static void deleteCustomer(String eMail) {
        Response response = ServiceLocator.getInstance().getWebTarget()
                .path("server/deletecustomer")
                .queryParam("eMail", eMail)
                .request(MediaType.APPLICATION_JSON)
                .delete();

        if (response.getStatus() == Response.Status.OK.getStatusCode()) {
            logger.info("Deleting customer: " + response.toString());
            JOptionPane.showMessageDialog(null, "Customer Correctly Deleted", "Success deleting customer", JOptionPane.INFORMATION_MESSAGE);

        } else {
            logger.info("ERROR deleting customer");
            JOptionPane.showMessageDialog(null, "Error deleting customer", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void addCustomer(CustomerData customer) {
        if (ServiceLocator.getInstance().getWebTarget() != null) {
            Response response = ServiceLocator.getInstance().getWebTarget()
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
