package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MonthlyTargetItem;
import com.cmsfoundation.misportal.repositories.MonthlyTargetItemRepository;
import com.cmsfoundation.misportal.services.MonthlyTargetItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class MonthlyTargetItemServiceImpl implements MonthlyTargetItemService {
    
    @Autowired
    private MonthlyTargetItemRepository monthlyTargetItemRepository;
    
    @Override
    public MonthlyTargetItem createMonthlyTarget(MonthlyTargetItem monthlyTarget) {
        return monthlyTargetItemRepository.save(monthlyTarget);
    }
    
    @Override
    public List<MonthlyTargetItem> createMultipleMonthlyTargets(List<MonthlyTargetItem> monthlyTargets) {
        return monthlyTargetItemRepository.saveAll(monthlyTargets);
    }
    
    @Override
    public List<MonthlyTargetItem> getAllMonthlyTargets() {
        return monthlyTargetItemRepository.findAll();
    }
    
    @Override
    public Optional<MonthlyTargetItem> getMonthlyTargetById(Long id) {
        return monthlyTargetItemRepository.findById(id);
    }
    
    @Override
    public MonthlyTargetItem updateMonthlyTarget(Long id, MonthlyTargetItem monthlyTarget) {
        if (monthlyTargetItemRepository.existsById(id)) {
            monthlyTarget.setId(id);
            return monthlyTargetItemRepository.save(monthlyTarget);
        }
        throw new RuntimeException("Monthly Target Item not found with id: " + id);
    }
    
    @Override
    public void deleteMonthlyTarget(Long id) {
        if (monthlyTargetItemRepository.existsById(id)) {
            monthlyTargetItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("Monthly Target Item not found with id: " + id);
        }
    }
    
    @Override
    public List<MonthlyTargetItem> getMonthlyTargetsByProject(Long projectId) {
        return monthlyTargetItemRepository.findByProjectId(projectId);
    }
    
    @Override
    public List<MonthlyTargetItem> getMonthlyTargetsByBudgetItem(Long budgetAllocationItemId) {
        return monthlyTargetItemRepository.findByBudgetAllocationItemId(budgetAllocationItemId);
    }
    
    @Override
    public List<MonthlyTargetItem> getMonthlyTargetsByProjectAndMonth(Long projectId, LocalDate targetMonth) {
        return monthlyTargetItemRepository.findByProjectIdAndTargetMonth(projectId, targetMonth);
    }
    
    @Override
    public List<MonthlyTargetItem> getMonthlyTargetsByDateRange(Long projectId, LocalDate startDate, LocalDate endDate) {
        return monthlyTargetItemRepository.findTargetsByDateRange(projectId, startDate, endDate);
    }
}