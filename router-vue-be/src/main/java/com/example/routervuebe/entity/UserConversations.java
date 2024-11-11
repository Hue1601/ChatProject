package com.example.routervuebe.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="userconversations")
public class UserConversations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name="userid")
    private Users userid;
    @ManyToOne
    @JoinColumn(name="conversationid")
    private Conversations conversationid;

    public UserConversations() {
    }

    public UserConversations(Integer id, Users userid, Conversations conversationid) {
        this.id = id;
        this.userid = userid;
        this.conversationid = conversationid;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getUserid() {
        return userid;
    }

    public void setUserid(Users userid) {
        this.userid = userid;
    }

    public Conversations getConversationid() {
        return conversationid;
    }

    public void setConversationid(Conversations conversationid) {
        this.conversationid = conversationid;
    }
}
