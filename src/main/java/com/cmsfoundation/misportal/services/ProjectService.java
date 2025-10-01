package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;

import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemDTO;
import com.cmsfoundation.misportal.dtos.MonthlyTargetItemDTO;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface ProjectService {
    
    // CRUD Operations
    Project createProject(Project project);
    Project createProjectWithDetails(ProjectCreateRequest request); // NEW method for full creation
    List<Project> getAllProjects();
    Optional<Project> getProjectById(Long id);
    Project updateProject(Long id, Project project);
    void deleteProject(Long id);
    
    // Business Operations
    Double getTotalBudgetProject();
    Double getBudgetUtilizedProject();
    Integer getTotalBeneficiariesProject();
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
}