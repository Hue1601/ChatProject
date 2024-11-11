package com.example.routervuebe.controller;

import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.Messages;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.ConversationsRepo;
import com.example.routervuebe.repo.MessagesRepo;
import com.example.routervuebe.repo.UsersRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api")
@CrossOrigin("http://localhost:5173")
public class ChatController {
    @Autowired
    private UsersRepo usersRepo;

    @Autowired
    private MessagesRepo messagesRepo;

    @Autowired
    private ConversationsRepo conversationsRepo;
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Users users) {
        Optional<Users> findUser = usersRepo.findByUsername(users.getUsername());
        if (findUser.isPresent() && users.getPass().equals(findUser.get().getPass())) {
            Map<String, String> response = new HashMap<>();
            response.put("message", "Login successful!");
            response.put("username", findUser.get().getUsername());
            response.put("id", findUser.get().getId().toString());
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
    }
    @GetMapping("/messages")
    public List<Messages> getMessages() {
        return messagesRepo.findAll();
    }

//    @GetMapping("/conversations")
//    public List<Conversations> getConversationsByUserId(@RequestParam(value = "userid", required = false) Integer userid) {
//        if (userid != null) {
//            return conversationsRepo.findByUserId(userid);
//        }
//        return new ArrayList<>();
//    }
    @GetMapping("/conversation")
   public List<Conversations> getAll(){
        return conversationsRepo.findAll();
    }
//    @PostMapping("/messages")
//    public ResponseEntity<Messages> sendMessage(@RequestBody Messages message) {
//
//        Conversations conversation = conversationsRepo.findById(message.getIdconversations().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("Conversation not found"));
//
//        Users user = usersRepo.findById(message.getIduser().getId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//        message.setTimestamp(LocalDateTime.now());
//        message.setIdconversations(conversation);
//        message.setIduser(user);
//        Messages savedMessage = messagesRepo.save(message);
//        return ResponseEntity.ok(savedMessage);
//    }

}
