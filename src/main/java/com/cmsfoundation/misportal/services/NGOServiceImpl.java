package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.QuickUpdate;
import com.cmsfoundation.misportal.repositories.NGORepository;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.QuickUpdateRepository;
import com.cmsfoundation.misportal.services.NGOService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NGOServiceImpl implements NGOService {
    
    @Autowired
    private NGORepository ngoRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Autowired
    private QuickUpdateRepository quickUpdateRepository;
    
    @Override
    public NGO createNGO(NGO ngo) {
        return ngoRepository.save(ngo);
    }
    
    @Override
    public List<NGO> getAllNGOs() {
        return ngoRepository.findAll();
    }
    
    @Override
    public Optional<NGO> getNGOById(Long id) {
        return ngoRepository.findById(id);
    }

    @Override
    public NGO updateNGO(Long id, NGO ngo) {

        NGO existing = ngoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("NGO not found with id: " + id));

        if (ngo.getNgoName() != null) existing.setNgoName(ngo.getNgoName());
        if (ngo.getLocation() != null) existing.setLocation(ngo.getLocation());
        if (ngo.getFounder() != null) existing.setFounder(ngo.getFounder());
        if (ngo.getRegistrationDetails() != null) existing.setRegistrationDetails(ngo.getRegistrationDetails());
        if (ngo.getRegistrationDate() != null) existing.setRegistrationDate(ngo.getRegistrationDate());
        if (ngo.getStatutoryDocs() != null) existing.setStatutoryDocs(ngo.getStatutoryDocs());
        if (ngo.getStatutoryDocsFile() != null) existing.setStatutoryDocsFile(ngo.getStatutoryDocsFile());
        if (ngo.getPancard() != null) existing.setPancard(ngo.getPancard());
        if (ngo.getCertificateValidity() != null) existing.setCertificateValidity(ngo.getCertificateValidity());
        if (ngo.getExemptionDetails() != null) existing.setExemptionDetails(ngo.getExemptionDetails());
        if (ngo.getAuditedStatements() != null) existing.setAuditedStatements(ngo.getAuditedStatements());
        if (ngo.getMou() != null) existing.setMou(ngo.getMou());
        if (ngo.getOrgBudget() != null) existing.setOrgBudget(ngo.getOrgBudget());
        if (ngo.getTotalExpenses() != null) existing.setTotalExpenses(ngo.getTotalExpenses());
        if (ngo.getIncomeVsExpenditure() != null) existing.setIncomeVsExpenditure(ngo.getIncomeVsExpenditure());
        if (ngo.getDirectProgrammeSpend() != null) existing.setDirectProgrammeSpend(ngo.getDirectProgrammeSpend());
        if (ngo.getFixedAssets() != null) existing.setFixedAssets(ngo.getFixedAssets());
        if (ngo.getReligiousAgenda() != null) existing.setReligiousAgenda(ngo.getReligiousAgenda());
        if (ngo.getAgendaDetails() != null) existing.setAgendaDetails(ngo.getAgendaDetails());
        if (ngo.getBeneficiaryCaste() != null) existing.setBeneficiaryCaste(ngo.getBeneficiaryCaste());
        if (ngo.getPoliticalNature() != null) existing.setPoliticalNature(ngo.getPoliticalNature());
        if (ngo.getSocialAdvocacy() != null) existing.setSocialAdvocacy(ngo.getSocialAdvocacy());
        if (ngo.getGovtConflict() != null) existing.setGovtConflict(ngo.getGovtConflict());
        if (ngo.getHumanRights() != null) existing.setHumanRights(ngo.getHumanRights());
        if (ngo.getEnvironmental() != null) existing.setEnvironmental(ngo.getEnvironmental());
        if (ngo.getGoverningBody() != null) existing.setGoverningBody(ngo.getGoverningBody());
        if (ngo.getGoverningDetails() != null) existing.setGoverningDetails(ngo.getGoverningDetails());
        if (ngo.getRelatedMembers() != null) existing.setRelatedMembers(ngo.getRelatedMembers());
        if (ngo.getRelationsSpecify() != null) existing.setRelationsSpecify(ngo.getRelationsSpecify());
        if (ngo.getMeetingFrequency() != null) existing.setMeetingFrequency(ngo.getMeetingFrequency());
        if (ngo.getFullTimeEmployees() != null) existing.setFullTimeEmployees(ngo.getFullTimeEmployees());
        if (ngo.getPartTimeEmployees() != null) existing.setPartTimeEmployees(ngo.getPartTimeEmployees());
        if (ngo.getSkilledStaff() != null) existing.setSkilledStaff(ngo.getSkilledStaff());
        if (ngo.getTrainedPersonnel() != null) existing.setTrainedPersonnel(ngo.getTrainedPersonnel());
        if (ngo.getDefinedRoles() != null) existing.setDefinedRoles(ngo.getDefinedRoles());
        if (ngo.getAvgStaffExperience() != null) existing.setAvgStaffExperience(ngo.getAvgStaffExperience());
        if (ngo.getStaffTraining() != null) existing.setStaffTraining(ngo.getStaffTraining());
        if (ngo.getVolunteerOpportunities() != null) existing.setVolunteerOpportunities(ngo.getVolunteerOpportunities());
        if (ngo.getFundingSources() != null) existing.setFundingSources(ngo.getFundingSources());
        if (ngo.getIndividualDonors() != null) existing.setIndividualDonors(ngo.getIndividualDonors());
        if (ngo.getIndividualDonorsDetails() != null) existing.setIndividualDonorsDetails(ngo.getIndividualDonorsDetails());
        if (ngo.getCorporateFunders() != null) existing.setCorporateFunders(ngo.getCorporateFunders());
        if (ngo.getCorporateFunderDetails() != null) existing.setCorporateFunderDetails(ngo.getCorporateFunderDetails());
        if (ngo.getGovtFunders() != null) existing.setGovtFunders(ngo.getGovtFunders());
        if (ngo.getGovtFundersDetails() != null) existing.setGovtFundersDetails(ngo.getGovtFundersDetails());
        if (ngo.getOtherAgencies() != null) existing.setOtherAgencies(ngo.getOtherAgencies());
        if (ngo.getOtherAgenciesDetails() != null) existing.setOtherAgenciesDetails(ngo.getOtherAgenciesDetails());
        if (ngo.getMainObjective() != null) existing.setMainObjective(ngo.getMainObjective());
        if (ngo.getTargetedBeneficiaries() != null) existing.setTargetedBeneficiaries(ngo.getTargetedBeneficiaries());
        if (ngo.getPercentLowSEB() != null) existing.setPercentLowSEB(ngo.getPercentLowSEB());
        if (ngo.getTotalOutReach() != null) existing.setTotalOutReach(ngo.getTotalOutReach());
        if (ngo.getCostPerBeneficiary() != null) existing.setCostPerBeneficiary(ngo.getCostPerBeneficiary());
        if (ngo.getAreasOfIntervention() != null) existing.setAreasOfIntervention(ngo.getAreasOfIntervention());
        if (ngo.getMonitoringMethods() != null) existing.setMonitoringMethods(ngo.getMonitoringMethods());
        if (ngo.getMonitoringResponsibility() != null) existing.setMonitoringResponsibility(ngo.getMonitoringResponsibility());
        if (ngo.getExternalEvaluation() != null) existing.setExternalEvaluation(ngo.getExternalEvaluation());
        if (ngo.getAccreditation() != null) existing.setAccreditation(ngo.getAccreditation());
        if (ngo.getNetworkMembership() != null) existing.setNetworkMembership(ngo.getNetworkMembership());
        if (ngo.getCreatedAt() != null) existing.setCreatedAt(ngo.getCreatedAt());
        if (ngo.getUpdatedAt() != null) existing.setUpdatedAt(ngo.getUpdatedAt());

        return ngoRepository.save(existing);
    }


    @Override
    public void deleteNGO(Long id) {
        if (ngoRepository.existsById(id)) {
            ngoRepository.deleteById(id);
        } else {
            throw new RuntimeException("NGO not found with id: " + id);
        }
    }
    
    @Override
    public List<Project> getProjectsByNGO(Long ngoId) {
        // Find NGO first to get the name
        Optional<NGO> ngo = ngoRepository.findById(ngoId);
        if (ngo.isPresent()) {
            return projectRepository.findByProjectNgoPartner(ngo.get().getNgoName());
        }
        throw new RuntimeException("NGO not found with id: " + ngoId);
    }
    
    @Override
    public Double getNGORatingByQuickUpdates(Long ngoId) {
        // Calculate rating based on quick updates progress
        Optional<NGO> ngo = ngoRepository.findById(ngoId);
        if (ngo.isPresent()) {
            List<Project> projects = getProjectsByNGO(ngoId);
            double totalRating = 0.0;
            int projectCount = 0;
            
            for (Project project : projects) {
                List<QuickUpdate> updates = quickUpdateRepository.findByProject(project.getProjectName());
                if (!updates.isEmpty()) {
                    double avgProgress = updates.stream()
                        .mapToInt(update -> update.getOverallProgress() != null ? update.getOverallProgress() : 0)
                        .average().orElse(0.0);
                    // Convert progress percentage to rating (0-5 scale)
                    totalRating += (avgProgress / 100.0) * 5.0;
                    projectCount++;
                }
            }
            
            return projectCount > 0 ? totalRating / projectCount : 0.0;
        }
        throw new RuntimeException("NGO not found with id: " + ngoId);
    }
    
    @Override
    public List<NGO> searchNGOByString(String searchTerm) {
        return ngoRepository.searchNGOByString(searchTerm);
    }
}