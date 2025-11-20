package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemDTO;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemWithSubProjectDTO;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemWithTargetsDTO;
import com.cmsfoundation.misportal.dtos.MonthlyTargetItemDTO;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.dtos.ProjectUpdateNestedRequest;
import com.cmsfoundation.misportal.dtos.SubProjectDTO;
import com.cmsfoundation.misportal.services.ProjectService; // ✅ CHANGED: Use interface instead of implementation

import com.cmsfoundation.misportal.services.BudgetAllocationItemService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService; // ✅ FIXED: Use interface instead of implementation

    @Autowired
    private BudgetAllocationItemService budgetAllocationItemService;

    // ✅ CREATE PROJECT - Using enhanced service method
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectCreateRequest request) {
        try {
            Project savedProject = projectService.createProjectWithDetails(request);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ GET ALL PROJECTS
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // ✅ GET ALL PROJECTS INCLUDING SUBPROJECTS
    @GetMapping("/all")
    public ResponseEntity<List<Project>> getAllProjectsWithSubprojects() {
        List<Project> projects = projectService.getAllProjectsWithSubprojects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // ✅ GET PROJECT BY ID
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // ✅ FIXED UPDATE PROJECT - Using entity-based method
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ NEW: UPDATE PROJECT WITH FULL DETAILS - Using DTO-based method
    @PutMapping("/{id}/details")
    public ResponseEntity<Project> updateProjectWithDetails(@PathVariable Long id, @RequestBody ProjectCreateRequest request) {
        try {
            Project updatedProject = projectService.updateProjectWithDetails(id, request);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }

    // ✅ DELETE PROJECT
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ========== BUDGET ALLOCATION ITEM OPERATIONS ==========

    // ✅ GET ALL BUDGET ITEMS FOR A PROJECT
    @GetMapping("/{id}/budget-items")
    public ResponseEntity<List<BudgetAllocationItem>> getProjectBudgetItems(@PathVariable Long id) {
        List<BudgetAllocationItem> budgetItems = budgetAllocationItemService.getBudgetItemsByProject(id);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    }

    // ✅ GET BUDGET ITEMS BY PROJECT AND BUDGET HEAD
    @GetMapping("/{id}/budget-items/head/{budgetHead}")
    public ResponseEntity<List<BudgetAllocationItem>> getProjectBudgetItemsByHead(
            @PathVariable Long id,
            @PathVariable BudgetHead budgetHead) {
        List<BudgetAllocationItem> budgetItems = budgetAllocationItemService.getBudgetItemsByProjectAndHead(id, budgetHead);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    }

    // ✅ GET BUDGET SUMMARY BY PROJECT
    @GetMapping("/{id}/budget-summary")
    public ResponseEntity<Map<BudgetHead, Double>> getProjectBudgetSummary(@PathVariable Long id) {
        Map<BudgetHead, Double> budgetSummary = budgetAllocationItemService.getBudgetSummaryByProject(id);
        return new ResponseEntity<>(budgetSummary, HttpStatus.OK);
    }

    // ✅ GET TOTAL BUDGET BY PROJECT
    @GetMapping("/{id}/total-budget")
    public ResponseEntity<Double> getProjectTotalBudget(@PathVariable Long id) {
        Double totalBudget = budgetAllocationItemService.getTotalBudgetByProject(id);
        return new ResponseEntity<>(totalBudget, HttpStatus.OK);
    }

    // ========== MONTHLY TARGET OPERATIONS ==========

    // ✅ NEW: GET MONTHLY TARGETS BY PROJECT
    @GetMapping("/{id}/monthly-targets")
    public ResponseEntity<List<MonthlyTarget>> getMonthlyTargetsByProject(@PathVariable Long id) {
        List<MonthlyTarget> monthlyTargets = projectService.getMonthlyTargetsByProject(id);
        return new ResponseEntity<>(monthlyTargets, HttpStatus.OK);
    }

    // ✅ NEW: GET MONTHLY TARGETS BY BUDGET ITEM
    @GetMapping("/budget-items/{budgetItemId}/monthly-targets")
    public ResponseEntity<List<MonthlyTarget>> getMonthlyTargetsByBudgetItem(@PathVariable Long budgetItemId) {
        List<MonthlyTarget> monthlyTargets = projectService.getMonthlyTargetsByBudgetItem(budgetItemId);
        return new ResponseEntity<>(monthlyTargets, HttpStatus.OK);
    }

    // ✅ NEW: GET PROJECT PERFORMANCE METRICS
    @GetMapping("/{id}/performance")
    public ResponseEntity<Map<String, Object>> getProjectPerformanceMetrics(@PathVariable Long id) {
        try {
            Map<String, Object> metrics = projectService.getProjectPerformanceMetrics(id);
            return new ResponseEntity<>(metrics, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // ========== PROJECT ANALYTICS & BUSINESS OPERATIONS ==========

    @GetMapping("/total-budget")
    public ResponseEntity<Double> getTotalBudget() {
        Double totalBudget = projectService.getTotalBudget(); // ✅ FIXED: Method name matches service
        return new ResponseEntity<>(totalBudget, HttpStatus.OK);
    }

    @GetMapping("/budget-utilized")
    public ResponseEntity<Double> getBudgetUtilized() {
        Double budgetUtilized = projectService.getBudgetUtilized(); // ✅ FIXED: Method name matches service
        return new ResponseEntity<>(budgetUtilized, HttpStatus.OK);
    }

    @GetMapping("/total-beneficiaries")
    public ResponseEntity<Integer> getTotalBeneficiaries() {
        Integer totalBeneficiaries = projectService.getTotalBeneficiaries(); // ✅ FIXED: Method name matches service
        return new ResponseEntity<>(totalBeneficiaries, HttpStatus.OK);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Project>> searchProjectByString(@RequestParam String searchTerm) {
        List<Project> projects = projectService.searchProjectByString(searchTerm);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Project>> getProjectByStatus(@PathVariable String status) {
        List<Project> projects = projectService.getProjectByStatus(status);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/theme/{theme}")
    public ResponseEntity<List<Project>> getProjectByTheme(@PathVariable String theme) {
        List<Project> projects = projectService.getProjectByTheme(theme);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/month/{month}/{year}")
    public ResponseEntity<List<Project>> getProjectByMonth(@PathVariable int month, @PathVariable int year) {
        List<Project> projects = projectService.getProjectByMonth(month, year);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/analytics")
    public ResponseEntity<Map<String, Object>> getProjectAnalytics() {
        Map<String, Object> analytics = projectService.getProjectAnalytics();
        return new ResponseEntity<>(analytics, HttpStatus.OK);
    }

    @GetMapping("/performance/{performanceCategory}")
    public ResponseEntity<List<Project>> getProjectByPerformance(@PathVariable String performanceCategory) {
        List<Project> projects = projectService.getProjectByPerformance(performanceCategory);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/ngo/{ngoPartner}")
    public ResponseEntity<List<Project>> getProjectByNGO(@PathVariable String ngoPartner) {
        List<Project> projects = projectService.getProjectByNGO(ngoPartner);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/ivdp")
    public ResponseEntity<List<Project>> getAllIVDPProjects() {
        List<Project> projects = projectService.getAllIVDPProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    @GetMapping("/thematic")
    public ResponseEntity<List<Project>> getAllThematicProjects() {
        List<Project> projects = projectService.getAllThematicProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }

    // ========== ADDITIONAL ANALYTICS APIs ==========

    @GetMapping("/count/status")
    public ResponseEntity<Map<String, Long>> getProjectCountByStatus() {
        Map<String, Long> statusCount = projectService.getProjectCountByStatus();
        return new ResponseEntity<>(statusCount, HttpStatus.OK);
    }

    @GetMapping("/count/theme")
    public ResponseEntity<Map<String, Long>> getProjectCountByTheme() {
        Map<String, Long> themeCount = projectService.getProjectCountByTheme();
        return new ResponseEntity<>(themeCount, HttpStatus.OK);
    }

    // ✅ NEW: RECALCULATE BUDGET SUMMARY
    @PutMapping("/{id}/recalculate-budget")
    public ResponseEntity<Project> recalculateBudgetSummary(@PathVariable Long id) {
        try {
            Project project = projectService.recalculateBudgetSummary(id);
            return new ResponseEntity<>(project, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    
 // ✅ NEW: Get projects by NGO ID
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<Project>> getProjectsByNGO(@PathVariable Long ngoId) {
        List<Project> projects = projectService.getProjectsByNGOId(ngoId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    
    // ✅ NEW: Get projects by NGO name
    @GetMapping("/ngo/name/{ngoName}")
    public ResponseEntity<List<Project>> getProjectsByNGOName(@PathVariable String ngoName) {
        List<Project> projects = projectService.getProjectsByNGOName(ngoName);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    
    // ✅ NEW: Get projects managed by user
    @GetMapping("/manager/{userId}")
    public ResponseEntity<List<Project>> getProjectsByManager(@PathVariable Long userId) {
        List<Project> projects = projectService.getProjectsByManager(userId);
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    
    // ✅ NEW: Get NGO performance metrics for specific project
    @GetMapping("/{id}/ngo-performance")
    public ResponseEntity<Map<String, Object>> getProjectNGOPerformance(@PathVariable Long id) {
        Map<String, Object> performance = projectService.getProjectNGOPerformance(id);
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }
    
    // ✅ NEW: Get financial performance metrics
    @GetMapping("/{id}/financial-performance")
    public ResponseEntity<Map<String, Object>> getProjectFinancialPerformance(@PathVariable Long id) {
        Map<String, Object> performance = projectService.getProjectFinancialPerformance(id);
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }
    
    // ✅ ENHANCED: Create project with NGO assignment
    @PostMapping("/with-ngo")
    public ResponseEntity<Project> createProjectWithNGO(@RequestBody ProjectCreateRequest request) {
        try {
            Project savedProject = projectService.createProjectWithNGO(request);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    // ✅ NEW: Assign NGO to existing project
    @PutMapping("/{projectId}/assign-ngo/{ngoId}")
    public ResponseEntity<Project> assignNGOToProject(@PathVariable Long projectId, @PathVariable Long ngoId) {
        try {
            Project updatedProject = projectService.assignNGOToProject(projectId, ngoId);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // ✅ NEW: Get project dashboard data for NGO
    @GetMapping("/dashboard/ngo/{ngoId}")
    public ResponseEntity<Map<String, Object>> getNGOProjectDashboard(@PathVariable Long ngoId) {
        Map<String, Object> dashboard = projectService.getNGOProjectDashboard(ngoId);
        return new ResponseEntity<>(dashboard, HttpStatus.OK);
    }
    
    @PutMapping("/{id}/updateNested")
    public ResponseEntity<Project> updateProjectNested(
            @PathVariable Long id,
            @RequestBody ProjectUpdateNestedRequest request) {

        try {
            List<BudgetAllocationItemDTO> budgetItems = new ArrayList<>();
            List<MonthlyTargetItemDTO> monthlyTargets = new ArrayList<>();

            if (request.getNestedBudgetAllocationItems() != null) {
                for (BudgetAllocationItemWithTargetsDTO item : request.getNestedBudgetAllocationItems()) {
                    BudgetAllocationItemDTO basicItem = new BudgetAllocationItemDTO();
                    basicItem.setSrNo(item.getSrNo());
                    basicItem.setItemName(item.getItemName());
                    basicItem.setDescription(item.getDescription());
                    basicItem.setUnits(item.getUnits());
                    basicItem.setUnitCost(item.getUnitCost());
                    basicItem.setCmsContribution(item.getCmsContribution());
                    basicItem.setNgoContribution(item.getNgoContribution());
                    basicItem.setGovernmentContribution(item.getGovernmentContribution());
                    basicItem.setBeneficiaryContribution(item.getBeneficiaryContribution());
                    basicItem.setBudgetType(item.getBudgetType());
                    budgetItems.add(basicItem);

                    if (item.getMonthlyTargets() != null) {
                        for (MonthlyTargetItemDTO target : item.getMonthlyTargets()) {
                            target.setBudgetAllocationItemSrNo(item.getSrNo());
                            monthlyTargets.add(target);
                        }
                    }
                }
            }

            ProjectCreateRequest convertedRequest = new ProjectCreateRequest();
            convertedRequest.setId(request.getId());
            convertedRequest.setProjectType(request.getProjectType());
            convertedRequest.setProjectHead(request.getProjectHead());
            convertedRequest.setProjectName(request.getProjectName());
            convertedRequest.setProjectTheme(request.getProjectTheme());
            convertedRequest.setExpectedBeneficiaries(request.getExpectedBeneficiaries());
            convertedRequest.setProjectLocation(request.getProjectLocation());
            convertedRequest.setProjectStartDate(request.getProjectStartDate());
            convertedRequest.setProjectEndDate(request.getProjectEndDate());
            convertedRequest.setProjectDescription(request.getProjectDescription());
            convertedRequest.setProjectObjectives(request.getProjectObjectives());
            convertedRequest.setBudget(request.getBudget());
            convertedRequest.setWorkPlan(request.getWorkPlan());
            convertedRequest.setProjectStatus(request.getProjectStatus());

            convertedRequest.setBudgetAllocationItems(budgetItems);
            convertedRequest.setMonthlyTargetItems(monthlyTargets);

            Project updatedProject = projectService.updateProjectWithDetails(id, convertedRequest);

            return ResponseEntity.ok(updatedProject);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
    
    @PutMapping("/{id}/updateNestedDirect")
    public ResponseEntity<Project> updateProjectNestedDirect(
            @PathVariable Long id,
            @RequestBody Project projectWithNestedData) {
        try {
            System.out.println("=== UPDATE NESTED DIRECT ===");
            System.out.println("Project ID: " + id);
            
            // ✅ NEW: Extract subprojects from incoming project
            List<SubProjectDTO> subProjectDTOs = new ArrayList<>();
            if (projectWithNestedData.getSubProjects() != null && !projectWithNestedData.getSubProjects().isEmpty()) {
                System.out.println("Processing " + projectWithNestedData.getSubProjects().size() + " subprojects");
                for (Project subProject : projectWithNestedData.getSubProjects()) {
                    SubProjectDTO dto = new SubProjectDTO();
                    dto.setId(subProject.getId());
                    dto.setProjectName(subProject.getProjectName());
                    dto.setProjectDescription(subProject.getProjectDescription());
                    dto.setProjectType(subProject.getProjectType());
                    dto.setProjectTheme(subProject.getProjectTheme());
                    dto.setProjectStartDate(subProject.getProjectStartDate());
                    dto.setProjectEndDate(subProject.getProjectEndDate());
                    dto.setProjectStatus(subProject.getProjectStatus());
                    subProjectDTOs.add(dto);
                }
            }
            
            // Extract budget allocation items from the incoming project
            List<BudgetAllocationItemDTO> budgetItemDTOs = new ArrayList<>();
            List<MonthlyTargetItemDTO> monthlyTargetDTOs = new ArrayList<>();
            
            if (projectWithNestedData.getBudgetAllocationItems() != null) {
                System.out.println("Processing " + projectWithNestedData.getBudgetAllocationItems().size() + " budget items");
                for (BudgetAllocationItem item : projectWithNestedData.getBudgetAllocationItems()) {
                    // Convert entity to DTO
                    BudgetAllocationItemDTO dto = new BudgetAllocationItemDTO();
                    dto.setSrNo(item.getSrNo());
                    dto.setItemName(item.getItemName());
                    dto.setDescription(item.getDescription());
                    dto.setUnits(item.getUnits());
                    dto.setUnitCost(item.getUnitCost());
                    dto.setCmsContribution(item.getCmsContribution());
                    dto.setNgoContribution(item.getNgoContribution());
                    dto.setGovernmentContribution(item.getGovernmentContribution());
                    dto.setBeneficiaryContribution(item.getBeneficiaryContribution());
                    dto.setBudgetType(item.getBudgetType());
                    
                    
                    
                    // ✅ NEW: Extract subProjectId if present
                    if (item.getSubProject() != null) {
                        dto.setSubProjectId(item.getSubProject().getId());
                        dto.setSubProjectName(item.getSubProject().getProjectName());
                        System.out.println("Budget item '" + item.getItemName() + 
                                         "' linked to subproject: " + item.getSubProject().getProjectName());
                    }
                    
                    budgetItemDTOs.add(dto);
                    
                    // Extract monthly targets from budget item
                    if (item.getMonthlyTargets() != null) {
                        for (MonthlyTarget target : item.getMonthlyTargets()) {
                            MonthlyTargetItemDTO targetDTO = new MonthlyTargetItemDTO();
                            targetDTO.setTargetMonth(target.getTargetMonth());
                            targetDTO.setPlannedTarget(target.getPlannedTarget());
                            targetDTO.setTargetDescription(target.getTargetDescription());
                            targetDTO.setBudgetAllocationItemSrNo(item.getSrNo());
                            
                            // ✅ Extract ALL monthly target fields
                            targetDTO.setAchievedTarget(target.getAchievedTarget());
                            targetDTO.setDeviation(target.getDeviation());
                            targetDTO.setAchievementPercentage(target.getAchievementPercentage());
                            targetDTO.setDeviationReason(target.getDeviationReason());
                            targetDTO.setMitigationPlan(target.getMitigationPlan());
                            
                            monthlyTargetDTOs.add(targetDTO);
                        }
                    }
                }
            }
            
            // Create request object
            ProjectCreateRequest convertedRequest = new ProjectCreateRequest();
            convertedRequest.setProjectType(projectWithNestedData.getProjectType());
            convertedRequest.setProjectHead(projectWithNestedData.getProjectHead());
            convertedRequest.setProjectName(projectWithNestedData.getProjectName());
            convertedRequest.setProjectTheme(projectWithNestedData.getProjectTheme());
            convertedRequest.setExpectedBeneficiaries(projectWithNestedData.getExpectedBeneficiaries());
            convertedRequest.setProjectLocation(projectWithNestedData.getProjectLocation());
            convertedRequest.setProjectStartDate(projectWithNestedData.getProjectStartDate());
            convertedRequest.setProjectEndDate(projectWithNestedData.getProjectEndDate());
            convertedRequest.setProjectDescription(projectWithNestedData.getProjectDescription());
            convertedRequest.setProjectObjectives(projectWithNestedData.getProjectObjectives());
            convertedRequest.setBudget(projectWithNestedData.getBudget());
            convertedRequest.setWorkPlan(projectWithNestedData.getWorkPlan());
            convertedRequest.setProjectStatus(projectWithNestedData.getProjectStatus());
            convertedRequest.setBudgetAllocationItems(budgetItemDTOs);
            convertedRequest.setMonthlyTargetItems(monthlyTargetDTOs);
            
            // ✅ NEW: Set subprojects in request
            convertedRequest.setSubProjects(subProjectDTOs);
            
            System.out.println("Converted request - Budget Items: " + budgetItemDTOs.size() + 
                             ", Monthly Targets: " + monthlyTargetDTOs.size() +
                             ", Subprojects: " + subProjectDTOs.size());
            
            Project updatedProject = projectService.updateProjectWithDetails(id, convertedRequest);
            return ResponseEntity.ok(updatedProject);
            
        } catch (IllegalArgumentException ex) {
            System.out.println("Validation ERROR: " + ex.getMessage());
            return ResponseEntity.badRequest().body(null);
        } catch (Exception ex) {
            System.out.println("ERROR in updateNestedDirect: " + ex.getMessage());
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ============================================================================
    // Additional Controller Methods for Subproject Support
    // ============================================================================

    // Get budget items with subproject info
    @GetMapping("/{projectId}/budget-items-with-subprojects")
    public ResponseEntity<List<Map<String, Object>>> getBudgetItemsWithSubProjects(
            @PathVariable Long projectId) {
        try {
            List<BudgetAllocationItem> items = budgetAllocationItemService.getBudgetItemsByProject(projectId);
            
            List<Map<String, Object>> result = items.stream()
                .map(item -> {
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", item.getId());
                    map.put("srNo", item.getSrNo());
                    map.put("itemName", item.getItemName());
                    map.put("description", item.getDescription());
                    map.put("units", item.getUnits());
                    map.put("unitCost", item.getUnitCost());
                    map.put("total", item.getTotal());
                    map.put("budgetType", item.getBudgetType());
                    map.put("cmsContribution", item.getCmsContribution());
                    map.put("ngoContribution", item.getNgoContribution());
                    map.put("governmentContribution", item.getGovernmentContribution());
                    map.put("beneficiaryContribution", item.getBeneficiaryContribution());
                    
                    if (item.getSubProject() != null) {
                        map.put("subProjectId", item.getSubProject().getId());
                        map.put("subProjectName", item.getSubProject().getProjectName());
                    } else {
                        map.put("subProjectId", null);
                        map.put("subProjectName", null);
                    }
                    
                    return map;
                })
                .collect(Collectors.toList());
            
            return ResponseEntity.ok(result);
        } catch (Exception ex) {
            System.out.println("ERROR getting budget items with subprojects: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // Get all subprojects for a project
    @GetMapping("/{projectId}/subprojects")
    public ResponseEntity<List<SubProjectDTO>> getSubProjects(@PathVariable Long projectId) {
        try {
            Optional<Project> projectOpt = projectService.getProjectById(projectId);
            
            if (projectOpt.isPresent() && projectOpt.get().getSubProjects() != null) {
                List<SubProjectDTO> subProjects = projectOpt.get().getSubProjects().stream()
                    .map(sp -> {
                        SubProjectDTO dto = new SubProjectDTO();
                        dto.setId(sp.getId());
                        dto.setProjectName(sp.getProjectName());
                        dto.setProjectDescription(sp.getProjectDescription());
                        dto.setProjectType(sp.getProjectType());
                        dto.setProjectTheme(sp.getProjectTheme());
                        dto.setProjectStartDate(sp.getProjectStartDate());
                        dto.setProjectEndDate(sp.getProjectEndDate());
                        dto.setProjectStatus(sp.getProjectStatus());
                        return dto;
                    })
                    .collect(Collectors.toList());
                
                return ResponseEntity.ok(subProjects);
            }
            
            return ResponseEntity.ok(new ArrayList<>());
        } catch (Exception ex) {
            System.out.println("ERROR getting subprojects: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ArrayList<>());
        }
    }

    // ✅ NEW: Create or update a subproject
    @PostMapping("/{projectId}/subprojects")
    public ResponseEntity<SubProjectDTO> createSubProject(
            @PathVariable Long projectId,
            @RequestBody SubProjectDTO subProjectDTO) {
        try {
            Optional<Project> parentProjectOpt = projectService.getProjectById(projectId);
            
            if (parentProjectOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Project parentProject = parentProjectOpt.get();
            
            // Create subproject
            Project subProject = new Project();
            subProject.setProjectName(subProjectDTO.getProjectName());
            subProject.setProjectDescription(subProjectDTO.getProjectDescription());
            subProject.setProjectType(subProjectDTO.getProjectType() != null ? 
                                      subProjectDTO.getProjectType() : parentProject.getProjectType());
            subProject.setProjectTheme(subProjectDTO.getProjectTheme() != null ? 
                                       subProjectDTO.getProjectTheme() : parentProject.getProjectTheme());
            subProject.setProjectStartDate(subProjectDTO.getProjectStartDate() != null ? 
                                           subProjectDTO.getProjectStartDate() : parentProject.getProjectStartDate());
            subProject.setProjectEndDate(subProjectDTO.getProjectEndDate() != null ? 
                                         subProjectDTO.getProjectEndDate() : parentProject.getProjectEndDate());
            subProject.setProjectStatus(subProjectDTO.getProjectStatus() != null ? 
                                        subProjectDTO.getProjectStatus() : "PLANNED");
            subProject.setParentProject(parentProject);
            
            // Copy parent attributes
            subProject.setProjectLocation(parentProject.getProjectLocation());
            subProject.setProjectHead(parentProject.getProjectHead());
            subProject.setProjectNgoPartner(parentProject.getProjectNgoPartner());
            
            // Save to database (assuming you have a method in service)
            Project savedSubProject = projectService.createProject(subProject);
            
            // Convert to DTO for response
            SubProjectDTO responseDTO = new SubProjectDTO();
            responseDTO.setId(savedSubProject.getId());
            responseDTO.setProjectName(savedSubProject.getProjectName());
            responseDTO.setProjectDescription(savedSubProject.getProjectDescription());
            responseDTO.setProjectType(savedSubProject.getProjectType());
            responseDTO.setProjectTheme(savedSubProject.getProjectTheme());
            responseDTO.setProjectStartDate(savedSubProject.getProjectStartDate());
            responseDTO.setProjectEndDate(savedSubProject.getProjectEndDate());
            responseDTO.setProjectStatus(savedSubProject.getProjectStatus());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
        } catch (Exception ex) {
            System.out.println("ERROR creating subproject: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // ✅ NEW: Delete a subproject
    @DeleteMapping("/{projectId}/subprojects/{subProjectId}")
    public ResponseEntity<Void> deleteSubProject(
            @PathVariable Long projectId,
            @PathVariable Long subProjectId) {
        try {
            // Verify the subproject belongs to this parent project
            Optional<Project> subProjectOpt = projectService.getProjectById(subProjectId);
            
            if (subProjectOpt.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            
            Project subProject = subProjectOpt.get();
            if (subProject.getParentProject() == null || 
                !subProject.getParentProject().getId().equals(projectId)) {
                return ResponseEntity.badRequest().build();
            }
            
            // Delete the subproject
            projectService.deleteProject(subProjectId);
            
            return ResponseEntity.noContent().build();
        } catch (Exception ex) {
            System.out.println("ERROR deleting subproject: " + ex.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


}