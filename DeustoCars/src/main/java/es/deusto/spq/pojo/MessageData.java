package es.deusto.spq.pojo;

public class MessageData {

    private String message;

    public MessageData() {
        // for serialization
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}