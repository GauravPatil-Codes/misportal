package com.cmsfoundation.misportal.dtos;

import java.util.Map;
import com.cmsfoundation.misportal.entities.BudgetHead;

public class BudgetSummaryDTO {
    private Map<BudgetHead, Double> budgetByHead;
    private Double totalBudget;
    private Double totalCmsContribution;
    private Double totalNgoContribution;
    private Double totalGovernmentContribution;
    private Double totalBeneficiaryContribution;
    
    // Performance metrics
    private Map<BudgetHead, Double> performanceByHead;
    private Double overallPerformance;
    
    public BudgetSummaryDTO() {}

    // Getters and Setters
    public Map<BudgetHead, Double> getBudgetByHead() { return budgetByHead; }
    public void setBudgetByHead(Map<BudgetHead, Double> budgetByHead) { this.budgetByHead = budgetByHead; }
    
    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }
    
    public Double getTotalCmsContribution() { return totalCmsContribution; }
    public void setTotalCmsContribution(Double totalCmsContribution) { this.totalCmsContribution = totalCmsContribution; }
    
    public Double getTotalNgoContribution() { return totalNgoContribution; }
    public void setTotalNgoContribution(Double totalNgoContribution) { this.totalNgoContribution = totalNgoContribution; }
    
    public Double getTotalGovernmentContribution() { return totalGovernmentContribution; }
    public void setTotalGovernmentContribution(Double totalGovernmentContribution) { this.totalGovernmentContribution = totalGovernmentContribution; }
    
    public Double getTotalBeneficiaryContribution() { return totalBeneficiaryContribution; }
    public void setTotalBeneficiaryContribution(Double totalBeneficiaryContribution) { this.totalBeneficiaryContribution = totalBeneficiaryContribution; }
    
    public Map<BudgetHead, Double> getPerformanceByHead() { return performanceByHead; }
    public void setPerformanceByHead(Map<BudgetHead, Double> performanceByHead) { this.performanceByHead = performanceByHead; }
    
    public Double getOverallPerformance() { return overallPerformance; }
    public void setOverallPerformance(Double overallPerformance) { this.overallPerformance = overallPerformance; }
}