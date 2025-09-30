package com.cmsfoundation.misportal.dtos;

import java.time.LocalDate;

public class MonthlyTargetItemDTO {
    private String budgetAllocationItemSrNo; // Reference to budget item
    private LocalDate targetMonth;
    private Integer plannedTarget;
    private String targetDescription;
    
    // Default constructor
    public MonthlyTargetItemDTO() {}
    
    // Getters and setters
    public String getBudgetAllocationItemSrNo() { return budgetAllocationItemSrNo; }
    public void setBudgetAllocationItemSrNo(String budgetAllocationItemSrNo) { this.budgetAllocationItemSrNo = budgetAllocationItemSrNo; }
    
    public LocalDate getTargetMonth() { return targetMonth; }
    public void setTargetMonth(LocalDate targetMonth) { this.targetMonth = targetMonth; }
    
    public Integer getPlannedTarget() { return plannedTarget; }
    public void setPlannedTarget(Integer plannedTarget) { this.plannedTarget = plannedTarget; }
    
    public String getTargetDescription() { return targetDescription; }
    public void setTargetDescription(String targetDescription) { this.targetDescription = targetDescription; }
}