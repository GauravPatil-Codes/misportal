package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.OTPEntity;
import com.cmsfoundation.misportal.repositories.OTPRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;

@Service
public class OTPService {

    private static final Logger logger = LoggerFactory.getLogger(OTPService.class);

    @Autowired
    private OTPRepository otpRepo;

    @Autowired
    private TwilioSMSService smsService;

    @Autowired
    private RedisTemplate<String, String> redis;

    @Value("${otp.expiry.minutes:5}")
    private int expiryMinutes;

    @Value("${otp.length:6}")
    private int otpLength;

    public boolean sendOTP(String phone) {
        try {
            logger.info("📱 Sending OTP to: {}", phone);
            
            String otp = generateOTP();
            LocalDateTime expires = LocalDateTime.now().plusMinutes(expiryMinutes);

            // Save to database
            otpRepo.save(new OTPEntity(phone, otp, expires));
            
            // Cache in Redis
            redis.opsForValue().set("otp:" + phone, otp, expiryMinutes, TimeUnit.MINUTES);

            // Try Verify API first, then fallback to direct SMS
            if (smsService.sendOTPUsingVerifyAPI(phone)) {
                logger.info("✅ OTP sent via Verify API to: {}", phone);
                return true;
            }

            // Fallback to direct SMS
            boolean sent = smsService.sendCustomOTP(phone, otp);
            if (sent) {
                logger.info("✅ OTP sent via direct SMS to: {}", phone);
            } else {
                logger.error("❌ Failed to send OTP to: {}", phone);
            }
            return sent;
            
        } catch (Exception e) {
            logger.error("💥 Error in sendOTP for {}: {}", phone, e.getMessage());
            return false;
        }
    }

    public boolean verifyOTP(String phone, String code) {
        try {
            logger.info("🔍 Verifying OTP for: {}", phone);
            
            // Check Redis first (fastest)
            String cached = (String) redis.opsForValue().get("otp:" + phone);
            if (cached != null && cached.equals(code)) {
                redis.delete("otp:" + phone);
                logger.info("✅ OTP verified from cache for: {}", phone);
                return true;
            }

            // Check database
            var entOpt = otpRepo.findByPhoneNumberAndOtpCodeAndIsUsedFalse(phone, code);
            if (entOpt.isPresent() && entOpt.get().getExpiresAt().isAfter(LocalDateTime.now())) {
                var ent = entOpt.get();
                ent.setUsed(true);
                otpRepo.save(ent);
                logger.info("✅ OTP verified from database for: {}", phone);
                return true;
            }

            logger.warn("❌ Invalid or expired OTP for: {}", phone);
            return false;
            
        } catch (Exception e) {
            logger.error("💥 Error verifying OTP for {}: {}", phone, e.getMessage());
            return false;
        }
    }

    /** Cleanup expired and used OTPs */
    public void cleanupExpiredOTPs() {
        try {
            logger.info("🧹 Cleaning up expired or used OTPs");
            otpRepo.deleteExpiredAndUsedOTPs(LocalDateTime.now());
        } catch (Exception e) {
            logger.error("💥 Error cleaning up OTPs: {}", e.getMessage());
        }
    }

    private String generateOTP() {
        SecureRandom rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < otpLength; i++) {
            sb.append(rnd.nextInt(10));
        }
        return sb.toString();
    }
}
