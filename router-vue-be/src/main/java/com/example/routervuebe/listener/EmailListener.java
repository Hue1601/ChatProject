package com.example.routervuebe.listener;

import com.example.routervuebe.request.OTPRequest;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.Properties;

@ControllerAdvice
public class EmailListener {
    @Async
    @EventListener
    public void handleSendEmail(OTPRequest request){
      try {
          sendOtpToEmail(request.getEmail(),request.getOtp());
      }catch (Exception e){
          e.printStackTrace();
      }
    }
    private void sendOtpToEmail(String email, String otp) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
               return new PasswordAuthentication("huethipham1601@gmail.com", "ygnx nzdh kuzo xmpw");

            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("huethipham1601@gmail.com"));

            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
            message.setSubject("Your OTP Code");
            message.setText("Your OTP code is: " + otp + "\nThis code is valid for 5 minutes.");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
