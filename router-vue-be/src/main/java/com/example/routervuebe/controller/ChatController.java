package com.example.routervuebe.controller;

import com.example.routervuebe.Repository.ConversationsRepo;
import com.example.routervuebe.Repository.UserConversationRepo;
import com.example.routervuebe.Response.ConversationResponse;
import com.example.routervuebe.service.ConversationService;
import com.example.routervuebe.Entity.Conversations;
import com.example.routervuebe.Entity.Messages;
import com.example.routervuebe.Repository.MessagesRepo;
import com.example.routervuebe.Repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")

public class ChatController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepo messagesRepo;
    @Autowired
    private UserConversationRepo userConversationRepo;
    @Autowired
    private ConversationService conversationService;

    @GetMapping("/messages")
    public List<Messages> getMessages(@RequestParam Integer conversationId) {
        return messagesRepo.getConversationDetail(conversationId);
    }

    @PostMapping("/send-message")
    public Messages sendMessage(@RequestBody Messages message) {
        messagesRepo.save(message);
        return message;
    }

    @GetMapping("/list-conversation")
    public ResponseEntity<?> getConversationByUsername(HttpServletRequest request) {
        String username = (String)  request.getAttribute("username");
        List<ConversationResponse> conversations = userConversationRepo.findConversationsByUsername(username);
        return ResponseEntity.ok(conversations);
    }

@GetMapping("/detail-conversation")
public List<Messages> getConversationDetails(@RequestParam Integer conversationId) {
    return messagesRepo.getConversationDetail(conversationId);
}

}
