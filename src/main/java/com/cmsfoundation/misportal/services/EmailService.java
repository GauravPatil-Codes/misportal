package com.cmsfoundation.misportal.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailService.class);
    
    @Autowired
    private JavaMailSender mailSender;
    
    /**
     * Send OTP to email for authentication
     */
    public void sendOTPEmail(String toEmail, String otp) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Your OTP for CMS Foundation MIS Portal");
            message.setText("Your One-Time Password (OTP) is: " + otp + "\n\nThis OTP will expire in 10 minutes.\n\nIf you didn't request this OTP, please ignore this email.\n\nBest regards,\nCMS Foundation Team");
            
            mailSender.send(message);
            logger.info("OTP email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send OTP email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send OTP email: " + e.getMessage());
        }
    }
    
    /**
     * Send login confirmation email
     */
    public void sendLoginConfirmationEmail(String toEmail, String loginType, LocalDateTime loginTime, String deviceInfo) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Login Confirmation - CMS Foundation MIS Portal");
            
            String emailBody = String.format(
                "Dear User,\n\n" +
                "You have successfully logged into CMS Foundation MIS Portal.\n\n" +
                "Login Details:\n" +
                "- Email: %s\n" +
                "- Login Type: %s\n" +
                "- Date & Time: %s\n" +
                "- Device: %s\n\n" +
                "If this wasn't you, please contact our support team immediately.\n\n" +
                "Best regards,\n" +
                "CMS Foundation Team",
                toEmail, loginType, loginTime, deviceInfo
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Login confirmation email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send login confirmation email to {}: {}", toEmail, e.getMessage());
            // Don't throw exception for non-critical emails
        }
    }
    
    /**
     * Send password reset email
     */
    public void sendPasswordResetEmail(String toEmail, String resetToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Password Reset - CMS Foundation MIS Portal");
            
            String emailBody = String.format(
                "Dear User,\n\n" +
                "You have requested to reset your password for CMS Foundation MIS Portal.\n\n" +
                "Reset Token: %s\n" +
                "This token will expire in 30 minutes.\n\n" +
                "If you didn't request this password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "CMS Foundation Team",
                resetToken
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Password reset email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send password reset email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send password reset email: " + e.getMessage());
        }
    }
    
    /**
     * Send account verification email
     */
    public void sendAccountVerificationEmail(String toEmail, String verificationToken) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Account Verification - CMS Foundation MIS Portal");
            
            String emailBody = String.format(
                "Dear User,\n\n" +
                "Welcome to CMS Foundation MIS Portal!\n\n" +
                "Please verify your email address using the verification token below:\n\n" +
                "Verification Token: %s\n" +
                "This token will expire in 24 hours.\n\n" +
                "If you didn't create an account, please ignore this email.\n\n" +
                "Best regards,\n" +
                "CMS Foundation Team",
                verificationToken
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Account verification email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send account verification email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send account verification email: " + e.getMessage());
        }
    }
    
    /**
     * Send security alert email
     */
    public void sendSecurityAlertEmail(String toEmail, String alertType, String details, LocalDateTime alertTime) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Security Alert - CMS Foundation MIS Portal");
            
            String emailBody = String.format(
                "Dear User,\n\n" +
                "A security activity has been detected on your CMS Foundation MIS Portal account.\n\n" +
                "Alert Details:\n" +
                "- Alert Type: %s\n" +
                "- Details: %s\n" +
                "- Date & Time: %s\n\n" +
                "If this wasn't you, please contact our support team immediately.\n\n" +
                "Best regards,\n" +
                "CMS Foundation Team",
                alertType, details, alertTime
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Security alert email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send security alert email to {}: {}", toEmail, e.getMessage());
            // Don't throw exception for security alerts
        }
    }
    
    /**
     * Send welcome email for new users
     */
    public void sendWelcomeEmail(String toEmail, String userName, String role) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject("Welcome to CMS Foundation MIS Portal");
            
            String emailBody = String.format(
                "Dear %s,\n\n" +
                "Welcome to CMS Foundation MIS Portal!\n\n" +
                "Your account has been successfully created with the following details:\n" +
                "- Email: %s\n" +
                "- Role: %s\n\n" +
                "You can now access the portal using your email credentials.\n\n" +
                "If you have any questions, please contact our support team.\n\n" +
                "Best regards,\n" +
                "CMS Foundation Team",
                userName, toEmail, role
            );
            
            message.setText(emailBody);
            mailSender.send(message);
            logger.info("Welcome email sent to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to {}: {}", toEmail, e.getMessage());
            // Don't throw exception for welcome emails
        }
    }
    
    /**
     * General email sending method
     */
    public void sendEmail(String toEmail, String subject, String text) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@cmsfoundation.org");
            message.setTo(toEmail);
            message.setSubject(subject);
            message.setText(text);
            
            mailSender.send(message);
            logger.info("Email sent successfully to: {}", toEmail);
        } catch (Exception e) {
            logger.error("Failed to send email to {}: {}", toEmail, e.getMessage());
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }
    }
}