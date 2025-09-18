package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.QuickUpdate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QuickUpdateService {
    
    // CRUD Operations
    QuickUpdate createQuickUpdate(QuickUpdate quickUpdate);
    List<QuickUpdate> getAllQuickUpdates();
    Optional<QuickUpdate> getQuickUpdateById(Long id);
    QuickUpdate updateQuickUpdate(Long id, QuickUpdate quickUpdate);
    void deleteQuickUpdate(Long id);
    
    // Business Operations
    List<QuickUpdate> getUpdatesByTime(LocalDateTime startDate, LocalDateTime endDate);
    Long getPendingReportsNumber();
    List<QuickUpdate> getUpdatesByStatus(String status);
    
    // Additional Monthly Target API
    Object getTargetMonthly(String projectName);
}