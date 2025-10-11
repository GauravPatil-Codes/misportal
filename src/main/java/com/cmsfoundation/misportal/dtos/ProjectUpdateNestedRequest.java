package com.cmsfoundation.misportal.dtos;

import java.util.List;

public class ProjectUpdateNestedRequest extends ProjectCreateRequest {

    private List<BudgetAllocationItemWithTargetsDTO> nestedBudgetAllocationItems;

    public List<BudgetAllocationItemWithTargetsDTO> getNestedBudgetAllocationItems() {
        return nestedBudgetAllocationItems;
    }

    public void setNestedBudgetAllocationItems(List<BudgetAllocationItemWithTargetsDTO> nestedBudgetAllocationItems) {
        this.nestedBudgetAllocationItems = nestedBudgetAllocationItems;
    }

    // other fields and methods from ProjectCreateRequest remain inherited 
}

