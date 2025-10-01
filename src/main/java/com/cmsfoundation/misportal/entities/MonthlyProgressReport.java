package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "monthly_progress_reports")
public class MonthlyProgressReport {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;

	// ✅ REPLACE with (if linking to specific target)
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "monthly_target_id")
	private MonthlyTarget monthlyTarget;

	@Column(name = "reporting_month")
	private LocalDate reportingMonth;

	@Column(name = "achieved_target")
	private Integer achievedTarget;

	@Column(name = "deviation")
	private Integer deviation; // Calculated as plannedTarget - achievedTarget

	@Column(name = "deviation_reason", columnDefinition = "TEXT")
	private String deviationReason;

	@Column(name = "mitigation_plan", columnDefinition = "TEXT")
	private String mitigationPlan;

	@Column(name = "next_month_adjusted_target")
	private Integer nextMonthAdjustedTarget;

	@Column(name = "achievement_percentage")
	private Double achievementPercentage; // Calculated as (achievedTarget/plannedTarget) * 100

	@Column(name = "submitted_at")
	private LocalDateTime submittedAt;

	@Column(name = "created_at")
	private LocalDateTime createdAt;

	@Column(name = "updated_at")
	private LocalDateTime updatedAt;

	@PrePersist
	protected void onCreate() {
		createdAt = LocalDateTime.now();
		updatedAt = LocalDateTime.now();
		calculateDeviationAndPercentage();
	}

	@PreUpdate
	protected void onUpdate() {
		updatedAt = LocalDateTime.now();
		calculateDeviationAndPercentage();
	}

	private void calculateDeviationAndPercentage() {
	    if (monthlyTarget != null && achievedTarget != null) {
	        Integer plannedTarget = monthlyTarget.getPlannedTarget();
	        if (plannedTarget != null && plannedTarget > 0) {
	            this.deviation = plannedTarget - achievedTarget;
	            this.achievementPercentage = (achievedTarget.doubleValue() / plannedTarget.doubleValue()) * 100;
	        }
	    }
	}


	// Default constructor
	public MonthlyProgressReport() {
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}



	public LocalDate getReportingMonth() {
		return reportingMonth;
	}

	public void setReportingMonth(LocalDate reportingMonth) {
		this.reportingMonth = reportingMonth;
	}

	public Integer getAchievedTarget() {
		return achievedTarget;
	}

	public void setAchievedTarget(Integer achievedTarget) {
		this.achievedTarget = achievedTarget;
	}

	public Integer getDeviation() {
		return deviation;
	}

	public void setDeviation(Integer deviation) {
		this.deviation = deviation;
	}

	public String getDeviationReason() {
		return deviationReason;
	}

	public void setDeviationReason(String deviationReason) {
		this.deviationReason = deviationReason;
	}

	public String getMitigationPlan() {
		return mitigationPlan;
	}

	public void setMitigationPlan(String mitigationPlan) {
		this.mitigationPlan = mitigationPlan;
	}

	public Integer getNextMonthAdjustedTarget() {
		return nextMonthAdjustedTarget;
	}

	public void setNextMonthAdjustedTarget(Integer nextMonthAdjustedTarget) {
		this.nextMonthAdjustedTarget = nextMonthAdjustedTarget;
	}

	public Double getAchievementPercentage() {
		return achievementPercentage;
	}

	public void setAchievementPercentage(Double achievementPercentage) {
		this.achievementPercentage = achievementPercentage;
	}

	public LocalDateTime getSubmittedAt() {
		return submittedAt;
	}

	public void setSubmittedAt(LocalDateTime submittedAt) {
		this.submittedAt = submittedAt;
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
}