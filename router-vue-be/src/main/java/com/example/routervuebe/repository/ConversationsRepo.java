package com.example.routervuebe.repository;

import com.example.routervuebe.entity.Conversations;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ConversationsRepo extends JpaRepository<Conversations,Integer> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Conversations c WHERE c.id = :conversationId")
    void deleteConversationById(@Param("conversationId") int conversationId);
}

