package com.example.routervuebe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="messages")
public class Messages {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
private Integer id;

    private String messagetext;
    private String messagetype;
    private LocalDateTime timestamp;

    @ManyToOne
    @JoinColumn(name="iduser")
    private Users iduser;

    @ManyToOne
    @JoinColumn(name = "idconversations")
    private Conversations conversation;

    public Messages() {
    }

    public Messages(Integer id, String messagetext, String messagetype, LocalDateTime timestamp, Users iduser, Conversations conversation) {
        this.id = id;
        this.messagetext = messagetext;
        this.messagetype = messagetype;
        this.timestamp = timestamp;
        this.iduser = iduser;
        this.conversation = conversation;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMessagetext() {
        return messagetext;
    }

    public void setMessagetext(String messagetext) {
        this.messagetext = messagetext;
    }

    public String getMessagetype() {
        return messagetype;
    }

    public void setMessagetype(String messagetype) {
        this.messagetype = messagetype;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Users getIduser() {
        return iduser;
    }

    public void setIduser(Users iduser) {
        this.iduser = iduser;
    }

    public Conversations getConversation() {
        return conversation;
    }

    public void setConversation(Conversations conversation) {
        this.conversation = conversation;
    }
}
