package com.example.routervuebe.Response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ConversationDetailResponse {
    private String messageText;
    private String messageType;
    private LocalDateTime createAt;
    private String username;
}
