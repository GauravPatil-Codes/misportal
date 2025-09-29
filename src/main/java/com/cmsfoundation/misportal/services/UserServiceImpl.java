package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.repositories.UserRepository;
import com.cmsfoundation.misportal.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public User createUser(User user) {
        // Check if email already exists
        if (userRepository.existsByEmailId(user.getEmailId())) {
            throw new RuntimeException("User already exists with email: " + user.getEmailId());
        }
        return userRepository.save(user);
    }
    
    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
    @Override
    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }
    
    @Override
    public User updateUser(Long id, User user) {

        User existing = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        if (user.getName() != null) {
            existing.setName(user.getName());
        }
        if (user.getEmailId() != null) {
            existing.setEmailId(user.getEmailId());
        }
        if (user.getMobileNumber() != null) {
            existing.setMobileNumber(user.getMobileNumber());
        }
        if (user.getPassword() != null) {
            existing.setPassword(user.getPassword());
        }
        if (user.getCreatedAt() != null) {
            existing.setCreatedAt(user.getCreatedAt());
        }
        if (user.getUpdatedAt() != null) {
            existing.setUpdatedAt(user.getUpdatedAt());
        }

        return userRepository.save(existing);
    }


    @Override
    public void deleteUser(Long id) {
        if (userRepository.existsById(id)) {
            userRepository.deleteById(id);
        } else {
            throw new RuntimeException("User not found with id: " + id);
        }
    }
    
    @Override
    public Optional<User> getUserByEmailId(String emailId) {
        return userRepository.findByEmailId(emailId);
    }
    
    @Override
    public boolean validateUser(String emailId, String password) {
        Optional<User> user = userRepository.findByEmailId(emailId);
        return user.isPresent() && user.get().getPassword().equals(password);
    }
    
    @Override
    public Optional<User> authenticateUser(String emailId, String password) {
        Optional<User> user = userRepository.findByEmailId(emailId);
        if (user.isPresent() && user.get().getPassword().equals(password)) {
            return user;
        }
        return Optional.empty();
    }
}