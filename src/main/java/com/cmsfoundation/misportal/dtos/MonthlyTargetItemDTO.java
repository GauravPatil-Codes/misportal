package com.cmsfoundation.misportal.dtos;

import java.time.LocalDate;

public class MonthlyTargetItemDTO {
    
    // ✅ Existing fields (keep these)
    private String budgetAllocationItemSrNo;
    private LocalDate targetMonth;
    private Integer plannedTarget;
    private String targetDescription;
    
    // 🚨 ADD ALL THESE MISSING FIELDS:
    private Integer achievedTarget;         // For achieved target values
    private Integer deviation;              // For deviation calculations  
    private Double achievementPercentage;   // For percentage calculations
    private String deviationReason;         // For deviation explanations
    private String mitigationPlan;          // For mitigation plans

    // Default constructor
    public MonthlyTargetItemDTO() {}

    // Full constructor with ALL fields
    public MonthlyTargetItemDTO(String budgetAllocationItemSrNo, LocalDate targetMonth,
                               Integer plannedTarget, String targetDescription,
                               Integer achievedTarget, Integer deviation, 
                               Double achievementPercentage, String deviationReason,
                               String mitigationPlan) {
        this.budgetAllocationItemSrNo = budgetAllocationItemSrNo;
        this.targetMonth = targetMonth;
        this.plannedTarget = plannedTarget;
        this.targetDescription = targetDescription;
        this.achievedTarget = achievedTarget;           // 🚨 ADD
        this.deviation = deviation;                     // 🚨 ADD
        this.achievementPercentage = achievementPercentage; // 🚨 ADD
        this.deviationReason = deviationReason;         // 🚨 ADD
        this.mitigationPlan = mitigationPlan;           // 🚨 ADD
    }

    // ✅ Existing getters/setters (keep these)
    public String getBudgetAllocationItemSrNo() { return budgetAllocationItemSrNo; }
    public void setBudgetAllocationItemSrNo(String budgetAllocationItemSrNo) { 
        this.budgetAllocationItemSrNo = budgetAllocationItemSrNo; 
    }
    
    public LocalDate getTargetMonth() { return targetMonth; }
    public void setTargetMonth(LocalDate targetMonth) { this.targetMonth = targetMonth; }
    
    public Integer getPlannedTarget() { return plannedTarget; }
    public void setPlannedTarget(Integer plannedTarget) { this.plannedTarget = plannedTarget; }
    
    public String getTargetDescription() { return targetDescription; }
    public void setTargetDescription(String targetDescription) { 
        this.targetDescription = targetDescription; 
    }

    // 🚨 ADD ALL THESE NEW GETTERS/SETTERS:
    public Integer getAchievedTarget() { return achievedTarget; }
    public void setAchievedTarget(Integer achievedTarget) { this.achievedTarget = achievedTarget; }
    
    public Integer getDeviation() { return deviation; }
    public void setDeviation(Integer deviation) { this.deviation = deviation; }
    
    public Double getAchievementPercentage() { return achievementPercentage; }
    public void setAchievementPercentage(Double achievementPercentage) { 
        this.achievementPercentage = achievementPercentage; 
    }
    
    public String getDeviationReason() { return deviationReason; }
    public void setDeviationReason(String deviationReason) { this.deviationReason = deviationReason; }
    
    public String getMitigationPlan() { return mitigationPlan; }
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }

    @Override
    public String toString() {
        return "MonthlyTargetItemDTO{" +
                "budgetAllocationItemSrNo='" + budgetAllocationItemSrNo + '\'' +
                ", targetMonth=" + targetMonth +
                ", plannedTarget=" + plannedTarget +
                ", targetDescription='" + targetDescription + '\'' +
                ", achievedTarget=" + achievedTarget +                    // 🚨 ADD
                ", deviation=" + deviation +                              // 🚨 ADD
                ", achievementPercentage=" + achievementPercentage +      // 🚨 ADD
                ", deviationReason='" + deviationReason + '\'' +          // 🚨 ADD
                ", mitigationPlan='" + mitigationPlan + '\'' +            // 🚨 ADD
                '}';
    }
}