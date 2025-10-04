package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;

@Service
public class OTPService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Autowired
    private SMSService smsService;
    
    public String generateOTP() {
        return String.format("%06d", new Random().nextInt(999999));
    }
    
    public void sendOTPEmail(User user) {
        String otp = generateOTP();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        
        emailService.sendOTPEmail(user.getEmailId(), otp);
    }
    
    public void sendOTPSMS(User user) {
        String otp = generateOTP();
        user.setOtpCode(otp);
        user.setOtpExpiry(LocalDateTime.now().plusMinutes(10));
        userRepository.save(user);
        
        smsService.sendOTPSMS(user.getPhoneNumber(), otp);
    }
    
    public boolean verifyOTP(String emailId, String otp) {
        Optional<User> userOpt = userRepository.findByEmailId(emailId);
        if (userOpt.isPresent()) {
            User user = userOpt.get();
            if (user.getOtpCode() != null && 
                user.getOtpCode().equals(otp) && 
                user.getOtpExpiry() != null &&
                user.getOtpExpiry().isAfter(LocalDateTime.now())) {
                
                user.setIsVerified(true);
                user.setOtpCode(null);
                user.setOtpExpiry(null);
                userRepository.save(user);
                return true;
            }
        }
        return false;
    }
}