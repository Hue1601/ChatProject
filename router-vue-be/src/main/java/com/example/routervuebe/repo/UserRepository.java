package com.example.routervuebe.repo;

import com.example.routervuebe.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users,Integer> {
  @Query("SELECT u FROM Users u WHERE u.username LIKE ?1")
    List<Users> search(String keyword);
//  Optional<Users> findByUsername(String username);
Optional<Users> findById(Integer userId);

//  @Query("SELECT u FROM Users u WHERE u.username LIKE ?1")
Users findByUsername(String username);
}



