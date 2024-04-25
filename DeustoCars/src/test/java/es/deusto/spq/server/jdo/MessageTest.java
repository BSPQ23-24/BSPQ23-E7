package es.deusto.spq.server.jdo;

import static org.junit.Assert.assertEquals;

import org.mockito.Mockito;
import org.mockito.MockedStatic;

import org.junit.Before;
import org.junit.Test;

import java.time.ZoneId;
import java.time.ZonedDateTime;

public class MessageTest {

    private static ZonedDateTime timestamp = ZonedDateTime.of(2023, 03, 23, 19, 15, 22, 0, ZoneId.of("Europe/Madrid"));
    private Message message;
    
    @Before
    public void setUp() {
        try (MockedStatic<ZonedDateTime> zonedDateTimeHelper = Mockito.mockStatic(ZonedDateTime.class)) {
            zonedDateTimeHelper.when(ZonedDateTime::now).thenReturn(timestamp);
            message = new Message("Hello world!");
        }
    }

    @Test
    public void getUser() {
        User user = new User("somelogin", "passwd");
        message.setUser(user);
        assertEquals(user, message.getUser());
    }

    @Test
    public void testToString() {
        assertEquals("Message: message --> Hello world!, timestamp -->  2023-03-23T19:15:22.000+0100", message.toString());
    }
}   
