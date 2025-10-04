package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.dtos.UserRegistrationRequest;
import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.entities.UserRole;
import com.cmsfoundation.misportal.repositories.UserRepository;
import com.cmsfoundation.misportal.services.UserService;
import com.cmsfoundation.misportal.repositories.NGORepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private NGORepository ngoRepository;
    
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

        if (user.getFirstName() != null) {
            existing.setFirstName(user.getFirstName());
        }
        if (user.getLastName() != null) {
            existing.setLastName(user.getLastName());
        }        if (user.getEmailId() != null) {
            existing.setEmailId(user.getEmailId());
        }
        if (user.getPhoneNumber() != null) {
            existing.setPhoneNumber(user.getPhoneNumber());
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
    
 // ✅ NEW: Register user with role and NGO
    @Override
    @Transactional
    public User registerUser(UserRegistrationRequest request) {
        // Check if email already exists
        if (userRepository.findByEmailId(request.getEmailId()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        
        User user = new User();
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setEmailId(request.getEmailId());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setPassword(request.getPassword()); // Should be encoded
        user.setUserRole(request.getUserRole());
        user.setIsActive(true);
        user.setIsVerified(false);
        
        // Assign NGO if user is NGO-related role
        if ((request.getUserRole() == UserRole.NGO_PARTNER || 
             request.getUserRole() == UserRole.NGO_FIELD_OFFICER) && 
            request.getNgoId() != null) {
            
            Optional<NGO> ngoOpt = ngoRepository.findById(request.getNgoId());
            if (ngoOpt.isPresent()) {
                user.setNgo(ngoOpt.get());
            }
        }
        
        return userRepository.save(user);
    }
    
    // ✅ NEW: Get user permissions based on role
    @Override
    public Map<String, Object> getUserPermissions(User user) {
        Map<String, Object> permissions = new HashMap<>();
        UserRole role = user.getUserRole();
        
        permissions.put("canCreateProject", 
            role == UserRole.CMS_SUPER_ADMIN || role == UserRole.CMS_PROJECT_MANAGER);
        permissions.put("canEditProject", 
            role == UserRole.CMS_SUPER_ADMIN || role == UserRole.CMS_PROJECT_MANAGER);
        permissions.put("canDeleteProject", 
            role == UserRole.CMS_SUPER_ADMIN);
        permissions.put("canSubmitMISReport", 
            role == UserRole.NGO_PARTNER || role == UserRole.NGO_FIELD_OFFICER || 
            role == UserRole.CMS_SUPER_ADMIN || role == UserRole.CMS_PROJECT_MANAGER);
        permissions.put("canApproveMISReport", 
            role == UserRole.CMS_SUPER_ADMIN || role == UserRole.CMS_PROJECT_MANAGER);
        permissions.put("canManageUsers", 
            role == UserRole.CMS_SUPER_ADMIN);
        permissions.put("canViewAllProjects", 
            role == UserRole.CMS_SUPER_ADMIN || role == UserRole.CMS_PROJECT_MANAGER);
        permissions.put("canViewNGOProjects", 
            role == UserRole.NGO_PARTNER || role == UserRole.NGO_FIELD_OFFICER);
        permissions.put("canManageNGO", 
            role == UserRole.CMS_SUPER_ADMIN);
            
        permissions.put("userRole", role.getDisplayName());
        permissions.put("ngoId", user.getNgo() != null ? user.getNgo().getId() : null);
        permissions.put("ngoName", user.getNgo() != null ? user.getNgo().getNgoName() : null);
        
        return permissions;
    }
}