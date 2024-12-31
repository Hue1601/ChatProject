package com.example.routervuebe.repo;

import com.example.routervuebe.entity.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepo extends JpaRepository<OTP, Integer> {
}
