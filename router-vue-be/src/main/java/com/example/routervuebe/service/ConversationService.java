package com.example.routervuebe.service;


import com.example.routervuebe.Entity.Conversations;
import com.example.routervuebe.Entity.UserConversations;
import com.example.routervuebe.Entity.Users;
import com.example.routervuebe.Repository.ConversationsRepo;
import com.example.routervuebe.Repository.UserConversationRepo;
import com.example.routervuebe.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConversationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ConversationsRepo conversationRepo;

    @Autowired
    private UserConversationRepo userConversationRepo;

    public Conversations createConversation(String name, String type, List<Integer> member){
        Conversations conversation = new Conversations();
        conversation.setConversationName(name);
        conversation.setType(type);
        conversationRepo.save(conversation);
        for (Integer userId : member) {
            Users user = userRepo.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found: " + userId));
            UserConversations userConversation = new UserConversations();
            userConversation.setUserid(user);
            userConversation.setConversationid(conversation);
            userConversationRepo.save(userConversation);
        }
        return conversation;
    }

}