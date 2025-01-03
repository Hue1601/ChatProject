package com.example.routervuebe.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@Table(name="userconversations")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
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

}
