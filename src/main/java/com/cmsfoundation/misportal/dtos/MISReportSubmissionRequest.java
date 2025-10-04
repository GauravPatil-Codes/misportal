package com.cmsfoundation.misportal.dtos;

import java.time.LocalDateTime;

public class MISReportSubmissionRequest {
    private Long projectId;
    private Long submittedByUserId;
    private String reportingPeriod;
    private String completedTarget;
    private String allocatedTarget;
    private String deviationReport;
    private String mitigationPlan;
    private String achievementPercentage;
    private String attachment1;
    private String attachment2;
    private String attachment3;
    
    public MISReportSubmissionRequest() {}
    
    public Long getProjectId() { return projectId; }
    public void setProjectId(Long projectId) { this.projectId = projectId; }
    
    public Long getSubmittedByUserId() { return submittedByUserId; }
    public void setSubmittedByUserId(Long submittedByUserId) { this.submittedByUserId = submittedByUserId; }
    
    public String getReportingPeriod() { return reportingPeriod; }
    public void setReportingPeriod(String reportingPeriod) { this.reportingPeriod = reportingPeriod; }
    
    public String getCompletedTarget() { return completedTarget; }
    public void setCompletedTarget(String completedTarget) { this.completedTarget = completedTarget; }
    
    public String getAllocatedTarget() { return allocatedTarget; }
    public void setAllocatedTarget(String allocatedTarget) { this.allocatedTarget = allocatedTarget; }
    
    public String getDeviationReport() { return deviationReport; }
    public void setDeviationReport(String deviationReport) { this.deviationReport = deviationReport; }
    
    public String getMitigationPlan() { return mitigationPlan; }
    public void setMitigationPlan(String mitigationPlan) { this.mitigationPlan = mitigationPlan; }
    
    public String getAchievementPercentage() { return achievementPercentage; }
    public void setAchievementPercentage(String achievementPercentage) { this.achievementPercentage = achievementPercentage; }
    
    public String getAttachment1() { return attachment1; }
    public void setAttachment1(String attachment1) { this.attachment1 = attachment1; }
    
    public String getAttachment2() { return attachment2; }
    public void setAttachment2(String attachment2) { this.attachment2 = attachment2; }
    
    public String getAttachment3() { return attachment3; }
    public void setAttachment3(String attachment3) { this.attachment3 = attachment3; }
}