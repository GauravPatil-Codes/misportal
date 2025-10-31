package com.cmsfoundation.misportal.dtos;

import java.time.LocalDate;

public class SubProjectDTO {
    private Long id;
    private String projectName;
    private String projectDescription;
    private String projectType;
    private String projectTheme;
    private LocalDate projectStartDate;
    private LocalDate projectEndDate;
    private String projectStatus;
    private Double allocatedBudget;
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getProjectName() {
		return projectName;
	}
	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}
	public String getProjectDescription() {
		return projectDescription;
	}
	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}
	public String getProjectType() {
		return projectType;
	}
	public void setProjectType(String projectType) {
		this.projectType = projectType;
	}
	public String getProjectTheme() {
		return projectTheme;
	}
	public void setProjectTheme(String projectTheme) {
		this.projectTheme = projectTheme;
	}
	public LocalDate getProjectStartDate() {
		return projectStartDate;
	}
	public void setProjectStartDate(LocalDate projectStartDate) {
		this.projectStartDate = projectStartDate;
	}
	public LocalDate getProjectEndDate() {
		return projectEndDate;
	}
	public void setProjectEndDate(LocalDate projectEndDate) {
		this.projectEndDate = projectEndDate;
	}
	public String getProjectStatus() {
		return projectStatus;
	}
	public void setProjectStatus(String projectStatus) {
		this.projectStatus = projectStatus;
	}
	public Double getAllocatedBudget() {
		return allocatedBudget;
	}
	public void setAllocatedBudget(Double allocatedBudget) {
		this.allocatedBudget = allocatedBudget;
	}
	public SubProjectDTO(Long id, String projectName, String projectDescription, String projectType,
			String projectTheme, LocalDate projectStartDate, LocalDate projectEndDate, String projectStatus,
			Double allocatedBudget) {
		super();
		this.id = id;
		this.projectName = projectName;
		this.projectDescription = projectDescription;
		this.projectType = projectType;
		this.projectTheme = projectTheme;
		this.projectStartDate = projectStartDate;
		this.projectEndDate = projectEndDate;
		this.projectStatus = projectStatus;
		this.allocatedBudget = allocatedBudget;
	}
	public SubProjectDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    // Default constructor + getters/setters
}