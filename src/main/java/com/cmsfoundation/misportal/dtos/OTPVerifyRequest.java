package com.cmsfoundation.misportal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class OTPVerifyRequest {
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    @NotBlank(message = "OTP code is required")
    @Size(min = 4, max = 8, message = "OTP must be between 4-8 digits")
    private String otpCode;
    
    private String deviceInfo;
    private String ipAddress;
    
    public OTPVerifyRequest() {}
    
    public OTPVerifyRequest(String phoneNumber, String otpCode) {
        this.phoneNumber = phoneNumber;
        this.otpCode = otpCode;
    }
    
    // Getters and setters
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public String getDeviceInfo() { return deviceInfo; }
    public void setDeviceInfo(String deviceInfo) { this.deviceInfo = deviceInfo; }
    
    public String getIpAddress() { return ipAddress; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}