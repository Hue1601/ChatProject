package com.example.routervuebe.repo;

import com.example.routervuebe.entity.Conversations;
import com.example.routervuebe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ConversationsRepo extends JpaRepository<Conversations,Integer> {
//    @Query("SELECT c FROM Conversations c JOIN UserConversations uc ON c.id = uc.conversationid.id WHERE uc.userid.id = :userid")
//    List<Conversations> findByUserId(@Param("userid") int userid);
}

