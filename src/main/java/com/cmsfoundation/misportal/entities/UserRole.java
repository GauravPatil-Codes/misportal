package com.cmsfoundation.misportal.entities;

public enum UserRole {
    CMS_SUPER_ADMIN("CMS Super Admin"),
    CMS_PROJECT_MANAGER("CMS Project Manager"), 
    CMS_COORDINATOR("CMS Coordinator"),
    NGO_PARTNER("NGO Partner"),
    NGO_FIELD_OFFICER("NGO Field Officer"),
    NGO_COORDINATOR("NGO Coordinator"),
    GOVERNMENT_OFFICIAL("Government Official"),
    BENEFICIARY_REPRESENTATIVE("Beneficiary Representative");
    
    private final String displayName;
    
    UserRole(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
}