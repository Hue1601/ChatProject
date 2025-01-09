package com.example.routervuebe.controller;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.exception.AuthenticationException;
import com.example.routervuebe.exception.MessageError;
import com.example.routervuebe.repository.UserRepository;
import com.example.routervuebe.request.OTPRequest;
import com.example.routervuebe.entity.OTP;
import com.example.routervuebe.repository.OTPRepo;
import com.example.routervuebe.request.UserRequest;
import com.example.routervuebe.response.RegisterResponse;
import com.example.routervuebe.response.UserResponse;
import com.example.routervuebe.service.RegisterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/authentication")
public class AuthController {

@Autowired
private OTPRepo otpRepo;

@Autowired
private RegisterService registerService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Users userRequest) {
        return ResponseEntity.status(HttpStatus.OK).body( registerService.register(userRequest));
    }

    @PostMapping("/verify")
    public ResponseEntity<?> verifyOTP(HttpServletRequest request, @RequestBody OTPRequest otpRequest) {
        String username = (String) request.getAttribute("username");
        if(username == null){
            throw new AuthenticationException(MessageError.USER_NULL,HttpStatus.UNAUTHORIZED);
        }
        OTP otp = otpRepo.findLatestOtpByUsername(username);
        if(otp == null){
            throw new AuthenticationException(MessageError.OTP_NULL,HttpStatus.BAD_REQUEST);
        }
        if (!otp.getOtp().equals(otpRequest.getOtp()) || otp.getExpireAt().isBefore(LocalDateTime.now())) {
            throw new AuthenticationException(MessageError.INVALID_EXPIRED_OTP,HttpStatus.UNAUTHORIZED);
        }
        otpRepo.delete(otp);
        return ResponseEntity.status(HttpStatus.OK).body("Successfully verified");
    }
}
