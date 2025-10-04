package com.cmsfoundation.misportal.entities;

import jakarta.persistence.Embeddable;

@Embeddable
public class Attachments {
    
    private String attachment1;
    private String attachment2;
    private String attachment3;
    
    // Constructors
    public Attachments() {}
    
    public Attachments(String attachment1, String attachment2, String attachment3) {
        this.attachment1 = attachment1;
        this.attachment2 = attachment2;
        this.attachment3 = attachment3;
    }
    
    // Getters and Setters
    public String getAttachment1() { 
        return attachment1; 
    }
    
    public void setAttachment1(String attachment1) { 
        this.attachment1 = attachment1; 
    }
    
    public String getAttachment2() { 
        return attachment2; 
    }
    
    public void setAttachment2(String attachment2) { 
        this.attachment2 = attachment2; 
    }
    
    public String getAttachment3() { 
        return attachment3; 
    }
    
    public void setAttachment3(String attachment3) { 
        this.attachment3 = attachment3; 
    }
}