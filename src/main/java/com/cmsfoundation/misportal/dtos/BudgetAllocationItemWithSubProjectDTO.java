package com.cmsfoundation.misportal.dtos;

import com.cmsfoundation.misportal.entities.BudgetHead;

public class BudgetAllocationItemWithSubProjectDTO {
    private Long id;
    private String itemName;
    private String description;
    private Double total;
    private BudgetHead budgetType;
    private Long subProjectId;
    private String subProjectName;
    
    
	public BudgetAllocationItemWithSubProjectDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public Double getTotal() {
		return total;
	}
	public void setTotal(Double total) {
		this.total = total;
	}
	public BudgetHead getBudgetType() {
		return budgetType;
	}
	public void setBudgetType(BudgetHead budgetType) {
		this.budgetType = budgetType;
	}
	public Long getSubProjectId() {
		return subProjectId;
	}
	public void setSubProjectId(Long subProjectId) {
		this.subProjectId = subProjectId;
	}
	public String getSubProjectName() {
		return subProjectName;
	}
	public void setSubProjectName(String subProjectName) {
		this.subProjectName = subProjectName;
	}
    
    // Getters and setters
    
}