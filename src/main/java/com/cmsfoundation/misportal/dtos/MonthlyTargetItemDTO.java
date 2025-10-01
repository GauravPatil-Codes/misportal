package com.cmsfoundation.misportal.dtos;

import java.time.LocalDate;

public class MonthlyTargetItemDTO {
    private String budgetAllocationItemSrNo; // Reference to budget item by srNo
    private LocalDate targetMonth;           // Month for this target (e.g., 2025-01-01)
    private Integer plannedTarget;           // Target set during project creation
    private String targetDescription;        // Description of what should be achieved
    
    // Default constructor
    public MonthlyTargetItemDTO() {}
    
    // Constructor with all fields
    public MonthlyTargetItemDTO(String budgetAllocationItemSrNo, LocalDate targetMonth, 
                               Integer plannedTarget, String targetDescription) {
        this.budgetAllocationItemSrNo = budgetAllocationItemSrNo;
        this.targetMonth = targetMonth;
        this.plannedTarget = plannedTarget;
        this.targetDescription = targetDescription;
    }
    
    // FIXED: All required getters and setters
    public String getBudgetAllocationItemSrNo() { 
        return budgetAllocationItemSrNo; 
    }
    
    public void setBudgetAllocationItemSrNo(String budgetAllocationItemSrNo) { 
        this.budgetAllocationItemSrNo = budgetAllocationItemSrNo; 
    }
    
    public LocalDate getTargetMonth() { 
        return targetMonth; 
    }
    
    public void setTargetMonth(LocalDate targetMonth) { 
        this.targetMonth = targetMonth; 
    }
    
    public Integer getPlannedTarget() { 
        return plannedTarget; 
    }
    
    public void setPlannedTarget(Integer plannedTarget) { 
        this.plannedTarget = plannedTarget; 
    }
    
    public String getTargetDescription() { 
        return targetDescription; 
    }
    
    public void setTargetDescription(String targetDescription) { 
        this.targetDescription = targetDescription; 
    }

    @Override
    public String toString() {
        return "MonthlyTargetItemDTO{" +
                "budgetAllocationItemSrNo='" + budgetAllocationItemSrNo + '\'' +
                ", targetMonth=" + targetMonth +
                ", plannedTarget=" + plannedTarget +
                ", targetDescription='" + targetDescription + '\'' +
                '}';
    }
}