package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.dtos.LoginResponse;
import com.cmsfoundation.misportal.dtos.OTPRequest;
import com.cmsfoundation.misportal.dtos.OTPVerifyRequest;
import com.cmsfoundation.misportal.services.AuthService;
import com.cmsfoundation.misportal.services.OTPService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")

public class AuthController {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);
    
    @Autowired
    private OTPService otpService;
    
    @Autowired
    private AuthService authService;
    
    /**
     * Send OTP to phone number
     */
    @PostMapping("/send-otp")
    public ResponseEntity<Map<String, Object>> sendOTP(@Valid @RequestBody OTPRequest request) {
        try {
            logger.info("OTP request received for: {}", request.getPhoneNumber());
            
            boolean sent = otpService.sendOTP(request.getPhoneNumber());
            
            Map<String, Object> response = new HashMap<>();
            if (sent) {
                response.put("success", true);
                response.put("message", "OTP sent successfully");
                response.put("phoneNumber", request.getPhoneNumber());
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to send OTP. Please try again.");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error in sendOTP: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Verify OTP and login
     */
    @PostMapping("/verify-otp")
    public ResponseEntity<LoginResponse> verifyOTP(@Valid @RequestBody OTPVerifyRequest request, 
                                                  HttpServletRequest httpRequest) {
        try {
            logger.info("OTP verification request received for: {}", request.getPhoneNumber());
            
            boolean verified = otpService.verifyOTP(request.getPhoneNumber(), request.getOtpCode());
            
            if (verified) {
                // Get device info and IP
                String deviceInfo = request.getDeviceInfo() != null ? 
                    request.getDeviceInfo() : httpRequest.getHeader("User-Agent");
                String ipAddress = getClientIP(httpRequest);
                
                // Create login session
                Map<String, Object> sessionResult = authService.createLoginSession(
                    request.getPhoneNumber(), deviceInfo, ipAddress);
                
                if ((Boolean) sessionResult.get("success")) {
                    LoginResponse response = new LoginResponse(true, "Login successful");
                    response.setSessionToken((String) sessionResult.get("sessionToken"));
                    response.setJwtToken((String) sessionResult.get("jwtToken"));
                    response.setExpiresAt((java.time.LocalDateTime) sessionResult.get("expiresAt"));
                    response.setPhoneNumber(request.getPhoneNumber());
                    
                    return ResponseEntity.ok(response);
                } else {
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                            .body(new LoginResponse(false, "Failed to create session"));
                }
                
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body(new LoginResponse(false, "Invalid OTP code"));
            }
            
        } catch (Exception e) {
            logger.error("Error in verifyOTP: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new LoginResponse(false, "Internal server error"));
        }
    }
    
    /**
     * Validate token
     */
    @PostMapping("/validate-token")
    public ResponseEntity<Map<String, Object>> validateToken(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            boolean valid = authService.validateToken(token);
            
            Map<String, Object> response = new HashMap<>();
            response.put("valid", valid);
            
            if (valid) {
                String phoneNumber = authService.getPhoneNumberFromToken(token);
                response.put("phoneNumber", phoneNumber);
                response.put("message", "Token is valid");
            } else {
                response.put("message", "Token is invalid or expired");
            }
            
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            logger.error("Error in validateToken: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("valid", false);
            errorResponse.put("message", "Token validation failed");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
        }
    }
    
    /**
     * Logout user
     */
    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(@RequestHeader("Authorization") String authHeader) {
        try {
            String token = authHeader.replace("Bearer ", "");
            String phoneNumber = authService.getPhoneNumberFromToken(token);
            
            if (phoneNumber != null) {
                // Extract session token and logout
                // This is a simplified version - you might want to extract session token from JWT
                boolean loggedOut = authService.logout(token);
                
                Map<String, Object> response = new HashMap<>();
                response.put("success", loggedOut);
                response.put("message", loggedOut ? "Logged out successfully" : "Logout failed");
                
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Invalid token");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error in logout: {}", e.getMessage());
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("success", false);
            errorResponse.put("message", "Logout failed");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Get client IP address
     */
    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}