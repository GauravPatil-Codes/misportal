package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "mis_reports")
public class MISReport {
    
	
	    
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    private Long id;
	    
	    // ✅ ENHANCED: Direct project relationship
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "project_id", nullable = false)
	    @JsonBackReference("project-misReports")
	    private Project project;
	    
	    // ✅ NEW: Submitted by user relationship
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "submitted_by_user_id", nullable = false)
	    private User submittedBy;
	    
	    // ✅ NEW: Report status
	    @Enumerated(EnumType.STRING)
	    @Column(name = "report_status")
	    private ReportStatus reportStatus = ReportStatus.DRAFT;
	    
	    @Column(name = "reporting_period")
	    private String reportingPeriod;
	    
	    @Column(name = "submission_date_time")
	    private LocalDateTime misReportSubmissionDateTime;
	    
	    @Column(name = "completed_target")
	    private String completedTarget;
	    
	    @Column(name = "allocated_target")
	    private String allocatedTarget;
	    
	    @Column(name = "deviation_report", length = 5000)
	    private String deviationReport;
	    
	    @Column(name = "mitigation_plan", length = 5000)
	    private String mitigationPlan;
	    
	    @Embedded
	    private Attachments attachments;
	    
	    @Column(name = "achievement_percentage")
	    private String achievementPercentage;
	    
	    // ✅ NEW: Approval workflow fields
	    @ManyToOne(fetch = FetchType.LAZY)
	    @JoinColumn(name = "approved_by_user_id")
	    private User approvedBy;
	    
	    @Column(name = "approval_date")
	    private LocalDateTime approvalDate;
	    
	    @Column(name = "approval_comments", length = 2000)
	    private String approvalComments;
	    
	    @Column(name = "created_at")
	    private LocalDateTime createdAt;
	    
	    @Column(name = "updated_at")
	    private LocalDateTime updatedAt;
	    
	    // ✅ NEW: Business methods
	    public boolean canBeEditedBy(User user) {
	        if (reportStatus == ReportStatus.APPROVED) return false;
	        if (user.getUserRole() == UserRole.CMS_SUPER_ADMIN) return true;
	        return submittedBy.getId().equals(user.getId());
	    }
	    
	    public void submitForApproval() {
	        this.reportStatus = ReportStatus.PENDING_APPROVAL;
	        this.misReportSubmissionDateTime = LocalDateTime.now();
	    }
	    
	    public void approve(User approver, String comments) {
	        this.reportStatus = ReportStatus.APPROVED;
	        this.approvedBy = approver;
	        this.approvalDate = LocalDateTime.now();
	        this.approvalComments = comments;
	    }
	    
	    // Getters and setters for new fields
	    public Project getProject() { return project; }
	    public void setProject(Project project) { this.project = project; }
	    
	    public User getSubmittedBy() { return submittedBy; }
	    public void setSubmittedBy(User submittedBy) { this.submittedBy = submittedBy; }
	    
	    public ReportStatus getReportStatus() { return reportStatus; }
	    public void setReportStatus(ReportStatus reportStatus) { this.reportStatus = reportStatus; }
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getReportingPeriod() {
			return reportingPeriod;
		}

		public void setReportingPeriod(String reportingPeriod) {
			this.reportingPeriod = reportingPeriod;
		}

		public LocalDateTime getMisReportSubmissionDateTime() {
			return misReportSubmissionDateTime;
		}

		public void setMisReportSubmissionDateTime(LocalDateTime misReportSubmissionDateTime) {
			this.misReportSubmissionDateTime = misReportSubmissionDateTime;
		}

		public String getCompletedTarget() {
			return completedTarget;
		}

		public void setCompletedTarget(String completedTarget) {
			this.completedTarget = completedTarget;
		}

		public String getAllocatedTarget() {
			return allocatedTarget;
		}

		public void setAllocatedTarget(String allocatedTarget) {
			this.allocatedTarget = allocatedTarget;
		}

		public String getDeviationReport() {
			return deviationReport;
		}

		public void setDeviationReport(String deviationReport) {
			this.deviationReport = deviationReport;
		}

		public String getMitigationPlan() {
			return mitigationPlan;
		}

		public void setMitigationPlan(String mitigationPlan) {
			this.mitigationPlan = mitigationPlan;
		}

		public Attachments getAttachments() {
			return attachments;
		}

		public void setAttachments(Attachments attachments) {
			this.attachments = attachments;
		}

		public String getAchievementPercentage() {
			return achievementPercentage;
		}

		public void setAchievementPercentage(String achievementPercentage) {
			this.achievementPercentage = achievementPercentage;
		}

		public User getApprovedBy() {
			return approvedBy;
		}

		public void setApprovedBy(User approvedBy) {
			this.approvedBy = approvedBy;
		}

		public LocalDateTime getApprovalDate() {
			return approvalDate;
		}

		public void setApprovalDate(LocalDateTime approvalDate) {
			this.approvalDate = approvalDate;
		}

		public String getApprovalComments() {
			return approvalComments;
		}

		public void setApprovalComments(String approvalComments) {
			this.approvalComments = approvalComments;
		}

		public LocalDateTime getCreatedAt() {
			return createdAt;
		}

		public void setCreatedAt(LocalDateTime createdAt) {
			this.createdAt = createdAt;
		}

		public LocalDateTime getUpdatedAt() {
			return updatedAt;
		}

		public void setUpdatedAt(LocalDateTime updatedAt) {
			this.updatedAt = updatedAt;
		}

		public MISReport(Long id, Project project, User submittedBy, ReportStatus reportStatus, String reportingPeriod,
				LocalDateTime misReportSubmissionDateTime, String completedTarget, String allocatedTarget,
				String deviationReport, String mitigationPlan, Attachments attachments, String achievementPercentage,
				User approvedBy, LocalDateTime approvalDate, String approvalComments, LocalDateTime createdAt,
				LocalDateTime updatedAt) {
			super();
			this.id = id;
			this.project = project;
			this.submittedBy = submittedBy;
			this.reportStatus = reportStatus;
			this.reportingPeriod = reportingPeriod;
			this.misReportSubmissionDateTime = misReportSubmissionDateTime;
			this.completedTarget = completedTarget;
			this.allocatedTarget = allocatedTarget;
			this.deviationReport = deviationReport;
			this.mitigationPlan = mitigationPlan;
			this.attachments = attachments;
			this.achievementPercentage = achievementPercentage;
			this.approvedBy = approvedBy;
			this.approvalDate = approvalDate;
			this.approvalComments = approvalComments;
			this.createdAt = createdAt;
			this.updatedAt = updatedAt;
		}

		@Override
		public String toString() {
			return "MISReport [id=" + id + ", project=" + project + ", submittedBy=" + submittedBy
					+ ", reportingPeriod=" + reportingPeriod + ", misReportSubmissionDateTime="
					+ misReportSubmissionDateTime + ", completedTarget=" + completedTarget + ", allocatedTarget="
					+ allocatedTarget + ", deviationReport=" + deviationReport + ", mitigationPlan=" + mitigationPlan
					+ ", attachments=" + attachments + ", achievementPercentage=" + achievementPercentage
					+ ", approvedBy=" + approvedBy + ", approvalDate=" + approvalDate + ", approvalComments="
					+ approvalComments + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
		}

		public MISReport() {
			super();
			// TODO Auto-generated constructor stub
		}



}

