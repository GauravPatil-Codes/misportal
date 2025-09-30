package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MonthlyTargetItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MonthlyTargetItemRepository extends JpaRepository<MonthlyTargetItem, Long> {
    
    // Find all monthly targets for a project
    List<MonthlyTargetItem> findByProjectId(Long projectId);
    
    // Find monthly targets for a specific budget allocation item
    List<MonthlyTargetItem> findByBudgetAllocationItemId(Long budgetAllocationItemId);
    
    // Find targets for a specific month
    List<MonthlyTargetItem> findByProjectIdAndTargetMonth(Long projectId, LocalDate targetMonth);
    
    // Find targets between date range
    @Query("SELECT m FROM MonthlyTargetItem m WHERE m.project.id = :projectId AND m.targetMonth BETWEEN :startDate AND :endDate")
    List<MonthlyTargetItem> findTargetsByDateRange(@Param("projectId") Long projectId, 
                                                   @Param("startDate") LocalDate startDate, 
                                                   @Param("endDate") LocalDate endDate);
}