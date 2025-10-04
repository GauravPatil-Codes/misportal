package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.ReportStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MISReportRepository extends JpaRepository<MISReport, Long> {
    
    // ✅ CORRECTED: Use Project entity, not String
    List<MISReport> findByProject(Project project);
    
    // ✅ RECOMMENDED: Find by project ID (easier to use)
    List<MISReport> findByProjectId(Long projectId);
    
    // Find reports by reporting period
    List<MISReport> findByReportingPeriod(String reportingPeriod);
    
    // Get reports by submission date range
    @Query("SELECT m FROM MISReport m WHERE m.misReportSubmissionDateTime BETWEEN :startDate AND :endDate")
    List<MISReport> getReportsByDateRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
    
    // Get reports by achievement percentage range
    @Query("SELECT m FROM MISReport m WHERE CAST(m.achievementPercentage AS DOUBLE) BETWEEN :minPercentage AND :maxPercentage")
    List<MISReport> getReportsByAchievementRange(@Param("minPercentage") double minPercentage, @Param("maxPercentage") double maxPercentage);
    
    // ✅ CORRECTED: Use Project entity parameter
    @Query("SELECT COUNT(m) FROM MISReport m WHERE m.project = :project")
    Long countReportsByProject(@Param("project") Project project);
    
    // ✅ NEW: Count by project ID (more practical)
    Long countByProjectId(Long projectId);
    
    // Your existing methods (these are correct)
    List<MISReport> findByProjectNgoPartnerId(Long ngoId);
    List<MISReport> findBySubmittedById(Long userId);
    List<MISReport> findByReportStatus(ReportStatus status);
    List<MISReport> findByProjectIdAndReportStatus(Long projectId, ReportStatus status);
    
    @Query("SELECT mr FROM MISReport mr WHERE mr.project.ngoPartner.id = :ngoId AND mr.reportStatus = :status")
    List<MISReport> findByNgoIdAndStatus(@Param("ngoId") Long ngoId, @Param("status") ReportStatus status);
}
