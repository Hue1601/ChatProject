package com.example.routervuebe.security;

import com.example.routervuebe.Request.LoginRequest;
import com.example.routervuebe.Response.LoginResponse;
import com.example.routervuebe.entity.OTP;
import com.example.routervuebe.entity.Users;
import com.example.routervuebe.repo.OTPRepo;
import com.example.routervuebe.repo.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
    private JwtUtil jwtUtil;

    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        try {
//            String authorization = request.getHeader("Authorization");

            if (isLogin(request)) {
                handleLogin(request, response);
                return;
            }
//            if (authorization != null && authorization.startsWith("Bearer ")) {
//
//            }
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        filterChain.doFilter(request, response);
    }

    public void handleLogin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String body = new BufferedReader(new InputStreamReader(request.getInputStream()))
                    .lines().collect(Collectors.joining(System.lineSeparator()));
            LoginRequest loginRequest = new ObjectMapper().readValue(body, LoginRequest.class);

            processLogin(loginRequest, response);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void processLogin(LoginRequest loginRequest, HttpServletResponse response)
            throws IOException {
        try {
            Users users = userRepo.findByUsername(loginRequest.getUsername());

            if (users == null || !loginRequest.getUsername().equals(users.getUsername()) || !loginRequest.getPassword().equals(users.getPass())) {
                System.out.println("loi");
            }
            String otp = createOTP();
            saveOTP(users.getUsername(), otp, users.getEmail());
            sendEmail(users.getEmail(), otp);
            String token = jwtUtil.generateToken(users.getUsername());

            LoginResponse loginResponse = new LoginResponse(token);
            response.getWriter().write(new ObjectMapper().writeValueAsString(loginResponse));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    private boolean isLogin(HttpServletRequest request) {
        return request.getServletPath().equals("/api/login")
                && request.getMethod().equals("POST");
    }

    private String createOTP() {
        return String.valueOf(new Random().nextInt(90000) + 10000);
    }

    private void saveOTP(String username, String otp, String email) {
        OTP otpEnty = new OTP(email, otp, username, LocalDateTime.now(), LocalDateTime.now().plusMinutes(5));
        otpRepo.save(otpEnty);
    }

    private void sendEmail(String email,String otp) {
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
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(email));
            message.setSubject("YOUR OTP");
            message.setText("Your otp is " + otp);

            Transport.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
