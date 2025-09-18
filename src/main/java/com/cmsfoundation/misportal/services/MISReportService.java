package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MISReport;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MISReportService {
    
    // CRUD Operations
    MISReport createMISReport(MISReport misReport);
    List<MISReport> getAllMISReports();
    Optional<MISReport> getMISReportById(Long id);
    MISReport updateMISReport(Long id, MISReport misReport);
    void deleteMISReport(Long id);
    
    // Business Operations
    List<MISReport> getReportsByProject(String project);
    List<MISReport> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MISReport> getReportsByAchievementRange(double minPercentage, double maxPercentage);
    Long countReportsByProject(String project);
}