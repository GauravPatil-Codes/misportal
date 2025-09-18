package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MediaEvidence;

import java.util.List;
import java.util.Optional;

public interface MediaEvidenceService {
    
    // CRUD Operations
    MediaEvidence createMediaEvidence(MediaEvidence mediaEvidence);
    List<MediaEvidence> getAllMediaEvidence();
    Optional<MediaEvidence> getMediaEvidenceById(Long id);
    MediaEvidence updateMediaEvidence(Long id, MediaEvidence mediaEvidence);
    void deleteMediaEvidence(Long id);
    
    // Business Operations
    List<MediaEvidence> searchMediaByTitle(String title);
    List<MediaEvidence> getMediaByType(String type);
}