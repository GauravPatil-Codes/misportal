package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "mis_reports")
public class MISReport {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "project")
    private String project;
    
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
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Default constructor
    public MISReport() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
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

	public MISReport(Long id, String project, String reportingPeriod, LocalDateTime misReportSubmissionDateTime,
			String completedTarget, String allocatedTarget, String deviationReport, String mitigationPlan,
			Attachments attachments, String achievementPercentage, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.project = project;
		this.reportingPeriod = reportingPeriod;
		this.misReportSubmissionDateTime = misReportSubmissionDateTime;
		this.completedTarget = completedTarget;
		this.allocatedTarget = allocatedTarget;
		this.deviationReport = deviationReport;
		this.mitigationPlan = mitigationPlan;
		this.attachments = attachments;
		this.achievementPercentage = achievementPercentage;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "MISReport [id=" + id + ", project=" + project + ", reportingPeriod=" + reportingPeriod
				+ ", misReportSubmissionDateTime=" + misReportSubmissionDateTime + ", completedTarget="
				+ completedTarget + ", allocatedTarget=" + allocatedTarget + ", deviationReport=" + deviationReport
				+ ", mitigationPlan=" + mitigationPlan + ", attachments=" + attachments + ", achievementPercentage="
				+ achievementPercentage + ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned
}

@Embeddable
class Attachments {
    @Column(name = "attachment_1")
    private String attachment1;
    
    @Column(name = "attachment_2")
    private String attachment2;
    
    @Column(name = "attachment_3")
    private String attachment3;
    
    // Default constructor
    public Attachments() {}
}