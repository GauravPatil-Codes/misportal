package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.FinancialTracking;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface FinancialTrackingService {
    
    // CRUD Operations
    FinancialTracking createFinancialTracking(FinancialTracking financialTracking);
    List<FinancialTracking> getAllFinancialTracking();
    Optional<FinancialTracking> getFinancialTrackingById(Long id);
    FinancialTracking updateFinancialTracking(Long id, FinancialTracking financialTracking);
    void deleteFinancialTracking(Long id);
    
    // Business Operations
    List<FinancialTracking> getFinanceByThematic();
    List<FinancialTracking> getFinanceByIVDP();
    Optional<FinancialTracking> getIVDPFinanceById(Long id);
    Optional<FinancialTracking> getThematicFinanceById(Long id);
    Map<String, Double> getOverallBudgetUtilized();
    Map<String, Object> getFinancialPerformance();
}