package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    
    // ========== BASIC CRUD OPERATIONS ==========
    Project createProject(Project project);
    Project createProjectWithDetails(ProjectCreateRequest request);
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project updateProject(Long id, Project project);
    Project updateProjectWithDetails(Long id, ProjectCreateRequest request);
    void deleteProject(Long id);
    
    // ========== BUDGET & TARGET OPERATIONS ==========
    List<BudgetAllocationItem> getBudgetAllocationItemsByProject(Long projectId);
    List<BudgetAllocationItem> getBudgetAllocationItemsByProjectAndBudgetHead(Long projectId, BudgetHead budgetHead);
    Map<BudgetHead, Double> getBudgetSummaryByProject(Long projectId);
    List<MonthlyTarget> getMonthlyTargetsByProject(Long projectId);
    List<MonthlyTarget> getMonthlyTargetsByBudgetItem(Long budgetItemId);
    Map<String, Object> getProjectPerformanceMetrics(Long projectId);
    Project recalculateBudgetSummary(Long projectId);
    
    // ========== BUSINESS ANALYTICS ==========
    Double getTotalBudget();
    Double getBudgetUtilized();
    Integer getTotalBeneficiaries();
    List<Project> searchProjectByString(String searchTerm);
    List<Project> getProjectByStatus(String status);
    List<Project> getProjectByTheme(String theme);
    List<Project> getProjectByMonth(int month, int year);
    Map<String, Object> getProjectAnalytics();
    List<Project> getProjectByPerformance(String performanceCategory);
    List<Project> getProjectByNGO(String ngoPartner);
    List<Project> getAllIVDPProjects();
    List<Project> getAllThematicProjects();
    Map<String, Long> getProjectCountByStatus();
    Map<String, Long> getProjectCountByTheme();
	List<Project> getProjectsByNGOId(Long ngoId);
	List<Project> getProjectsByNGOName(String ngoName);
	List<Project> getProjectsByManager(Long userId);
	Map<String, Object> getProjectNGOPerformance(Long projectId);
	Map<String, Object> getNGOProjectDashboard(Long ngoId);
	Project assignNGOToProject(Long projectId, Long ngoId);
	Project createProjectWithNGO(ProjectCreateRequest request);
	Map<String, Object> getProjectFinancialPerformance(Long projectId);
	
	
}