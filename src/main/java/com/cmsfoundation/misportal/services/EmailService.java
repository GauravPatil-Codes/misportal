package com.cmsfoundation.misportal.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    public void sendOTPEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Your OTP for CMS Foundation MIS Portal");
            message.setText("Your One-Time Password (OTP) is: " + otp + "\n\nThis OTP will expire in 10 minutes.\n\nIf you didn't request this OTP, please ignore this email.\n\nBest regards,\nCMS Foundation Team");
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
    
    public void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
        } catch (Exception e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}