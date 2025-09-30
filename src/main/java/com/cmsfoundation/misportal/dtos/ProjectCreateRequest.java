package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.*;

import java.time.LocalDate;
import java.util.List;

public class ProjectCreateRequest {
    
    // Basic project fields
    private String projectType;
    private String projectHead;
    private String projectName;
    private String projectTheme;
    private String projectNgoPartner;
    private String expectedBeneficiaries;
    private String projectLocation;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectDescription;
    private String projectObjectives;
    private String projectdpr;
    private String projectStatus;
    
    // Keep existing embedded objects for backward compatibility
    private Budget budget;
    private BudgetAllocationMatrix budgetAllocationMatrix;
    private WorkPlan workPlan;  
    private MonthlyTarget monthlyTarget;
    
    // NEW: Arrays for multiple budget items and targets
    private List<BudgetAllocationItemDTO> budgetAllocationItems;
    private List<MonthlyTargetItemDTO> monthlyTargetItems;
    
    // Default constructor
    public ProjectCreateRequest() {}

    // All getters and setters
    public String getProjectType() { return projectType; }
    public void setProjectType(String projectType) { this.projectType = projectType; }
    
    public String getProjectHead() { return projectHead; }
    public void setProjectHead(String projectHead) { this.projectHead = projectHead; }
    
    public String getProjectName() { return projectName; }
    public void setProjectName(String projectName) { this.projectName = projectName; }
    
    public String getProjectTheme() { return projectTheme; }
    public void setProjectTheme(String projectTheme) { this.projectTheme = projectTheme; }
    
    public String getProjectNgoPartner() { return projectNgoPartner; }
    public void setProjectNgoPartner(String projectNgoPartner) { this.projectNgoPartner = projectNgoPartner; }
    
    public String getExpectedBeneficiaries() { return expectedBeneficiaries; }
    public void setExpectedBeneficiaries(String expectedBeneficiaries) { this.expectedBeneficiaries = expectedBeneficiaries; }
    
    public String getProjectLocation() { return projectLocation; }
    public void setProjectLocation(String projectLocation) { this.projectLocation = projectLocation; }
    
    public LocalDate getProjectStartDate() { return projectStartDate; }
    public void setProjectStartDate(LocalDate projectStartDate) { this.projectStartDate = projectStartDate; }
    
    public LocalDate getProjectEndDate() { return projectEndDate; }
    public void setProjectEndDate(LocalDate projectEndDate) { this.projectEndDate = projectEndDate; }
    
    public String getProjectDescription() { return projectDescription; }
    public void setProjectDescription(String projectDescription) { this.projectDescription = projectDescription; }
    
    public String getProjectObjectives() { return projectObjectives; }
    public void setProjectObjectives(String projectObjectives) { this.projectObjectives = projectObjectives; }
    
    public String getProjectdpr() { return projectdpr; }
    public void setProjectdpr(String projectdpr) { this.projectdpr = projectdpr; }
    
    public String getProjectStatus() { return projectStatus; }
    public void setProjectStatus(String projectStatus) { this.projectStatus = projectStatus; }
    
    public Budget getBudget() { return budget; }
    public void setBudget(Budget budget) { this.budget = budget; }
    
    public BudgetAllocationMatrix getBudgetAllocationMatrix() { return budgetAllocationMatrix; }
    public void setBudgetAllocationMatrix(BudgetAllocationMatrix budgetAllocationMatrix) { this.budgetAllocationMatrix = budgetAllocationMatrix; }
    
    public WorkPlan getWorkPlan() { return workPlan; }
    public void setWorkPlan(WorkPlan workPlan) { this.workPlan = workPlan; }
    
    public MonthlyTarget getMonthlyTarget() { return monthlyTarget; }
    public void setMonthlyTarget(MonthlyTarget monthlyTarget) { this.monthlyTarget = monthlyTarget; }
    
    public List<BudgetAllocationItemDTO> getBudgetAllocationItems() { return budgetAllocationItems; }
    public void setBudgetAllocationItems(List<BudgetAllocationItemDTO> budgetAllocationItems) { this.budgetAllocationItems = budgetAllocationItems; }
    
    public List<MonthlyTargetItemDTO> getMonthlyTargetItems() { return monthlyTargetItems; }
    public void setMonthlyTargetItems(List<MonthlyTargetItemDTO> monthlyTargetItems) { this.monthlyTargetItems = monthlyTargetItems; }
}