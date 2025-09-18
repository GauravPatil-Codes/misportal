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
        if (ngoRepository.existsById(id)) {
            ngo.setId(id);
            return ngoRepository.save(ngo);
        }
        throw new RuntimeException("NGO not found with id: " + id);
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