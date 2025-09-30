package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.MonthlyTargetItem;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemDTO;
import com.cmsfoundation.misportal.dtos.MonthlyTargetItemDTO;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.services.BudgetAllocationItemService;
import com.cmsfoundation.misportal.services.MonthlyTargetItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private BudgetAllocationItemService budgetAllocationItemService;
    
    @Autowired
    private MonthlyTargetItemService monthlyTargetItemService;
    
    @Override
    public Project createProject(Project project) {
        return projectRepository.save(project);
    }
    
    // NEW: Enhanced method to handle full project creation with DTOs
    @Override
    public Project createProjectWithDetails(ProjectCreateRequest request) {
        // Create basic project entity
        Project project = new Project();
        project.setProjectType(request.getProjectType());
        project.setProjectHead(request.getProjectHead());
        project.setProjectName(request.getProjectName());
        project.setProjectTheme(request.getProjectTheme());
        project.setProjectNgoPartner(request.getProjectNgoPartner());
        project.setExpectedBeneficiaries(request.getExpectedBeneficiaries());
        project.setProjectLocation(request.getProjectLocation());
        project.setProjectStartDate(request.getProjectStartDate());
        project.setProjectEndDate(request.getProjectEndDate());
        project.setProjectDescription(request.getProjectDescription());
        project.setProjectObjectives(request.getProjectObjectives());
        project.setProjectdpr(request.getProjectdpr());
        project.setProjectStatus(request.getProjectStatus());
        
        // Set embedded objects for backward compatibility
        if (request.getBudget() != null) {
            project.setBudget(request.getBudget());
        }
        if (request.getBudgetAllocationMatrix() != null) {
            project.setBudgetAllocationMatrix(request.getBudgetAllocationMatrix());
        }
        if (request.getWorkPlan() != null) {
            project.setWorkPlan(request.getWorkPlan());
        }
        if (request.getMonthlyTarget() != null) {
            project.setMonthlyTarget(request.getMonthlyTarget());
        }
        
        // Save project first
        Project savedProject = projectRepository.save(project);
        
        // Convert and save budget allocation items
        if (request.getBudgetAllocationItems() != null && !request.getBudgetAllocationItems().isEmpty()) {
            List<BudgetAllocationItem> budgetItems = convertBudgetAllocationDTOsToEntities(
                request.getBudgetAllocationItems(), savedProject);
            budgetAllocationItemService.createMultipleBudgetItems(budgetItems);
        }
        
        // Convert and save monthly target items
        if (request.getMonthlyTargetItems() != null && !request.getMonthlyTargetItems().isEmpty()) {
            List<MonthlyTargetItem> monthlyTargets = convertMonthlyTargetDTOsToEntities(
                request.getMonthlyTargetItems(), savedProject);
            monthlyTargetItemService.createMultipleMonthlyTargets(monthlyTargets);
        }
        
        return savedProject;
    }
    
    // Helper method to convert BudgetAllocationItemDTO to BudgetAllocationItem
    private List<BudgetAllocationItem> convertBudgetAllocationDTOsToEntities(
            List<BudgetAllocationItemDTO> dtos, Project project) {
        
        return dtos.stream().map(dto -> {
            BudgetAllocationItem entity = new BudgetAllocationItem();
            entity.setSrNo(dto.getSrNo());
            entity.setItemName(dto.getItemName());
            entity.setDescription(dto.getDescription());
            entity.setUnits(dto.getUnits());
            entity.setUnitCost(dto.getUnitCost());
            entity.setCmsContribution(dto.getCmsContribution());
            entity.setNgoContribution(dto.getNgoContribution());
            entity.setGovernmentContribution(dto.getGovernmentContribution());
            entity.setBeneficiaryContribution(dto.getBeneficiaryContribution());
            entity.setBudgetType(dto.getBudgetType());
            entity.setProject(project);
            return entity;
        }).collect(Collectors.toList());
    }
    
    // Helper method to convert MonthlyTargetItemDTO to MonthlyTargetItem
    private List<MonthlyTargetItem> convertMonthlyTargetDTOsToEntities(
    	    List<MonthlyTargetItemDTO> dtos, Project project) {

    	    // Load all budget allocation items for this project once for lookup
    	    Map<String, BudgetAllocationItem> budgetItemMap = budgetAllocationItemService
    	        .getBudgetItemsByProject(project.getId())
    	        .stream()
    	        .collect(Collectors.toMap(BudgetAllocationItem::getSrNo, item -> item));

    	    return dtos.stream()
    	        .map(dto -> {
    	            MonthlyTargetItem entity = new MonthlyTargetItem();
    	            entity.setTargetMonth(dto.getTargetMonth());
    	            entity.setPlannedTarget(dto.getPlannedTarget());
    	            entity.setTargetDescription(dto.getTargetDescription());
    	            entity.setProject(project);

    	            BudgetAllocationItem linkedItem = budgetItemMap.get(dto.getBudgetAllocationItemSrNo());
    	            if (linkedItem == null) {
    	                throw new RuntimeException("Invalid budgetAllocationItemSrNo: " + dto.getBudgetAllocationItemSrNo());
    	            }
    	            entity.setBudgetAllocationItem(linkedItem);

    	            return entity;
    	        }).collect(Collectors.toList());
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
        
        // Update new relationships if provided
        if (project.getBudgetAllocationItems() != null) {
            existing.setBudgetAllocationItems(project.getBudgetAllocationItems());
        }
        if (project.getMonthlyTargetItems() != null) {
            existing.setMonthlyTargetItems(project.getMonthlyTargetItems());
        }

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