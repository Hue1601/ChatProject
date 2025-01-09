package com.example.routervuebe.Repository;

import com.example.routervuebe.Entity.Conversations;
import com.example.routervuebe.Entity.UserConversations;
import com.example.routervuebe.Response.ConversationResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConversationRepo extends JpaRepository<UserConversations, Integer> {

//@Query("SELECT new com.example.routervuebe.Response.ConversationResponse(c.id, c.conversationName, c.type,c.createAt) " +
//        "FROM Conversations c " +
//        "JOIN c.userConversations uc " +
//        "JOIN uc.userid u " +
//        "WHERE u.username = :username")
//List<ConversationResponse> findConversationsByUsername(@Param("username") String username);

    @Query("SELECT new com.example.routervuebe.Response.ConversationResponse(c.id, c.conversationName, c.type, c.createAt, " +
            "(SELECT m.messagetext FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1), " +
            "(SELECT m.timestamp FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1)) " +
            "FROM Conversations c " +
            "JOIN c.userConversations uc " +
            "JOIN uc.userid u " +
            "WHERE u.username = :username")
    List<ConversationResponse> findConversationsByUsername(@Param("username") String username);

}
