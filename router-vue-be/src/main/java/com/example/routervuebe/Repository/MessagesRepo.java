package com.example.routervuebe.repository;
import com.example.routervuebe.entity.Messages;
import com.example.routervuebe.response.ConversationDetailResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepo extends JpaRepository<Messages,Integer> {
    @Query("SELECT new com.example.routervuebe.response.ConversationDetailResponse(m.messagetext, m.messagetype, m.timestamp, u.username,u.id) " +
            "FROM Messages m " +
            "JOIN m.iduser u " +
            "WHERE m.conversation.id = :conversationId")
    List<ConversationDetailResponse> getConversationDetail(@Param("conversationId") Integer conversationId);

}
