//package es.deusto.spq.server;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.UUID;
//
//import javax.jdo.JDOHelper;
//import javax.jdo.PersistenceManager;
//import javax.jdo.PersistenceManagerFactory;
//import javax.jdo.Transaction;
//import javax.ws.rs.client.Client;
//import javax.ws.rs.client.ClientBuilder;
//import javax.ws.rs.client.Entity;
//import javax.ws.rs.client.WebTarget;
//import javax.ws.rs.core.MediaType;
//import javax.ws.rs.core.Response;
//import javax.ws.rs.core.Response.Status.Family;
//import org.glassfish.grizzly.http.server.HttpServer;
//import org.junit.AfterClass;
//import org.junit.Before;
//import org.junit.BeforeClass;
//import org.junit.Rule;
//import org.junit.Test;
//import org.junit.experimental.categories.Category;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import com.github.noconnor.junitperf.JUnitPerfRule;
//import com.github.noconnor.junitperf.JUnitPerfTest;
//import com.github.noconnor.junitperf.reporting.providers.HtmlReportGenerator;
//
//import es.deusto.spq.pojo.DirectMessage;
//import es.deusto.spq.pojo.MessageData;
//import es.deusto.spq.pojo.UserData;
//import es.deusto.spq.pojo.VehicleData;
//import es.deusto.spq.server.jdo.Message;
//import es.deusto.spq.server.jdo.User;
//
//public class ResourcePerfTest {
//
//    private static final PersistenceManagerFactory pmf = JDOHelper.getPersistenceManagerFactory("datanucleus.properties");
//    
//    private static final Logger logger = LoggerFactory.getLogger(ResourcePerfTest.class);
//
//    private static HttpServer server;
//    private WebTarget target;
//
//    @Rule
//    public JUnitPerfRule perfTestRule = new JUnitPerfRule(new HtmlReportGenerator("target/junitperf/report.html"));
//
//    private static long registerVehicleMaxTime = 0;
//    private static long registerVehicleTotalTime = 0;
//    private static long updateVehicleMaxTime = 0;
//    private static long updateVehicleTotalTime = 0;
//    private static long registerVehicleInvocationCount = 0;
//    private static long updateVehicleInvocationCount = 0;
//
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
//
//    @Before
//    public void setUp() {
//        // create the client
//        Client c = ClientBuilder.newClient();
//        target = c.target(Main.BASE_URI).path("resource");
//    }
//
//    @AfterClass
//    public static void tearDownServer() throws Exception {
//        server.shutdown();
//
//        PersistenceManager pm = pmf.getPersistenceManager();
//        Transaction tx = pm.currentTransaction();
//        try {
//            tx.begin();
//            pm.newQuery(User.class).deletePersistentAll();
//            pm.newQuery(Message.class).deletePersistentAll();
//            tx.commit();
//        } finally {
//            if (tx.isActive()) {
//                tx.rollback();
//            }
//            pm.close();
//        }
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs = 2000)
//    public void testRegisterVehiclePerf() {
//        long startTime = System.currentTimeMillis();
//        // Preparar datos del vehículo a registrar
//        VehicleData vehicleData = new VehicleData("123ABC", "Toyota", "Corolla");
//
//        // Llamar al método de registro de vehículo del recurso
//        Response response = target.path("registerVehicle").request(MediaType.APPLICATION_JSON)
//                .post(Entity.entity(vehicleData, MediaType.APPLICATION_JSON));
//
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//
//        // Actualizar el tiempo máximo
//        if (elapsedTime > registerVehicleMaxTime) {
//            registerVehicleMaxTime = elapsedTime;
//        }
//
//        // Incrementar el contador de invocaciones
//        registerVehicleInvocationCount++;
//
//        // Agregar al tiempo total
//        registerVehicleTotalTime += elapsedTime;
//
//        // Verificar que la respuesta es exitosa
//        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
//
//        logger.info("testRegisterVehiclePerf:");
//        logger.info("Número de invocaciones: {}", registerVehicleInvocationCount);
//        logger.info("Tiempo promedio: {} ms", (registerVehicleTotalTime / registerVehicleInvocationCount));
//        logger.info("Tiempo máximo: {} ms", registerVehicleMaxTime);
//    }
//
//    @Test
//    @JUnitPerfTest(threads = 10, durationMs = 2000)
//    public void testUpdateVehiclePerf() {
//        long startTime = System.currentTimeMillis();
//        // Preparar datos del vehículo a actualizar
//        VehicleData vehicleData = new VehicleData("123ABC", "Toyota", "Corolla");
//
//        // Llamar al método de actualización de vehículo del recurso
//        Response response = target.path("updateVehicle").request(MediaType.APPLICATION_JSON)
//                .put(Entity.entity(vehicleData, MediaType.APPLICATION_JSON));
//
//        long endTime = System.currentTimeMillis();
//        long elapsedTime = endTime - startTime;
//
//        // Actualizar el tiempo máximo
//        if (elapsedTime > updateVehicleMaxTime) {
//            updateVehicleMaxTime = elapsedTime;
//        }
//
//        // Incrementar el contador de invocaciones
//        updateVehicleInvocationCount++;
//
//        // Agregar al tiempo total
//        updateVehicleTotalTime += elapsedTime;
//
//        // Verificar que la respuesta es exitosa
//        assertEquals(Family.SUCCESSFUL, response.getStatusInfo().getFamily());
//
//        logger.info("testUpdateVehiclePerf:");
//        logger.info("Número de invocaciones: {}", updateVehicleInvocationCount);
//        logger.info("Tiempo promedio: {} ms", (updateVehicleTotalTime / updateVehicleInvocationCount));
//        logger.info("Tiempo máximo: {} ms", updateVehicleMaxTime);
//    }
//}
//
//
