package com.cmsfoundation.misportal.config;

import com.twilio.Twilio;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TwilioConfiguration {
    
    @Value("${twilio.account.sid}")
    private String accountSid;
    
    @Value("${twilio.auth.token}")
    private String authToken;
    
    @jakarta.annotation.PostConstruct
    public void initTwilio() {
        Twilio.init(accountSid, authToken);
    }
}