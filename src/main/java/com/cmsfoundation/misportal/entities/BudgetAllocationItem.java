package com.cmsfoundation.misportal.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "budget_allocation_items")
public class BudgetAllocationItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // MANY-TO-ONE relationship with Project
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id", nullable = false)
    @JsonBackReference("project-budgetItems")
    private Project project;
    
    @Column(name = "sr_no")
    private String srNo;
    
    @Column(name = "item_name")
    private String itemName;
    
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "units")
    private Integer units;
    
    @Column(name = "unit_cost")
    private Double unitCost;
    
    @Column(name = "total")
    private Double total; // Auto-calculated: units * unitCost
    
    @Column(name = "cms_contribution")
    private Double cmsContribution;
    
    @Column(name = "ngo_contribution")
    private Double ngoContribution;
    
    @Column(name = "government_contribution")
    private Double governmentContribution;
    
    @Column(name = "beneficiary_contribution")
    private Double beneficiaryContribution;
    
    // Each item is categorized under one of the 8 budget heads
    @Column(name = "budget_type")
    @Enumerated(EnumType.STRING)
    private BudgetHead budgetType;
    
    // NEW: One-to-Many relationship with MonthlyTargets
    // Each budget item has monthly targets throughout project lifecycle
    @OneToMany(mappedBy = "budgetAllocationItem", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("budgetItem-monthlyTargets")
    private List<MonthlyTarget> monthlyTargets;
    
    // Default constructor
    public BudgetAllocationItem() {}
    
    // Auto-calculate total before saving
    @PrePersist
    @PreUpdate
    private void calculateTotal() {
        if (units != null && unitCost != null) {
            this.total = units * unitCost;
        }
    }

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Project getProject() { return project; }
    public void setProject(Project project) { this.project = project; }
    
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
    
    public Double getTotal() { return total; }
    public void setTotal(Double total) { this.total = total; }
    
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
    
    public List<MonthlyTarget> getMonthlyTargets() { return monthlyTargets; }
    public void setMonthlyTargets(List<MonthlyTarget> monthlyTargets) { this.monthlyTargets = monthlyTargets; }

    @Override
    public String toString() {
        return "BudgetAllocationItem{" +
                "id=" + id +
                ", srNo='" + srNo + '\'' +
                ", itemName='" + itemName + '\'' +
                ", description='" + description + '\'' +
                ", units=" + units +
                ", unitCost=" + unitCost +
                ", total=" + total +
                ", budgetType=" + budgetType +
                '}';
    }
}