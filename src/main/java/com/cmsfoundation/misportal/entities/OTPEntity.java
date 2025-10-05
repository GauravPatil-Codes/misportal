package com.cmsfoundation.misportal.entities;


import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "otp_verifications")
public class OTPEntity {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private String otpCode;
    
    @Column(nullable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime expiresAt;
    
    @Column(nullable = false)
    private boolean isUsed = false;
    
    @Column(nullable = false)
    private int attempts = 0;
    
    @Column(nullable = false)
    private boolean isBlocked = false;
    
    private LocalDateTime blockedUntil;
    
    // Constructors, getters, setters
    public OTPEntity() {}
    
    public OTPEntity(String phoneNumber, String otpCode, LocalDateTime expiresAt) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
        this.createdAt = LocalDateTime.now();
        this.expiresAt = expiresAt;
    }
    
    // Getters and setters...
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }
    
    public boolean isUsed() { return isUsed; }
    public void setUsed(boolean used) { isUsed = used; }
    
    public int getAttempts() { return attempts; }
    public void setAttempts(int attempts) { this.attempts = attempts; }
    
    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
    
    public LocalDateTime getBlockedUntil() { return blockedUntil; }
    public void setBlockedUntil(LocalDateTime blockedUntil) { this.blockedUntil = blockedUntil; }
}
