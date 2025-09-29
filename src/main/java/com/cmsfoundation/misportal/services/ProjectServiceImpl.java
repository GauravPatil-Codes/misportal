package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
    
    @Override
    public List<Project> getAllProjects() {
        return projectRepository.findAll();
    }
    
    @Override
    public Optional<Project> getProjectById(Long id) {
        return projectRepository.findById(id);
    }
    
    @Override
    public Project updateProject(Long id, Project project) {
        // Fetch existing Project or throw exception if not found
        Project existing = projectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        if (project.getProjectType() != null) existing.setProjectType(project.getProjectType());
        if (project.getProjectHead() != null) existing.setProjectHead(project.getProjectHead());
        if (project.getProjectName() != null) existing.setProjectName(project.getProjectName());
        if (project.getProjectTheme() != null) existing.setProjectTheme(project.getProjectTheme());
        if (project.getProjectNgoPartner() != null) existing.setProjectNgoPartner(project.getProjectNgoPartner());
        if (project.getExpectedBeneficiaries() != null) existing.setExpectedBeneficiaries(project.getExpectedBeneficiaries());
        if (project.getProjectLocation() != null) existing.setProjectLocation(project.getProjectLocation());
        if (project.getProjectStartDate() != null) existing.setProjectStartDate(project.getProjectStartDate());
        if (project.getProjectEndDate() != null) existing.setProjectEndDate(project.getProjectEndDate());
        if (project.getProjectDescription() != null) existing.setProjectDescription(project.getProjectDescription());
        if (project.getProjectObjectives() != null) existing.setProjectObjectives(project.getProjectObjectives());
        if (project.getBudget() != null) existing.setBudget(project.getBudget());
        if (project.getBudgetAllocationMatrix() != null) existing.setBudgetAllocationMatrix(project.getBudgetAllocationMatrix());
        if (project.getWorkPlan() != null) existing.setWorkPlan(project.getWorkPlan());
        if (project.getMonthlyTarget() != null) existing.setMonthlyTarget(project.getMonthlyTarget());
        if (project.getProjectStatus() != null) existing.setProjectStatus(project.getProjectStatus());

        return projectRepository.save(existing);
    }


    @Override
    public void deleteProject(Long id) {
        if (projectRepository.existsById(id)) {
            projectRepository.deleteById(id);
        } else {
            throw new RuntimeException("Project not found with id: " + id);
        }
    }
    
    @Override
    public Double getTotalBudgetProject() {
        Double total = projectRepository.getTotalBudgetAllProjects();
        return total != null ? total : 0.0;
    }
    
    @Override
    public Double getBudgetUtilizedProject() {
        Double utilized = projectRepository.getTotalBudgetUtilized();
        return utilized != null ? utilized : 0.0;
    }
    
    @Override
    public Integer getTotalBeneficiariesProject() {
        Integer total = projectRepository.getTotalBeneficiaries();
        return total != null ? total : 0;
    }
    
    @Override
    public List<Project> searchProjectByString(String searchTerm) {
        return projectRepository.searchProjectsByString(searchTerm);
    }
    
    @Override
    public List<Project> getProjectByStatus(String status) {
        return projectRepository.findByProjectStatus(status);
    }
    
    @Override
    public List<Project> getProjectByTheme(String theme) {
        return projectRepository.findByProjectTheme(theme);
    }
    
    @Override
    public List<Project> getProjectByMonth(int month, int year) {
        return projectRepository.getProjectsByMonth(month, year);
    }
    
    @Override
    public Map<String, Object> getProjectAnalytics() {
        Map<String, Object> analytics = new HashMap<>();
        analytics.put("totalProjects", projectRepository.count());
        analytics.put("totalBudget", getTotalBudgetProject());
        analytics.put("budgetUtilized", getBudgetUtilizedProject());
        analytics.put("totalBeneficiaries", getTotalBeneficiariesProject());
        analytics.put("projectsByStatus", getProjectCountByStatus());
        analytics.put("projectsByTheme", getProjectCountByTheme());
        return analytics;
    }
    
    @Override
    public List<Project> getProjectByPerformance(String performanceCategory) {
        switch (performanceCategory.toLowerCase()) {
            case "excellent":
                return projectRepository.getProjectsByPerformance(80.0, 100.0);
            case "good":
                return projectRepository.getProjectsByPerformance(50.0, 79.9);
            case "attention":
                return projectRepository.getProjectsByPerformance(30.0, 49.9);
            case "critical":
                return projectRepository.getProjectsByPerformance(0.0, 29.9);
            default:
                return getAllProjects();
        }
    }
    
    @Override
    public List<Project> getProjectByNGO(String ngoPartner) {
        return projectRepository.findByProjectNgoPartner(ngoPartner);
    }
    
    @Override
    public List<Project> getAllIVDPProjects() {
        return projectRepository.getAllIVDPProjects();
    }
    
    @Override
    public List<Project> getAllThematicProjects() {
        return projectRepository.getAllThematicProjects();
    }
    
    @Override
    public Map<String, Long> getProjectCountByStatus() {
        List<Object[]> results = projectRepository.getProjectCountByStatus();
        Map<String, Long> statusCount = new HashMap<>();
        for (Object[] result : results) {
            statusCount.put((String) result[0], (Long) result[1]);
        }
        return statusCount;
    }
    
    @Override
    public Map<String, Long> getProjectCountByTheme() {
        List<Object[]> results = projectRepository.getProjectCountByTheme();
        Map<String, Long> themeCount = new HashMap<>();
        for (Object[] result : results) {
            themeCount.put((String) result[0], (Long) result[1]);
        }
        return themeCount;
    }
}