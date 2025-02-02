package com.example.routervuebe.service;


import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.Messages;
import com.example.routervuebe.entity.UserConversations;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repository.ConversationsRepo;
import com.example.routervuebe.repository.MessagesRepo;
import com.example.routervuebe.repository.UserConversationRepo;
import com.example.routervuebe.repository.UserRepository;
import com.example.routervuebe.request.MessageRequest;
import com.example.routervuebe.response.MessageResponse;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;


@Service
public class ConversationService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private ConversationsRepo conversationRepo;

    @Autowired
    private UserConversationRepo userConversationRepo;

    @Autowired
    private MessagesRepo messagesRepo;
    public Conversations createConversation(String name, String type, List<Integer> member){
        Conversations conversation = new Conversations();
        conversation.setConversationName(name);
        conversation.setType(type);
        conversation.setCreateAt(LocalDateTime.now());

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

    public MessageResponse sendMessage(@RequestBody MessageRequest request) {

        Users user = userRepo.findById(request.getUser())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Conversations conversation = conversationRepo.findById(request.getConversation())
                .orElseThrow(() -> new RuntimeException("Conversation not found"));

        Messages messages = new Messages();
        messages.setMessagetext(request.getMessageText());
        messages.setMessagetype(request.getMessageType());
        messages.setTimestamp(LocalDateTime.now());
        messages.setIduser(user);
        messages.setConversation(conversation);

        messagesRepo.save(messages);
        return new MessageResponse(
                messages.getMessagetext(),
                messages.getMessagetype(),
                messages.getTimestamp()
        );
    }
}