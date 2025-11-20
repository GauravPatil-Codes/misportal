package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.dtos.MISReportSubmissionRequest;
import com.cmsfoundation.misportal.entities.Attachments;
import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.ReportStatus;
import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.UserRepository;
import com.cmsfoundation.misportal.services.MISReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/mis-reports")
public class MISReportController {

    @Autowired
    private MISReportService misReportService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    // List all reports
    @GetMapping
    public ResponseEntity<List<MISReport>> getAllMISReports() {
        return new ResponseEntity<>(misReportService.getAllMISReports(), HttpStatus.OK);
    }

    // MIS Report List with filtering options
    @GetMapping("/list")
    public ResponseEntity<Map<String, Object>> getMISReportList(
            @RequestParam(required = false) Long projectId,
            @RequestParam(required = false) Long ngoId,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<MISReport> reports;
            
            if (projectId != null) {
                reports = misReportService.getMISReportsByProject(projectId);
            } else if (ngoId != null) {
                reports = misReportService.getMISReportsByNGO(ngoId);
            } else if (status != null && !status.isEmpty()) {
                ReportStatus reportStatus = ReportStatus.valueOf(status.toUpperCase());
                reports = misReportService.getReportsByStatus(reportStatus);
            } else {
                reports = misReportService.getAllMISReports();
            }
            
            // Simple pagination (in a real app, you'd use Pageable)
            int start = Math.min(page * size, reports.size());
            int end = Math.min(start + size, reports.size());
            List<MISReport> paginatedReports = reports.subList(start, end);
            
            response.put("reports", paginatedReports);
            response.put("currentPage", page);
            response.put("pageSize", size);
            response.put("totalElements", reports.size());
            response.put("totalPages", (int) Math.ceil((double) reports.size() / size));
            response.put("success", true);
            
            return new ResponseEntity<>(response, HttpStatus.OK);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("error", "Failed to fetch MIS reports: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Get single report
    @GetMapping("/{id}")
    public ResponseEntity<MISReport> getMISReportById(@PathVariable Long id) {
        return misReportService.getMISReportById(id)
            .map(r -> new ResponseEntity<>(r, HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/submit")
    public ResponseEntity<MISReport> submitMISReport(@RequestBody MISReportSubmissionRequest req) {
        try {
            MISReport saved = misReportService.submitMISReport(req);
            return new ResponseEntity<>(saved, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }


    // Approve a report
    @PutMapping("/{id}/approve")
    public ResponseEntity<Map<String,Object>> approveMISReport(
            @PathVariable Long id,
            @RequestBody Map<String,Object> data) {
        Long approverId = ((Number)data.get("approverId")).longValue();
        String comments = (String)data.get("comments");
        MISReport approved = misReportService.approveMISReport(id, approverId, comments);
        Map<String,Object> resp = new HashMap<>();
        resp.put("success", true);
        resp.put("report", approved);
        return new ResponseEntity<>(resp, HttpStatus.OK);
    }

    // Get pending reports
    @GetMapping("/pending-approval")
    public ResponseEntity<List<MISReport>> getPendingReports() {
        return new ResponseEntity<>(misReportService.getPendingReports(), HttpStatus.OK);
    }

    // Get by status
    @GetMapping("/status/{status}")
    public ResponseEntity<List<MISReport>> getReportsByStatus(@PathVariable String status) {
        ReportStatus rs = ReportStatus.valueOf(status.toUpperCase());
        return new ResponseEntity<>(misReportService.getReportsByStatus(rs), HttpStatus.OK);
    }

    // Get by project
    @GetMapping("/project/{projectId}")
    public ResponseEntity<List<MISReport>> getByProject(@PathVariable Long projectId) {
        return new ResponseEntity<>(misReportService.getMISReportsByProject(projectId), HttpStatus.OK);
    }

    // Get by NGO
    @GetMapping("/ngo/{ngoId}")
    public ResponseEntity<List<MISReport>> getByNGO(@PathVariable Long ngoId) {
        return new ResponseEntity<>(misReportService.getMISReportsByNGO(ngoId), HttpStatus.OK);
    }
}
