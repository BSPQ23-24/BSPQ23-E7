package es.deusto.spq.pojo;

/**
 * Represents user data.
 */
public class UserData {

    private String login;
    private String password;

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
        return this.login;
    }

    /**
     * Sets the login of the user.
     *
     * @param login The login to set.
     */
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
     * Returns a string representation of the UserData object.
     *
     * @return A string representation of the UserData object.
     */
    @Override
    public String toString() {
        return String.format("[login=%s, password=%s]", login, password);
    }
}
