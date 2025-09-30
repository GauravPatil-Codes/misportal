package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BudgetAllocationItemRepository extends JpaRepository<BudgetAllocationItem, Long> {
    
    // Find all budget items for a project
    List<BudgetAllocationItem> findByProjectId(Long projectId);
    
    // Find budget items by project and budget head
    List<BudgetAllocationItem> findByProjectIdAndBudgetType(Long projectId, BudgetHead budgetType);
    
    // Get total budget by project
    @Query("SELECT SUM(b.total) FROM BudgetAllocationItem b WHERE b.project.id = :projectId")
    Double getTotalBudgetByProject(@Param("projectId") Long projectId);
    
    // Get total budget by project and budget head
    @Query("SELECT SUM(b.total) FROM BudgetAllocationItem b WHERE b.project.id = :projectId AND b.budgetType = :budgetType")
    Double getTotalBudgetByProjectAndHead(@Param("projectId") Long projectId, @Param("budgetType") BudgetHead budgetType);
    
    // Get budget summary by budget head for a project
    @Query("SELECT b.budgetType, SUM(b.total) FROM BudgetAllocationItem b WHERE b.project.id = :projectId GROUP BY b.budgetType")
    List<Object[]> getBudgetSummaryByProject(@Param("projectId") Long projectId);
}