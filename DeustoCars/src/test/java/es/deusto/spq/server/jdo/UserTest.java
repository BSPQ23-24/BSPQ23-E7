package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.mockito.Mockito;
import org.mockito.MockedStatic;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class UserTest {
    
    private static ZonedDateTime timestamp = ZonedDateTime.of(2023, 03, 23, 19, 15, 22, 0, ZoneId.of("Europe/Madrid"));
    private User user;

    @Before
    public void setUp() {
        user = new User("test-login", "passwd");
    }

    @Test
    public void testGetLogin() {
        assertEquals("test-login", user.getLogin());
    }

    @Test
    public void testGetPassword() {
        assertEquals("passwd", user.getPassword());
    }

    @Test
    public void testSetPassword() {
        user.setPassword("newpasswd");
        assertEquals("newpasswd", user.getPassword());
    }

    @Test
    public void testGetMessages() {
        assertTrue(user.getMessages().isEmpty());
    }

    @Test
    public void testAddMessage() {
        user.addMessage(new Message("hello"));
        assertEquals(1, user.getMessages().size());
    }

    @Test
    public void testRemoveMessage() {
        Message message = new Message("hello");
        
        user.addMessage(message);
        assertEquals(1, user.getMessages().size());
        
        user.removeMessage(message);
        assertTrue(user.getMessages().isEmpty());
    }

    @Test
    public void testToString() {
        try (MockedStatic<ZonedDateTime> zonedDateTimeHelper = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeHelper.when(() -> ZonedDateTime.now()).thenReturn(timestamp);
            user.addMessage(new Message("hello"));
            user.addMessage(new Message("goodbye"));
        }

        String expected = String.format("User: login --> test-login, password --> passwd, messages --> %s",
            "[Message: message --> hello, timestamp -->  2023-03-23T19:15:22.000+0100 - Message: message --> goodbye, timestamp -->  2023-03-23T19:15:22.000+0100 - ]"
        );
        assertEquals(expected, user.toString());
    }
}
