package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.NGO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NGORepository extends JpaRepository<NGO, Long> {
    
    // Find NGO by ID
    Optional<NGO> findById(Long id);
    
    // Search NGO by name, location, or founder
    @Query("SELECT n FROM NGO n WHERE LOWER(n.ngoName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(n.location) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
           "LOWER(n.founder) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
    List<NGO> searchNGOByString(@Param("searchTerm") String searchTerm);
    
    // Find NGO by name
    Optional<NGO> findByNgoName(String ngoName);
    
    // Find NGOs by location
    List<NGO> findByLocation(String location);
    
    // Find NGOs by founder
    List<NGO> findByFounder(String founder);
}