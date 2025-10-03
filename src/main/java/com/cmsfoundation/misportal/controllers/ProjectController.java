package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.services.ProjectService; // ✅ CHANGED: Use interface instead of implementation
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
}