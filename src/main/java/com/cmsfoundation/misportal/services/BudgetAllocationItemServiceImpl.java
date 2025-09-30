package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.repositories.BudgetAllocationItemRepository;
import com.cmsfoundation.misportal.services.BudgetAllocationItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BudgetAllocationItemServiceImpl implements BudgetAllocationItemService {

    @Autowired
    private BudgetAllocationItemRepository budgetAllocationItemRepository;

    @Override
    public BudgetAllocationItem createBudgetItem(BudgetAllocationItem budgetItem) {
        return budgetAllocationItemRepository.save(budgetItem);
    }

    @Override
    public List<BudgetAllocationItem> getAllBudgetItems() {
        return budgetAllocationItemRepository.findAll();
    }

    @Override
    public Optional<BudgetAllocationItem> getBudgetItemById(Long id) {
        return budgetAllocationItemRepository.findById(id);
    }

    @Override
    public BudgetAllocationItem updateBudgetItem(Long id, BudgetAllocationItem budgetItem) {
        if (budgetAllocationItemRepository.existsById(id)) {
            budgetItem.setId(id);
            return budgetAllocationItemRepository.save(budgetItem);
        } else {
            throw new RuntimeException("BudgetAllocationItem not found with id: " + id);
        }
    }

    @Override
    public void deleteBudgetItem(Long id) {
        if (budgetAllocationItemRepository.existsById(id)) {
            budgetAllocationItemRepository.deleteById(id);
        } else {
            throw new RuntimeException("BudgetAllocationItem not found with id: " + id);
        }
    }

    @Override
    public List<BudgetAllocationItem> getBudgetItemsByProject(Long projectId) {
        return budgetAllocationItemRepository.findByProjectId(projectId);
    }

    @Override
    public List<BudgetAllocationItem> getBudgetItemsByProjectAndHead(Long projectId, BudgetHead budgetHead) {
        return budgetAllocationItemRepository.findByProjectIdAndBudgetType(projectId, budgetHead);
    }

    @Override
    public Double getTotalBudgetByProject(Long projectId) {
        Double total = budgetAllocationItemRepository.getTotalBudgetByProject(projectId);
        return total != null ? total : 0.0;
    }

    @Override
    public Double getTotalBudgetByProjectAndHead(Long projectId, BudgetHead budgetHead) {
        Double total = budgetAllocationItemRepository.getTotalBudgetByProjectAndHead(projectId, budgetHead);
        return total != null ? total : 0.0;
    }

    @Override
    public Map<BudgetHead, Double> getBudgetSummaryByProject(Long projectId) {
        List<Object[]> summary = budgetAllocationItemRepository.getBudgetSummaryByProject(projectId);
        return summary.stream()
                .collect(Collectors.toMap(
                        entry -> (BudgetHead) entry[0],
                        entry -> (Double) entry[1]
                ));
    }

    @Override
    public List<BudgetAllocationItem> createMultipleBudgetItems(List<BudgetAllocationItem> budgetItems) {
        return budgetAllocationItemRepository.saveAll(budgetItems);
    }
}
