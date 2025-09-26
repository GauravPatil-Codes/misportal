package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

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
    
    @Embedded
    private Budget budget;
    
    @Embedded
    private BudgetAllocationMatrix budgetAllocationMatrix;
    
    @Embedded
    private WorkPlan workPlan;
    
    @Embedded
    private MonthlyTarget monthlyTarget;
    
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
    
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned



}



@Embeddable
class Budget {
    @Column(name = "human_resources_cost")
    private Double humanResourcesCost = 0.0;
    
    @Column(name = "admin_cost")
    private Double adminCost = 0.0;
    
    @Column(name = "management_coordination_cost")
    private Double managementAndCoordinationCost = 0.0;
    
    @Column(name = "miscellaneous_cost")
    private Double miscellaneousCost = 0.0;
    
    @Column(name = "government_convergence_cost")
    private Double governmentConvergenceCost = 0.0;
    
    @Column(name = "total_budget")
    private Double totalBudget = 0.0;
    
    // Default constructor
    public Budget() {}

	// (Optional) Getters
	public Double getHumanResourcesCost() { return humanResourcesCost; }
	public Double getAdminCost() { return adminCost; }
	public Double getManagementAndCoordinationCost() { return managementAndCoordinationCost; }
	public Double getMiscellaneousCost() { return miscellaneousCost; }
	public Double getGovernmentConvergenceCost() { return governmentConvergenceCost; }
	public Double getTotalBudget() { return totalBudget; }

	// (Optional) Setters if you want to populate data
	public void setHumanResourcesCost(Double humanResourcesCost) { this.humanResourcesCost = humanResourcesCost; }
	public void setAdminCost(Double adminCost) { this.adminCost = adminCost; }
	public void setManagementAndCoordinationCost(Double managementAndCoordinationCost) { this.managementAndCoordinationCost = managementAndCoordinationCost; }
	public void setMiscellaneousCost(Double miscellaneousCost) { this.miscellaneousCost = miscellaneousCost; }
	public void setGovernmentConvergenceCost(Double governmentConvergenceCost) { this.governmentConvergenceCost = governmentConvergenceCost; }
	public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }

	@Override
	public String toString() {
		return "Budget{" +
				"humanResourcesCost=" + humanResourcesCost +
				", adminCost=" + adminCost +
				", managementAndCoordinationCost=" + managementAndCoordinationCost +
				", miscellaneousCost=" + miscellaneousCost +
				", governmentConvergenceCost=" + governmentConvergenceCost +
				", totalBudget=" + totalBudget +
				'}';
	}
}

@Embeddable
class BudgetAllocationMatrix {
    @Column(name = "sr_no")
    private String srNo;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "unit")
    private Integer unit;
    
    @Column(name = "unit_cost")
    private Integer unitCost;
    
    @Column(name = "quantity")
    private Integer quantity;
    
    @Column(name = "cms_contribution")
    private Integer cmsContri;
    
    @Column(name = "ngo_contribution")
    private Integer ngoContri;
    
    @Column(name = "beneficiary_contribution")
    private Integer beneficiaryContri;
    
    @Column(name = "government_contribution")
    private Integer governmentContri;
    
    // Default constructor
    public BudgetAllocationMatrix() {}

	// Getters
	public String getSrNo() { return srNo; }
	public String getDescription() { return description; }
	public Integer getUnit() { return unit; }
	public Integer getUnitCost() { return unitCost; }
	public Integer getQuantity() { return quantity; }
	public Integer getCmsContri() { return cmsContri; }
	public Integer getNgoContri() { return ngoContri; }
	public Integer getBeneficiaryContri() { return beneficiaryContri; }
	public Integer getGovernmentContri() { return governmentContri; }

	// Setters
	public void setSrNo(String srNo) { this.srNo = srNo; }
	public void setDescription(String description) { this.description = description; }
	public void setUnit(Integer unit) { this.unit = unit; }
	public void setUnitCost(Integer unitCost) { this.unitCost = unitCost; }
	public void setQuantity(Integer quantity) { this.quantity = quantity; }
	public void setCmsContri(Integer cmsContri) { this.cmsContri = cmsContri; }
	public void setNgoContri(Integer ngoContri) { this.ngoContri = ngoContri; }
	public void setBeneficiaryContri(Integer beneficiaryContri) { this.beneficiaryContri = beneficiaryContri; }
	public void setGovernmentContri(Integer governmentContri) { this.governmentContri = governmentContri; }

	@Override
	public String toString() {
		return "BudgetAllocationMatrix{" +
				"srNo='" + srNo + '\'' +
				", description='" + description + '\'' +
				", unit=" + unit +
				", unitCost=" + unitCost +
				", quantity=" + quantity +
				", cmsContri=" + cmsContri +
				", ngoContri=" + ngoContri +
				", beneficiaryContri=" + beneficiaryContri +
				", governmentContri=" + governmentContri +
				'}';
	}
}

