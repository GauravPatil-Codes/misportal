package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_login_sessions")
public class UserLoginSession {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false, unique = true)
    private String sessionToken;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private boolean isActive = true;
    
    private String deviceInfo;
    private String ipAddress;
    
    // Constructors, getters, setters...
    public UserLoginSession() {}
    
    public UserLoginSession(String phoneNumber, String sessionToken, LocalDateTime expiresAt) {
        this.phoneNumber = phoneNumber;
        this.sessionToken = sessionToken;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }
    
    // Getters and setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getSessionToken() { return sessionToken; }
    public void setSessionToken(String sessionToken) { this.sessionToken = sessionToken; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
    
    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}