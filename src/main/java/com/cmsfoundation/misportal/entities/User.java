package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "users")
public class User {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "first_name")
    private String firstName;
    
    @Column(name = "last_name") 
    private String lastName;
    
    @Column(name = "email_id", unique = true)
    private String emailId;
    
    @Column(name = "password")
    private String password;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ngo_id", nullable = true)
    @JsonBackReference("ngo-users")
    private NGO ngo;
    
    @OneToMany(mappedBy = "projectManager", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("manager-projects")
    private List<Project> managedProjects;
    
    @Column(name = "otp_code")
    private String otpCode;
    
    @Column(name = "otp_expiry")
    private LocalDateTime otpExpiry;
    
    @Column(name = "is_verified")
    private Boolean isVerified = false;
    
    @Column(name = "is_active")
    private Boolean isActive = true;
    
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
    
    // Business methods
    public boolean canManageProject(Long projectId) {
        if (userRole == UserRole.CMS_SUPER_ADMIN) return true;
        if (userRole == UserRole.CMS_PROJECT_MANAGER && managedProjects != null) {
            return managedProjects.stream().anyMatch(p -> p.getId().equals(projectId));
        }
        return false;
    }
    
    public boolean canSubmitMISReport(Project project) {
        if (userRole == UserRole.NGO_PARTNER || userRole == UserRole.NGO_FIELD_OFFICER) {
            return ngo != null && project.getNgoPartner() != null && 
                   ngo.getId().equals(project.getNgoPartner().getId());
        }
        return userRole == UserRole.CMS_SUPER_ADMIN || userRole == UserRole.CMS_PROJECT_MANAGER;
    }
    
    // Getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmailId() { return emailId; }
    public void setEmailId(String emailId) { this.emailId = emailId; }
    
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    
    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
    
    public NGO getNgo() { return ngo; }
    public void setNgo(NGO ngo) { this.ngo = ngo; }
    
    public List<Project> getManagedProjects() { return managedProjects; }
    public void setManagedProjects(List<Project> managedProjects) { this.managedProjects = managedProjects; }
    
    public String getOtpCode() { return otpCode; }
    public void setOtpCode(String otpCode) { this.otpCode = otpCode; }
    
    public LocalDateTime getOtpExpiry() { return otpExpiry; }
    public void setOtpExpiry(LocalDateTime otpExpiry) { this.otpExpiry = otpExpiry; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public Boolean getIsActive() { return isActive; }
    public void setIsActive(Boolean isActive) { this.isActive = isActive; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }

	public User(Long id, String firstName, String lastName, String emailId, String password, String phoneNumber,
			UserRole userRole, NGO ngo, List<Project> managedProjects, String otpCode, LocalDateTime otpExpiry,
			Boolean isVerified, Boolean isActive, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.password = password;
		this.phoneNumber = phoneNumber;
		this.userRole = userRole;
		this.ngo = ngo;
		this.managedProjects = managedProjects;
		this.otpCode = otpCode;
		this.otpExpiry = otpExpiry;
		this.isVerified = isVerified;
		this.isActive = isActive;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", emailId=" + emailId
				+ ", password=" + password + ", phoneNumber=" + phoneNumber + ", userRole=" + userRole + ", ngo=" + ngo
				+ ", managedProjects=" + managedProjects + ", otpCode=" + otpCode + ", otpExpiry=" + otpExpiry
				+ ", isVerified=" + isVerified + ", isActive=" + isActive + ", createdAt=" + createdAt + ", updatedAt="
				+ updatedAt + "]";
	}

	public User() {
		super();
		// TODO Auto-generated constructor stub
	}
    
    
}