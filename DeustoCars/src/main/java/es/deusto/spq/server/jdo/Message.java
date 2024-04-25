package es.deusto.spq.server.jdo;

import javax.jdo.annotations.PersistenceCapable;

import java.time.format.DateTimeFormatter;
import java.time.ZonedDateTime;

@PersistenceCapable
public class Message {
	User user=null;
	String text=null;
	ZonedDateTime timestamp;

    public Message(String text) {
        this.text = text;
		this.timestamp = ZonedDateTime.now();
    }

	public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        return "Message: message --> " + this.text + ", timestamp -->  " + formatter.format(timestamp);
    }
}