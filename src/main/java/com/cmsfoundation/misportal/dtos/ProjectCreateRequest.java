package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.Budget;
import com.cmsfoundation.misportal.entities.WorkPlan;
import java.time.LocalDate;
import java.util.List;

public class ProjectCreateRequest {
    
	// Basic project fields
    private String projectType;
    private String projectHead;
    private String projectName;
    private String projectTheme;
    private String expectedBeneficiaries;
    private String projectLocation;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectDescription;
    private String projectObjectives;
    private String projectdpr;
    private String projectStatus;
    
    // ✅ NEW: NGO and Manager assignment
    private Long ngoPartnerId;
    private Long projectManagerId;
    
    // Budget and work plan
    private Budget budget;
    private WorkPlan workPlan;
    
    // Budget allocation items and targets
    private List<BudgetAllocationItemDTO> budgetAllocationItems;
    private List<MonthlyTargetItemDTO> monthlyTargetItems;
    
    // Constructors, getters, and setters
    public ProjectCreateRequest() {}
    
    // Getters and setters for new fields
    public Long getNgoPartnerId() { return ngoPartnerId; }
    public void setNgoPartnerId(Long ngoPartnerId) { this.ngoPartnerId = ngoPartnerId; }
    
    public Long getProjectManagerId() { return projectManagerId; }
    public void setProjectManagerId(Long projectManagerId) { this.projectManagerId = projectManagerId; }

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

	public String getProjectdpr() {
		return projectdpr;
	}

	public void setProjectdpr(String projectdpr) {
		this.projectdpr = projectdpr;
	}

	public String getProjectStatus() {
		return projectStatus;
	}

	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}

	public Budget getBudget() {
		return budget;
	}

	public void setBudget(Budget budget) {
		this.budget = budget;
	}

	public WorkPlan getWorkPlan() {
		return workPlan;
	}

	public void setWorkPlan(WorkPlan workPlan) {
		this.workPlan = workPlan;
	}

	public List<BudgetAllocationItemDTO> getBudgetAllocationItems() {
		return budgetAllocationItems;
	}

	public void setBudgetAllocationItems(List<BudgetAllocationItemDTO> budgetAllocationItems) {
		this.budgetAllocationItems = budgetAllocationItems;
	}

	public List<MonthlyTargetItemDTO> getMonthlyTargetItems() {
		return monthlyTargetItems;
	}

	public void setMonthlyTargetItems(List<MonthlyTargetItemDTO> monthlyTargetItems) {
		this.monthlyTargetItems = monthlyTargetItems;
	}



	

    public ProjectCreateRequest(String projectType, String projectHead, String projectName, String projectTheme,
			String expectedBeneficiaries, String projectLocation, LocalDate projectStartDate, LocalDate projectEndDate,
			String projectDescription, String projectObjectives, String projectdpr, String projectStatus,
			Long ngoPartnerId, Long projectManagerId, Budget budget, WorkPlan workPlan,
			List<BudgetAllocationItemDTO> budgetAllocationItems, List<MonthlyTargetItemDTO> monthlyTargetItems,
			String projectNgoPartnerName) {
		super();
		this.projectType = projectType;
		this.projectHead = projectHead;
		this.projectName = projectName;
		this.projectTheme = projectTheme;
		this.expectedBeneficiaries = expectedBeneficiaries;
		this.projectLocation = projectLocation;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.projectDescription = projectDescription;
		this.projectObjectives = projectObjectives;
		this.projectdpr = projectdpr;
		this.projectStatus = projectStatus;
		this.ngoPartnerId = ngoPartnerId;
		this.projectManagerId = projectManagerId;
		this.budget = budget;
		this.workPlan = workPlan;
		this.budgetAllocationItems = budgetAllocationItems;
		this.monthlyTargetItems = monthlyTargetItems;
		this.projectNgoPartnerName = projectNgoPartnerName;
	}

	@Override
	public String toString() {
		return "ProjectCreateRequest [projectType=" + projectType + ", projectHead=" + projectHead + ", projectName="
				+ projectName + ", projectTheme=" + projectTheme + ", expectedBeneficiaries=" + expectedBeneficiaries
				+ ", projectLocation=" + projectLocation + ", projectStartDate=" + projectStartDate
				+ ", projectEndDate=" + projectEndDate + ", projectDescription=" + projectDescription
				+ ", projectObjectives=" + projectObjectives + ", projectdpr=" + projectdpr + ", projectStatus="
				+ projectStatus + ", ngoPartnerId=" + ngoPartnerId + ", projectManagerId=" + projectManagerId
				+ ", budget=" + budget + ", workPlan=" + workPlan + ", budgetAllocationItems=" + budgetAllocationItems
				+ ", monthlyTargetItems=" + monthlyTargetItems + ", projectNgoPartnerName=" + projectNgoPartnerName
				+ "]";
	}

	// Getters and Setters
	// ✅ ADD TO ProjectCreateRequest.java
	public String getProjectNgoPartner() { 
	    return this.projectNgoPartnerName; 
	}

	public void setProjectNgoPartner(String projectNgoPartnerName) { 
	    this.projectNgoPartnerName = projectNgoPartnerName; 
	}

	// Add this field if not present
	private String projectNgoPartnerName;
}