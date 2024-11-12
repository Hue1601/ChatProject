package com.example.routervuebe.repo;

import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.UserConversations;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserConversationRepo extends JpaRepository<UserConversations, Integer> {
    @Query("SELECT uc.conversationid FROM UserConversations uc WHERE uc.userid.id = :userId")
    List<Conversations> findConversationsByUserId(@Param("userId") Integer userId);
}
