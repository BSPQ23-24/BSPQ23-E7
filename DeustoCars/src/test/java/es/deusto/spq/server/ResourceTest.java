//package es.deusto.spq.server;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.ArgumentMatchers.anyString;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import javax.jdo.JDOHelper;
//import javax.jdo.JDOObjectNotFoundException;
//import javax.jdo.PersistenceManager;
//import javax.jdo.PersistenceManagerFactory;
//import javax.jdo.Query;
//import javax.jdo.Transaction;
//import javax.ws.rs.core.Response;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.mockito.ArgumentCaptor;
//import org.mockito.Mock;
//import org.mockito.MockedStatic;
//import org.mockito.Mockito;
//import org.mockito.MockitoAnnotations;
//
//import es.deusto.spq.pojo.DirectMessage;
//import es.deusto.spq.pojo.MessageData;
//import es.deusto.spq.pojo.UserData;
//import es.deusto.spq.server.jdo.User;
//
//
//public class ResourceTest {
//
//    private Resource resource;
//
//    @Mock
//    private PersistenceManager persistenceManager;
//
//    @Mock
//    private Transaction transaction;
//
//    @Before
//    public void setUp() {
//        MockitoAnnotations.openMocks(this);
//
//        // initializing static mock object PersistenceManagerFactory
//        try (MockedStatic<JDOHelper> jdoHelper = Mockito.mockStatic(JDOHelper.class)) {
//            PersistenceManagerFactory pmf = mock(PersistenceManagerFactory.class);
//            jdoHelper.when(() -> JDOHelper.getPersistenceManagerFactory("datanucleus.properties")).thenReturn(pmf);
//            
//            when(pmf.getPersistenceManager()).thenReturn(persistenceManager);
//            when(persistenceManager.currentTransaction()).thenReturn(transaction);
//
//            // instantiate tested object with mock dependencies
//            resource = new Resource();
//        }
//    }
//
//
//    @Test
//    public void testRegisterUser() {
//        // prepare mock Persistence Manager to return User
//        UserData userData = new UserData();
//        userData.setLogin("test-login");
//        userData.setPassword("passwd");
//
//        // simulate that 
//        User user = spy(User.class);
//        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);
//
//        // call tested method
//        Response response = resource.registerUser(userData);
//
//        // check that the user is set by the code with the password
//        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
//        verify(user).setPassword(passwordCaptor.capture());
//        assertEquals("passwd", passwordCaptor.getValue());
//
//        // check expected response
//        assertEquals(Response.Status.OK, response.getStatusInfo());
//    }
//}
