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
@CrossOrigin("http://localhost:5173")
public class ChatController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepo messagesRepo;

    @Autowired
    private ConversationService conversationService;

//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(@RequestBody Users users) {
//       Optional<Users> findUser = userRepository.findByUsername(users.getUsername());
//        if (findUser.isPresent() && users.getPass().equals(findUser.get().getPass())) {
//            Map<String, String> response = new HashMap<>();
//            response.put("message", "Login successful!");
//            response.put("username", findUser.get().getUsername());
//            response.put("id", findUser.get().getId().toString());
//            return ResponseEntity.ok(response);
//        } else {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
//        }
//    }
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
