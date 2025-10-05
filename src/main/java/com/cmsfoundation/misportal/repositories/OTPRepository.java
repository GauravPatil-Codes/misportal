package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.OTPEntity;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OTPRepository extends JpaRepository<OTPEntity, Long> {
    
    Optional<OTPEntity> findTopByPhoneNumberAndIsUsedFalseOrderByCreatedAtDesc(String phoneNumber);
    
    @Query("SELECT o FROM OTPEntity o WHERE o.phoneNumber = :phoneNumber " +
           "AND o.createdAt >= :since AND o.isUsed = false")
    List<OTPEntity> findRecentOTPsByPhoneNumber(@Param("phoneNumber") String phoneNumber, 
                                               @Param("since") LocalDateTime since);
    
    
    
    long countByPhoneNumberAndCreatedAtAfter(String phoneNumber, LocalDateTime after);
    
    Optional<OTPEntity> findByPhoneNumberAndOtpCodeAndIsUsedFalse(String phoneNumber, String otpCode);

    
    @Modifying
    @Transactional
    @Query("DELETE FROM OTPEntity o WHERE o.expiresAt < :now OR o.isUsed = true")
    void deleteExpiredAndUsedOTPs(@Param("now") LocalDateTime now);

}

