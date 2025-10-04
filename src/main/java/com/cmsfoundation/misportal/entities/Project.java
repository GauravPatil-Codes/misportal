package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonBackReference;
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
    
    // ✅ ENHANCED: Proper NGO relationship instead of just string
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_partner_id", nullable = true)
    @JsonBackReference("ngo-projects")
    private NGO ngoPartner;
    
    // Keep backward compatibility
    @Column(name = "project_ngo_partner")
    private String projectNgoPartnerName;
    
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
    
    // ✅ ENHANCED: Project Manager relationship
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_manager_id", nullable = true)
    @JsonBackReference("manager-projects")
    private User projectManager;
    
    @Embedded
    private Budget budget;
    
    @Embedded
    private WorkPlan workPlan;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-budgetItems")
    private List<BudgetAllocationItem> budgetAllocationItems;
    
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-monthlyTargets")
    private List<MonthlyTarget> monthlyTargets;
    
    // ✅ NEW: MIS Reports relationship
    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("project-misReports")
    private List<MISReport> misReports;
    
    @Column(name = "project_status")
    private String projectStatus;
    
    @Column(name = "created_at")
    private LocalDateTime projectCreatedAt;
    
    @Column(name = "updated_at")
    private LocalDateTime projectUpdatedAt;
    
    // ✅ NEW: Performance calculations based on MIS Reports
    public Double getCurrentAchievementPercentage() {
        if (misReports == null || misReports.isEmpty()) {
            return getOverallAchievementPercentage(); // Fallback to monthly targets
        }
        
        return misReports.stream()
            .filter(report -> report.getAchievementPercentage() != null)
            .mapToDouble(report -> {
                try {
                    return Double.parseDouble(report.getAchievementPercentage().replace("%", ""));
                } catch (NumberFormatException e) {
                    return 0.0;
                }
            })
            .average()
            .orElse(0.0);
    }
    
    // ✅ NEW: Financial performance metrics
    public Map<String, Object> getFinancialPerformance() {
        Map<String, Object> performance = new HashMap<>();
        Double totalBudget = getTotalBudgetFromItems();
        Double utilizationRate = getCurrentAchievementPercentage();
        
        performance.put("totalAllocated", totalBudget);
        performance.put("utilizationPercentage", utilizationRate);
        performance.put("remainingBudget", totalBudget * (100 - utilizationRate) / 100);
        performance.put("spentAmount", totalBudget * utilizationRate / 100);
        
        return performance;
    }    
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

    // ✅ ENHANCED METHODS - Your Perfect Business Logic
    public Map<BudgetHead, Double> getBudgetSummaryByHead() {
        if (budgetAllocationItems == null) return Map.of();
        
        return budgetAllocationItems.stream()
            .collect(Collectors.groupingBy(
                BudgetAllocationItem::getBudgetType,
                Collectors.summingDouble(item -> item.getTotal() != null ? item.getTotal() : 0.0)
            ));
    }
    
    public Double getTotalBudgetForHead(BudgetHead budgetHead) {
        if (budgetAllocationItems == null) return 0.0;
        
        return budgetAllocationItems.stream()
            .filter(item -> budgetHead.equals(item.getBudgetType()))
            .mapToDouble(item -> item.getTotal() != null ? item.getTotal() : 0.0)
            .sum();
    }
    
    public Double getTotalBudgetFromItems() {
        if (budgetAllocationItems == null) return 0.0;
        
        return budgetAllocationItems.stream()
            .mapToDouble(item -> item.getTotal() != null ? item.getTotal() : 0.0)
            .sum();
    }
    
    // ✅ PROJECT PERFORMANCE - Based on your MonthlyTarget logic
    public Double getOverallAchievementPercentage() {
        if (monthlyTargets == null || monthlyTargets.isEmpty()) return 0.0;
        
        double totalPlanned = monthlyTargets.stream()
            .mapToDouble(target -> target.getPlannedTarget() != null ? target.getPlannedTarget() : 0.0)
            .sum();
            
        double totalAchieved = monthlyTargets.stream()
            .mapToDouble(target -> target.getAchievedTarget() != null ? target.getAchievedTarget() : 0.0)
            .sum();
            
        return totalPlanned > 0 ? (totalAchieved / totalPlanned) * 100 : 0.0;
    }
    
    // ✅ PERFORMANCE BY BUDGET HEAD - Your Advanced Analytics
    public Map<BudgetHead, Double> getPerformanceByBudgetHead() {
        if (monthlyTargets == null) return Map.of();
        
        return monthlyTargets.stream()
            .collect(Collectors.groupingBy(
                target -> target.getBudgetAllocationItem().getBudgetType(),
                Collectors.averagingDouble(target -> target.getAchievementPercentage() != null ? target.getAchievementPercentage() : 0.0)
            ));
    }

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

	public NGO getNgoPartner() {
		return ngoPartner;
	}

	public void setNgoPartner(NGO ngoPartner) {
		this.ngoPartner = ngoPartner;
	}

	public String getProjectNgoPartnerName() {
		return projectNgoPartnerName;
	}

	public void setProjectNgoPartnerName(String projectNgoPartnerName) {
		this.projectNgoPartnerName = projectNgoPartnerName;
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

	public User getProjectManager() {
		return projectManager;
	}

	public void setProjectManager(User projectManager) {
		this.projectManager = projectManager;
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

	public List<BudgetAllocationItem> getBudgetAllocationItems() {
		return budgetAllocationItems;
	}

	public void setBudgetAllocationItems(List<BudgetAllocationItem> budgetAllocationItems) {
		this.budgetAllocationItems = budgetAllocationItems;
	}

	public List<MonthlyTarget> getMonthlyTargets() {
		return monthlyTargets;
	}

	public void setMonthlyTargets(List<MonthlyTarget> monthlyTargets) {
		this.monthlyTargets = monthlyTargets;
	}

	public List<MISReport> getMisReports() {
		return misReports;
	}

	public void setMisReports(List<MISReport> misReports) {
		this.misReports = misReports;
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

	@Override
	public String toString() {
		return "Project [id=" + id + ", projectType=" + projectType + ", projectHead=" + projectHead + ", projectName="
				+ projectName + ", projectTheme=" + projectTheme + ", ngoPartner=" + ngoPartner
				+ ", projectNgoPartnerName=" + projectNgoPartnerName + ", expectedBeneficiaries="
				+ expectedBeneficiaries + ", projectLocation=" + projectLocation + ", projectStartDate="
				+ projectStartDate + ", projectEndDate=" + projectEndDate + ", projectDescription=" + projectDescription
				+ ", projectObjectives=" + projectObjectives + ", projectdpr=" + projectdpr + ", projectManager="
				+ projectManager + ", budget=" + budget + ", workPlan=" + workPlan + ", budgetAllocationItems="
				+ budgetAllocationItems + ", monthlyTargets=" + monthlyTargets + ", misReports=" + misReports
				+ ", projectStatus=" + projectStatus + ", projectCreatedAt=" + projectCreatedAt + ", projectUpdatedAt="
				+ projectUpdatedAt + "]";
	}

	public Project(Long id, String projectType, String projectHead, String projectName, String projectTheme,
			NGO ngoPartner, String projectNgoPartnerName, String expectedBeneficiaries, String projectLocation,
			LocalDate projectStartDate, LocalDate projectEndDate, String projectDescription, String projectObjectives,
			String projectdpr, User projectManager, Budget budget, WorkPlan workPlan,
			List<BudgetAllocationItem> budgetAllocationItems, List<MonthlyTarget> monthlyTargets,
			List<MISReport> misReports, String projectStatus, LocalDateTime projectCreatedAt,
			LocalDateTime projectUpdatedAt) {
		super();
		this.id = id;
		this.projectType = projectType;
		this.projectHead = projectHead;
		this.projectName = projectName;
		this.projectTheme = projectTheme;
		this.ngoPartner = ngoPartner;
		this.projectNgoPartnerName = projectNgoPartnerName;
		this.expectedBeneficiaries = expectedBeneficiaries;
		this.projectLocation = projectLocation;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.projectDescription = projectDescription;
		this.projectObjectives = projectObjectives;
		this.projectdpr = projectdpr;
		this.projectManager = projectManager;
		this.budget = budget;
		this.workPlan = workPlan;
		this.budgetAllocationItems = budgetAllocationItems;
		this.monthlyTargets = monthlyTargets;
		this.misReports = misReports;
		this.projectStatus = projectStatus;
		this.projectCreatedAt = projectCreatedAt;
		this.projectUpdatedAt = projectUpdatedAt;
	}
	
	public String getProjectNgoPartner() { 
	    return this.projectNgoPartnerName; 
	}

	public void setProjectNgoPartner(String projectNgoPartnerName) { 
	    this.projectNgoPartnerName = projectNgoPartnerName; 
	}

    // ✅ CORE GETTERS AND SETTERS
  
    // ✅ REMOVED: getMonthlyTarget() and setMonthlyTarget() - not needed anymore
    
    
}