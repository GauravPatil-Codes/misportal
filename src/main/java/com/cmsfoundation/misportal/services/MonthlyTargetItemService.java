package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MonthlyTargetItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MonthlyTargetItemService {
    
    // CRUD Operations
    MonthlyTargetItem createMonthlyTarget(MonthlyTargetItem monthlyTarget);
    List<MonthlyTargetItem> createMultipleMonthlyTargets(List<MonthlyTargetItem> monthlyTargets);
    List<MonthlyTargetItem> getAllMonthlyTargets();
    Optional<MonthlyTargetItem> getMonthlyTargetById(Long id);
    MonthlyTargetItem updateMonthlyTarget(Long id, MonthlyTargetItem monthlyTarget);
    void deleteMonthlyTarget(Long id);
    
    // Business Operations
    List<MonthlyTargetItem> getMonthlyTargetsByProject(Long projectId);
    List<MonthlyTargetItem> getMonthlyTargetsByBudgetItem(Long budgetAllocationItemId);
    List<MonthlyTargetItem> getMonthlyTargetsByProjectAndMonth(Long projectId, LocalDate targetMonth);
    List<MonthlyTargetItem> getMonthlyTargetsByDateRange(Long projectId, LocalDate startDate, LocalDate endDate);
}