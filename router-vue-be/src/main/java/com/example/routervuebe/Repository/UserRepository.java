package com.example.routervuebe.repository;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.response.UserResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<Users, Integer> {
    @Query("SELECT u FROM Users u WHERE u.username LIKE ?1")
    List<Users> search(String keyword);

    Optional<Users> findById(Integer userId);

    Users findByUsername(String username);

    @Query("SELECT new com.example.routervuebe.response.UserResponse(u.id,u.username) FROM Users u")
    List<UserResponse> getUsers();

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

}



