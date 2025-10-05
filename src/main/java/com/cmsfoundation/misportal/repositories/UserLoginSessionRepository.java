package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.UserLoginSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserLoginSessionRepository extends JpaRepository<UserLoginSession, Long> {
    
    Optional<UserLoginSession> findBySessionTokenAndIsActiveTrue(String sessionToken);
    
    List<UserLoginSession> findByPhoneNumberAndIsActiveTrue(String phoneNumber);
    
    @Query("UPDATE UserLoginSession s SET s.isActive = false WHERE s.phoneNumber = :phoneNumber")
    void deactivateAllSessionsByPhoneNumber(@Param("phoneNumber") String phoneNumber);
    
    @Query("DELETE FROM UserLoginSession s WHERE s.expiresAt < :now OR s.isActive = false")
    void deleteExpiredAndInactiveSessions(@Param("now") LocalDateTime now);
}