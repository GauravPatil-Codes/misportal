package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.entities.UserRole;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by ID
    Optional<User> findById(Long id);
    
    
    
    // Find user by mobile number
    Optional<User> findByPhoneNumber(String phoneNumber);
    
    // Check if email exists
    boolean existsByEmailId(String emailId);
    
    // Check if mobile number exists
    boolean existsByPhoneNumber(String PhoneNumber);
    
    Optional<User> findByEmailId(String emailId);
    List<User> findByUserRole(UserRole userRole);
    List<User> findByNgoId(Long ngoId);
    List<User> findByUserRoleAndNgoId(UserRole userRole, Long ngoId);
    
    @Query("SELECT u FROM User u WHERE u.otpCode = :otp AND u.otpExpiry > :currentTime")
    Optional<User> findByValidOTP(@Param("otp") String otp, @Param("currentTime") LocalDateTime currentTime);
}