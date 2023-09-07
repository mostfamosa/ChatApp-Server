package chatApp.Entities;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "ChatMessage")
public class ChatMessage {
    private String sender;

    private String chatId;
    private String content;
    private LocalDate dateMessage;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    public LocalDate getDateMessage() {
        return dateMessage;
    }

    public int getId() {
        return id;
    }

    public String getChatId() {
        return chatId;
    }

    public void setChatId(String chatId) {
        this.chatId = chatId;
    }

    public ChatMessage() {
    }

    public ChatMessage(String sender, String content, String chatId) {
        this.sender = sender;
        this.content = content;
        this.chatId = chatId;
        this.dateMessage = LocalDate.now();
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
        return "ChatMessage{" +
                "sender='" + sender + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

}