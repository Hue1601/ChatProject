package com.example.routervuebe.security;

import com.example.routervuebe.exception.AuthenticationException;
import com.example.routervuebe.exception.MessageError;
import com.example.routervuebe.request.LoginRequest;
import com.example.routervuebe.request.OTPRequest;
import com.example.routervuebe.response.LoginResponse;
import com.example.routervuebe.entity.OTP;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repository.OTPRepo;
import com.example.routervuebe.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.Properties;
import java.util.Random;
import java.util.stream.Collectors;

@Component
public class CustomAuthenticationFilter extends OncePerRequestFilter {
    @Autowired
    private OTPRepo otpRepo;
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private JwtUtils jwtUtil;
    @Autowired
    private ObjectMapper objectMapper;
    @Lazy
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ApplicationEventPublisher eventPublisher;
    @Autowired
    public CustomAuthenticationFilter(OTPRepo otpRepo) {
        this.otpRepo = otpRepo;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        // Nếu là endpoint /register hoặc /login thì bỏ qua kiểm tra token
        if (isPublicEndpoint(request)) {
            if (request.getServletPath().equalsIgnoreCase("/api/login")
                    && request.getMethod().equalsIgnoreCase("POST")) {
                handleLogin(request, response); // Gọi logic xử lý login
                return;
            }
            filterChain.doFilter(request, response);
            return;
        }

        // Logic xử lý token
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            try {
                Claims claims = jwtUtil.validateToken(token);
                String username = claims.getSubject();
                request.setAttribute("username", username);
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                response.getWriter().write(objectMapper.writeValueAsString(MessageError.INVALID_EXPIRED_TOKEN));
                return;
            }
        } else {
            // Nếu không có Authorization header, trả về lỗi
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write(objectMapper.writeValueAsString(MessageError.MISSING_TOKEN));
            return;
        }

        filterChain.doFilter(request, response);
    }


    public void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));

            LoginRequest loginRequest = new ObjectMapper().readValue(body, LoginRequest.class);

            processLogin(loginRequest, response);
        }  catch (AuthenticationException ex) {
            // Xử lý lỗi xác thực
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            response.setContentType("application/json");
            response.getWriter().write(objectMapper.writeValueAsString(ex.getMessageError().getMessage()));

        }

    }

    public void processLogin(LoginRequest loginRequest, HttpServletResponse response) throws IOException {
        Users users = userRepo.findByUsername(loginRequest.getUsername());

       if (users == null || !loginRequest.getUsername().equals(users.getUsername()) ||
           !passwordEncoder.matches(loginRequest.getPassword(),users.getPass())) {
            throw new AuthenticationException(MessageError.INVALID_USERNAME_PASSWORD, HttpStatus.FORBIDDEN);
        }

        String otp = createOTP();
       System.out.println("1111"+otp);
        saveOTP(users.getUsername(), otp, users.getEmail());
//      sendEmail(users.getEmail(), otp);

        this.eventPublisher.publishEvent(new OTPRequest( users.getEmail(),users.getUsername(), otp));

        String token = jwtUtil.generateToken(users.getUsername());

        LoginResponse loginResponse = new LoginResponse();
        loginResponse.setUsername(users.getUsername());
        loginResponse.setToken(token);
        loginResponse.setId(users.getId());
        response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
    }

    private boolean isLogin(HttpServletRequest request) {
        return request.getServletPath().equalsIgnoreCase("/api/login")
                && request.getMethod().equalsIgnoreCase("POST");
    }
    private boolean isPublicEndpoint(HttpServletRequest request) {
        String path = request.getServletPath();
        return (path.equalsIgnoreCase("/api/login") && request.getMethod().equalsIgnoreCase("POST"))
                || (path.equalsIgnoreCase("/api/authentication/register") && request.getMethod().equalsIgnoreCase("POST"));
    }
    private String createOTP() {
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    private void saveOTP(String username, String otp, String email) {
        OTP otpEnty = new OTP(email, otp, username, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otpEnty);
    }



}
