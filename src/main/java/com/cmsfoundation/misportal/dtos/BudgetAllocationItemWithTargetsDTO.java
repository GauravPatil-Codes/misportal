package com.cmsfoundation.misportal.dtos;

import java.util.List;

public class BudgetAllocationItemWithTargetsDTO extends BudgetAllocationItemDTO {
    private List<MonthlyTargetItemDTO> monthlyTargets;

    // getters and setters
    public List<MonthlyTargetItemDTO> getMonthlyTargets() { return monthlyTargets; }
    public void setMonthlyTargets(List<MonthlyTargetItemDTO> monthlyTargets) { this.monthlyTargets = monthlyTargets; }



}
