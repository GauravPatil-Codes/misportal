package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MediaEvidence;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MediaEvidenceRepository extends JpaRepository<MediaEvidence, Long> {
    
    // Find media evidence by ID
    Optional<MediaEvidence> findById(Long id);
    
    // Find media evidence by title
    List<MediaEvidence> findByTitleContainingIgnoreCase(String title);
    
    // Find media evidence by type
    List<MediaEvidence> findByMediaEvidence(String mediaEvidence);
}