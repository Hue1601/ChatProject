package com.example.routervuebe.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ConversationResponse {
    private int id;
    private String conversationName;
    private String type;
    private LocalDateTime createdAt;

}
