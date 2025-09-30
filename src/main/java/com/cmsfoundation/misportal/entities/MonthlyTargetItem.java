package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "monthly_target_items")
public class MonthlyTargetItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference("project-monthlyTargets")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_allocation_item_id")
    @JsonIgnore
    private BudgetAllocationItem budgetAllocationItem;

    
    @Column(name = "target_month")
    private LocalDate targetMonth; // Year-Month format like 2025-01, 2025-02
    
    @Column(name = "planned_target")
    private Integer plannedTarget;
    
    @Column(name = "target_description", columnDefinition = "TEXT")
    private String targetDescription;
    
    // Default constructor
    public MonthlyTargetItem() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
    public BudgetAllocationItem getBudgetAllocationItem() { return budgetAllocationItem; }
    public void setBudgetAllocationItem(BudgetAllocationItem budgetAllocationItem) { this.budgetAllocationItem = budgetAllocationItem; }
    
    public LocalDate getTargetMonth() { return targetMonth; }
    public void setTargetMonth(LocalDate targetMonth) { this.targetMonth = targetMonth; }
    
    public Integer getPlannedTarget() { return plannedTarget; }
    public void setPlannedTarget(Integer plannedTarget) { this.plannedTarget = plannedTarget; }
    
    public String getTargetDescription() { return targetDescription; }
    public void setTargetDescription(String targetDescription) { this.targetDescription = targetDescription; }
}