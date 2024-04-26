package es.deusto.spq.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

/**
 * Represents a message entity.
 */
@PersistenceCapable
public class Message {
    User user;
    String text;
    ZonedDateTime timestamp;

    /**
     * Constructs a Message object with the specified text and current timestamp.
     * 
     * @param text The text of the message.
     */
    public Message(String text) {
        this.text = text;
        this.timestamp = ZonedDateTime.now();
    }

    /**
     * Retrieves the user associated with the message.
     * 
     * @return The user associated with the message.
     */
    public User getUser() {
        return user;
    }

    /**
     * Sets the user associated with the message.
     * 
     * @param user The user to set.
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Returns a string representation of the Message object.
     * 
     * @return A string representation of the Message object.
     */
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return "Message: message --> " + this.text + ", timestamp -->  " + formatter.format(timestamp);
    }
}
