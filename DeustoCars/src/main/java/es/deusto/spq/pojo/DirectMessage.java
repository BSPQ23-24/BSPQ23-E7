package es.deusto.spq.pojo;

public class DirectMessage {

    private UserData userData;
    private MessageData messageData;

    public DirectMessage() {
        // for serialization
    }

    public void setUserData(UserData userData) {
        this.userData = userData;
    }

    public UserData getUserData() {
        return this.userData;
    }

    public void setMessageData(MessageData messageData) {
        this.messageData = messageData;
    }

    public MessageData getMessageData() {
        return this.messageData;
    }
}