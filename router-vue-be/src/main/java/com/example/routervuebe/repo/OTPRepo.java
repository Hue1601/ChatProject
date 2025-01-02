package com.example.routervuebe.repo;

import com.example.routervuebe.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepo extends JpaRepository<OTP, Integer> {

@Query(value = "SELECT TOP 1 * FROM Otp o WHERE o.username = :username ORDER BY o.create_at DESC", nativeQuery = true)
OTP findLatestOtpByUsername(@Param("username") String username);
}
