package es.deusto.spq.pojo;

public class UserData {

    private String login;
    private String password;

    public UserData() {
        // for serialization
    }

    public String getLogin() {
        return this.login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String toString() {
        return String.format("[login=%s, password=%s]", login, password);
    }
}