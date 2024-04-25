package es.deusto.spq.pojo;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;


public class DirectMessageTest {

    DirectMessage directMessage;

    @Mock
    UserData userData;

    @Mock
    MessageData messageData;

    @Before
    public void setUp() {
        directMessage = new DirectMessage();
        directMessage.setUserData(userData);
        directMessage.setMessageData(messageData);
    }

    @Test
    public void getUserData() {
        assertEquals(userData, directMessage.getUserData());
    }

    @Test
    public void getMessageData() {
        assertEquals(messageData, directMessage.getMessageData());
    }
}
