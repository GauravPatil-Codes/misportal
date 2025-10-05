package com.cmsfoundation.misportal.schedulers;

import com.cmsfoundation.misportal.services.AuthService;
import com.cmsfoundation.misportal.services.OTPService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CleanupScheduler {
    
    private static final Logger logger = LoggerFactory.getLogger(CleanupScheduler.class);
    
    @Autowired
    private OTPService otpService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * Cleanup expired OTPs every hour
     */
    @Scheduled(fixedRate = 3600000) // 1 hour
    public void cleanupExpiredOTPs() {
        logger.info("Starting cleanup of expired OTPs");
        otpService.cleanupExpiredOTPs();
    }
    
    /**
     * Cleanup expired sessions every 6 hours
     */
    @Scheduled(fixedRate = 21600000) // 6 hours
    public void cleanupExpiredSessions() {
        logger.info("Starting cleanup of expired sessions");
        authService.cleanupExpiredSessions();
    }
}