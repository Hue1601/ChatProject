package com.example.routervuebe.security;

import com.example.routervuebe.Exception.AuthenticationException;
import com.example.routervuebe.Exception.MessageError;
import com.example.routervuebe.Request.LoginRequest;
import com.example.routervuebe.Response.LoginResponse;
import com.example.routervuebe.Entity.OTP;
import com.example.routervuebe.Entity.Users;
import com.example.routervuebe.Repository.OTPRepo;
import com.example.routervuebe.Repository.UserRepository;
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
import org.springframework.http.HttpStatus;
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
    @Autowired
    public CustomAuthenticationFilter(OTPRepo otpRepo) {
        this.otpRepo = otpRepo;
    }
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException,IOException  {
        String authHeader = request.getHeader("Authorization");

            if (isLogin(request)) {
                handleLogin(request, response);
                return;
            }
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

       if (users == null || !loginRequest.getUsername().equals(users.getUsername()) || !loginRequest.getPassword().equals(users.getPass())) {
            throw new AuthenticationException(MessageError.INVALID_USERNAME_PASSWORD, HttpStatus.FORBIDDEN);
        }

        String otp = createOTP();
        saveOTP(users.getUsername(), otp, users.getEmail());
        sendEmail(users.getEmail(), otp);
        String token = jwtUtil.generateToken(users.getUsername());
//        LoginResponse loginResponse = new LoginResponse(token);
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

    private String createOTP() {
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    private void saveOTP(String username, String otp, String email) {
        OTP otpEnty = new OTP(email, otp, username, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otpEnty);
    }

    private void sendEmail(String email, String otp) {
        Properties properties = new Properties();
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");

        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("huethipham1601@gmail.com", "ygnx nzdh kuzo xmpw");

            }
        });
        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("huethipham1601@gmail.com"));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("YOUR OTP");
            message.setText("Your otp is " + otp);

            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
