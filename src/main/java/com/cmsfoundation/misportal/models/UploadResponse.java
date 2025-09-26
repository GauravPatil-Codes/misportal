package com.cmsfoundation.misportal.models;

import java.util.List;

public class UploadResponse {
    private int status;
    private String message;
    private List<String> uploadedUrls;

    public UploadResponse(int status, String message, List<String> uploadedUrls) {
        this.status = status;
        this.message = message;
        this.uploadedUrls = uploadedUrls;
    }

    // Getters and Setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getUploadedUrls() {
        return uploadedUrls;
    }

    public void setUploadedUrls(List<String> uploadedUrls) {
        this.uploadedUrls = uploadedUrls;
    }
}
