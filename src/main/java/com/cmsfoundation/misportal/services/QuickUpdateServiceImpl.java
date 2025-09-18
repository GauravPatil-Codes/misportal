package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.QuickUpdate;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.QuickUpdateRepository;
import com.cmsfoundation.misportal.services.QuickUpdateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class QuickUpdateServiceImpl implements QuickUpdateService {
    
    @Autowired
    private QuickUpdateRepository quickUpdateRepository;
    
    @Autowired
    private ProjectRepository projectRepository;
    
    @Override
    public QuickUpdate createQuickUpdate(QuickUpdate quickUpdate) {
        return quickUpdateRepository.save(quickUpdate);
    }
    
    @Override
    public List<QuickUpdate> getAllQuickUpdates() {
        return quickUpdateRepository.findAll();
    }
    
    @Override
    public Optional<QuickUpdate> getQuickUpdateById(Long id) {
        return quickUpdateRepository.findById(id);
    }
    
    @Override
    public QuickUpdate updateQuickUpdate(Long id, QuickUpdate quickUpdate) {
        if (quickUpdateRepository.existsById(id)) {
            quickUpdate.setId(id);
            return quickUpdateRepository.save(quickUpdate);
        }
        throw new RuntimeException("QuickUpdate not found with id: " + id);
    }
    
    @Override
    public void deleteQuickUpdate(Long id) {
        if (quickUpdateRepository.existsById(id)) {
            quickUpdateRepository.deleteById(id);
        } else {
            throw new RuntimeException("QuickUpdate not found with id: " + id);
        }
    }
    
    @Override
    public List<QuickUpdate> getUpdatesByTime(LocalDateTime startDate, LocalDateTime endDate) {
        return quickUpdateRepository.getUpdatesByTime(startDate, endDate);
    }
    
    @Override
    public Long getPendingReportsNumber() {
        return quickUpdateRepository.getPendingReportsCount();
    }
    
    @Override
    public List<QuickUpdate> getUpdatesByStatus(String status) {
        return quickUpdateRepository.findByStatus(status);
    }
    
    @Override
    public Object getTargetMonthly(String projectName) {
        // Find project by name and return monthly targets
        List<Project> projects = projectRepository.searchProjectsByString(projectName);
        if (!projects.isEmpty()) {
            Project project = projects.get(0);
            return project.getMonthlyTarget();
        }
        throw new RuntimeException("Project not found with name: " + projectName);
    }
}