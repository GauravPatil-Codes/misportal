package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.FinancialTracking;
import com.cmsfoundation.misportal.repositories.FinancialTrackingRepository;
import com.cmsfoundation.misportal.services.FinancialTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class FinancialTrackingServiceImpl implements FinancialTrackingService {
    
    @Autowired
    private FinancialTrackingRepository financialTrackingRepository;
    
    @Override
    public FinancialTracking createFinancialTracking(FinancialTracking financialTracking) {
        return financialTrackingRepository.save(financialTracking);
    }
    
    @Override
    public List<FinancialTracking> getAllFinancialTracking() {
        return financialTrackingRepository.findAll();
    }
    
    @Override
    public Optional<FinancialTracking> getFinancialTrackingById(Long id) {
        return financialTrackingRepository.findById(id);
    }
    
    @Override
    public FinancialTracking updateFinancialTracking(Long id, FinancialTracking financialTracking) {
        if (financialTrackingRepository.existsById(id)) {
            financialTracking.setId(id);
            return financialTrackingRepository.save(financialTracking);
        }
        throw new RuntimeException("Financial Tracking not found with id: " + id);
    }
    
    @Override
    public void deleteFinancialTracking(Long id) {
        if (financialTrackingRepository.existsById(id)) {
            financialTrackingRepository.deleteById(id);
        } else {
            throw new RuntimeException("Financial Tracking not found with id: " + id);
        }
    }
    
    @Override
    public List<FinancialTracking> getFinanceByThematic() {
        return financialTrackingRepository.getThematicFinance();
    }
    
    @Override
    public List<FinancialTracking> getFinanceByIVDP() {
        return financialTrackingRepository.getIVDPFinance();
    }
    
    @Override
    public Optional<FinancialTracking> getIVDPFinanceById(Long id) {
        return financialTrackingRepository.getIVDPFinanceById(id);
    }
    
    @Override
    public Optional<FinancialTracking> getThematicFinanceById(Long id) {
        return financialTrackingRepository.getThematicFinanceById(id);
    }
    
    @Override
    public Map<String, Double> getOverallBudgetUtilized() {
        Map<String, Double> budgetInfo = new HashMap<>();
        
        Double totalAllocated = financialTrackingRepository.getTotalAllocatedBudget();
        Double totalSpent = financialTrackingRepository.getTotalSpentBudget();
        Double remaining = financialTrackingRepository.getRemainingBudget();
        Double efficiencyRate = financialTrackingRepository.getEfficiencyRate();
        
        budgetInfo.put("totalAllocated", totalAllocated != null ? totalAllocated : 0.0);
        budgetInfo.put("totalSpent", totalSpent != null ? totalSpent : 0.0);
        budgetInfo.put("remaining", remaining != null ? remaining : 0.0);
        budgetInfo.put("efficiencyRate", efficiencyRate != null ? efficiencyRate : 0.0);
        
        return budgetInfo;
    }
    
    @Override
    public Map<String, Object> getFinancialPerformance() {
        Map<String, Object> performance = new HashMap<>();
        
        Double averageProjectBudget = financialTrackingRepository.getAverageProjectBudget();
        Double fundUtilizationRate = financialTrackingRepository.getFundUtilizationRate();
        
        // Calculate cost per beneficiary and monthly burn rate (you'll need to implement these based on your data)
        performance.put("averageProjectBudget", averageProjectBudget != null ? averageProjectBudget : 0.0);
        performance.put("fundUtilizationRate", fundUtilizationRate != null ? fundUtilizationRate : 0.0);
        performance.put("costPerBeneficiary", calculateCostPerBeneficiary());
        performance.put("monthlyBurnRate", calculateMonthlyBurnRate());
        
        return performance;
    }
    
    private Double calculateCostPerBeneficiary() {
        // This would need to be calculated based on project data
        // For now, returning a placeholder
        return 0.0;
    }
    
    private Double calculateMonthlyBurnRate() {
        // This would need to be calculated based on monthly spending data
        // For now, returning a placeholder
        return 0.0;
    }
}