package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.stereotype.Service;

@Service
public interface BudgetAllocationItemService {
    
    // CRUD Operations
    BudgetAllocationItem createBudgetItem(BudgetAllocationItem budgetItem);
    List<BudgetAllocationItem> getAllBudgetItems();
    Optional<BudgetAllocationItem> getBudgetItemById(Long id);
    BudgetAllocationItem updateBudgetItem(Long id, BudgetAllocationItem budgetItem);
    void deleteBudgetItem(Long id);
    
    // Business Operations
    List<BudgetAllocationItem> getBudgetItemsByProject(Long projectId);
    List<BudgetAllocationItem> getBudgetItemsByProjectAndHead(Long projectId, BudgetHead budgetHead);
    Double getTotalBudgetByProject(Long projectId);
    Double getTotalBudgetByProjectAndHead(Long projectId, BudgetHead budgetHead);
    Map<BudgetHead, Double> getBudgetSummaryByProject(Long projectId);
    
    // Bulk operations
    List<BudgetAllocationItem> createMultipleBudgetItems(List<BudgetAllocationItem> budgetItems);
}