@Embeddable
class WorkPlan {
    @Column(name = "work_plan_details", columnDefinition = "TEXT")
    private String workPlanDetails;
    
    // Default constructor
    public WorkPlan() {}

	public String getWorkPlanDetails() { return workPlanDetails; }
	public void setWorkPlanDetails(String workPlanDetails) { this.workPlanDetails = workPlanDetails; }

	@Override
	public String toString() {
		return "WorkPlan{" +
				"workPlanDetails='" + workPlanDetails + '\'' +
				'}';
	}
}

@Embeddable
class MonthlyTarget {
    @Column(name = "jan_target")
    private Integer jan = 0;
    
    @Column(name = "feb_target")
    private Integer feb = 0;
    
    @Column(name = "mar_target")
    private Integer mar = 0;
    
    @Column(name = "apr_target")
    private Integer apr = 0;
    
    @Column(name = "may_target")
    private Integer may = 0;
    
    @Column(name = "june_target")
    private Integer june = 0;
    
    @Column(name = "july_target")
    private Integer july = 0;
    
    @Column(name = "aug_target")
    private Integer aug = 0;
    
    @Column(name = "sep_target")
    private Integer sep = 0;
    
    @Column(name = "oct_target")
    private Integer oct = 0;
    
    @Column(name = "nov_target")
    private Integer nov = 0;
    
    @Column(name = "dec_target")
    private Integer dec = 0;
    
    // Default constructor
    public MonthlyTarget() {}

	// Getters
	public Integer getJan() { return jan; }
	public Integer getFeb() { return feb; }
	public Integer getMar() { return mar; }
	public Integer getApr() { return apr; }
	public Integer getMay() { return may; }
	public Integer getJune() { return june; }
	public Integer getJuly() { return july; }
	public Integer getAug() { return aug; }
	public Integer getSep() { return sep; }
	public Integer getOct() { return oct; }
	public Integer getNov() { return nov; }
	public Integer getDec() { return dec; }

	// Setters
	public void setJan(Integer jan) { this.jan = jan; }
	public void setFeb(Integer feb) { this.feb = feb; }
	public void setMar(Integer mar) { this.mar = mar; }
	public void setApr(Integer apr) { this.apr = apr; }
	public void setMay(Integer may) { this.may = may; }
	public void setJune(Integer june) { this.june = june; }
	public void setJuly(Integer july) { this.july = july; }
	public void setAug(Integer aug) { this.aug = aug; }
	public void setSep(Integer sep) { this.sep = sep; }
	public void setOct(Integer oct) { this.oct = oct; }
	public void setNov(Integer nov) { this.nov = nov; }
	public void setDec(Integer dec) { this.dec = dec; }

	@Override
	public String toString() {
		return "MonthlyTarget{" +
				"jan=" + jan +
				", feb=" + feb +
				", mar=" + mar +
				", apr=" + apr +
				", may=" + may +
				", june=" + june +
				", july=" + july +
				", aug=" + aug +
				", sep=" + sep +
				", oct=" + oct +
				", nov=" + nov +
				", dec=" + dec +
				'}';
	}
}


