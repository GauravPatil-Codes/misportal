package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;

@Embeddable
public class Budget {
    @Column(name = "human_resources_cost")
    private Double humanResourcesCost = 0.0;
    
    @Column(name = "admin_cost")
    private Double adminCost = 0.0;
    
    @Column(name = "management_coordination_cost")
    private Double managementAndCoordinationCost = 0.0;
    
    @Column(name = "miscellaneous_cost")
    private Double miscellaneousCost = 0.0;
    
    @Column(name = "government_convergence_cost")
    private Double governmentConvergenceCost = 0.0;
    
    @Column(name = "total_budget")
    private Double totalBudget = 0.0;
    
    // Default constructor
    public Budget() {}

    // Getters
    public Double getHumanResourcesCost() { return humanResourcesCost; }
    public Double getAdminCost() { return adminCost; }
    public Double getManagementAndCoordinationCost() { return managementAndCoordinationCost; }
    public Double getMiscellaneousCost() { return miscellaneousCost; }
    public Double getGovernmentConvergenceCost() { return governmentConvergenceCost; }
    public Double getTotalBudget() { return totalBudget; }

    // Setters
    public void setHumanResourcesCost(Double humanResourcesCost) { this.humanResourcesCost = humanResourcesCost; }
    public void setAdminCost(Double adminCost) { this.adminCost = adminCost; }
    public void setManagementAndCoordinationCost(Double managementAndCoordinationCost) { this.managementAndCoordinationCost = managementAndCoordinationCost; }
    public void setMiscellaneousCost(Double miscellaneousCost) { this.miscellaneousCost = miscellaneousCost; }
    public void setGovernmentConvergenceCost(Double governmentConvergenceCost) { this.governmentConvergenceCost = governmentConvergenceCost; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }

    @Override
    public String toString() {
        return "Budget{" +
                "humanResourcesCost=" + humanResourcesCost +
                ", adminCost=" + adminCost +
                ", managementAndCoordinationCost=" + managementAndCoordinationCost +
                ", miscellaneousCost=" + miscellaneousCost +
                ", governmentConvergenceCost=" + governmentConvergenceCost +
                ", totalBudget=" + totalBudget +
                '}';
    }
}