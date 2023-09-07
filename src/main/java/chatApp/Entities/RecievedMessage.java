package chatApp.Entities;

public class RecievedMessage {

    private String sender;
    private String content;
    private String token;
    private String chatId;

    public RecievedMessage(String sender, String content) {
        this.sender = sender;
        this.content = content;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public RecievedMessage() {
    }

    public RecievedMessage(String sender, String content ,String chatId) {
        this.sender = sender;
        this.content = content;
        this.chatId = chatId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "RecievedMessage{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                ", token='" + token + '\'' +
                ", chatId='" + chatId + '\'' +
                '}';
    }
}
