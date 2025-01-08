package com.example.routervuebe.Request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageRequest {
    private String messageText;
    private String messageType;
    private Integer user;
    private Integer conversation;
}
