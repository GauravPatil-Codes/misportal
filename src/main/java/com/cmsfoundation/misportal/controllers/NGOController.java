package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.services.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ngos")
public class NGOController {
    
    @Autowired
    private NGOService ngoService;
    
    // CRUD Operations
    
    @PostMapping
    public ResponseEntity<NGO> createNGO(@RequestBody NGO ngo) {
        try {
            NGO savedNGO = ngoService.createNGO(ngo);
            return new ResponseEntity<>(savedNGO, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<NGO>> getAllNGOs() {
        List<NGO> ngos = ngoService.getAllNGOs();
        return new ResponseEntity<>(ngos, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<NGO> getNGOById(@PathVariable Long id) {
        Optional<NGO> ngo = ngoService.getNGOById(id);
        return ngo.map(n -> new ResponseEntity<>(n, HttpStatus.OK))
                  .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<NGO> updateNGO(@PathVariable Long id, @RequestBody NGO ngo) {
        try {
            NGO updatedNGO = ngoService.updateNGO(id, ngo);
            return new ResponseEntity<>(updatedNGO, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteNGO(@PathVariable Long id) {
        try {
            ngoService.deleteNGO(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Business Operations
    
    @GetMapping("/{id}/projects")
    public ResponseEntity<List<Project>> getProjectsByNGO(@PathVariable Long id) {
        try {
            List<Project> projects = ngoService.getProjectsByNGO(id);
            return new ResponseEntity<>(projects, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{id}/rating")
    public ResponseEntity<Double> getNGORatingByQuickUpdates(@PathVariable Long id) {
        try {
            Double rating = ngoService.getNGORatingByQuickUpdates(id);
            return new ResponseEntity<>(rating, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/search")
    public ResponseEntity<List<NGO>> searchNGOByString(@RequestParam String searchTerm) {
        List<NGO> ngos = ngoService.searchNGOByString(searchTerm);
        return new ResponseEntity<>(ngos, HttpStatus.OK);
    }
    
 // ✅ NEW: Get NGO with all projects and performance
    @GetMapping("/{id}/complete")
    public ResponseEntity<Map<String, Object>> getNGOComplete(@PathVariable Long id) {
        try {
            Map<String, Object> ngoData = ngoService.getNGOCompleteData(id);
            return new ResponseEntity<>(ngoData, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ✅ NEW: Get NGO performance dashboard
    @GetMapping("/{id}/dashboard")
    public ResponseEntity<Map<String, Object>> getNGODashboard(@PathVariable Long id) {
        Map<String, Object> dashboard = ngoService.getNGODashboard(id);
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
    
    // ✅ NEW: Get NGO users
    @GetMapping("/{id}/users")
    public ResponseEntity<List<User>> getNGOUsers(@PathVariable Long id) {
        List<User> users = ngoService.getNGOUsers(id);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    
    // ✅ NEW: Assign user to NGO
    @PostMapping("/{ngoId}/assign-user/{userId}")
    public ResponseEntity<User> assignUserToNGO(@PathVariable Long ngoId, @PathVariable Long userId) {
        try {
            User updatedUser = ngoService.assignUserToNGO(ngoId, userId);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ✅ NEW: Get all NGOs with basic performance metrics
    @GetMapping("/with-performance")
    public ResponseEntity<List<Map<String, Object>>> getAllNGOsWithPerformance() {
        List<Map<String, Object>> ngosWithPerformance = ngoService.getAllNGOsWithPerformance();
        return new ResponseEntity<>(ngosWithPerformance, HttpStatus.OK);
    }
}