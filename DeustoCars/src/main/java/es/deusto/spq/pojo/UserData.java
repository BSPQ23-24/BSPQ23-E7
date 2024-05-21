package es.deusto.spq.pojo;

/**
 * Represents user data.
 */
public class UserData {

    private String email;
    private String password;
    private Boolean isAdmin;

    /**
     * Default constructor required for serialization.
     */
    public UserData() {
        // for serialization
    }

    /**
     * Retrieves the login of the user.
     *
     * @return The login of the user.
     */
    public String getLogin() {
        return this.email;
    }

    /**
     * Sets the login of the user.
     *
     * @param login The login to set.
     */
    public void setLogin(String login) {
        this.email = login;
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
     * Returns a string representation of the UserData object.
     *
     * @return A string representation of the UserData object.
     */
    @Override
    public String toString() {
        return String.format("[login=%s, password=%s]", email, password);
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

}
