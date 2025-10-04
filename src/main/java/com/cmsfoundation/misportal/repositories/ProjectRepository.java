package com.cmsfoundation.misportal.repositories;

import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

	// Find project by ID
	Optional<Project> findById(Long id);

	// Find projects by status
	List<Project> findByProjectStatus(String projectStatus);

	// Find projects by theme
	List<Project> findByProjectTheme(String projectTheme);

	

	// Find projects by project type (IVDP/Thematic)
	List<Project> findByProjectType(String projectType);

	// Search projects by name or description
	@Query("SELECT p FROM Project p WHERE LOWER(p.projectName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
			+ "LOWER(p.projectDescription) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR "
			+ "LOWER(p.projectTheme) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
	List<Project> searchProjectsByString(@Param("searchTerm") String searchTerm);

	// Get projects by month (created in specific month)
	@Query("SELECT p FROM Project p WHERE MONTH(p.projectCreatedAt) = :month AND YEAR(p.projectCreatedAt) = :year")
	List<Project> getProjectsByMonth(@Param("month") int month, @Param("year") int year);

	// Get projects by performance range (based on budget utilization or other
	// metrics)
	@Query("SELECT p FROM Project p WHERE " + "CASE WHEN p.budget.totalBudget > 0 THEN "
			+ "((p.budget.procurementCost + p.budget.trainingCost + p.budget.civilConstructionCost + "
			+ "p.budget.contingencyMiscellaneousCost + p.budget.humanResourcesCost + p.budget.adminCost + "
			+ "p.budget.managementAndCoordinationCost + p.budget.governmentConvergenceCost) / p.budget.totalBudget * 100) "
			+ "ELSE 0 END >= :minPercentage AND " + "CASE WHEN p.budget.totalBudget > 0 THEN "
			+ "((p.budget.procurementCost + p.budget.trainingCost + p.budget.civilConstructionCost + "
			+ "p.budget.contingencyMiscellaneousCost + p.budget.humanResourcesCost + p.budget.adminCost + "
			+ "p.budget.managementAndCoordinationCost + p.budget.governmentConvergenceCost) / p.budget.totalBudget * 100) "
			+ "ELSE 0 END <= :maxPercentage")
	List<Project> getProjectsByPerformance(@Param("minPercentage") double minPercentage,
			@Param("maxPercentage") double maxPercentage);

	// Get all IVDP projects
	@Query("SELECT p FROM Project p WHERE LOWER(p.projectType) = 'ivdp'")
	List<Project> getAllIVDPProjects();

	// Get all thematic projects
	@Query("SELECT p FROM Project p WHERE LOWER(p.projectType) = 'thematic'")
	List<Project> getAllThematicProjects();

	// Calculate total budget across all projects
	@Query("SELECT SUM(p.budget.totalBudget) FROM Project p")
	Double getTotalBudgetAllProjects();

	// Calculate total budget utilized across all projects
	@Query("SELECT SUM(p.budget.procurementCost + p.budget.trainingCost + p.budget.civilConstructionCost + " +
		       "p.budget.contingencyMiscellaneousCost + p.budget.humanResourcesCost + p.budget.adminCost + " +
		       "p.budget.managementAndCoordinationCost + p.budget.governmentConvergenceCost) FROM Project p")
		Double getTotalBudgetUtilized();


	// Get total beneficiaries across all projects
	@Query("SELECT SUM(CAST(p.expectedBeneficiaries AS INTEGER)) FROM Project p WHERE p.expectedBeneficiaries IS NOT NULL")
	Integer getTotalBeneficiaries();

	// Get project analytics - count by status
	@Query("SELECT p.projectStatus, COUNT(p) FROM Project p GROUP BY p.projectStatus")
	List<Object[]> getProjectCountByStatus();

	// Get project analytics - count by theme
	@Query("SELECT p.projectTheme, COUNT(p) FROM Project p GROUP BY p.projectTheme")
	List<Object[]> getProjectCountByTheme();
	
	
	
	// ✅ NEW: NGO-based queries
    List<Project> findByNgoPartner(NGO ngo);
    List<Project> findByNgoPartnerId(Long ngoId);
    List<Project> findByNgoPartnerNgoName(String ngoName);
    List<Project> findByProjectNgoPartnerName(String ngoPartnerName);
    
    // ✅ NEW: Project Manager queries
    List<Project> findByProjectManager(User projectManager);
    List<Project> findByProjectManagerId(Long projectManagerId);
    
    // ✅ ENHANCED: Performance-based queries
    @Query("SELECT p FROM Project p WHERE p.id IN " +
           "(SELECT DISTINCT mt.project.id FROM MonthlyTarget mt WHERE " +
           "COALESCE(mt.achievementPercentage, 0) >= :minPerformance AND " +
           "COALESCE(mt.achievementPercentage, 0) <= :maxPerformance)")
    List<Project> findByPerformanceRange(@Param("minPerformance") Double minPerformance, 
                                       @Param("maxPerformance") Double maxPerformance);
    
    // ✅ NEW: Financial queries
    @Query("SELECT SUM(COALESCE(b.totalBudget, 0)) FROM Project p JOIN p.budget b WHERE p.ngoPartner.id = :ngoId")
    Double getTotalBudgetByNGO(@Param("ngoId") Long ngoId);
    
    @Query("SELECT p FROM Project p WHERE p.projectStatus = :status AND p.ngoPartner.id = :ngoId")
    List<Project> findByStatusAndNgoId(@Param("status") String status, @Param("ngoId") Long ngoId);
    
    // Analytics queries
    @Query("SELECT COUNT(p), p.projectStatus FROM Project p WHERE p.ngoPartner.id = :ngoId GROUP BY p.projectStatus")
    List<Object[]> getProjectCountByStatusAndNgo(@Param("ngoId") Long ngoId);
    
    @Query("SELECT MONTH(p.projectStartDate), COUNT(p) FROM Project p WHERE YEAR(p.projectStartDate) = :year GROUP BY MONTH(p.projectStartDate)")
    List<Object[]> getProjectCountByMonth(@Param("year") Integer year);
}