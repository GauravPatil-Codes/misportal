package com.cmsfoundation.misportal.entities;

public enum BudgetHead {
    PROCUREMENT_COST("Procurement Cost"),
    TRAINING_COST("Training Cost"),
    CIVIL_CONSTRUCTION_COST("Civil/Construction Cost"),
    CONTINGENCY_MISCELLANEOUS_COST("Contingency/Miscellaneous Cost"),
    HR_COST("HR Cost"),
    ADMIN_COST("Admin Cost"),
    MANAGEMENT_COORDINATION_COST("Management and Coordination Cost"),
    GOVERNMENT_CONVERGENCE_COST("Government Convergence Cost");
    
    private final String displayName;
    
    BudgetHead(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}