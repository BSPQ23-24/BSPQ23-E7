package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;


public class MessageDataTest {

    MessageData messageData;

    @Before
    public void setUp() {
        messageData = new MessageData();
        messageData.setMessage("Hello World!");
    }

    @Test
    public void testGetMessage() {
        assertEquals("Hello World!", messageData.getMessage());;
    }
}
