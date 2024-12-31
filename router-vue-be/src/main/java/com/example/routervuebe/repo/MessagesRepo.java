package com.example.routervuebe.repo;
import com.example.routervuebe.entity.Messages;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessagesRepo extends JpaRepository<Messages,Integer> {
@Query("SELECT m FROM Messages m WHERE m.conversation.id = :conversationId")
List<Messages> getConversationDetail(@Param("conversationId") Integer conversationId);

}
