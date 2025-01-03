package com.example.routervuebe.controller;

import com.example.routervuebe.Exception.AuthenticationException;
import com.example.routervuebe.Exception.MessageError;
import com.example.routervuebe.Request.OTPRequest;
import com.example.routervuebe.Entity.OTP;
import com.example.routervuebe.Repository.OTPRepo;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class AuthController {
@Autowired
private OTPRepo otpRepo;

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
