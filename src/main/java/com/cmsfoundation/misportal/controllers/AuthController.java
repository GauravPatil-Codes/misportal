package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.dtos.EmailLoginRequest;
import com.cmsfoundation.misportal.dtos.EmailLoginResponse;
import com.cmsfoundation.misportal.dtos.EmailOTPRequest;
import com.cmsfoundation.misportal.dtos.EmailOTPVerifyRequest;
import com.cmsfoundation.misportal.dtos.LoginResponse;
import com.cmsfoundation.misportal.dtos.OTPRequest;
import com.cmsfoundation.misportal.dtos.OTPVerifyRequest;
import com.cmsfoundation.misportal.services.AuthService;
import com.cmsfoundation.misportal.services.EmailOTPService;
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
    private EmailOTPService emailOTPService;
    
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
     * Send OTP to email
     */
    @PostMapping("/send-email-otp")
    public ResponseEntity<Map<String, Object>> sendEmailOTP(@Valid @RequestBody EmailOTPRequest request) {
        try {
            logger.info("Email OTP request received for: {}", request.getEmail());
            
            // Check if email is eligible for OTP
            if (!emailOTPService.isEmailEligibleForOTP(request.getEmail())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Maximum OTP attempts exceeded. Please try again later.");
                response.put("remainingAttempts", 0);
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(response);
            }
            
            boolean sent = emailOTPService.sendEmailOTP(request.getEmail());
            
            Map<String, Object> response = new HashMap<>();
            if (sent) {
                response.put("success", true);
                response.put("message", "OTP sent successfully to your email");
                response.put("email", request.getEmail());
                response.put("remainingAttempts", emailOTPService.getRemainingAttempts(request.getEmail()));
                return ResponseEntity.ok(response);
            } else {
                response.put("success", false);
                response.put("message", "Failed to send OTP. Please try again.");
                response.put("remainingAttempts", emailOTPService.getRemainingAttempts(request.getEmail()));
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
            }
            
        } catch (Exception e) {
            logger.error("Error in sendEmailOTP: {}", e.getMessage());
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
     * Verify Email OTP and login
     */
    @PostMapping("/verify-email-otp")
    public ResponseEntity<EmailLoginResponse> verifyEmailOTP(@Valid @RequestBody EmailOTPVerifyRequest request,
                                                           HttpServletRequest httpRequest) {
        try {
            logger.info("Email OTP verification request received for: {}", request.getEmail());
            
            boolean verified = emailOTPService.verifyEmailOTP(request.getEmail(), request.getOtpCode());
            
            if (verified) {
                // Get device info and IP
                String deviceInfo = request.getDeviceInfo() != null ? 
                    request.getDeviceInfo() : httpRequest.getHeader("User-Agent");
                String ipAddress = getClientIP(httpRequest);
                
                // Create login session
                Map<String, Object> sessionResult = authService.createEmailLoginSession(
                    request.getEmail(), deviceInfo, ipAddress);
                
                if ((Boolean) sessionResult.get("success")) {
                    EmailLoginResponse response = new EmailLoginResponse(true, "Login successful");
                    response.setSessionToken((String) sessionResult.get("sessionToken"));
                    response.setJwtToken((String) sessionResult.get("jwtToken"));
                    response.setExpiresAt((java.time.LocalDateTime) sessionResult.get("expiresAt"));
                    response.setEmail(request.getEmail());
                    response.setRole((String) sessionResult.get("role"));
                    
                    return ResponseEntity.ok(response);
                } else {
                    EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Failed to create session");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
                
            } else {
                EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Invalid OTP code");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error in verifyEmailOTP: {}", e.getMessage());
            EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
    
    /**
     * Email and password login
     */
    @PostMapping("/login")
    public ResponseEntity<EmailLoginResponse> loginEmail(@Valid @RequestBody EmailLoginRequest request,
                                                        HttpServletRequest httpRequest) {
        try {
            logger.info("Email login request received for: {}", request.getEmail());
            
            // Validate credentials
            boolean validCredentials = authService.validateEmailCredentials(
                request.getEmail(), request.getPassword());
            
            if (validCredentials) {
                // Get device info and IP
                String deviceInfo = request.getDeviceInfo() != null ? 
                    request.getDeviceInfo() : httpRequest.getHeader("User-Agent");
                String ipAddress = getClientIP(httpRequest);
                
                // Create login session
                Map<String, Object> sessionResult = authService.createEmailLoginSession(
                    request.getEmail(), deviceInfo, ipAddress);
                
                if ((Boolean) sessionResult.get("success")) {
                    EmailLoginResponse response = new EmailLoginResponse(true, "Login successful");
                    response.setSessionToken((String) sessionResult.get("sessionToken"));
                    response.setJwtToken((String) sessionResult.get("jwtToken"));
                    response.setExpiresAt((java.time.LocalDateTime) sessionResult.get("expiresAt"));
                    response.setEmail(request.getEmail());
                    response.setRole((String) sessionResult.get("role"));
                    
                    return ResponseEntity.ok(response);
                } else {
                    EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Failed to create session");
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
                }
                
            } else {
                EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Invalid email or password");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            }
            
        } catch (Exception e) {
            logger.error("Error in email login: {}", e.getMessage());
            EmailLoginResponse errorResponse = new EmailLoginResponse(false, "Internal server error");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
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
                // Try to get email first, then phone number
                String email = authService.getEmailFromToken(token);
                String phoneNumber = authService.getPhoneNumberFromToken(token);
                String userIdentifier = authService.getUserIdentifierFromToken(token);
                
                if (email != null) {
                    response.put("email", email);
                    response.put("loginType", "email");
                } else if (phoneNumber != null) {
                    response.put("phoneNumber", phoneNumber);
                    response.put("loginType", "phone");
                }
                
                response.put("userIdentifier", userIdentifier);
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
            String userIdentifier = authService.getUserIdentifierFromToken(token);
            
            if (userIdentifier != null) {
                // Extract session token from JWT and logout
                // This is a simplified version - you might want to extract session token from JWT claims
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