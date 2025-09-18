package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.MediaEvidence;
import com.cmsfoundation.misportal.services.MediaEvidenceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/media-evidence")
@CrossOrigin(origins = "*")
public class MediaEvidenceController {
    
    @Autowired
    private MediaEvidenceService mediaEvidenceService;
    
    // CRUD Operations
    
    @PostMapping
    public ResponseEntity<MediaEvidence> createMediaEvidence(@RequestBody MediaEvidence mediaEvidence) {
        try {
            MediaEvidence savedMediaEvidence = mediaEvidenceService.createMediaEvidence(mediaEvidence);
            return new ResponseEntity<>(savedMediaEvidence, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<MediaEvidence>> getAllMediaEvidence() {
        List<MediaEvidence> mediaEvidenceList = mediaEvidenceService.getAllMediaEvidence();
        return new ResponseEntity<>(mediaEvidenceList, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<MediaEvidence> getMediaEvidenceById(@PathVariable Long id) {
        Optional<MediaEvidence> mediaEvidence = mediaEvidenceService.getMediaEvidenceById(id);
        return mediaEvidence.map(me -> new ResponseEntity<>(me, HttpStatus.OK))
                           .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<MediaEvidence> updateMediaEvidence(@PathVariable Long id, @RequestBody MediaEvidence mediaEvidence) {
        try {
            MediaEvidence updatedMediaEvidence = mediaEvidenceService.updateMediaEvidence(id, mediaEvidence);
            return new ResponseEntity<>(updatedMediaEvidence, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMediaEvidence(@PathVariable Long id) {
        try {
            mediaEvidenceService.deleteMediaEvidence(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Business Operations
    
    @GetMapping("/search")
    public ResponseEntity<List<MediaEvidence>> searchMediaByTitle(@RequestParam String title) {
        List<MediaEvidence> mediaList = mediaEvidenceService.searchMediaByTitle(title);
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }
    
    @GetMapping("/type/{type}")
    public ResponseEntity<List<MediaEvidence>> getMediaByType(@PathVariable String type) {
        List<MediaEvidence> mediaList = mediaEvidenceService.getMediaByType(type);
        return new ResponseEntity<>(mediaList, HttpStatus.OK);
    }
}