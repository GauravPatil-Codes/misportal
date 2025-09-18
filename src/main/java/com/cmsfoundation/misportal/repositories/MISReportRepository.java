package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MISReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MISReportRepository extends JpaRepository<MISReport, Long> {
    
    // Find MIS report by ID
    Optional<MISReport> findById(Long id);
    
    // Find reports by project
    List<MISReport> findByProject(String project);
    
    // Find reports by reporting period
    List<MISReport> findByReportingPeriod(String reportingPeriod);
    
    // Get reports by submission date range
    @Query("SELECT m FROM MISReport m WHERE m.misReportSubmissionDateTime BETWEEN :startDate AND :endDate")
    List<MISReport> getReportsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Get reports by achievement percentage range
    @Query("SELECT m FROM MISReport m WHERE CAST(m.achievementPercentage AS DOUBLE) BETWEEN :minPercentage AND :maxPercentage")
    List<MISReport> getReportsByAchievementRange(@Param("minPercentage") double minPercentage, @Param("maxPercentage") double maxPercentage);
    
    // Count reports by project
    @Query("SELECT COUNT(m) FROM MISReport m WHERE m.project = :project")
    Long countReportsByProject(@Param("project") String project);
}