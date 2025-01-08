package com.example.routervuebe.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name="messages")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
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
}
