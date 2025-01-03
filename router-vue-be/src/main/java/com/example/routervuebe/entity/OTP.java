package com.example.routervuebe.Entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "otp")
@NoArgsConstructor
public class OTP {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String email;
    private String otp;
    private String username;
    private LocalDateTime createAt;
    private LocalDateTime expireAt;

    public OTP(String email, String otp, String username, LocalDateTime createAt, LocalDateTime expireAt) {
        this.email = email;
        this.otp = otp;
        this.username = username;
        this.createAt = createAt;
        this.expireAt = expireAt;
    }
}
