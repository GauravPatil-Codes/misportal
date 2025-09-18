package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    
    // CRUD Operations
    User createUser(User user);
    List<User> getAllUsers();
    Optional<User> getUserById(Long id);
    User updateUser(Long id, User user);
    void deleteUser(Long id);
    
    // Authentication Operations
    Optional<User> getUserByEmailId(String emailId);
    boolean validateUser(String emailId, String password);
    Optional<User> authenticateUser(String emailId, String password);
}