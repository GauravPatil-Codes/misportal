package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MonthlyTarget;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface MonthlyTargetRepository extends JpaRepository<MonthlyTarget, Long> {
    
    // Find monthly targets by project ID
    List<MonthlyTarget> findByProjectId(Long projectId);
    
    // Find monthly targets by budget allocation item ID
    List<MonthlyTarget> findByBudgetAllocationItemId(Long budgetAllocationItemId);
    
    // Find monthly targets by project and target month
    List<MonthlyTarget> findByProjectIdAndTargetMonth(Long projectId, LocalDate targetMonth);
    
    // Find monthly targets within date range for a project
    @Query("SELECT mt FROM MonthlyTarget mt WHERE mt.project.id = :projectId " +
           "AND mt.targetMonth BETWEEN :startDate AND :endDate")
    List<MonthlyTarget> findTargetsByDateRange(@Param("projectId") Long projectId, 
                                              @Param("startDate") LocalDate startDate, 
                                              @Param("endDate") LocalDate endDate);
    
    // Find targets that need achievement updates (planned > 0 but achieved = 0)
    @Query("SELECT mt FROM MonthlyTarget mt WHERE mt.project.id = :projectId " +
           "AND mt.plannedTarget > 0 AND mt.achievedTarget = 0")
    List<MonthlyTarget> findPendingTargets(@Param("projectId") Long projectId);
    
    // Get achievement summary for a project
    @Query("SELECT SUM(mt.plannedTarget), SUM(mt.achievedTarget) FROM MonthlyTarget mt " +
           "WHERE mt.project.id = :projectId")
    Object[] getProjectAchievementSummary(@Param("projectId") Long projectId);
}