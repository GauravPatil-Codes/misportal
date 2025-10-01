package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.repositories.MonthlyTargetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MonthlyTargetService {
    
    @Autowired
    private MonthlyTargetRepository monthlyTargetRepository;
    
    // Update achieved target during MIS submission
    @Transactional
    public MonthlyTarget updateAchievedTarget(Long projectId, Long budgetItemId, 
                                            LocalDate targetMonth, Integer achievedTarget,
                                            String deviationReason, String mitigationPlan) {
        
        // Find the specific monthly target
        List<MonthlyTarget> targets = monthlyTargetRepository
            .findByProjectIdAndTargetMonth(projectId, targetMonth);
        
        Optional<MonthlyTarget> targetOpt = targets.stream()
            .filter(t -> t.getBudgetAllocationItem().getId().equals(budgetItemId))
            .findFirst();
        
        if (targetOpt.isPresent()) {
            MonthlyTarget target = targetOpt.get();
            target.setAchievedTarget(achievedTarget);
            target.setDeviationReason(deviationReason);
            target.setMitigationPlan(mitigationPlan);
            
            return monthlyTargetRepository.save(target);
        } else {
            throw new RuntimeException("Monthly target not found for project: " + projectId + 
                                     ", budget item: " + budgetItemId + 
                                     ", month: " + targetMonth);
        }
    }
    
    // Get all targets for a project and month (for MIS form)
    public List<MonthlyTarget> getTargetsForMIS(Long projectId, LocalDate targetMonth) {
        return monthlyTargetRepository.findByProjectIdAndTargetMonth(projectId, targetMonth);
    }
    
    // Get targets that need achievement updates
    public List<MonthlyTarget> getPendingTargets(Long projectId) {
        return monthlyTargetRepository.findPendingTargets(projectId);
    }
    
    // Get project achievement summary
    public Object[] getProjectAchievementSummary(Long projectId) {
        return monthlyTargetRepository.getProjectAchievementSummary(projectId);
    }
    
    // Get all targets for a budget item
    public List<MonthlyTarget> getTargetsByBudgetItem(Long budgetItemId) {
        return monthlyTargetRepository.findByBudgetAllocationItemId(budgetItemId);
    }
    
    // Calculate performance percentage for a project
    public Double calculateProjectPerformance(Long projectId) {
        Object[] summary = getProjectAchievementSummary(projectId);
        if (summary != null && summary.length >= 2) {
            Long totalPlanned = (Long) summary[0];
            Long totalAchieved = (Long) summary[1];
            
            if (totalPlanned != null && totalPlanned > 0 && totalAchieved != null) {
                return (totalAchieved.doubleValue() / totalPlanned.doubleValue()) * 100.0;
            }
        }
        return 0.0;
    }
}