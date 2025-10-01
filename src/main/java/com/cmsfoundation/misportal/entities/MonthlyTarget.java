package com.cmsfoundation.misportal.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_targets")
public class MonthlyTarget {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // MANY-TO-ONE relationship with BudgetAllocationItem
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_allocation_item_id", nullable = false)
    @JsonBackReference("budgetItem-monthlyTargets")
    private BudgetAllocationItem budgetAllocationItem;
    
    // MANY-TO-ONE relationship with Project (for easier querying)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference("project-monthlyTargets")
    private Project project;
    
    @Column(name = "target_month")
    private LocalDate targetMonth; // e.g., 2025-01-01 for Jan 2025
    
    @Column(name = "planned_target")
    private Integer plannedTarget; // Set during project creation
    
    @Column(name = "target_description", columnDefinition = "TEXT")
    private String targetDescription; // Description of what needs to be achieved
    
    @Column(name = "achieved_target")
    private Integer achievedTarget = 0; // Updated during MIS submission
    
    @Column(name = "deviation")
    private Integer deviation = 0; // Calculated: plannedTarget - achievedTarget
    
    @Column(name = "achievement_percentage")
    private Double achievementPercentage = 0.0; // Calculated: (achievedTarget/plannedTarget) * 100
    
    @Column(name = "deviation_reason", columnDefinition = "TEXT")
    private String deviationReason; // Explanation for deviation during MIS submission
    
    @Column(name = "mitigation_plan", columnDefinition = "TEXT")
    private String mitigationPlan; // Plan to address deviation
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        calculatePerformanceMetrics();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
        calculatePerformanceMetrics();
    }
    
    // Auto-calculate performance metrics
    private void calculatePerformanceMetrics() {
        if (plannedTarget != null && achievedTarget != null) {
            this.deviation = plannedTarget - achievedTarget;
            if (plannedTarget > 0) {
                this.achievementPercentage = (achievedTarget.doubleValue() / plannedTarget.doubleValue()) * 100;
            }
        }
    }
    
    // Default constructor
    public MonthlyTarget() {}
    
    // Constructor for project creation
    public MonthlyTarget(BudgetAllocationItem budgetAllocationItem, Project project, 
                        LocalDate targetMonth, Integer plannedTarget, String targetDescription) {
        this.budgetAllocationItem = budgetAllocationItem;
        this.project = project;
        this.targetMonth = targetMonth;
        this.plannedTarget = plannedTarget;
        this.targetDescription = targetDescription;
        this.achievedTarget = 0;
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public BudgetAllocationItem getBudgetAllocationItem() { return budgetAllocationItem; }
    public void setBudgetAllocationItem(BudgetAllocationItem budgetAllocationItem) { this.budgetAllocationItem = budgetAllocationItem; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
    public LocalDate getTargetMonth() { return targetMonth; }
    public void setTargetMonth(LocalDate targetMonth) { this.targetMonth = targetMonth; }
    
    public Integer getPlannedTarget() { return plannedTarget; }
    public void setPlannedTarget(Integer plannedTarget) { this.plannedTarget = plannedTarget; }
    
    public String getTargetDescription() { return targetDescription; }
    public void setTargetDescription(String targetDescription) { this.targetDescription = targetDescription; }
    
    public Integer getAchievedTarget() { return achievedTarget; }
    public void setAchievedTarget(Integer achievedTarget) { this.achievedTarget = achievedTarget; }
    
    public Integer getDeviation() { return deviation; }
    public void setDeviation(Integer deviation) { this.deviation = deviation; }
    
    public Double getAchievementPercentage() { return achievementPercentage; }
    public void setAchievementPercentage(Double achievementPercentage) { this.achievementPercentage = achievementPercentage; }
    
    public String getDeviationReason() { return deviationReason; }
    public void setDeviationReason(String deviationReason) { this.deviationReason = deviationReason; }
    
    public String getMitigationPlan() { return mitigationPlan; }
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return "MonthlyTarget{" +
                "id=" + id +
                ", targetMonth=" + targetMonth +
                ", plannedTarget=" + plannedTarget +
                ", achievedTarget=" + achievedTarget +
                ", achievementPercentage=" + achievementPercentage +
                '}';
    }
}