package com.example.routervuebe.controller;

import com.example.routervuebe.service.ConversationService;
import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.Messages;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.ConversationsRepo;
import com.example.routervuebe.repo.MessagesRepo;
import com.example.routervuebe.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<List<Conversations>> getUserConversations(@RequestParam Integer userId) {
        List<Conversations> conversations = conversationService.getConversationById(userId);
        return ResponseEntity.ok(conversations);
    }

@GetMapping("/detail-conversation")
public List<Messages> getConversationDetails(@RequestParam Integer conversationId) {
    return messagesRepo.getConversationDetail(conversationId);
}

}
