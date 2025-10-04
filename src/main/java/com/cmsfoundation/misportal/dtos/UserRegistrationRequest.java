package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.UserRole;

public class UserRegistrationRequest {
    private String firstName;
    private String lastName;
    private String emailId;
    private String phoneNumber;
    private String password;
    private UserRole userRole;
    private Long ngoId; // Optional, for NGO-related roles
    
    // Constructors, getters, and setters
    public UserRegistrationRequest() {}
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
    
    public Long getNgoId() { return ngoId; }
    public void setNgoId(Long ngoId) { this.ngoId = ngoId; }
}