package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;

@Embeddable
public class Budget {
    
    // All 8 Budget Heads - calculated from BudgetAllocationItems
    @Column(name = "procurement_cost")
    private Double procurementCost = 0.0;
    
    @Column(name = "training_cost")
    private Double trainingCost = 0.0;
    
    @Column(name = "civil_construction_cost")
    private Double civilConstructionCost = 0.0;
    
    @Column(name = "contingency_miscellaneous_cost")
    private Double contingencyMiscellaneousCost = 0.0;
    
    @Column(name = "human_resources_cost")
    private Double humanResourcesCost = 0.0;
    
    @Column(name = "admin_cost")
    private Double adminCost = 0.0;
    
    @Column(name = "management_coordination_cost")
    private Double managementAndCoordinationCost = 0.0;
    
    @Column(name = "government_convergence_cost")
    private Double governmentConvergenceCost = 0.0;
    
    @Column(name = "total_budget")
    private Double totalBudget = 0.0;
    
    // Additional summary fields
    @Column(name = "total_cms_contribution")
    private Double totalCmsContribution = 0.0;
    
    @Column(name = "total_ngo_contribution")
    private Double totalNgoContribution = 0.0;
    
    @Column(name = "total_government_contribution")
    private Double totalGovernmentContribution = 0.0;
    
    @Column(name = "total_beneficiary_contribution")
    private Double totalBeneficiaryContribution = 0.0;
    
    // Default constructor
    public Budget() {}
    
    // Method to calculate totals from BudgetAllocationItems
    public void calculateTotalsFromItems(java.util.List<BudgetAllocationItem> budgetItems) {
        if (budgetItems == null || budgetItems.isEmpty()) {
            resetAllTotals();
            return;
        }
        
        // Reset all totals first
        resetAllTotals();
        
        // Calculate totals by budget head
        for (BudgetAllocationItem item : budgetItems) {
            Double itemTotal = item.getTotal() != null ? item.getTotal() : 0.0;
            Double itemCms = item.getCmsContribution() != null ? item.getCmsContribution() : 0.0;
            Double itemNgo = item.getNgoContribution() != null ? item.getNgoContribution() : 0.0;
            Double itemGov = item.getGovernmentContribution() != null ? item.getGovernmentContribution() : 0.0;
            Double itemBen = item.getBeneficiaryContribution() != null ? item.getBeneficiaryContribution() : 0.0;
            
            // Add to respective budget head
            switch (item.getBudgetType()) {
                case PROCUREMENT_COST:
                    this.procurementCost += itemTotal;
                    break;
                case TRAINING_COST:
                    this.trainingCost += itemTotal;
                    break;
                case CIVIL_CONSTRUCTION_COST:
                    this.civilConstructionCost += itemTotal;
                    break;
                case CONTINGENCY_MISCELLANEOUS_COST:
                    this.contingencyMiscellaneousCost += itemTotal;
                    break;
                case HR_COST:
                    this.humanResourcesCost += itemTotal;
                    break;
                case ADMIN_COST:
                    this.adminCost += itemTotal;
                    break;
                case MANAGEMENT_COORDINATION_COST:
                    this.managementAndCoordinationCost += itemTotal;
                    break;
                case GOVERNMENT_CONVERGENCE_COST:
                    this.governmentConvergenceCost += itemTotal;
                    break;
            }
            
            // Add to contribution totals
            this.totalCmsContribution += itemCms;
            this.totalNgoContribution += itemNgo;
            this.totalGovernmentContribution += itemGov;
            this.totalBeneficiaryContribution += itemBen;
        }
        
        // Calculate total budget
        this.totalBudget = this.procurementCost + this.trainingCost + this.civilConstructionCost + 
                          this.contingencyMiscellaneousCost + this.humanResourcesCost + this.adminCost + 
                          this.managementAndCoordinationCost + this.governmentConvergenceCost;
    }
    
