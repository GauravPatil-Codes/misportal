package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;

@Embeddable
public class BudgetAllocationMatrix {
    @Column(name = "sr_no")
    private String srNo;
    
    @Column(name = "Name")
    private String Name;
    
    @Column(name = "description")
    private String description;
    
    @Column(name = "unit")
    private Integer unit;
    
    @Column(name = "unit_cost")
    private Integer unitCost;
    
    @Column(name = "unit_total")
    private Integer total;
    
    @Column(name = "cms_contribution")
    private Integer cmsContri;
    
    @Column(name = "ngo_contribution")
    private Integer ngoContri;
    
    @Column(name = "beneficiary_contribution")
    private Integer beneficiaryContri;
    
    @Column(name = "government_contribution")
    private Integer governmentContri;
    
    @Column(name = "budget_type")
    private String budget_type;

    public String getSrNo() { return srNo; }
    public void setSrNo(String srNo) { this.srNo = srNo; }
    
    public String getName() { return Name; }
    public void setName(String name) { Name = name; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public Integer getUnit() { return unit; }
    public void setUnit(Integer unit) { this.unit = unit; }
    
    public Integer getUnitCost() { return unitCost; }
    public void setUnitCost(Integer unitCost) { this.unitCost = unitCost; }
    
    public Integer getTotal() { return total; }
    public void setTotal(Integer total) { this.total = total; }
    
    public Integer getCmsContri() { return cmsContri; }
    public void setCmsContri(Integer cmsContri) { this.cmsContri = cmsContri; }
    
    public Integer getNgoContri() { return ngoContri; }
    public void setNgoContri(Integer ngoContri) { this.ngoContri = ngoContri; }
    
    public Integer getBeneficiaryContri() { return beneficiaryContri; }
    public void setBeneficiaryContri(Integer beneficiaryContri) { this.beneficiaryContri = beneficiaryContri; }
    
    public Integer getGovernmentContri() { return governmentContri; }
    public void setGovernmentContri(Integer governmentContri) { this.governmentContri = governmentContri; }
    
    public String getBudget_type() { return budget_type; }
    public void setBudget_type(String budget_type) { this.budget_type = budget_type; }

    public BudgetAllocationMatrix(String srNo, String name, String description, Integer unit, Integer unitCost,
            Integer total, Integer cmsContri, Integer ngoContri, Integer beneficiaryContri, Integer governmentContri,
            String budget_type) {
        super();
        this.srNo = srNo;
        this.Name = name;
        this.description = description;
        this.unit = unit;
        this.unitCost = unitCost;
        this.total = total;
        this.cmsContri = cmsContri;
        this.ngoContri = ngoContri;
        this.beneficiaryContri = beneficiaryContri;
        this.governmentContri = governmentContri;
        this.budget_type = budget_type;
    }

    public BudgetAllocationMatrix() {
        super();
    }

    @Override
    public String toString() {
        return "BudgetAllocationMatrix [srNo=" + srNo + ", Name=" + Name + ", description=" + description + ", unit="
                + unit + ", unitCost=" + unitCost + ", total=" + total + ", cmsContri=" + cmsContri + ", ngoContri="
                + ngoContri + ", beneficiaryContri=" + beneficiaryContri + ", governmentContri=" + governmentContri
                + ", budget_type=" + budget_type + "]";
    }
}