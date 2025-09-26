package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.QuickUpdate;
import com.cmsfoundation.misportal.services.QuickUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/quick-updates")
//@CrossOrigin(origins = "*")
public class QuickUpdateController {
    
    @Autowired
    private QuickUpdateService quickUpdateService;
    
    // CRUD Operations
    
    @PostMapping
    public ResponseEntity<QuickUpdate> createQuickUpdate(@RequestBody QuickUpdate quickUpdate) {
        try {
            QuickUpdate savedQuickUpdate = quickUpdateService.createQuickUpdate(quickUpdate);
            return new ResponseEntity<>(savedQuickUpdate, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<QuickUpdate>> getAllQuickUpdates() {
        List<QuickUpdate> quickUpdates = quickUpdateService.getAllQuickUpdates();
        return new ResponseEntity<>(quickUpdates, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<QuickUpdate> getQuickUpdateById(@PathVariable Long id) {
        Optional<QuickUpdate> quickUpdate = quickUpdateService.getQuickUpdateById(id);
        return quickUpdate.map(qu -> new ResponseEntity<>(qu, HttpStatus.OK))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<QuickUpdate> updateQuickUpdate(@PathVariable Long id, @RequestBody QuickUpdate quickUpdate) {
        try {
            QuickUpdate updatedQuickUpdate = quickUpdateService.updateQuickUpdate(id, quickUpdate);
            return new ResponseEntity<>(updatedQuickUpdate, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteQuickUpdate(@PathVariable Long id) {
        try {
            quickUpdateService.deleteQuickUpdate(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Business Operations
    
    @GetMapping("/by-time")
    public ResponseEntity<List<QuickUpdate>> getUpdatesByTime(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<QuickUpdate> updates = quickUpdateService.getUpdatesByTime(startDate, endDate);
        return new ResponseEntity<>(updates, HttpStatus.OK);
    }
    
    @GetMapping("/pending-count")
    public ResponseEntity<Long> getPendingReportsNumber() {
        Long pendingCount = quickUpdateService.getPendingReportsNumber();
        return new ResponseEntity<>(pendingCount, HttpStatus.OK);
    }
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<QuickUpdate>> getUpdatesByStatus(@PathVariable String status) {
        List<QuickUpdate> updates = quickUpdateService.getUpdatesByStatus(status);
        return new ResponseEntity<>(updates, HttpStatus.OK);
    }
    
    @GetMapping("/target-monthly")
    public ResponseEntity<Object> getTargetMonthly(@RequestParam String projectName) {
        try {
            Object monthlyTarget = quickUpdateService.getTargetMonthly(projectName);
            return new ResponseEntity<>(monthlyTarget, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}