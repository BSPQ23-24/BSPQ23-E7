package es.deusto.spq.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import javax.jdo.JDOHelper;
import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;
import javax.ws.rs.core.Response;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.deusto.spq.pojo.DirectMessage;
import es.deusto.spq.pojo.MessageData;
import es.deusto.spq.pojo.UserData;
import es.deusto.spq.server.jdo.User;


public class ResourceTest {

    private Resource resource;

    @Mock
    private PersistenceManager persistenceManager;

    @Mock
    private Transaction transaction;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // initializing static mock object PersistenceManagerFactory
        try (MockedStatic<JDOHelper> jdoHelper = Mockito.mockStatic(JDOHelper.class)) {
            PersistenceManagerFactory pmf = mock(PersistenceManagerFactory.class);
            jdoHelper.when(() -> JDOHelper.getPersistenceManagerFactory("datanucleus.properties")).thenReturn(pmf);
            
            when(pmf.getPersistenceManager()).thenReturn(persistenceManager);
            when(persistenceManager.currentTransaction()).thenReturn(transaction);

            // instantiate tested object with mock dependencies
            resource = new Resource();
        }
    }

    @Test
    public void testSayHello() {
        Response response = resource.sayHello();
        assertEquals(Response.Status.OK, response.getStatusInfo());
        assertEquals("Hello world!", response.getEntity().toString());
    }

    @Test
    public void testSayMessage() {
        // prepare a mock query object to be returned by
        // mock persistence manager
        @SuppressWarnings("unchecked") Query<User> query = mock(Query.class);
        when(persistenceManager.newQuery(User.class)).thenReturn(query);
        
        // prepare response when execute method on mock Query object is
        // is called with expected parameters
        User user = new User("test-login", "passwd");
        when(query.execute(user.getLogin(), user.getPassword())).thenReturn(user);
        
        DirectMessage directMessage = new DirectMessage();
        UserData userData = new UserData();
        userData.setLogin("test-login");
        userData.setPassword("passwd");
        directMessage.setUserData(userData);

        MessageData messageData = new MessageData();
        messageData.setMessage("Message");
        directMessage.setMessageData(messageData);

        // prepare mock transaction behaviour
        when(transaction.isActive()).thenReturn(false);

        // call tested method
        Response response = resource.sayMessage(directMessage);

        //verify that the filter is set with the correct value
        ArgumentCaptor<String> filterCaptor = ArgumentCaptor.forClass(String.class);
        verify(query).setFilter(filterCaptor.capture());
        assertEquals("this.login == :login && this.password == :password", filterCaptor.getValue());

        //verify that the unique is set to true
        ArgumentCaptor<Boolean> uniqueCaptor = ArgumentCaptor.forClass(Boolean.class);
        verify(query).setUnique(uniqueCaptor.capture());
        assertTrue(uniqueCaptor.getValue());
        
        // check expected response
        assertEquals(Response.Status.OK, response.getStatusInfo());
        MessageData responseData = (MessageData) response.getEntity();
        assertEquals("Message", responseData.getMessage());
    }

    @Test
    public void testSayMessageUserNotFound() {
        // prepare a mock query object to be returned by
        // mock persistence manager
        @SuppressWarnings("unchecked") Query<User> query = mock(Query.class);
        when(persistenceManager.newQuery(User.class)).thenReturn(query);
        
        // simulate that the object is not found in the database
        when(query.execute(anyString(), anyString())).thenReturn(null);
        
        DirectMessage directMessage = new DirectMessage();
        UserData userData = new UserData();
        userData.setLogin("test-login");
        userData.setPassword("passwd");
        directMessage.setUserData(userData);

        MessageData messageData = new MessageData();
        messageData.setMessage("Message");
        directMessage.setMessageData(messageData);

        // prepare mock transaction behaviour
        when(transaction.isActive()).thenReturn(true);

        // call tested method
        Response response = resource.sayMessage(directMessage);

        // expected Bad Request as response
        assertEquals(Response.Status.BAD_REQUEST, response.getStatusInfo());
    }

    @Test
    public void testRegisterUserNotFound() {
        // prepare mock Persistence Manager to return User
        UserData userData = new UserData();
        userData.setLogin("test-login");
        userData.setPassword("passwd");

        // simulate that the object is not found in the database
        when(persistenceManager.getObjectById(any(), anyString())).thenThrow(new JDOObjectNotFoundException());

        // prepare mock transaction behaviour
        when(transaction.isActive()).thenReturn(true);

        // call tested method
        Response response = resource.registerUser(userData);

        // check that the new user is stored in the database with the correct values
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(persistenceManager).makePersistent(userCaptor.capture());
        assertEquals("test-login", userCaptor.getValue().getLogin());
        assertEquals("passwd", userCaptor.getValue().getPassword());

        // check expected response
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }

    @Test
    public void testRegisterUser() {
        // prepare mock Persistence Manager to return User
        UserData userData = new UserData();
        userData.setLogin("test-login");
        userData.setPassword("passwd");

        // simulate that 
        User user = spy(User.class);
        when(persistenceManager.getObjectById(User.class, userData.getLogin())).thenReturn(user);

        // call tested method
        Response response = resource.registerUser(userData);

        // check that the user is set by the code with the password
        ArgumentCaptor<String> passwordCaptor = ArgumentCaptor.forClass(String.class);
        verify(user).setPassword(passwordCaptor.capture());
        assertEquals("passwd", passwordCaptor.getValue());

        // check expected response
        assertEquals(Response.Status.OK, response.getStatusInfo());
    }
}
