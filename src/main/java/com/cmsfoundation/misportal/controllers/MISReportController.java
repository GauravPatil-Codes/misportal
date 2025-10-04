package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.dtos.MISReportSubmissionRequest;
import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.entities.ReportStatus;
import com.cmsfoundation.misportal.services.MISReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/mis-reports")
public class MISReportController {
    
    @Autowired
    private MISReportService misReportService;
    
    // CRUD Operations
    
    @PostMapping
    public ResponseEntity<MISReport> createMISReport(@RequestBody MISReport misReport) {
        try {
            MISReport savedMISReport = misReportService.createMISReport(misReport);
            return new ResponseEntity<>(savedMISReport, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<MISReport>> getAllMISReports() {
        List<MISReport> misReports = misReportService.getAllMISReports();
        return new ResponseEntity<>(misReports, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MISReport> getMISReportById(@PathVariable Long id) {
        Optional<MISReport> misReport = misReportService.getMISReportById(id);
        return misReport.map(mr -> new ResponseEntity<>(mr, HttpStatus.OK))
                       .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MISReport> updateMISReport(@PathVariable Long id, @RequestBody MISReport misReport) {
        try {
            MISReport updatedMISReport = misReportService.updateMISReport(id, misReport);
            return new ResponseEntity<>(updatedMISReport, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMISReport(@PathVariable Long id) {
        try {
            misReportService.deleteMISReport(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Business Operations
    
    @GetMapping("/date-range")
    public ResponseEntity<List<MISReport>> getReportsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<MISReport> reports = misReportService.getReportsByDateRange(startDate, endDate);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    @GetMapping("/achievement-range")
    public ResponseEntity<List<MISReport>> getReportsByAchievementRange(
            @RequestParam double minPercentage,
            @RequestParam double maxPercentage) {
        List<MISReport> reports = misReportService.getReportsByAchievementRange(minPercentage, maxPercentage);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    // Using project ID for project-specific queries and counts
    
    @GetMapping("/count/project/{projectId}")
    public ResponseEntity<Long> countReportsByProject(@PathVariable Long projectId) {
        Long count = misReportService.countByProjectId(projectId);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }

    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<MISReport>> getMISReportsByProject(@PathVariable Long projectId) {
        List<MISReport> reports = misReportService.getMISReportsByProject(projectId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }

    // Submit MIS report with validation

    @PostMapping("/submit")
    public ResponseEntity<Map<String, Object>> submitMISReport(@RequestBody MISReportSubmissionRequest request) {
        try {
            MISReport savedReport = misReportService.submitMISReport(request);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "MIS Report submitted successfully");
            response.put("reportId", savedReport.getId());
            response.put("status", savedReport.getReportStatus());
            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (SecurityException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Access denied: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Failed to submit report: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Get MIS reports by NGO
    
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<MISReport>> getMISReportsByNGO(@PathVariable Long ngoId) {
        List<MISReport> reports = misReportService.getMISReportsByNGO(ngoId);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
    // Approve MIS report
    
    @PutMapping("/{reportId}/approve")
    public ResponseEntity<Map<String, Object>> approveMISReport(
            @PathVariable Long reportId, 
            @RequestBody Map<String, Object> approvalData) {
        try {
            Long approverId = ((Number) approvalData.get("approverId")).longValue();
            String comments = (String) approvalData.get("comments");
            MISReport approvedReport = misReportService.approveMISReport(reportId, approverId, comments);
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Report approved successfully");
            response.put("report", approvedReport);
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (SecurityException e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Access denied: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Approval failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }
    
    // Get pending reports for approval
    
    @GetMapping("/pending-approval")
    public ResponseEntity<List<MISReport>> getPendingReports() {
        List<MISReport> pendingReports = misReportService.getPendingReports();
        return new ResponseEntity<>(pendingReports, HttpStatus.OK);
    }
    
    // Get reports by status
    
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MISReport>> getReportsByStatus(@PathVariable String status) {
        try {
            ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
            List<MISReport> reports = misReportService.getReportsByStatus(reportStatus);
            return new ResponseEntity<>(reports, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
}
