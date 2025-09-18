package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MediaEvidence;
import com.cmsfoundation.misportal.repositories.MediaEvidenceRepository;
import com.cmsfoundation.misportal.services.MediaEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MediaEvidenceServiceImpl implements MediaEvidenceService {
    
    @Autowired
    private MediaEvidenceRepository mediaEvidenceRepository;
    
    @Override
    public MediaEvidence createMediaEvidence(MediaEvidence mediaEvidence) {
        return mediaEvidenceRepository.save(mediaEvidence);
    }
    
    @Override
    public List<MediaEvidence> getAllMediaEvidence() {
        return mediaEvidenceRepository.findAll();
    }
    
    @Override
    public Optional<MediaEvidence> getMediaEvidenceById(Long id) {
        return mediaEvidenceRepository.findById(id);
    }
    
    @Override
    public MediaEvidence updateMediaEvidence(Long id, MediaEvidence mediaEvidence) {
        if (mediaEvidenceRepository.existsById(id)) {
            mediaEvidence.setId(id);
            return mediaEvidenceRepository.save(mediaEvidence);
        }
        throw new RuntimeException("Media Evidence not found with id: " + id);
    }
    
    @Override
    public void deleteMediaEvidence(Long id) {
        if (mediaEvidenceRepository.existsById(id)) {
            mediaEvidenceRepository.deleteById(id);
        } else {
            throw new RuntimeException("Media Evidence not found with id: " + id);
        }
    }
    
    @Override
    public List<MediaEvidence> searchMediaByTitle(String title) {
        return mediaEvidenceRepository.findByTitleContainingIgnoreCase(title);
    }
    
    @Override
    public List<MediaEvidence> getMediaByType(String type) {
        return mediaEvidenceRepository.findByMediaEvidence(type);
    }
}