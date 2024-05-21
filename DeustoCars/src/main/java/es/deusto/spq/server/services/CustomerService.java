package es.deusto.spq.server.services;

import java.util.List;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.CustomerAssembler;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.server.jdo.CustomerJDO;

public class CustomerService {
    private static CustomerService instance;

    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManager pm;
	private Transaction tx;
    private PersistenceManagerFactory pmf;

    public CustomerService() {
        pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static  CustomerService getInstance() {
        if (instance == null) {
            instance = new CustomerService();
        }
        return instance;
    }

    /**
     * Adds a new customer to the database.
     * @param customerData The data of the customer to be added.
     * @return Response indicating success or failure of the operation.
     */
    public Response addCustomer(CustomerData customerData) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
                logger.info("Customer with that email already exists, editing entry...");
                customer.setName(customerData.getName());
                customer.setSurname(customerData.getSurname());
                customer.setDateOfBirth(customerData.getDateOfBirth());
                pm.makePersistent(customer); // Persist the customer object
                tx.commit(); // Commit the transaction
                return Response.ok().entity("Customer with that email already exists, entry edited.").build();
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
     * Retrieves all customers from the database.
     * @return Response containing the list of customers or an error message if no customers are found.
     */
    public Response getCustomers() {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
     * Retrieves a customer by email from the database.
     * @param eMail The email of the customer to retrieve.
     * @return Response containing the customer data or an error message if the customer is not found.
     */
    public Response getCustomer(String eMail) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
     * Deletes a customer from the database.
     * @param email The email of the customer to delete.
     * @return Response indicating success or failure of the operation.
     */
    public Response deleteCustomer(String email) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
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
