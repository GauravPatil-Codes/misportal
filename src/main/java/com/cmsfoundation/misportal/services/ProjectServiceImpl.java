package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.entities.Budget;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemDTO;
import com.cmsfoundation.misportal.dtos.MonthlyTargetItemDTO;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.MonthlyTargetRepository;
import com.cmsfoundation.misportal.services.BudgetAllocationItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ProjectServiceImpl implements ProjectService {
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private MonthlyTargetRepository monthlyTargetRepository;
    
    @Autowired
    private BudgetAllocationItemService budgetAllocationItemService;
    
    @Override
    @Transactional
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
        if (request.getWorkPlan() != null) {
            project.setWorkPlan(request.getWorkPlan());
        }
        
        // ✅ REMOVED: MonthlyTargetItem references - using only MonthlyTarget now
        // if (request.getMonthlyTarget() != null) {
        //     project.setMonthlyTarget(request.getMonthlyTarget());
        // }
        
        // Save project first
        Project savedProject = projectRepository.save(project);
        
        // Convert and save budget allocation items with monthly targets
        if (request.getBudgetAllocationItems() != null && !request.getBudgetAllocationItems().isEmpty()) {
            List<BudgetAllocationItem> savedBudgetItems = createBudgetItemsWithTargets(
                request.getBudgetAllocationItems(), savedProject, request.getMonthlyTargetItems());
            
            savedProject.setBudgetAllocationItems(savedBudgetItems);
            
            // Calculate and update budget summary
            Budget budget = savedProject.getBudget() != null ? savedProject.getBudget() : new Budget();
            budget.calculateTotalsFromItems(savedBudgetItems);
            savedProject.setBudget(budget);
            
            // Save project with updated budget summary
            savedProject = projectRepository.save(savedProject);
        }
        
        return savedProject;
    }
    
    @Transactional
    private List<BudgetAllocationItem> createBudgetItemsWithTargets(
            List<BudgetAllocationItemDTO> budgetItemDTOs, 
            Project project, 
            List<MonthlyTargetItemDTO> monthlyTargetDTOs) {
        
        List<BudgetAllocationItem> savedBudgetItems = new ArrayList<>();
        
        // Create map of monthly targets by budget item srNo for quick lookup
        Map<String, List<MonthlyTargetItemDTO>> targetsByBudgetItem = new HashMap<>();
        if (monthlyTargetDTOs != null && !monthlyTargetDTOs.isEmpty()) {
            for (MonthlyTargetItemDTO dto : monthlyTargetDTOs) {
                String srNo = dto.getBudgetAllocationItemSrNo();
                targetsByBudgetItem.computeIfAbsent(srNo, k -> new ArrayList<>()).add(dto);
            }
        }
        
        for (BudgetAllocationItemDTO dto : budgetItemDTOs) {
            // Convert DTO to entity and save
            BudgetAllocationItem budgetItem = convertBudgetAllocationDTO(dto, project);
            BudgetAllocationItem savedBudgetItem = budgetAllocationItemService.createBudgetItem(budgetItem);
            
            // Create monthly targets for this budget item
            List<MonthlyTarget> monthlyTargets = createMonthlyTargetsForBudgetItem(
                savedBudgetItem, project, targetsByBudgetItem.get(dto.getSrNo()));
            
            // Save monthly targets
            if (!monthlyTargets.isEmpty()) {
                List<MonthlyTarget> savedTargets = monthlyTargetRepository.saveAll(monthlyTargets);
                savedBudgetItem.setMonthlyTargets(savedTargets);
            }
            
            savedBudgetItems.add(savedBudgetItem);
        }
        
        return savedBudgetItems;
    }
    
    private BudgetAllocationItem convertBudgetAllocationDTO(BudgetAllocationItemDTO dto, Project project) {
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
    }
    
    private List<MonthlyTarget> createMonthlyTargetsForBudgetItem(
            BudgetAllocationItem budgetItem, 
            Project project, 
            List<MonthlyTargetItemDTO> targetDTOs) {
        
        List<MonthlyTarget> monthlyTargets = new ArrayList<>();
        
        // Generate all months between project start and end date
        LocalDate startDate = project.getProjectStartDate();
        LocalDate endDate = project.getProjectEndDate();
        
        if (startDate == null || endDate == null) {
            return monthlyTargets; // Return empty list if dates are not set
        }
        
        YearMonth start = YearMonth.from(startDate);
        YearMonth end = YearMonth.from(endDate);
        
        // Create map of provided targets by month for quick lookup
        Map<LocalDate, MonthlyTargetItemDTO> providedTargets = new HashMap<>();
        if (targetDTOs != null && !targetDTOs.isEmpty()) {
            for (MonthlyTargetItemDTO dto : targetDTOs) {
                providedTargets.put(dto.getTargetMonth(), dto);
            }
        }
        
        // Create monthly target for each month in project lifecycle
        for (YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)) {
            LocalDate targetMonth = month.atDay(1);
            MonthlyTargetItemDTO providedTarget = providedTargets.get(targetMonth);
            
            MonthlyTarget monthlyTarget = new MonthlyTarget();
            monthlyTarget.setTargetMonth(targetMonth);
            monthlyTarget.setBudgetAllocationItem(budgetItem);
            monthlyTarget.setProject(project);
            
            if (providedTarget != null) {
                // Use provided target data
                monthlyTarget.setPlannedTarget(providedTarget.getPlannedTarget());
                monthlyTarget.setTargetDescription(providedTarget.getTargetDescription());
            } else {
                // Set default values for months without specific targets
                monthlyTarget.setPlannedTarget(0);
                monthlyTarget.setTargetDescription("No specific target set for " + month.getMonth() + " " + month.getYear());
            }
            
            monthlyTargets.add(monthlyTarget);
        }
        
        return monthlyTargets;
    }
    
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
        if (project.getWorkPlan() != null) existing.setWorkPlan(project.getWorkPlan());
        if (project.getProjectStatus() != null) existing.setProjectStatus(project.getProjectStatus());
        if (project.getBudgetAllocationItems() != null) existing.setBudgetAllocationItems(project.getBudgetAllocationItems());
        if (project.getMonthlyTargets() != null) existing.setMonthlyTargets(project.getMonthlyTargets());
        
        // ✅ REMOVED: MonthlyTargetItem references
        // if (project.getMonthlyTarget() != null) existing.setMonthlyTarget(project.getMonthlyTarget());

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
    
    // Method to recalculate budget when budget items are updated
    @Transactional
    public Project recalculateBudgetSummary(Long projectId) {
        Optional<Project> projectOpt = projectRepository.findById(projectId);
        if (projectOpt.isPresent()) {
            Project project = projectOpt.get();
            
            // Recalculate budget summary
            if (project.getBudget() == null) {
                project.setBudget(new Budget());
            }
            project.getBudget().calculateTotalsFromItems(project.getBudgetAllocationItems());
            
            return projectRepository.save(project);
        }
        throw new RuntimeException("Project not found with id: " + projectId);
    }
}