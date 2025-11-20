package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.UserLoginSession;
import com.cmsfoundation.misportal.repositories.UserLoginSessionRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class AuthService {
    
    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private UserLoginSessionRepository sessionRepository;
    
    @Autowired
    private EmailService emailService;
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Validate virtual credentials for email login
     */
    public boolean validateEmailCredentials(String email, String password) {
        // Virtual credentials for demonstration
        switch (email.toLowerCase()) {
            case "admin@cmsfoundation.org":
                return "admin123".equals(password);
            case "officer@cmsfoundation.org":
                return "officer123".equals(password);
            case "head@cmsfoundation.org":
                return "head123".equals(password);
            case "ngo@healthfoundation.org":
                return "ngo123".equals(password);
            default:
                return false;
        }
    }
    
    /**
     * Get user role from email
     */
    public String getUserRole(String email) {
        switch (email.toLowerCase()) {
            case "admin@cmsfoundation.org":
                return "ADMIN";
            case "officer@cmsfoundation.org":
                return "PROJECT_OFFICER";
            case "head@cmsfoundation.org":
                return "CSR_HEAD";
            case "ngo@healthfoundation.org":
                return "NGO_PARTNER";
            default:
                return "USER";
        }
    }

    /**
     * Create login session and JWT token for phone number
     */
    public Map<String, Object> createLoginSession(String phoneNumber, String deviceInfo, String ipAddress) {
        try {
            // Deactivate existing sessions
            sessionRepository.deactivateAllSessionsByPhoneNumber(phoneNumber);
            
            // Create new session
            String sessionToken = UUID.randomUUID().toString();
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // 7 days session
            
            UserLoginSession session = new UserLoginSession(phoneNumber, sessionToken, expiresAt);
            session.setDeviceInfo(deviceInfo);
            session.setIpAddress(ipAddress);
            sessionRepository.save(session);
            
            // Create JWT token
            String jwtToken = createJWTToken(phoneNumber, sessionToken);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("sessionToken", sessionToken);
            result.put("jwtToken", jwtToken);
            result.put("expiresAt", expiresAt);
            result.put("message", "Login successful");
            result.put("phoneNumber", phoneNumber);
            result.put("role", "USER");
            
            logger.info("Phone login session created successfully for: {}", phoneNumber);
            return result;
            
        } catch (Exception e) {
            logger.error("Error creating login session for {}: {}", phoneNumber, e.getMessage());
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "Failed to create login session");
            return errorResult;
        }
    }
    
    /**
     * Create login session and JWT token for email
     */
    public Map<String, Object> createEmailLoginSession(String email, String deviceInfo, String ipAddress) {
        try {
            // Deactivate existing sessions by email (we'll store email in phoneNumber field for now)
            sessionRepository.deactivateAllSessionsByPhoneNumber(email);
            
            // Create new session
            String sessionToken = UUID.randomUUID().toString();
            LocalDateTime expiresAt = LocalDateTime.now().plusDays(7); // 7 days session
            
            UserLoginSession session = new UserLoginSession(email, sessionToken, expiresAt);
            session.setDeviceInfo(deviceInfo);
            session.setIpAddress(ipAddress);
            sessionRepository.save(session);
            
            // Create JWT token
            String jwtToken = createEmailJWTToken(email, sessionToken);
            
            Map<String, Object> result = new HashMap<>();
            result.put("success", true);
            result.put("sessionToken", sessionToken);
            result.put("jwtToken", jwtToken);
            result.put("expiresAt", expiresAt);
            result.put("message", "Login successful");
            result.put("email", email);
            result.put("role", getUserRole(email));
            
            logger.info("Email login session created successfully for: {}", email);
            
            // Send login confirmation email asynchronously
            try {
                emailService.sendLoginConfirmationEmail(
                    email, 
                    "Email & Password", 
                    LocalDateTime.now(), 
                    deviceInfo
                );
            } catch (Exception emailEx) {
                logger.warn("Failed to send login confirmation email to {}: {}", email, emailEx.getMessage());
                // Don't fail the login if email fails
            }
            
            return result;
            
        } catch (Exception e) {
            logger.error("Error creating email login session for {}: {}", email, e.getMessage());
            Map<String, Object> errorResult = new HashMap<>();
            errorResult.put("success", false);
            errorResult.put("message", "Failed to create login session");
            return errorResult;
        }
    }
    
    /**
     * Create JWT token for phone number
     */
    private String createJWTToken(String phoneNumber, String sessionToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("phoneNumber", phoneNumber);
        claims.put("sessionToken", sessionToken);
        claims.put("type", "access_token");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(phoneNumber)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Create JWT token for email
     */
    private String createEmailJWTToken(String email, String sessionToken) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + jwtExpiration);
        
        Map<String, Object> claims = new HashMap<>();
        claims.put("email", email);
        claims.put("sessionToken", sessionToken);
        claims.put("type", "access_token");
        
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    
    /**
     * Validate JWT token
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            String sessionToken = claims.get("sessionToken", String.class);
            
            // Check if session is still active
            var session = sessionRepository.findBySessionTokenAndIsActiveTrue(sessionToken);
            return session.isPresent() && session.get().getExpiresAt().isAfter(LocalDateTime.now());
            
        } catch (Exception e) {
            logger.error("Token validation failed: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Get phone number from token
     */
    public String getPhoneNumberFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("phoneNumber", String.class);
            
        } catch (Exception e) {
            logger.error("Failed to extract phone number from token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Get email from token
     */
    public String getEmailFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            return claims.get("email", String.class);
            
        } catch (Exception e) {
            logger.error("Failed to extract email from token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Get user identifier (phone or email) from token
     */
    public String getUserIdentifierFromToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
            
            // Try email first, then phone number
            String email = claims.get("email", String.class);
            if (email != null && !email.isEmpty()) {
                return email;
            }
            
            return claims.get("phoneNumber", String.class);
            
        } catch (Exception e) {
            logger.error("Failed to extract user identifier from token: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Logout user by JWT token
     */
    public boolean logout(String jwtToken) {
        try {
            // Extract session token from JWT
            String sessionToken = extractSessionTokenFromJWT(jwtToken);
            if (sessionToken == null) {
                logger.error("Failed to extract session token from JWT");
                return false;
            }
            
            var session = sessionRepository.findBySessionTokenAndIsActiveTrue(sessionToken);
            if (session.isPresent()) {
                session.get().setActive(false);
                sessionRepository.save(session.get());
                logger.info("User logged out successfully: {}", session.get().getPhoneNumber());
                return true;
            }
            return false;
            
        } catch (Exception e) {
            logger.error("Error during logout: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Extract session token from JWT
     */
    private String extractSessionTokenFromJWT(String jwtToken) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(jwtToken)
                    .getBody();
            
            return claims.get("sessionToken", String.class);
        } catch (Exception e) {
            logger.error("Failed to extract session token from JWT: {}", e.getMessage());
            return null;
        }
    }
    
    /**
     * Cleanup expired sessions
     */
    public void cleanupExpiredSessions() {
        sessionRepository.deleteExpiredAndInactiveSessions(LocalDateTime.now());
        logger.info("Cleanup completed for expired sessions");
    }
}