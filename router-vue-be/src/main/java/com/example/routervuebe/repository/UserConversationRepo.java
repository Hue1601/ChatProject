package com.example.routervuebe.repository;

import com.example.routervuebe.entity.UserConversations;
import com.example.routervuebe.response.ConversationResponse;
import com.example.routervuebe.response.UserResponse;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

//    @Query("SELECT new com.example.routervuebe.response.ConversationResponse(c.id, c.conversationName, c.type, c.createAt, " +
//            "(SELECT m.messagetext FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1), " +
//            "(SELECT m.timestamp FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1)) " +
//            "FROM Conversations c " +
//            "JOIN c.userConversations uc " +
//            "JOIN uc.userid u " +
//            "WHERE u.username = :username")
//    List<ConversationResponse> findConversationsByUsername(@Param("username") String username);

    @Query("SELECT new com.example.routervuebe.response.ConversationResponse(" +
            "c.id, " +
            "c.conversationName, " +
            "c.type, " +
            "c.createAt, " +
            "(SELECT m.messagetext FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1), " +
            "(SELECT m.timestamp FROM Messages m WHERE m.conversation.id = c.id ORDER BY m.timestamp DESC LIMIT 1)) " +
            "FROM Conversations c " +
            "JOIN c.userConversations uc " +
            "JOIN uc.userid u " +
            "WHERE u.username = :username " +
            "ORDER BY COALESCE((SELECT MAX(m.timestamp) FROM Messages m WHERE m.conversation.id = c.id), c.createAt) DESC")
    List<ConversationResponse> findConversationsByUsername(@Param("username") String username);


    @Query("SELECT new com.example.routervuebe.response.UserResponse (u.id,u.username) " +
            "FROM Users u JOIN UserConversations uc ON u.id = uc.userid.id WHERE uc.conversationid.id = :conversationId")
List<UserResponse> findUsersByConversationId(@Param("conversationId") int conversationId);
    @Modifying
    @Transactional
@Query("DELETE FROM UserConversations uc WHERE uc.conversationid.id = :conversationId")
void deleteUCByConversationId(@Param("conversationId") int conversationId);
}
