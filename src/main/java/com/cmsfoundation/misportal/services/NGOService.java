package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface NGOService {
    
    // CRUD Operations
    NGO createNGO(NGO ngo);
    List<NGO> getAllNGOs();
    Optional<NGO> getNGOById(Long id);
    NGO updateNGO(Long id, NGO ngo);
    void deleteNGO(Long id);
    
    // Business Operations
    List<Project> getProjectsByNGO(Long ngoId);
    Double getNGORatingByQuickUpdates(Long ngoId);
    List<NGO> searchNGOByString(String searchTerm);
   
 // ✅ ADD THESE TO NGOService.java interface
    Map<String, Object> getNGOCompleteData(Long id);
    Map<String, Object> getNGODashboard(Long id);
    List<User> getNGOUsers(Long id);
    User assignUserToNGO(Long ngoId, Long userId);
    List<Map<String, Object>> getAllNGOsWithPerformance();

}