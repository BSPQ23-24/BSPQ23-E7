package es.deusto.spq.server.jdo;

import java.util.Set;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;
import javax.jdo.annotations.Join;
import javax.jdo.annotations.Persistent;
import java.util.LinkedHashSet;

/**
 * Represents a user entity.
 */
@PersistenceCapable
public class User {
    @PrimaryKey
    String login;
    String password;

    @Persistent(mappedBy="user", dependentElement="true")
    @Join
    Set<Message> messages = new LinkedHashSet<>();

    /**
     * Default constructor for User.
     */
    User() {
    }

    /**
     * Constructs a User object with the specified login and password.
     * 
     * @param login    The login of the user.
     * @param password The password of the user.
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Adds a message to the user's set of messages.
     * 
     * @param message The message to add.
     */
    public void addMessage(Message message) {
        messages.add(message);
    }

    /**
     * Removes a message from the user's set of messages.
     * 
     * @param message The message to remove.
     */
    public void removeMessage(Message message) {
        messages.remove(message);
    }

    /**
     * Retrieves the login of the user.
     * 
     * @return The login of the user.
     */
    public String getLogin() {
        return this.login;
    }

    /**
     * Retrieves the password of the user.
     * 
     * @return The password of the user.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Sets the password of the user.
     * 
     * @param password The password to set.
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Retrieves the set of messages associated with the user.
     * 
     * @return The set of messages associated with the user.
     */
    public Set<Message> getMessages() {
        return this.messages;
    }

    /**
     * Returns a string representation of the User object.
     * 
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        StringBuilder messagesStr = new StringBuilder();
        for (Message message: this.messages) {
            messagesStr.append(message.toString() + " - ");
        }
        return "User: login --> " + this.login + ", password --> " + this.password + ", messages --> [" + messagesStr + "]";
    }
}
