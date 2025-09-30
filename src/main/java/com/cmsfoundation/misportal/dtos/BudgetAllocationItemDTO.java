package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.BudgetHead;

public class BudgetAllocationItemDTO {
    private String srNo;
    private String itemName;
    private String description;
    private Integer units;
    private Double unitCost;
    private Double cmsContribution;
    private Double ngoContribution;
    private Double governmentContribution;
    private Double beneficiaryContribution;
    private BudgetHead budgetType;
    
    // Default constructor
    public BudgetAllocationItemDTO() {}
    
    // Getters and setters
    public String getSrNo() { return srNo; }
    public void setSrNo(String srNo) { this.srNo = srNo; }
    
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getUnits() { return units; }
    public void setUnits(Integer units) { this.units = units; }
    
    public Double getUnitCost() { return unitCost; }
    public void setUnitCost(Double unitCost) { this.unitCost = unitCost; }
    
    public Double getCmsContribution() { return cmsContribution; }
    public void setCmsContribution(Double cmsContribution) { this.cmsContribution = cmsContribution; }
    
    public Double getNgoContribution() { return ngoContribution; }
    public void setNgoContribution(Double ngoContribution) { this.ngoContribution = ngoContribution; }
    
    public Double getGovernmentContribution() { return governmentContribution; }
    public void setGovernmentContribution(Double governmentContribution) { this.governmentContribution = governmentContribution; }
    
    public Double getBeneficiaryContribution() { return beneficiaryContribution; }
    public void setBeneficiaryContribution(Double beneficiaryContribution) { this.beneficiaryContribution = beneficiaryContribution; }
    
    public BudgetHead getBudgetType() { return budgetType; }
    public void setBudgetType(BudgetHead budgetType) { this.budgetType = budgetType; }
}