package com.cmsfoundation.misportal.services;

import com.twilio.Twilio;
import com.twilio.exception.TwilioException;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.rest.verify.v2.service.Verification;
import com.twilio.rest.verify.v2.service.VerificationCheck;
import com.twilio.type.PhoneNumber;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class TwilioSMSService {

    private static final Logger logger = LoggerFactory.getLogger(TwilioSMSService.class);

    @Value("${twilio.account.sid}")
    private String accountSid;

    @Value("${twilio.auth.token}")
    private String authToken;

    /** Verify Service SID (starts with VA…) - if you have one */
    @Value("${twilio.service.sid:}")
    private String verifyServiceSid;

    /** Twilio Phone Number for direct SMS */
    @Value("${twilio.phone.number}")
    private String fromPhoneNumber;

    @PostConstruct
    public void init() {
        Twilio.init(accountSid, authToken);
        logger.info("✅ Twilio initialized with Account SID: {}", accountSid);
    }

    /**
     * Send OTP via Twilio Verify API (if available)
     */
    public boolean sendOTPUsingVerifyAPI(String phoneNumber) {
        if (verifyServiceSid == null || verifyServiceSid.isEmpty()) {
            logger.warn("⚠️ No Verify Service SID configured, skipping Verify API");
            return false;
        }
        
        try {
            Verification verification = Verification.creator(
                    verifyServiceSid,
                    phoneNumber,
                    "sms"
            ).create();

            logger.info("✅ Verify API OTP sent to {} → Status: {}", phoneNumber, verification.getStatus());
            return "pending".equals(verification.getStatus());
        } catch (TwilioException e) {
            logger.error("❌ Verify API blocked for {}: Code={}, Msg={}", phoneNumber, e.getClass(), e.getMessage());
            return false;
        }
    }

    /**
     * Verify OTP via Twilio Verify API
     */
    public boolean verifyOTPUsingVerifyAPI(String phoneNumber, String otpCode) {
        if (verifyServiceSid == null || verifyServiceSid.isEmpty()) {
            return false;
        }
        
        try {
            VerificationCheck check = VerificationCheck.creator(verifyServiceSid)
                    .setTo(phoneNumber)
                    .setCode(otpCode)
                    .create();

            logger.info("✅ Verify API OTP verification for {} → Status: {}", phoneNumber, check.getStatus());
            return "approved".equals(check.getStatus());
        } catch (TwilioException e) {
            logger.error("❌ Verify API verify blocked for {}: Code={}, Msg={}", phoneNumber, e.getClass(), e.getMessage());
            return false;
        }
    }

    /**
     * Send OTP via direct SMS using phone number
     */
    public boolean sendCustomOTP(String phoneNumber, String otpCode) {
        try {
            String body = String.format(
                "🔐 Your CMS Foundation verification code: %s\n\n" +
                "⏰ Valid for 5 minutes\n" +
                "🚫 Do not share this code\n\n" +
                "- Team CMS Foundation", 
                otpCode
            );
            
            Message msg = Message.creator(
                    new PhoneNumber(phoneNumber),           // TO
                    new PhoneNumber(fromPhoneNumber),       // FROM (your Twilio number)
                    body                                    // MESSAGE
            ).create();

            logger.info("✅ Custom SMS to {} → SID: {}, Status: {}", phoneNumber, msg.getSid(), msg.getStatus());
            
            return msg.getStatus() == Message.Status.QUEUED
                    || msg.getStatus() == Message.Status.SENT
                    || msg.getStatus() == Message.Status.DELIVERED;
                    
        } catch (TwilioException e) {
            logger.error("❌ Custom SMS error for {}: Code={}, Msg={}", phoneNumber, e.getClass(), e.getMessage());
            return false;
        }
    }
}
