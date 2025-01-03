package com.example.routervuebe.Repository;

import com.example.routervuebe.Entity.Conversations;
import com.example.routervuebe.Entity.UserConversations;
import com.example.routervuebe.Response.ConversationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConversationRepo extends JpaRepository<UserConversations, Integer> {

@Query("SELECT new com.example.routervuebe.Response.ConversationResponse(c.id, c.conversationName, c.isGroup) " +
        "FROM Conversations c " +
        "JOIN c.userConversations uc " +
        "JOIN uc.userid u " +
        "WHERE u.username = :username")
List<ConversationResponse> findConversationsByUsername(@Param("username") String username);

}
