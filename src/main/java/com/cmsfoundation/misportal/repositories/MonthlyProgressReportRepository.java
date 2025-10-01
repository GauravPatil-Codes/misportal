package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MonthlyProgressReport;
import com.cmsfoundation.misportal.entities.BudgetHead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface MonthlyProgressReportRepository extends JpaRepository<MonthlyProgressReport, Long> {
    
    // Find all progress reports for a project
    List<MonthlyProgressReport> findByProjectId(Long projectId);
    
    // Find progress report for specific month and project
    Optional<MonthlyProgressReport> findByProjectIdAndReportingMonth(Long projectId, LocalDate reportingMonth);
    
    // Find reports by date range
    @Query("SELECT m FROM MonthlyProgressReport m WHERE m.project.id = :projectId AND m.reportingMonth BETWEEN :startDate AND :endDate")
    List<MonthlyProgressReport> findReportsByDateRange(@Param("projectId") Long projectId, 
                                                      @Param("startDate") LocalDate startDate, 
                                                      @Param("endDate") LocalDate endDate);
    
    // Get average achievement percentage for a project
    @Query("SELECT AVG(m.achievementPercentage) FROM MonthlyProgressReport m WHERE m.project.id = :projectId")
    Double getAverageAchievementPercentage(@Param("projectId") Long projectId);
    
    // Get reports with deviations (where achieved < planned)
    @Query("SELECT m FROM MonthlyProgressReport m WHERE m.project.id = :projectId AND m.deviation > 0")
    List<MonthlyProgressReport> getReportsWithDeviations(@Param("projectId") Long projectId);
    
    // Get performance by budget head
    @Query("SELECT m.monthlyTarget.budgetAllocationItem.budgetType, AVG(m.achievementPercentage) " +
    	       "FROM MonthlyProgressReport m WHERE m.project.id = :projectId " +
    	       "GROUP BY m.monthlyTarget.budgetAllocationItem.budgetType")
    	List<Object[]> getPerformanceByBudgetHead(@Param("projectId") Long projectId);

    
    // Get monthly performance trend
    @Query("SELECT m.reportingMonth, AVG(m.achievementPercentage) " +
           "FROM MonthlyProgressReport m WHERE m.project.id = :projectId " +
           "GROUP BY m.reportingMonth ORDER BY m.reportingMonth")
    List<Object[]> getMonthlyPerformanceTrend(@Param("projectId") Long projectId);
}