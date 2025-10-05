package com.cmsfoundation.misportal.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class OTPRequest {
    
    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^\\+[1-9]\\d{1,14}$", message = "Invalid phone number format")
    private String phoneNumber;
    
    public OTPRequest() {}
    
    public OTPRequest(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
}