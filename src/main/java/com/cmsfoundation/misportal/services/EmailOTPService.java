package com.cmsfoundation.misportal.services;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class EmailOTPService {
    
    private static final Logger logger = LoggerFactory.getLogger(EmailOTPService.class);
    
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    
    @Autowired
    private EmailService emailService;
    
    @Value("${otp.expiry.minutes:10}")
    private int otpExpiryMinutes;
    
    @Value("${otp.max.attempts:3}")
    private int maxAttempts;
    
    @Value("${otp.length:6}")
    private int otpLength;
    
    private static final String EMAIL_OTP_PREFIX = "email_otp:";
    private static final String EMAIL_OTP_ATTEMPTS_PREFIX = "email_otp_attempts:";
    
    private final SecureRandom secureRandom = new SecureRandom();
    
    /**
     * Generate and send OTP to email
     */
    public boolean sendEmailOTP(String email) {
        try {
            // Check if user has exceeded max attempts
            String attemptsKey = EMAIL_OTP_ATTEMPTS_PREFIX + email;
            String attemptsStr = redisTemplate.opsForValue().get(attemptsKey);
            
            if (attemptsStr != null) {
                int attempts = Integer.parseInt(attemptsStr);
                if (attempts >= maxAttempts) {
                    logger.warn("Maximum OTP attempts exceeded for email: {}", email);
                    return false;
                }
            }
            
            // Generate 6-digit OTP
            String otp = generateOTP();
            
            // Store OTP in Redis with expiry
            String otpKey = EMAIL_OTP_PREFIX + email;
            redisTemplate.opsForValue().set(otpKey, otp, Duration.ofMinutes(otpExpiryMinutes));
            
            // Increment attempts counter
            redisTemplate.opsForValue().increment(attemptsKey);
            redisTemplate.expire(attemptsKey, Duration.ofHours(1)); // Reset attempts after 1 hour
            
            // Send OTP via email
            emailService.sendOTPEmail(email, otp);
            
            logger.info("OTP sent successfully to email: {}", email);
            return true;
            
        } catch (Exception e) {
            logger.error("Error sending OTP to email {}: {}", email, e.getMessage());
            return false;
        }
    }
    
    /**
     * Verify OTP for email
     */
    public boolean verifyEmailOTP(String email, String otpCode) {
        try {
            String otpKey = EMAIL_OTP_PREFIX + email;
            String storedOTP = redisTemplate.opsForValue().get(otpKey);
            
            if (storedOTP == null) {
                logger.warn("OTP not found or expired for email: {}", email);
                return false;
            }
            
            boolean isValid = storedOTP.equals(otpCode);
            
            if (isValid) {
                // Clear OTP and attempts after successful verification
                redisTemplate.delete(otpKey);
                redisTemplate.delete(EMAIL_OTP_ATTEMPTS_PREFIX + email);
                logger.info("OTP verified successfully for email: {}", email);
            } else {
                logger.warn("Invalid OTP provided for email: {}", email);
            }
            
            return isValid;
            
        } catch (Exception e) {
            logger.error("Error verifying OTP for email {}: {}", email, e.getMessage());
            return false;
        }
    }
    
    /**
     * Generate random OTP
     */
    private String generateOTP() {
        StringBuilder otp = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            otp.append(secureRandom.nextInt(10));
        }
        return otp.toString();
    }
    
    /**
     * Check if email is eligible for OTP (not exceeded attempts)
     */
    public boolean isEmailEligibleForOTP(String email) {
        try {
            String attemptsKey = EMAIL_OTP_ATTEMPTS_PREFIX + email;
            String attemptsStr = redisTemplate.opsForValue().get(attemptsKey);
            
            if (attemptsStr != null) {
                int attempts = Integer.parseInt(attemptsStr);
                return attempts < maxAttempts;
            }
            
            return true; // No attempts made yet
            
        } catch (Exception e) {
            logger.error("Error checking OTP eligibility for email {}: {}", email, e.getMessage());
            return true; // Allow on error
        }
    }
    
    /**
     * Reset OTP attempts for email
     */
    public void resetOTPAttempts(String email) {
        try {
            redisTemplate.delete(EMAIL_OTP_ATTEMPTS_PREFIX + email);
            logger.info("OTP attempts reset for email: {}", email);
        } catch (Exception e) {
            logger.error("Error resetting OTP attempts for email {}: {}", email, e.getMessage());
        }
    }
    
    /**
     * Get remaining attempts for email
     */
    public int getRemainingAttempts(String email) {
        try {
            String attemptsKey = EMAIL_OTP_ATTEMPTS_PREFIX + email;
            String attemptsStr = redisTemplate.opsForValue().get(attemptsKey);
            
            if (attemptsStr != null) {
                int attempts = Integer.parseInt(attemptsStr);
                return Math.max(0, maxAttempts - attempts);
            }
            
            return maxAttempts; // No attempts made yet
            
        } catch (Exception e) {
            logger.error("Error getting remaining attempts for email {}: {}", email, e.getMessage());
            return maxAttempts;
        }
    }
}