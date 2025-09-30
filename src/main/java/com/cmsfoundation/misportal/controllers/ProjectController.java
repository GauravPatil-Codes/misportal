package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.services.ProjectService;
import com.cmsfoundation.misportal.services.BudgetAllocationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {
    
    @Autowired
    private ProjectService projectService;
    
    @Autowired
    private BudgetAllocationItemService budgetAllocationItemService;
    
    // UPDATED: Clean controller method using service layer for conversion
    @PostMapping
    public ResponseEntity<Project> createProject(@RequestBody ProjectCreateRequest request) {
        try {
            // Use the enhanced service method that handles DTO conversion
            Project savedProject = projectService.createProjectWithDetails(request);
            return new ResponseEntity<>(savedProject, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<Project>> getAllProjects() {
        List<Project> projects = projectService.getAllProjects();
        return new ResponseEntity<>(projects, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long id) {
        Optional<Project> project = projectService.getProjectById(id);
        return project.map(p -> new ResponseEntity<>(p, HttpStatus.OK))
                     .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Project> updateProject(@PathVariable Long id, @RequestBody Project project) {
        try {
            Project updatedProject = projectService.updateProject(id, project);
            return new ResponseEntity<>(updatedProject, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProject(@PathVariable Long id) {
        try {
            projectService.deleteProject(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Budget Operations
    @GetMapping("/{id}/budget-items")
    public ResponseEntity<List<BudgetAllocationItem>> getProjectBudgetItems(@PathVariable Long id) {
        List<BudgetAllocationItem> budgetItems = budgetAllocationItemService.getBudgetItemsByProject(id);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/budget-items/head/{budgetHead}")
    public ResponseEntity<List<BudgetAllocationItem>> getProjectBudgetItemsByHead(
            @PathVariable Long id, @PathVariable BudgetHead budgetHead) {
        List<BudgetAllocationItem> budgetItems = budgetAllocationItemService.getBudgetItemsByProjectAndHead(id, budgetHead);
        return new ResponseEntity<>(budgetItems, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/budget-summary")
    public ResponseEntity<Map<BudgetHead, Double>> getProjectBudgetSummary(@PathVariable Long id) {
        Map<BudgetHead, Double> budgetSummary = budgetAllocationItemService.getBudgetSummaryByProject(id);
        return new ResponseEntity<>(budgetSummary, HttpStatus.OK);
    }
    
    @GetMapping("/{id}/total-budget")
    public ResponseEntity<Double> getProjectTotalBudget(@PathVariable Long id) {
        Double totalBudget = budgetAllocationItemService.getTotalBudgetByProject(id);
        return new ResponseEntity<>(totalBudget, HttpStatus.OK);
    }
    
    // Business Operations (existing methods)
    @GetMapping("/total-budget")
    public ResponseEntity<Double> getTotalBudgetProject() {
        Double totalBudget = projectService.getTotalBudgetProject();
        return new ResponseEntity<>(totalBudget, HttpStatus.OK);
    }
    
    @GetMapping("/budget-utilized")
    public ResponseEntity<Double> getBudgetUtilizedProject() {
        Double budgetUtilized = projectService.getBudgetUtilizedProject();
        return new ResponseEntity<>(budgetUtilized, HttpStatus.OK);
    }
    
    @GetMapping("/total-beneficiaries")
    public ResponseEntity<Integer> getTotalBeneficiariesProject() {
        Integer totalBeneficiaries = projectService.getTotalBeneficiariesProject();
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
    
    // Additional Analytics APIs
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
}