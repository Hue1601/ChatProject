package com.example.routervuebe.controller;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.exception.MessageError;
import com.example.routervuebe.repository.ConversationsRepo;
import com.example.routervuebe.repository.UserConversationRepo;
import com.example.routervuebe.request.MessageRequest;
import com.example.routervuebe.response.ConversationResponse;
import com.example.routervuebe.response.MessageResponse;
import com.example.routervuebe.response.UserResponse;
import com.example.routervuebe.service.ConversationService;
import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.repository.MessagesRepo;
import com.example.routervuebe.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
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
@Autowired
private ConversationsRepo conversationsRepo;
//    @MessageMapping("/chat.sendMessage") // Endpoint client gửi tin nhắn
//    @SendTo("/topic/messages") // Endpoint để phát tin nhắn tới các client
//    public MessageResponse sendMessage(MessageRequest request) {
//        // Lưu tin nhắn vào DB và trả về tin nhắn cho tất cả client
//      //  return conversationService.sendMessage(request);
//        try {
//            return conversationService.sendMessage(request);
//        } catch (Exception e) {
//            // Handle exceptions and log error
//            throw new RuntimeException("Failed to send message", e);
//        }
//    }
    @GetMapping("/list-conversation")
    public ResponseEntity<?> getConversationByUsername(HttpServletRequest request) {
        String username = (String) request.getAttribute("username");
        List<ConversationResponse> conversations = userConversationRepo.findConversationsByUsername(username);
        return ResponseEntity.ok(conversations);
    }
    @PostMapping("/send-message")
    public ResponseEntity<?> sendMessage(@RequestBody MessageRequest request) {
        MessageResponse message = conversationService.sendMessage(request);
        return ResponseEntity.status(HttpStatus.OK).body(message);
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

    @GetMapping("/member-in-group/{conversationId}")
    public ResponseEntity<?> getUsersInConversation(@PathVariable int conversationId) {
        List<UserResponse> users = userConversationRepo.findUsersByConversationId(conversationId);
        return ResponseEntity.ok(users);
    }
    @DeleteMapping("/delete-conversation/{conversationId}")
    public ResponseEntity<?> deleteConversation(@PathVariable int conversationId) {
           messagesRepo.deleteMessagesByConversationId(conversationId);
            return ResponseEntity.ok(MessageError.DELETE_MESSAGE);
    }

    @DeleteMapping("/leave-group/{conversationId}")
    public ResponseEntity<?> leaveGroup(@PathVariable int conversationId, @RequestParam int userId) {
        int rowsAffected = userConversationRepo.leaveConversation(userId, conversationId);
        if (rowsAffected > 0) {
            return ResponseEntity.ok(MessageError.LEAVE_GROUP);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User is not part of the conversation.");
        }
    }

}
