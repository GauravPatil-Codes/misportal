package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Find user by ID
    Optional<User> findById(Long id);
    
    // Find user by email ID (for authentication)
    Optional<User> findByEmailId(String emailId);
    
    // Find user by mobile number
    Optional<User> findByMobileNumber(String mobileNumber);
    
    // Check if email exists
    boolean existsByEmailId(String emailId);
    
    // Check if mobile number exists
    boolean existsByMobileNumber(String mobileNumber);
}