    private void resetAllTotals() {
        this.procurementCost = 0.0;
        this.trainingCost = 0.0;
        this.civilConstructionCost = 0.0;
        this.contingencyMiscellaneousCost = 0.0;
        this.humanResourcesCost = 0.0;
        this.adminCost = 0.0;
        this.managementAndCoordinationCost = 0.0;
        this.governmentConvergenceCost = 0.0;
        this.totalBudget = 0.0;
        this.totalCmsContribution = 0.0;
        this.totalNgoContribution = 0.0;
        this.totalGovernmentContribution = 0.0;
        this.totalBeneficiaryContribution = 0.0;
    }

    // All Getters and Setters
    public Double getProcurementCost() { return procurementCost; }
    public void setProcurementCost(Double procurementCost) { this.procurementCost = procurementCost; }
    
    public Double getTrainingCost() { return trainingCost; }
    public void setTrainingCost(Double trainingCost) { this.trainingCost = trainingCost; }
    
    public Double getCivilConstructionCost() { return civilConstructionCost; }
    public void setCivilConstructionCost(Double civilConstructionCost) { this.civilConstructionCost = civilConstructionCost; }
    
    public Double getContingencyMiscellaneousCost() { return contingencyMiscellaneousCost; }
    public void setContingencyMiscellaneousCost(Double contingencyMiscellaneousCost) { this.contingencyMiscellaneousCost = contingencyMiscellaneousCost; }
    
    public Double getHumanResourcesCost() { return humanResourcesCost; }
    public void setHumanResourcesCost(Double humanResourcesCost) { this.humanResourcesCost = humanResourcesCost; }
    
    public Double getAdminCost() { return adminCost; }
    public void setAdminCost(Double adminCost) { this.adminCost = adminCost; }
    
    public Double getManagementAndCoordinationCost() { return managementAndCoordinationCost; }
    public void setManagementAndCoordinationCost(Double managementAndCoordinationCost) { this.managementAndCoordinationCost = managementAndCoordinationCost; }
    
    public Double getGovernmentConvergenceCost() { return governmentConvergenceCost; }
    public void setGovernmentConvergenceCost(Double governmentConvergenceCost) { this.governmentConvergenceCost = governmentConvergenceCost; }
    
    public Double getTotalBudget() { return totalBudget; }
    public void setTotalBudget(Double totalBudget) { this.totalBudget = totalBudget; }
    
    public Double getTotalCmsContribution() { return totalCmsContribution; }
    public void setTotalCmsContribution(Double totalCmsContribution) { this.totalCmsContribution = totalCmsContribution; }
    
    public Double getTotalNgoContribution() { return totalNgoContribution; }
    public void setTotalNgoContribution(Double totalNgoContribution) { this.totalNgoContribution = totalNgoContribution; }
    
    public Double getTotalGovernmentContribution() { return totalGovernmentContribution; }
    public void setTotalGovernmentContribution(Double totalGovernmentContribution) { this.totalGovernmentContribution = totalGovernmentContribution; }
    
    public Double getTotalBeneficiaryContribution() { return totalBeneficiaryContribution; }
    public void setTotalBeneficiaryContribution(Double totalBeneficiaryContribution) { this.totalBeneficiaryContribution = totalBeneficiaryContribution; }

    @Override
    public String toString() {
        return "Budget{" +
                "procurementCost=" + procurementCost +
                ", trainingCost=" + trainingCost +
                ", civilConstructionCost=" + civilConstructionCost +
                ", contingencyMiscellaneousCost=" + contingencyMiscellaneousCost +
                ", humanResourcesCost=" + humanResourcesCost +
                ", adminCost=" + adminCost +
                ", managementAndCoordinationCost=" + managementAndCoordinationCost +
                ", governmentConvergenceCost=" + governmentConvergenceCost +
                ", totalBudget=" + totalBudget +
                '}';
    }
}