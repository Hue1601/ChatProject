package com.example.routervuebe.controller;

import com.example.routervuebe.exception.AuthenticationException;
import com.example.routervuebe.exception.MessageError;
import com.example.routervuebe.request.OTPRequest;
import com.example.routervuebe.entity.OTP;
import com.example.routervuebe.repository.OTPRepo;
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
@RequestMapping("/api/authentication")
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
