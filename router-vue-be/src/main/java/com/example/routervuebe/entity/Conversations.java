package com.example.routervuebe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name="conversations")

public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String conversationname;
//    @ManyToOne @JoinColumn(name="userid")
//    private Users userid;


    public Conversations() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getConversationname() {
        return conversationname;
    }

    public void setConversationname(String conversationname) {
        this.conversationname = conversationname;
    }

    public Conversations(Integer id, String conversationname) {
        this.id = id;
        this.conversationname = conversationname;
    }
}
