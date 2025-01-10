package com.example.routervuebe.listener;

import com.example.routervuebe.request.OTPRequest;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Properties;

@Component
public class EmailListener {
    @Async
    @EventListener
    public void handleSendEmail(OTPRequest request){
      try {
          sendEmail(request.getEmail(),request.getOtp());
      }catch (Exception e){
          e.printStackTrace();
      }
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
