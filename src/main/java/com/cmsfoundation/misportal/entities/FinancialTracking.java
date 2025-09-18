package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "financial_tracking")
public class FinancialTracking {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "total_budget")
    private Integer totalBudget;
    
    @Column(name = "allocated_budget")
    private Integer allocatedBudget;
    
    @Column(name = "ivdp_budget")
    private Integer ivdpBudget;
    
    @Column(name = "health_budget")
    private Integer healthBudget;
    
    @Column(name = "education_budget")
    private Integer educationBudget;
    
    @Column(name = "climate_resilience_budget")
    private Integer climateResilience;
    
    @Column(name = "livelihood_budget")
    private Integer livelihood;
    
    @Column(name = "government_convergence_budget")
    private Integer governmentConvergence;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Default constructor
    public FinancialTracking() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getTotalBudget() {
		return totalBudget;
	}

	public void setTotalBudget(Integer totalBudget) {
		this.totalBudget = totalBudget;
	}

	public Integer getAllocatedBudget() {
		return allocatedBudget;
	}

	public void setAllocatedBudget(Integer allocatedBudget) {
		this.allocatedBudget = allocatedBudget;
	}

	public Integer getIvdpBudget() {
		return ivdpBudget;
	}

	public void setIvdpBudget(Integer ivdpBudget) {
		this.ivdpBudget = ivdpBudget;
	}

	public Integer getHealthBudget() {
		return healthBudget;
	}

	public void setHealthBudget(Integer healthBudget) {
		this.healthBudget = healthBudget;
	}

	public Integer getEducationBudget() {
		return educationBudget;
	}

	public void setEducationBudget(Integer educationBudget) {
		this.educationBudget = educationBudget;
	}

	public Integer getClimateResilience() {
		return climateResilience;
	}

	public void setClimateResilience(Integer climateResilience) {
		this.climateResilience = climateResilience;
	}

	public Integer getLivelihood() {
		return livelihood;
	}

	public void setLivelihood(Integer livelihood) {
		this.livelihood = livelihood;
	}

	public Integer getGovernmentConvergence() {
		return governmentConvergence;
	}

	public void setGovernmentConvergence(Integer governmentConvergence) {
		this.governmentConvergence = governmentConvergence;
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

	public FinancialTracking(Long id, Integer totalBudget, Integer allocatedBudget, Integer ivdpBudget,
			Integer healthBudget, Integer educationBudget, Integer climateResilience, Integer livelihood,
			Integer governmentConvergence, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.totalBudget = totalBudget;
		this.allocatedBudget = allocatedBudget;
		this.ivdpBudget = ivdpBudget;
		this.healthBudget = healthBudget;
		this.educationBudget = educationBudget;
		this.climateResilience = climateResilience;
		this.livelihood = livelihood;
		this.governmentConvergence = governmentConvergence;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "FinancialTracking [id=" + id + ", totalBudget=" + totalBudget + ", allocatedBudget=" + allocatedBudget
				+ ", ivdpBudget=" + ivdpBudget + ", healthBudget=" + healthBudget + ", educationBudget="
				+ educationBudget + ", climateResilience=" + climateResilience + ", livelihood=" + livelihood
				+ ", governmentConvergence=" + governmentConvergence + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}
    
    
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned
}