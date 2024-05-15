package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;


import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Transaction;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status.Family;
import org.glassfish.grizzly.http.server.HttpServer;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.github.noconnor.junitperf.JUnitPerfRule;
import com.github.noconnor.junitperf.JUnitPerfTest;
import com.github.noconnor.junitperf.JUnitPerfTestRequirement;
import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;

import categories.PerformanceTest;
import es.deusto.spq.pojo.CustomerData;
import es.deusto.spq.pojo.VehicleData;
import es.deusto.spq.server.jdo.User;

@Category(PerformanceTest.class)
public class ResourcePerfTest {

    private static final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
    
    private static final Logger logger = LoggerFactory.getLogger(ResourcePerfTest.class);

    private static HttpServer server;
    private WebTarget target;

    @Rule
    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));

//    @BeforeClass
//    public static void prepareTests() throws Exception {
//        // start the server
//        server = Main.startServer();
//
//        PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx = pm.currentTransaction();
//        try {
//            tx.begin();
//            pm.makePersistent(new User("john", "1234"));
//            tx.commit();
//        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            pm.close();
//        }
//    }

    @Before
    public void setUp() {
        // create the client
    	String hostname = "localhost";
    	String port = "8080";
        Client c = ClientBuilder.newClient();
        target = c.target(String.format("http://%s:%s/deustocars", hostname, port));

    }

//    @AfterClass
//    public static void tearDownServer() throws Exception {
//        server.shutdown();
//
//        PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx = pm.currentTransaction();
//        try {
//            tx.begin();
//            pm.newQuery(User.class).deletePersistentAll();
//            tx.commit();
//        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            pm.close();
//        }
//    }
    
    
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs=1000)
//    @JUnitPerfTestRequirement(maxLatency=1000)
//    public void testDeleteVehicle() {
//
//        Response response = target.path("server/deletevehicle")
//                .queryParam("numberPlate", "123AB")
//                .request(MediaType.APPLICATION_JSON)
//                .delete();
//
//
//        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
//    }
//
    @Test
    @JUnitPerfTest(threads = 10, durationMs=1000)
    @JUnitPerfTestRequirement(maxLatency=1000)
    public void testInsertVehicle() {
        VehicleData vehicle = new VehicleData("PruebasSQL", "Toyota", "Corolla", true, true);
        logger.info("Error");

        Response response = target.path("server/addvehicle")
            .request(MediaType.APPLICATION_JSON)
            .post(Entity.entity(vehicle, MediaType.APPLICATION_JSON));
        logger.info(response.toString());
        
        logger.info("Error22");

        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
    }
    
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs=1000)
//    @JUnitPerfTestRequirement(maxLatency=1000)
//    public void testInsertCustomer() {
//        CustomerData customer = new CustomerData("CustomerSQL@gmail.com", "Ej", "AL");
//
//        Response response = target.path("server/addcustomer")
//            .request(MediaType.APPLICATION_JSON)
//            .post(Entity.entity(customer, MediaType.APPLICATION_JSON));
//
//        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
//    }
}


