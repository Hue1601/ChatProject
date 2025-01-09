package com.example.routervuebe.controller;

import com.example.routervuebe.repository.UserConversationRepo;
import com.example.routervuebe.request.MessageRequest;
import com.example.routervuebe.response.ConversationResponse;
import com.example.routervuebe.response.MessageResponse;
import com.example.routervuebe.service.ConversationService;
import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.repository.MessagesRepo;
import com.example.routervuebe.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagesRepo messagesRepo;
    @Autowired
    private UserConversationRepo userConversationRepo;
    @Autowired
    private ConversationService conversationService;


    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request) {
        MessageResponse message = conversationService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.OK).body(message);
    }


    @GetMapping("/list-conversation")
    public ResponseEntity<?> getConversationByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<ConversationResponse> conversations = userConversationRepo.findConversationsByUsername(username);
        return ResponseEntity.ok(conversations);
    }

    //    http://localhost:8080/detail-conversation?id=1
//    @GetMapping("/detail-conversation")
//    public List<Messages> getConversationDetails(@RequestParam Integer id) {
//        return messagesRepo.getConversationDetail(id);
//    }
    @GetMapping("/detail-conversation/{conversationId}")
    public ResponseEntity<?> getConversationById(@PathVariable Integer conversationId) {
        return ResponseEntity.ok(messagesRepo.getConversationDetail(conversationId));

    }

    @PostMapping("/create-conversation")
    public ResponseEntity<?> createConversation(@RequestBody Map<String, Object> requestBody) {
        try {
            String name = (String) requestBody.get("name");
            String type = (String) requestBody.get("type");
            List<Integer> userIds = (List<Integer>) requestBody.get("member");

            Conversations conversation = conversationService.createConversation(name, type, userIds);
            return ResponseEntity.ok(conversation);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
