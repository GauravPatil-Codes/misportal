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
    
    @Value("${jwt.secret}")
    private String jwtSecret;
    
    @Value("${jwt.expiration:86400000}")
    private long jwtExpiration;
    
    private SecretKey getSigningKey() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes());
    }
    
    /**
     * Create login session and JWT token
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
            
            logger.info("Login session created successfully for: {}", phoneNumber);
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
     * Create JWT token
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
     * Logout user
     */
    public boolean logout(String sessionToken) {
        try {
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
     * Cleanup expired sessions
     */
    public void cleanupExpiredSessions() {
        sessionRepository.deleteExpiredAndInactiveSessions(LocalDateTime.now());
        logger.info("Cleanup completed for expired sessions");
    }
}