package com.example.routervuebe.websocket;

import com.example.routervuebe.security.JwtUtils;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeFailureException;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Component
public class CustomHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtils jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) throws HandshakeFailureException {
        System.out.println("Before handshake is called.");
        if (request instanceof ServletServerHttpRequest) {
            HttpServletRequest httpRequest = ((ServletServerHttpRequest) request).getServletRequest();
            String token = httpRequest.getHeader("Authorization");

            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7); // Loại bỏ "Bearer "
                if (validateToken(token)) {
                    return true; // Token hợp lệ
                }
            }
        }
        throw new HandshakeFailureException("Invalid token");
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        // No additional processing needed after handshake
    }

    private boolean validateToken(String token) {
        if (token == null || token.isEmpty()) {
            System.out.println("Token is null or empty.");
            return false;
        }

        try {
            Claims claims = jwtUtil.validateToken(token);
            System.out.println("Token claims: " + claims);
            return claims != null && claims.getSubject() != null;
        } catch (Exception e) {
            System.out.println("Exception during token validation: " + e.getMessage());
            return false;
        }
    }

}

