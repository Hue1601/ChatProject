package com.example.routervuebe.service;

import com.example.routervuebe.entity.Users;
import com.example.routervuebe.exception.AuthenticationException;
import com.example.routervuebe.exception.MessageError;
import com.example.routervuebe.repository.UserRepository;
import com.example.routervuebe.response.RegisterResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
public class RegisterService {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public Object register(Users userRequest) {
        // Check if the username already exists
        if (userRepo.existsByUsername(userRequest.getUsername())) {

            throw  new AuthenticationException(MessageError.USERNAME_VALIDATE,HttpStatus.BAD_REQUEST);
        }

        // Check if the email already exists
        if (userRepo.existsByEmail(userRequest.getEmail())) {
            throw new AuthenticationException(MessageError.EMAIL_VALIDATE,HttpStatus.BAD_REQUEST);
        }


        String password = userRequest.getPass();
        String passwordRegex = "^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).+$";

        if (!password.matches(passwordRegex)) {
            throw new AuthenticationException(MessageError.VALIDATE_PASSWORD,HttpStatus.BAD_REQUEST);
        }

        userRequest.setPass(passwordEncoder.encode(password));

        userRepo.save(userRequest);

        RegisterResponse response = new RegisterResponse();
        response.setUsername(userRequest.getUsername());
        response.setEmail(userRequest.getEmail());
        response.setGioitinh(userRequest.getGioitinh());
        response.setSdt(userRequest.getSdt());

        return response;
    }
}
