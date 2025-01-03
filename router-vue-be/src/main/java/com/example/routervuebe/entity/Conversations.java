package com.example.routervuebe.Entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name="conversations")

public class Conversations {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "conversationname")
    private String conversationName;

    @Column(name = "isgroup")
    private Boolean isGroup;

    @OneToMany(mappedBy = "conversationid")
    private List<UserConversations> userConversations;
}

