package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.dtos.UserRegistrationRequest;
import com.cmsfoundation.misportal.services.UserService;
import com.cmsfoundation.misportal.services.OTPService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private OTPService otpService;
    
    @PostMapping("/request-otp")
    public ResponseEntity<Map<String, Object>> requestOTP(@RequestBody Map<String, String> request) {
        String emailId = request.get("emailId");
        String method = request.get("method");
        
        try {
            Optional<User> userOpt = userService.getUserByEmailId(emailId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                
                if ("sms".equals(method)) {
                    otpService.sendOTPSMS(user);
                } else {
                    otpService.sendOTPEmail(user);
                }
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "OTP sent successfully");
                response.put("method", method);
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "User not found");
                return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to send OTP");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/verify-otp")
    public ResponseEntity<Map<String, Object>> verifyOTPAndLogin(@RequestBody Map<String, String> request) {
        String emailId = request.get("emailId");
        String otp = request.get("otp");
        
        try {
            boolean isValid = otpService.verifyOTP(emailId, otp);
            
            if (isValid) {
                Optional<User> userOpt = userService.getUserByEmailId(emailId);
                User user = userOpt.get();
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", true);
                response.put("message", "Login successful");
                response.put("user", user);
                response.put("userRole", user.getUserRole().getDisplayName());
                response.put("ngoId", user.getNgo() != null ? user.getNgo().getId() : null);
                response.put("ngoName", user.getNgo() != null ? user.getNgo().getNgoName() : null);
                
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid or expired OTP");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "OTP verification failed");
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<Map<String, Object>> registerUser(@RequestBody UserRegistrationRequest request) {
        try {
            User newUser = userService.registerUser(request);
            
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("userId", newUser.getId());
            
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping("/permissions/{userId}")
    public ResponseEntity<Map<String, Object>> getUserPermissions(@PathVariable Long userId) {
        try {
            Optional<User> userOpt = userService.getUserById(userId);
            if (userOpt.isPresent()) {
                User user = userOpt.get();
                Map<String, Object> permissions = userService.getUserPermissions(user);
                return new ResponseEntity<>(permissions, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}