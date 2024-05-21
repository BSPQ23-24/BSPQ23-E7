package es.deusto.spq.server.jdo;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.PrimaryKey;

/**
 * Represents a user entity.
 */
@PersistenceCapable
public class User {
    @PrimaryKey
    String login;
    String password;
    Boolean isAdmin;

    /**
     * Default constructor for User.
     */
    public User() {
    }

    /**
     * Constructs a User object with the specified login and password.
     * 
     * @param login     The login of the user.
     * @param password  The password of the user.
     * @param isAdmin   Boolean indicating if the user is admin or not
     */
    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    /**
     * Retrieves the login of the user.
     * 
     * @return The login of the user.
     */
    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
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
     * Retrieves if the user is admin or not.
     * 
     * @return If the user is admin or not.
     */
    public Boolean getIsAdmin() {
        return this.isAdmin;
    }

    /**
     * Sets if the user is admin.
     * 
     * @param isAdmin The boolean isAdmin to set.
     */
    public void setIsAdmin(Boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * Returns a string representation of the User object.
     * 
     * @return A string representation of the User object.
     */
    @Override
    public String toString() {
        return "User: login --> " + this.login + ", password --> " + this.password;
    }
}
