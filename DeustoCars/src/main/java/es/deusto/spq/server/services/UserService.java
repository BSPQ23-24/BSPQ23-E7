package es.deusto.spq.server.services;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Response;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import es.deusto.spq.pojo.UserAssembler;
import es.deusto.spq.server.jdo.User;

public class UserService {
    private static UserService instance;

    protected static final Logger logger = LogManager.getLogger();

	private PersistenceManager pm;
	private Transaction tx;
    private PersistenceManagerFactory pmf;

    public UserService() {	
       pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    }

    public static  UserService getInstance() {
        if (instance == null) {
            instance = new UserService();
        }
        return instance;
    }

    /**
     * Retrieves a user by eMail from the database.
     * @param email The email of the user.
     * @param password The password of the user.
     * @return Response containing the user data or an error message if the user is not found.
     */
    public Response getUser(@QueryParam("eMail") String eMail, @QueryParam("password") String password) {
        pm = pmf.getPersistenceManager();
        tx = pm.currentTransaction();
        try {
            User user = pm.getObjectById(User.class, eMail);
            if (user != null) {
                return Response.ok(UserAssembler.getInstance().UserToData(user)).build();
            } else {
                logger.info("User not found");
                return Response.status(Response.Status.NOT_FOUND).entity("User not found").build();
            }
        } catch (Exception e) {
            logger.info(e);
            logger.info("Error occurred while fetching user");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error occurred while fetching user.").build();
        } finally {
            pm.close();
        }
    }

}
