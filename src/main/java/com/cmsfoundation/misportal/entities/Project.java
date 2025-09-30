package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "projects")
public class Project {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "project_type")
    private String projectType;
    
    @Column(name = "project_head")
    private String projectHead;
    
    @Column(name = "project_name")
    private String projectName;
    
    @Column(name = "project_theme")
    private String projectTheme;
    
    @Column(name = "project_ngo_partner")
    private String projectNgoPartner;
    
    @Column(name = "expected_beneficiaries")
    private String expectedBeneficiaries;
    
    @Column(name = "project_location")
    private String projectLocation;
    
    @Column(name = "project_start_date")
    private LocalDate projectStartDate;
    
    @Column(name = "project_end_date")
    private LocalDate projectEndDate;
    
    @Column(name = "project_description", columnDefinition = "TEXT")
    private String projectDescription;
    
    @Column(name = "project_objectives", columnDefinition = "TEXT")
    private String projectObjectives;
    
    @Column(name = "projectdpr", columnDefinition = "TEXT")
    private String projectdpr;
    
    // Keep existing embedded objects for backward compatibility
    @Embedded
    private Budget budget;
    
    @Embedded
    private BudgetAllocationMatrix budgetAllocationMatrix;
    
    @Embedded
    private WorkPlan workPlan;
    
    @Embedded
    private MonthlyTarget monthlyTarget;
    
    // NEW: Add relationships for multiple budget allocation items
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-budgetItems")
    private List<BudgetAllocationItem> budgetAllocationItems;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-monthlyTargets")
    private List<MonthlyTargetItem> monthlyTargetItems;
    
    @Column(name = "project_status")
    private String projectStatus;
    
    @Column(name = "created_at")
    private LocalDateTime projectCreatedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime projectUpdatedAt;
    
    @PrePersist
    protected void onCreate() {
        projectCreatedAt = LocalDateTime.now();
        projectUpdatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        projectUpdatedAt = LocalDateTime.now();
    }
    
    // Default constructor
    public Project() {}

    // All existing getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getProjectType() {
        return projectType;
    }

    public void setProjectType(String projectType) {
        this.projectType = projectType;
    }

    public String getProjectHead() {
        return projectHead;
    }

    public void setProjectHead(String projectHead) {
        this.projectHead = projectHead;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getProjectTheme() {
        return projectTheme;
    }

    public void setProjectTheme(String projectTheme) {
        this.projectTheme = projectTheme;
    }

    public String getProjectNgoPartner() {
        return projectNgoPartner;
    }

    public void setProjectNgoPartner(String projectNgoPartner) {
        this.projectNgoPartner = projectNgoPartner;
    }

    public String getExpectedBeneficiaries() {
        return expectedBeneficiaries;
    }

    public void setExpectedBeneficiaries(String expectedBeneficiaries) {
        this.expectedBeneficiaries = expectedBeneficiaries;
    }

    public String getProjectLocation() {
        return projectLocation;
    }

    public void setProjectLocation(String projectLocation) {
        this.projectLocation = projectLocation;
    }

    public LocalDate getProjectStartDate() {
        return projectStartDate;
    }

    public void setProjectStartDate(LocalDate projectStartDate) {
        this.projectStartDate = projectStartDate;
    }

    public LocalDate getProjectEndDate() {
        return projectEndDate;
    }

    public void setProjectEndDate(LocalDate projectEndDate) {
        this.projectEndDate = projectEndDate;
    }

    public String getProjectDescription() {
        return projectDescription;
    }

    public void setProjectDescription(String projectDescription) {
        this.projectDescription = projectDescription;
    }

    public String getProjectObjectives() {
        return projectObjectives;
    }

    public void setProjectObjectives(String projectObjectives) {
        this.projectObjectives = projectObjectives;
    }

    public Budget getBudget() {
        return budget;
    }

    public void setBudget(Budget budget) {
        this.budget = budget;
    }

    public BudgetAllocationMatrix getBudgetAllocationMatrix() {
        return budgetAllocationMatrix;
    }

    public void setBudgetAllocationMatrix(BudgetAllocationMatrix budgetAllocationMatrix) {
        this.budgetAllocationMatrix = budgetAllocationMatrix;
    }

    public WorkPlan getWorkPlan() {
        return workPlan;
    }

    public void setWorkPlan(WorkPlan workPlan) {
        this.workPlan = workPlan;
    }

    public MonthlyTarget getMonthlyTarget() {
        return monthlyTarget;
    }

    public void setMonthlyTarget(MonthlyTarget monthlyTarget) {
        this.monthlyTarget = monthlyTarget;
    }

    public String getProjectStatus() {
        return projectStatus;
    }

    public void setProjectStatus(String projectStatus) {
        this.projectStatus = projectStatus;
    }

    public LocalDateTime getProjectCreatedAt() {
        return projectCreatedAt;
    }

    public void setProjectCreatedAt(LocalDateTime projectCreatedAt) {
        this.projectCreatedAt = projectCreatedAt;
    }

    public LocalDateTime getProjectUpdatedAt() {
        return projectUpdatedAt;
    }

    public void setProjectUpdatedAt(LocalDateTime projectUpdatedAt) {
        this.projectUpdatedAt = projectUpdatedAt;
    }

    public String getProjectdpr() {
        return projectdpr;
    }

    public void setProjectdpr(String projectdpr) {
        this.projectdpr = projectdpr;
    }

    // NEW: Getters and setters for new relationships
    public List<BudgetAllocationItem> getBudgetAllocationItems() {
        return budgetAllocationItems;
    }

    public void setBudgetAllocationItems(List<BudgetAllocationItem> budgetAllocationItems) {
        this.budgetAllocationItems = budgetAllocationItems;
    }

    public List<MonthlyTargetItem> getMonthlyTargetItems() {
        return monthlyTargetItems;
    }

    public void setMonthlyTargetItems(List<MonthlyTargetItem> monthlyTargetItems) {
        this.monthlyTargetItems = monthlyTargetItems;
    }

    // Helper methods to work with budget heads
    public Double getTotalBudgetFromItems() {
        if (budgetAllocationItems == null) return 0.0;
        return budgetAllocationItems.stream()
            .mapToDouble(item -> item.getTotal() != null ? item.getTotal() : 0.0)
            .sum();
    }
    
    public Double getTotalByBudgetHead(BudgetHead budgetHead) {
        if (budgetAllocationItems == null) return 0.0;
        return budgetAllocationItems.stream()
            .filter(item -> budgetHead.equals(item.getBudgetType()))
            .mapToDouble(item -> item.getTotal() != null ? item.getTotal() : 0.0)
            .sum();
    }

    // Keep existing constructor for backward compatibility
    public Project(Long id, String projectType, String projectHead, String projectName, String projectTheme,
            String projectNgoPartner, String expectedBeneficiaries, String projectLocation, LocalDate projectStartDate,
            LocalDate projectEndDate, String projectDescription, String projectObjectives, Budget budget,
            BudgetAllocationMatrix budgetAllocationMatrix, WorkPlan workPlan, MonthlyTarget monthlyTarget,
            String projectStatus, LocalDateTime projectCreatedAt, LocalDateTime projectUpdatedAt) {
        super();
        this.id = id;
        this.projectType = projectType;
        this.projectHead = projectHead;
        this.projectName = projectName;
        this.projectTheme = projectTheme;
        this.projectNgoPartner = projectNgoPartner;
        this.expectedBeneficiaries = expectedBeneficiaries;
        this.projectLocation = projectLocation;
        this.projectStartDate = projectStartDate;
        this.projectEndDate = projectEndDate;
        this.projectDescription = projectDescription;
        this.projectObjectives = projectObjectives;
        this.budget = budget;
        this.budgetAllocationMatrix = budgetAllocationMatrix;
        this.workPlan = workPlan;
        this.monthlyTarget = monthlyTarget;
        this.projectStatus = projectStatus;
        this.projectCreatedAt = projectCreatedAt;
        this.projectUpdatedAt = projectUpdatedAt;
    }

    @Override
    public String toString() {
        return "Project [id=" + id + ", projectType=" + projectType + ", projectHead=" + projectHead + ", projectName="
                + projectName + ", projectTheme=" + projectTheme + ", projectNgoPartner=" + projectNgoPartner
                + ", expectedBeneficiaries=" + expectedBeneficiaries + ", projectLocation=" + projectLocation
                + ", projectStartDate=" + projectStartDate + ", projectEndDate=" + projectEndDate
                + ", projectDescription=" + projectDescription + ", projectObjectives=" + projectObjectives
                + ", budget=" + budget + ", budgetAllocationMatrix=" + budgetAllocationMatrix + ", workPlan=" + workPlan
                + ", monthlyTarget=" + monthlyTarget + ", projectStatus=" + projectStatus + ", projectCreatedAt="
                + projectCreatedAt + ", projectUpdatedAt=" + projectUpdatedAt + "]";
    }
}