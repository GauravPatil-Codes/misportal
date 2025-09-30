package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;

@Embeddable
public class WorkPlan {
    @Column(name = "work_plan_details", columnDefinition = "TEXT")
    private String workPlanDetails;
    
    // Default constructor
    public WorkPlan() {}

    public String getWorkPlanDetails() { return workPlanDetails; }
    public void setWorkPlanDetails(String workPlanDetails) { this.workPlanDetails = workPlanDetails; }

    @Override
    public String toString() {
        return "WorkPlan{" +
                "workPlanDetails='" + workPlanDetails + '\'' +
                '}';
    }
}