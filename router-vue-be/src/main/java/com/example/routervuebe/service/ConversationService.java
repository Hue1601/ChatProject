package com.example.routervuebe.service;


import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.UserConversationRepo;
import com.example.routervuebe.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConversationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserConversationRepo userConversationRepository;

//    public List<Conversations> getConversationsByUsername(String username) {
//        Users user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        return userConversationRepository.findConversationsByUserId(user.getId());
//    }
    public List<Conversations> getConversationById(Integer userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return  userConversationRepository.findConversationsByUserId(user.getId());
    }
}