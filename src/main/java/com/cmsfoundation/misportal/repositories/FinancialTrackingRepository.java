package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.FinancialTracking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FinancialTrackingRepository extends JpaRepository<FinancialTracking, Long> {
    
    // Find financial tracking by ID
    Optional<FinancialTracking> findById(Long id);
    
    // Get thematic finance data (all non-IVDP budgets)
    @Query("SELECT f FROM FinancialTracking f WHERE f.healthBudget > 0 OR f.educationBudget > 0 OR f.climateResilience > 0 OR f.livelihood > 0")
    List<FinancialTracking> getThematicFinance();
    
    // Get IVDP finance data
    @Query("SELECT f FROM FinancialTracking f WHERE f.ivdpBudget > 0")
    List<FinancialTracking> getIVDPFinance();
    
    // Get IVDP finance by ID
    @Query("SELECT f FROM FinancialTracking f WHERE f.id = :id AND f.ivdpBudget > 0")
    Optional<FinancialTracking> getIVDPFinanceById(@Param("id") Long id);
    
    // Get thematic finance by ID
    @Query("SELECT f FROM FinancialTracking f WHERE f.id = :id AND (f.healthBudget > 0 OR f.educationBudget > 0 OR f.climateResilience > 0 OR f.livelihood > 0)")
    Optional<FinancialTracking> getThematicFinanceById(@Param("id") Long id);
    
    // Calculate total allocated budget across all records
    @Query("SELECT SUM(f.allocatedBudget) FROM FinancialTracking f")
    Double getTotalAllocatedBudget();
    
    // Calculate total spent budget (sum of all category budgets)
    @Query("SELECT SUM(f.ivdpBudget + f.healthBudget + f.educationBudget + f.climateResilience + f.livelihood + f.governmentConvergence) FROM FinancialTracking f")
    Double getTotalSpentBudget();
    
    // Calculate remaining budget
    @Query("SELECT (SUM(f.totalBudget) - SUM(f.allocatedBudget)) FROM FinancialTracking f")
    Double getRemainingBudget();
    
    // Calculate efficiency rate (allocated vs total)
    @Query("SELECT (SUM(f.allocatedBudget) / SUM(f.totalBudget)) * 100 FROM FinancialTracking f WHERE SUM(f.totalBudget) > 0")
    Double getEfficiencyRate();
    
    // Get average project budget
    @Query("SELECT AVG(f.totalBudget) FROM FinancialTracking f")
    Double getAverageProjectBudget();
    
    // Calculate fund utilization rate
    @Query("SELECT (SUM(f.ivdpBudget + f.healthBudget + f.educationBudget + f.climateResilience + f.livelihood + f.governmentConvergence) / SUM(f.allocatedBudget)) * 100 FROM FinancialTracking f WHERE SUM(f.allocatedBudget) > 0")
    Double getFundUtilizationRate();
}