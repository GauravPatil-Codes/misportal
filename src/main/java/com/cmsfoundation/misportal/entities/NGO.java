package com.cmsfoundation.misportal.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonManagedReference;

@Entity
@Table(name = "ngos")
public class NGO {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    
    
    @Column(name = "ngo_name", unique = true)
    private String ngoName;
    
    // ✅ NEW: Relationships
    @OneToMany(mappedBy = "ngoPartner", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("ngo-projects")
    private List<Project> projects;
    
    @OneToMany(mappedBy = "ngo", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference("ngo-users")
    private List<User> ngoUsers;
    
    // ✅ NEW: Performance metrics for NGO
    public Map<String, Object> getNGOPerformanceMetrics() {
        if (projects == null || projects.isEmpty()) {
            return Map.of("totalProjects", 0, "avgPerformance", 0.0);
        }
        
        Map<String, Object> metrics = new HashMap<>();
        metrics.put("totalProjects", projects.size());
        metrics.put("activeProjects", projects.stream()
            .filter(p -> "ACTIVE".equalsIgnoreCase(p.getProjectStatus()))
            .count());
        metrics.put("avgPerformance", projects.stream()
            .mapToDouble(Project::getCurrentAchievementPercentage)
            .average().orElse(0.0));
        metrics.put("totalBudgetManaged", projects.stream()
            .mapToDouble(Project::getTotalBudgetFromItems)
            .sum());
            
        return metrics;
    }
    
    // Existing fields and new getters/setters
    public List<Project> getProjects() { return projects; }
    public void setProjects(List<Project> projects) { this.projects = projects; }
    
    public List<User> getNgoUsers() { return ngoUsers; }
    public void setNgoUsers(List<User> ngoUsers) { this.ngoUsers = ngoUsers; }
    
    
    @Column(name = "location")
    private String location;
    
    @Column(name = "founder")
    private String founder;
    
    @Column(name = "registration_details")
    private String registrationDetails;
    
    @Column(name = "registration_date")
    private LocalDate registrationDate;
    
    @Column(name = "statutory_docs")
    private Boolean statutoryDocs;
    
    @Column(name = "statutory_docs_file")
    private String statutoryDocsFile;
    
    @Column(name = "pancard")
    private Boolean pancard;
    
    @Column(name = "certificate_validity")
    private Boolean certificateValidity;
    
    @Column(name = "exemption_details")
    private String exemptionDetails;
    
    @Column(name = "audited_statements")
    private Boolean auditedStatements;
    
    @Column(name = "mou")
    private Boolean mou;
    
    @Column(name = "org_budget")
    private Double orgBudget;
    
    @Column(name = "total_expenses")
    private Double totalExpenses;
    
    @Column(name = "income_vs_expenditure")
    private String incomeVsExpenditure;
    
    @Column(name = "direct_programme_spend")
    private Double directProgrammeSpend;
    
    @Column(name = "fixed_assets")
    private String fixedAssets;
    
    @Column(name = "religious_agenda")
    private Boolean religiousAgenda;
    
    @Column(name = "agenda_details")
    private String agendaDetails;
    
    @Column(name = "beneficiary_caste")
    private Boolean beneficiaryCaste;
    
    @Column(name = "political_nature")
    private Boolean politicalNature;
    
    @Column(name = "social_advocacy")
    private Boolean socialAdvocacy;
    
    @Column(name = "govt_conflict")
    private Boolean govtConflict;
    
    @Column(name = "human_rights")
    private Boolean humanRights;
    
    @Column(name = "environmental")
    private Boolean environmental;
    
    @Column(name = "governing_body")
    private Boolean governingBody;
    
    @Column(name = "governing_details")
    private Boolean governingDetails;
    
    @Column(name = "related_members")
    private Boolean relatedMembers;
    
    @Column(name = "relations_specify")
    private String relationsSpecify;
    
    @Column(name = "meeting_frequency")
    private String meetingFrequency;
    
    @Column(name = "full_time_employees")
    private Integer fullTimeEmployees;
    
    @Column(name = "part_time_employees")
    private Integer partTimeEmployees;
    
    @Column(name = "skilled_staff")
    private Integer skilledStaff;
    
    @Column(name = "trained_personnel")
    private Boolean trainedPersonnel;
    
    @Column(name = "defined_roles")
    private Boolean definedRoles;
    
    @Column(name = "avg_staff_experience")
    private Double avgStaffExperience;
    
    @Column(name = "staff_training")
    private String staffTraining;
    
    @Column(name = "volunteer_opportunities")
    private Boolean volunteerOpportunities;
    
    @Column(name = "funding_sources")
    private String fundingSources;
    
    @Column(name = "individual_donors")
    private Boolean individualDonors;
    
    @Column(name = "individual_donors_details")
    private String individualDonorsDetails;
    
    @Column(name = "corporate_funders")
    private Boolean corporateFunders;
    
    @Column(name = "corporate_funder_details")
    private String corporateFunderDetails;
    
    @Column(name = "govt_funders")
    private Boolean govtFunders;
    
    @Column(name = "govt_funders_details")
    private String govtFundersDetails;
    
    @Column(name = "other_agencies")
    private Boolean otherAgencies;
    
    @Column(name = "other_agencies_details")
    private String otherAgenciesDetails;
    
    @Column(name = "main_objective", columnDefinition = "TEXT")
    private String mainObjective;
    
    @Column(name = "targeted_beneficiaries")
    private String targetedBeneficiaries;
    
    @Column(name = "percent_low_seb")
    private Double percentLowSEB;
    
    @Column(name = "total_outreach")
    private Integer totalOutReach;
    
    @Column(name = "cost_per_beneficiary")
    private Double costPerBeneficiary;
    
    @Column(name = "areas_of_intervention")
    private String areasOfIntervention;
    
    @Column(name = "monitoring_methods")
    private Boolean monitoringMethods;
    
    @Column(name = "monitoring_responsibility")
    private Boolean monitoringResponsibility;
    
    @Column(name = "external_evaluation")
    private Boolean externalEvaluation;
    
    @Column(name = "accreditation")
    private Boolean accreditation;
    
    @Column(name = "network_membership")
    private Boolean networkMembership;
    
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
    public NGO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNgoName() {
		return ngoName;
	}

	public void setNgoName(String ngoName) {
		this.ngoName = ngoName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFounder() {
		return founder;
	}

	public void setFounder(String founder) {
		this.founder = founder;
	}

	public String getRegistrationDetails() {
		return registrationDetails;
	}

	public void setRegistrationDetails(String registrationDetails) {
		this.registrationDetails = registrationDetails;
	}

	public LocalDate getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(LocalDate registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Boolean getStatutoryDocs() {
		return statutoryDocs;
	}

	public void setStatutoryDocs(Boolean statutoryDocs) {
		this.statutoryDocs = statutoryDocs;
	}

	public String getStatutoryDocsFile() {
		return statutoryDocsFile;
	}

	public void setStatutoryDocsFile(String statutoryDocsFile) {
		this.statutoryDocsFile = statutoryDocsFile;
	}

	public Boolean getPancard() {
		return pancard;
	}

	public void setPancard(Boolean pancard) {
		this.pancard = pancard;
	}

	public Boolean getCertificateValidity() {
		return certificateValidity;
	}

	public void setCertificateValidity(Boolean certificateValidity) {
		this.certificateValidity = certificateValidity;
	}

	public String getExemptionDetails() {
		return exemptionDetails;
	}

	public void setExemptionDetails(String exemptionDetails) {
		this.exemptionDetails = exemptionDetails;
	}

	public Boolean getAuditedStatements() {
		return auditedStatements;
	}

	public void setAuditedStatements(Boolean auditedStatements) {
		this.auditedStatements = auditedStatements;
	}

	public Boolean getMou() {
		return mou;
	}

	public void setMou(Boolean mou) {
		this.mou = mou;
	}

	public Double getOrgBudget() {
		return orgBudget;
	}

	public void setOrgBudget(Double orgBudget) {
		this.orgBudget = orgBudget;
	}

	public Double getTotalExpenses() {
		return totalExpenses;
	}

	public void setTotalExpenses(Double totalExpenses) {
		this.totalExpenses = totalExpenses;
	}

	public String getIncomeVsExpenditure() {
		return incomeVsExpenditure;
	}

	public void setIncomeVsExpenditure(String incomeVsExpenditure) {
		this.incomeVsExpenditure = incomeVsExpenditure;
	}

	public Double getDirectProgrammeSpend() {
		return directProgrammeSpend;
	}

	public void setDirectProgrammeSpend(Double directProgrammeSpend) {
		this.directProgrammeSpend = directProgrammeSpend;
	}

	public String getFixedAssets() {
		return fixedAssets;
	}

	public void setFixedAssets(String fixedAssets) {
		this.fixedAssets = fixedAssets;
	}

	public Boolean getReligiousAgenda() {
		return religiousAgenda;
	}

	public void setReligiousAgenda(Boolean religiousAgenda) {
		this.religiousAgenda = religiousAgenda;
	}

	public String getAgendaDetails() {
		return agendaDetails;
	}

	public void setAgendaDetails(String agendaDetails) {
		this.agendaDetails = agendaDetails;
	}

	public Boolean getBeneficiaryCaste() {
		return beneficiaryCaste;
	}

	public void setBeneficiaryCaste(Boolean beneficiaryCaste) {
		this.beneficiaryCaste = beneficiaryCaste;
	}

	public Boolean getPoliticalNature() {
		return politicalNature;
	}

	public void setPoliticalNature(Boolean politicalNature) {
		this.politicalNature = politicalNature;
	}

	public Boolean getSocialAdvocacy() {
		return socialAdvocacy;
	}

	public void setSocialAdvocacy(Boolean socialAdvocacy) {
		this.socialAdvocacy = socialAdvocacy;
	}

	public Boolean getGovtConflict() {
		return govtConflict;
	}

	public void setGovtConflict(Boolean govtConflict) {
		this.govtConflict = govtConflict;
	}

	public Boolean getHumanRights() {
		return humanRights;
	}

	public void setHumanRights(Boolean humanRights) {
		this.humanRights = humanRights;
	}

	public Boolean getEnvironmental() {
		return environmental;
	}

	public void setEnvironmental(Boolean environmental) {
		this.environmental = environmental;
	}

	public Boolean getGoverningBody() {
		return governingBody;
	}

	public void setGoverningBody(Boolean governingBody) {
		this.governingBody = governingBody;
	}

	public Boolean getGoverningDetails() {
		return governingDetails;
	}

	public void setGoverningDetails(Boolean governingDetails) {
		this.governingDetails = governingDetails;
	}

	public Boolean getRelatedMembers() {
		return relatedMembers;
	}

	public void setRelatedMembers(Boolean relatedMembers) {
		this.relatedMembers = relatedMembers;
	}

	public String getRelationsSpecify() {
		return relationsSpecify;
	}

	public void setRelationsSpecify(String relationsSpecify) {
		this.relationsSpecify = relationsSpecify;
	}

	public String getMeetingFrequency() {
		return meetingFrequency;
	}

	public void setMeetingFrequency(String meetingFrequency) {
		this.meetingFrequency = meetingFrequency;
	}

	public Integer getFullTimeEmployees() {
		return fullTimeEmployees;
	}

	public void setFullTimeEmployees(Integer fullTimeEmployees) {
		this.fullTimeEmployees = fullTimeEmployees;
	}

	public Integer getPartTimeEmployees() {
		return partTimeEmployees;
	}

	public void setPartTimeEmployees(Integer partTimeEmployees) {
		this.partTimeEmployees = partTimeEmployees;
	}

	public Integer getSkilledStaff() {
		return skilledStaff;
	}

	public void setSkilledStaff(Integer skilledStaff) {
		this.skilledStaff = skilledStaff;
	}

	public Boolean getTrainedPersonnel() {
		return trainedPersonnel;
	}

	public void setTrainedPersonnel(Boolean trainedPersonnel) {
		this.trainedPersonnel = trainedPersonnel;
	}

	public Boolean getDefinedRoles() {
		return definedRoles;
	}

	public void setDefinedRoles(Boolean definedRoles) {
		this.definedRoles = definedRoles;
	}

	public Double getAvgStaffExperience() {
		return avgStaffExperience;
	}

	public void setAvgStaffExperience(Double avgStaffExperience) {
		this.avgStaffExperience = avgStaffExperience;
	}

	public String getStaffTraining() {
		return staffTraining;
	}

	public void setStaffTraining(String staffTraining) {
		this.staffTraining = staffTraining;
	}

	public Boolean getVolunteerOpportunities() {
		return volunteerOpportunities;
	}

	public void setVolunteerOpportunities(Boolean volunteerOpportunities) {
		this.volunteerOpportunities = volunteerOpportunities;
	}

	public String getFundingSources() {
		return fundingSources;
	}

	public void setFundingSources(String fundingSources) {
		this.fundingSources = fundingSources;
	}

	public Boolean getIndividualDonors() {
		return individualDonors;
	}

	public void setIndividualDonors(Boolean individualDonors) {
		this.individualDonors = individualDonors;
	}

	public String getIndividualDonorsDetails() {
		return individualDonorsDetails;
	}

	public void setIndividualDonorsDetails(String individualDonorsDetails) {
		this.individualDonorsDetails = individualDonorsDetails;
	}

	public Boolean getCorporateFunders() {
		return corporateFunders;
	}

	public void setCorporateFunders(Boolean corporateFunders) {
		this.corporateFunders = corporateFunders;
	}

	public String getCorporateFunderDetails() {
		return corporateFunderDetails;
	}

	public void setCorporateFunderDetails(String corporateFunderDetails) {
		this.corporateFunderDetails = corporateFunderDetails;
	}

	public Boolean getGovtFunders() {
		return govtFunders;
	}

	public void setGovtFunders(Boolean govtFunders) {
		this.govtFunders = govtFunders;
	}

	public String getGovtFundersDetails() {
		return govtFundersDetails;
	}

	public void setGovtFundersDetails(String govtFundersDetails) {
		this.govtFundersDetails = govtFundersDetails;
	}

	public Boolean getOtherAgencies() {
		return otherAgencies;
	}

	public void setOtherAgencies(Boolean otherAgencies) {
		this.otherAgencies = otherAgencies;
	}

	public String getOtherAgenciesDetails() {
		return otherAgenciesDetails;
	}

	public void setOtherAgenciesDetails(String otherAgenciesDetails) {
		this.otherAgenciesDetails = otherAgenciesDetails;
	}

	public String getMainObjective() {
		return mainObjective;
	}

	public void setMainObjective(String mainObjective) {
		this.mainObjective = mainObjective;
	}

	public String getTargetedBeneficiaries() {
		return targetedBeneficiaries;
	}

	public void setTargetedBeneficiaries(String targetedBeneficiaries) {
		this.targetedBeneficiaries = targetedBeneficiaries;
	}

	public Double getPercentLowSEB() {
		return percentLowSEB;
	}

	public void setPercentLowSEB(Double percentLowSEB) {
		this.percentLowSEB = percentLowSEB;
	}

	public Integer getTotalOutReach() {
		return totalOutReach;
	}

	public void setTotalOutReach(Integer totalOutReach) {
		this.totalOutReach = totalOutReach;
	}

	public Double getCostPerBeneficiary() {
		return costPerBeneficiary;
	}

	public void setCostPerBeneficiary(Double costPerBeneficiary) {
		this.costPerBeneficiary = costPerBeneficiary;
	}

	public String getAreasOfIntervention() {
		return areasOfIntervention;
	}

	public void setAreasOfIntervention(String areasOfIntervention) {
		this.areasOfIntervention = areasOfIntervention;
	}

	public Boolean getMonitoringMethods() {
		return monitoringMethods;
	}

	public void setMonitoringMethods(Boolean monitoringMethods) {
		this.monitoringMethods = monitoringMethods;
	}

	public Boolean getMonitoringResponsibility() {
		return monitoringResponsibility;
	}

	public void setMonitoringResponsibility(Boolean monitoringResponsibility) {
		this.monitoringResponsibility = monitoringResponsibility;
	}

	public Boolean getExternalEvaluation() {
		return externalEvaluation;
	}

	public void setExternalEvaluation(Boolean externalEvaluation) {
		this.externalEvaluation = externalEvaluation;
	}

	public Boolean getAccreditation() {
		return accreditation;
	}

	public void setAccreditation(Boolean accreditation) {
		this.accreditation = accreditation;
	}

	public Boolean getNetworkMembership() {
		return networkMembership;
	}

	public void setNetworkMembership(Boolean networkMembership) {
		this.networkMembership = networkMembership;
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

	public NGO(Long id, String ngoName, String location, String founder, String registrationDetails,
			LocalDate registrationDate, Boolean statutoryDocs, String statutoryDocsFile, Boolean pancard,
			Boolean certificateValidity, String exemptionDetails, Boolean auditedStatements, Boolean mou,
			Double orgBudget, Double totalExpenses, String incomeVsExpenditure, Double directProgrammeSpend,
			String fixedAssets, Boolean religiousAgenda, String agendaDetails, Boolean beneficiaryCaste,
			Boolean politicalNature, Boolean socialAdvocacy, Boolean govtConflict, Boolean humanRights,
			Boolean environmental, Boolean governingBody, Boolean governingDetails, Boolean relatedMembers,
			String relationsSpecify, String meetingFrequency, Integer fullTimeEmployees, Integer partTimeEmployees,
			Integer skilledStaff, Boolean trainedPersonnel, Boolean definedRoles, Double avgStaffExperience,
			String staffTraining, Boolean volunteerOpportunities, String fundingSources, Boolean individualDonors,
			String individualDonorsDetails, Boolean corporateFunders, String corporateFunderDetails,
			Boolean govtFunders, String govtFundersDetails, Boolean otherAgencies, String otherAgenciesDetails,
			String mainObjective, String targetedBeneficiaries, Double percentLowSEB, Integer totalOutReach,
			Double costPerBeneficiary, String areasOfIntervention, Boolean monitoringMethods,
			Boolean monitoringResponsibility, Boolean externalEvaluation, Boolean accreditation,
			Boolean networkMembership, LocalDateTime createdAt, LocalDateTime updatedAt) {
		super();
		this.id = id;
		this.ngoName = ngoName;
		this.location = location;
		this.founder = founder;
		this.registrationDetails = registrationDetails;
		this.registrationDate = registrationDate;
		this.statutoryDocs = statutoryDocs;
		this.statutoryDocsFile = statutoryDocsFile;
		this.pancard = pancard;
		this.certificateValidity = certificateValidity;
		this.exemptionDetails = exemptionDetails;
		this.auditedStatements = auditedStatements;
		this.mou = mou;
		this.orgBudget = orgBudget;
		this.totalExpenses = totalExpenses;
		this.incomeVsExpenditure = incomeVsExpenditure;
		this.directProgrammeSpend = directProgrammeSpend;
		this.fixedAssets = fixedAssets;
		this.religiousAgenda = religiousAgenda;
		this.agendaDetails = agendaDetails;
		this.beneficiaryCaste = beneficiaryCaste;
		this.politicalNature = politicalNature;
		this.socialAdvocacy = socialAdvocacy;
		this.govtConflict = govtConflict;
		this.humanRights = humanRights;
		this.environmental = environmental;
		this.governingBody = governingBody;
		this.governingDetails = governingDetails;
		this.relatedMembers = relatedMembers;
		this.relationsSpecify = relationsSpecify;
		this.meetingFrequency = meetingFrequency;
		this.fullTimeEmployees = fullTimeEmployees;
		this.partTimeEmployees = partTimeEmployees;
		this.skilledStaff = skilledStaff;
		this.trainedPersonnel = trainedPersonnel;
		this.definedRoles = definedRoles;
		this.avgStaffExperience = avgStaffExperience;
		this.staffTraining = staffTraining;
		this.volunteerOpportunities = volunteerOpportunities;
		this.fundingSources = fundingSources;
		this.individualDonors = individualDonors;
		this.individualDonorsDetails = individualDonorsDetails;
		this.corporateFunders = corporateFunders;
		this.corporateFunderDetails = corporateFunderDetails;
		this.govtFunders = govtFunders;
		this.govtFundersDetails = govtFundersDetails;
		this.otherAgencies = otherAgencies;
		this.otherAgenciesDetails = otherAgenciesDetails;
		this.mainObjective = mainObjective;
		this.targetedBeneficiaries = targetedBeneficiaries;
		this.percentLowSEB = percentLowSEB;
		this.totalOutReach = totalOutReach;
		this.costPerBeneficiary = costPerBeneficiary;
		this.areasOfIntervention = areasOfIntervention;
		this.monitoringMethods = monitoringMethods;
		this.monitoringResponsibility = monitoringResponsibility;
		this.externalEvaluation = externalEvaluation;
		this.accreditation = accreditation;
		this.networkMembership = networkMembership;
		this.createdAt = createdAt;
		this.updatedAt = updatedAt;
	}

	@Override
	public String toString() {
		return "NGO [id=" + id + ", ngoName=" + ngoName + ", location=" + location + ", founder=" + founder
				+ ", registrationDetails=" + registrationDetails + ", registrationDate=" + registrationDate
				+ ", statutoryDocs=" + statutoryDocs + ", statutoryDocsFile=" + statutoryDocsFile + ", pancard="
				+ pancard + ", certificateValidity=" + certificateValidity + ", exemptionDetails=" + exemptionDetails
				+ ", auditedStatements=" + auditedStatements + ", mou=" + mou + ", orgBudget=" + orgBudget
				+ ", totalExpenses=" + totalExpenses + ", incomeVsExpenditure=" + incomeVsExpenditure
				+ ", directProgrammeSpend=" + directProgrammeSpend + ", fixedAssets=" + fixedAssets
				+ ", religiousAgenda=" + religiousAgenda + ", agendaDetails=" + agendaDetails + ", beneficiaryCaste="
				+ beneficiaryCaste + ", politicalNature=" + politicalNature + ", socialAdvocacy=" + socialAdvocacy
				+ ", govtConflict=" + govtConflict + ", humanRights=" + humanRights + ", environmental=" + environmental
				+ ", governingBody=" + governingBody + ", governingDetails=" + governingDetails + ", relatedMembers="
				+ relatedMembers + ", relationsSpecify=" + relationsSpecify + ", meetingFrequency=" + meetingFrequency
				+ ", fullTimeEmployees=" + fullTimeEmployees + ", partTimeEmployees=" + partTimeEmployees
				+ ", skilledStaff=" + skilledStaff + ", trainedPersonnel=" + trainedPersonnel + ", definedRoles="
				+ definedRoles + ", avgStaffExperience=" + avgStaffExperience + ", staffTraining=" + staffTraining
				+ ", volunteerOpportunities=" + volunteerOpportunities + ", fundingSources=" + fundingSources
				+ ", individualDonors=" + individualDonors + ", individualDonorsDetails=" + individualDonorsDetails
				+ ", corporateFunders=" + corporateFunders + ", corporateFunderDetails=" + corporateFunderDetails
				+ ", govtFunders=" + govtFunders + ", govtFundersDetails=" + govtFundersDetails + ", otherAgencies="
				+ otherAgencies + ", otherAgenciesDetails=" + otherAgenciesDetails + ", mainObjective=" + mainObjective
				+ ", targetedBeneficiaries=" + targetedBeneficiaries + ", percentLowSEB=" + percentLowSEB
				+ ", totalOutReach=" + totalOutReach + ", costPerBeneficiary=" + costPerBeneficiary
				+ ", areasOfIntervention=" + areasOfIntervention + ", monitoringMethods=" + monitoringMethods
				+ ", monitoringResponsibility=" + monitoringResponsibility + ", externalEvaluation="
				+ externalEvaluation + ", accreditation=" + accreditation + ", networkMembership=" + networkMembership
				+ ", createdAt=" + createdAt + ", updatedAt=" + updatedAt + "]";
	}
    
	
    // Note: Getters, setters, toString, and constructors will be added by user as mentioned
}