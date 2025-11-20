package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.UserLoginSession;
import java.time.LocalDateTime;

public class EmailLoginResponse {
    
    private boolean success;
    private String message;
    private String sessionToken;
    private String jwtToken;
    private LocalDateTime expiresAt;
    private String phoneNumber;
    private String email;
    private String role;
    private UserLoginSession userSession;
    
    // Constructors
    public EmailLoginResponse() {}
    
    public EmailLoginResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getSessionToken() {
        return sessionToken;
    }
    
    public void setSessionToken(String sessionToken) {
        this.sessionToken = sessionToken;
    }
    
    public String getJwtToken() {
        return jwtToken;
    }
    
    public void setJwtToken(String jwtToken) {
        this.jwtToken = jwtToken;
    }
    
    public LocalDateTime getExpiresAt() {
        return expiresAt;
    }
    
    public void setExpiresAt(LocalDateTime expiresAt) {
        this.expiresAt = expiresAt;
    }
    
    public String getPhoneNumber() {
        return phoneNumber;
    }
    
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role = role;
    }
    
    public UserLoginSession getUserSession() {
        return userSession;
    }
    
    public void setUserSession(UserLoginSession userSession) {
        this.userSession = userSession;
    }
}