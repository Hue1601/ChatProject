package com.example.routervuebe.security;

import com.example.routervuebe.repo.UserRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtils {
    private UserRepository userRepository;
    public static final String PRIVATE_KEY;

    static {
        try {
            PRIVATE_KEY = Files.readString(Path.of(new ClassPathResource("key/privatekey.pem").getURI()))
                    .replaceAll("-----.*-----", "")
                    .replaceAll("\\s", "");
            System.out.println("privatekey " +PRIVATE_KEY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static final String PUBLIC_KEY;

    static {
        try {
            PUBLIC_KEY = Files.readString(Path.of(new ClassPathResource("key/publickey.pem").getURI()))
                    .replaceAll("-----.*-----", "")
                    .replaceAll("\\s", "");
            System.out.println("PUBLIC_KEY " +PUBLIC_KEY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public PrivateKey getPrivateKey() throws Exception {
        String privateKeyContent = PRIVATE_KEY.replaceAll("\\s+", "");
        byte[] keyBytes = Base64.getDecoder().decode(privateKeyContent);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePrivate(spec);
    }

    public PublicKey getPublicKey() throws Exception {
        String publivKeyContent = PUBLIC_KEY.replaceAll("\\s+","");
        byte[] keyBytes = Base64.getDecoder().decode(publivKeyContent);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return kf.generatePublic(spec);
    }

    public String generateToken(String username)  {
      try {
        PrivateKey privateKey = getPrivateKey();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+120000))
                .signWith(privateKey)
                .compact();
    } catch (Exception e) {
        throw new RuntimeException("Error generating JWT", e);
    }
    }

    public Claims validateToken(String token) throws Exception {
        PublicKey publicKey = getPublicKey();
        return Jwts.parserBuilder()
                .setSigningKey(publicKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
