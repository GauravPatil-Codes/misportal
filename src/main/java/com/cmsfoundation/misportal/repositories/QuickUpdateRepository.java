package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.QuickUpdate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuickUpdateRepository extends JpaRepository<QuickUpdate, Long> {
    
    // Find update by ID
    Optional<QuickUpdate> findById(Long id);
    
    // Find updates by project
    List<QuickUpdate> findByProject(String project);
    
    // Find updates by status
    List<QuickUpdate> findByStatus(String status);
    
    // Find updates by update type
    List<QuickUpdate> findByUpdateType(String updateType);
    
    // Get updates by time range
    @Query("SELECT q FROM QuickUpdate q WHERE q.createdAt BETWEEN :startDate AND :endDate")
    List<QuickUpdate> getUpdatesByTime(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Get pending reports count
    @Query("SELECT COUNT(q) FROM QuickUpdate q WHERE LOWER(q.status) = 'pending'")
    Long getPendingReportsCount();
    
    // Get updates by status count
    @Query("SELECT q.status, COUNT(q) FROM QuickUpdate q GROUP BY q.status")
    List<Object[]> getUpdateCountByStatus();
    
    // Get recent updates (last 30 days)
    @Query("SELECT q FROM QuickUpdate q WHERE q.createdAt >= :thirtyDaysAgo ORDER BY q.createdAt DESC")
    List<QuickUpdate> getRecentUpdates(@Param("thirtyDaysAgo") LocalDateTime thirtyDaysAgo);
}