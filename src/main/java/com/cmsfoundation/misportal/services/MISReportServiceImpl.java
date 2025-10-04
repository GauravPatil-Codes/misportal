package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.dtos.MISReportSubmissionRequest;
import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.ReportStatus;
import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.entities.UserRole;
import com.cmsfoundation.misportal.repositories.MISReportRepository;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.UserRepository;
import com.cmsfoundation.misportal.services.MISReportService;
import com.cmsfoundation.misportal.entities.Attachments;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MISReportServiceImpl implements MISReportService {
    
    @Autowired
    private MISReportRepository misReportRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Override
    public MISReport createMISReport(MISReport misReport) {
        return misReportRepository.save(misReport);
    }
    
    @Override
    public List<MISReport> getAllMISReports() {
        return misReportRepository.findAll();
    }
    
    @Override
    public Optional<MISReport> getMISReportById(Long id) {
        return misReportRepository.findById(id);
    }
    
    @Override
    public MISReport updateMISReport(Long id, MISReport misReport) {

        MISReport existing = misReportRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("MIS Report not found with id: " + id));

        if (misReport.getProject() != null) existing.setProject(misReport.getProject());
        if (misReport.getReportingPeriod() != null) existing.setReportingPeriod(misReport.getReportingPeriod());
        if (misReport.getMisReportSubmissionDateTime() != null)
            existing.setMisReportSubmissionDateTime(misReport.getMisReportSubmissionDateTime());
        if (misReport.getCompletedTarget() != null) existing.setCompletedTarget(misReport.getCompletedTarget());
        if (misReport.getAllocatedTarget() != null) existing.setAllocatedTarget(misReport.getAllocatedTarget());
        if (misReport.getDeviationReport() != null) existing.setDeviationReport(misReport.getDeviationReport());
        if (misReport.getMitigationPlan() != null) existing.setMitigationPlan(misReport.getMitigationPlan());
        if (misReport.getAttachments() != null) existing.setAttachments(misReport.getAttachments());
        if (misReport.getAchievementPercentage() != null) existing.setAchievementPercentage(misReport.getAchievementPercentage());
        if (misReport.getCreatedAt() != null) existing.setCreatedAt(misReport.getCreatedAt());
        if (misReport.getUpdatedAt() != null) existing.setUpdatedAt(misReport.getUpdatedAt());

        return misReportRepository.save(existing);
    }


    @Override
    public void deleteMISReport(Long id) {
        if (misReportRepository.existsById(id)) {
            misReportRepository.deleteById(id);
        } else {
            throw new RuntimeException("MIS Report not found with id: " + id);
        }
    }
    
  
    @Override
    public List<MISReport> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return misReportRepository.getReportsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<MISReport> getReportsByAchievementRange(double minPercentage, double maxPercentage) {
        return misReportRepository.getReportsByAchievementRange(minPercentage, maxPercentage);
    }
    
    @Override
    public List<MISReport> getReportsByProjectByEntity(Project project) {
        return misReportRepository.findByProject(project);
    }

    @Override
    public List<MISReport> getReportsByProjectId(Long projectId) {
        return misReportRepository.findByProjectId(projectId);
    }

    @Override
    public Long countReportsByProjectByEntity(Project project) {
        return misReportRepository.countReportsByProject(project);
    }

    @Override
    public Long countByProjectId(Long projectId) {
        return misReportRepository.countByProjectId(projectId);
    }

    
    
 // ✅ NEW: Submit MIS Report with validation
    @Override
    @Transactional
    public MISReport submitMISReport(MISReportSubmissionRequest request) {
        // Validate project exists
        Project project = projectRepository.findById(request.getProjectId())
            .orElseThrow(() -> new RuntimeException("Project not found"));
        
        // Validate user exists and has permission
        User user = userRepository.findById(request.getSubmittedByUserId())
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Check if user can submit report for this project
        if (!user.canSubmitMISReport(project)) {
            throw new SecurityException("User does not have permission to submit report for this project");
        }
        
        // Create MIS Report
        MISReport report = new MISReport();
        report.setProject(project);
        report.setSubmittedBy(user);
        report.setReportingPeriod(request.getReportingPeriod());
        report.setCompletedTarget(request.getCompletedTarget());
        report.setAllocatedTarget(request.getAllocatedTarget());
        report.setDeviationReport(request.getDeviationReport());
        report.setMitigationPlan(request.getMitigationPlan());
        report.setAchievementPercentage(request.getAchievementPercentage());
        
        // Set attachments
        Attachments attachments = new Attachments();
        attachments.setAttachment1(request.getAttachment1());
        attachments.setAttachment2(request.getAttachment2());
        attachments.setAttachment3(request.getAttachment3());
        report.setAttachments(attachments);
        
        // Submit for approval
        report.submitForApproval();
        
        return misReportRepository.save(report);
    }
    
    // ✅ NEW: Approve MIS Report
    @Override
    @Transactional
    public MISReport approveMISReport(Long reportId, Long approverId, String comments) {
        MISReport report = misReportRepository.findById(reportId)
            .orElseThrow(() -> new RuntimeException("Report not found"));
        
        User approver = userRepository.findById(approverId)
            .orElseThrow(() -> new RuntimeException("Approver not found"));
        
        // Check if user can approve reports
        if (approver.getUserRole() != UserRole.CMS_SUPER_ADMIN && 
            approver.getUserRole() != UserRole.CMS_PROJECT_MANAGER) {
            throw new SecurityException("User does not have permission to approve reports");
        }
        
        report.approve(approver, comments);
        return misReportRepository.save(report);
    }
    
    // ✅ NEW: Get reports by project
    @Override
    public List<MISReport> getMISReportsByProject(Long projectId) {
        return misReportRepository.findByProjectId(projectId);
    }
    
    // ✅ NEW: Get reports by NGO
    @Override
    public List<MISReport> getMISReportsByNGO(Long ngoId) {
        return misReportRepository.findByProjectNgoPartnerId(ngoId);
    }
    
    // ✅ NEW: Get pending reports
    @Override
    public List<MISReport> getPendingReports() {
        return misReportRepository.findByReportStatus(ReportStatus.PENDING_APPROVAL);
    }
    
    // ✅ NEW: Get reports by status
    @Override
    public List<MISReport> getReportsByStatus(ReportStatus status) {
        return misReportRepository.findByReportStatus(status);
    }

	@Override
	public Long countReportsByProjectId(Long projectId) {
		// TODO Auto-generated method stub
		return  misReportRepository.countByProjectId(projectId);
	}
}