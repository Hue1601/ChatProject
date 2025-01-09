package com.example.routervuebe.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
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

    @Column(name = "type")
    private String type;

    @Column(name="create_at")
    private LocalDateTime createAt;
    @OneToMany(mappedBy = "conversationid")
    private List<UserConversations> userConversations;
}

