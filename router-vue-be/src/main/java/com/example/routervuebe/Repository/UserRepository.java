package com.example.routervuebe.Repository;

import com.example.routervuebe.Entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
  @Query("SELECT u FROM Users u WHERE u.username LIKE ?1")
    List<Users> search(String keyword);
Optional<Users> findById(Integer userId);

Users findByUsername(String username);
}



