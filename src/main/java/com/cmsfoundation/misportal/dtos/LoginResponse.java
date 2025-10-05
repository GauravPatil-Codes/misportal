package com.cmsfoundation.misportal.dtos;

import java.time.LocalDateTime;

public class LoginResponse {
    
    private boolean success;
    private String message;
    private String sessionToken;
    private String jwtToken;
    private LocalDateTime expiresAt;
    private String phoneNumber;
    
    public LoginResponse() {}
    
    public LoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Getters and setters
    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    
    public String getJwtToken() { return jwtToken; }
    public void setJwtToken(String jwtToken) { this.jwtToken = jwtToken; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}