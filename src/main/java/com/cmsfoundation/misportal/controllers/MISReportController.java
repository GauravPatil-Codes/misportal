package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.services.MISReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
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
    
    @GetMapping("/project/{project}")
    public ResponseEntity<List<MISReport>> getReportsByProject(@PathVariable String project) {
        List<MISReport> reports = misReportService.getReportsByProject(project);
        return new ResponseEntity<>(reports, HttpStatus.OK);
    }
    
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
    
    @GetMapping("/count/project/{project}")
    public ResponseEntity<Long> countReportsByProject(@PathVariable String project) {
        Long count = misReportService.countReportsByProject(project);
        return new ResponseEntity<>(count, HttpStatus.OK);
    }
}