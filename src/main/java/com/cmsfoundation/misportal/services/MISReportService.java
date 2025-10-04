package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.dtos.MISReportSubmissionRequest;
import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.ReportStatus;

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
    
    List<MISReport> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate);
    List<MISReport> getReportsByAchievementRange(double minPercentage, double maxPercentage);
   
	MISReport submitMISReport(MISReportSubmissionRequest request);
	List<MISReport> getMISReportsByProject(Long projectId);
	MISReport approveMISReport(Long reportId, Long approverId, String comments);
	List<MISReport> getMISReportsByNGO(Long ngoId);
	List<MISReport> getPendingReports();
	List<MISReport> getReportsByStatus(ReportStatus status);
	
	// Replace with these signatures
	List<MISReport> getReportsByProjectByEntity(Project project);
	List<MISReport> getReportsByProjectId(Long projectId);

	Long countReportsByProjectByEntity(Project project);
	Long countReportsByProjectId(Long projectId);
	Long countByProjectId(Long projectId);

}