package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.Project;
import com.cmsfoundation.misportal.entities.User;
import com.cmsfoundation.misportal.entities.BudgetAllocationItem;
import com.cmsfoundation.misportal.entities.BudgetHead;
import com.cmsfoundation.misportal.entities.MonthlyTarget;
import com.cmsfoundation.misportal.entities.NGO;
import com.cmsfoundation.misportal.entities.Budget;
import com.cmsfoundation.misportal.dtos.ProjectCreateRequest;
import com.cmsfoundation.misportal.dtos.BudgetAllocationItemDTO;
import com.cmsfoundation.misportal.dtos.MonthlyTargetItemDTO;
import com.cmsfoundation.misportal.repositories.ProjectRepository;
import com.cmsfoundation.misportal.repositories.UserRepository;
import com.cmsfoundation.misportal.repositories.MonthlyTargetRepository;
import com.cmsfoundation.misportal.repositories.NGORepository;
import com.cmsfoundation.misportal.repositories.BudgetAllocationItemRepository;
import com.cmsfoundation.misportal.services.BudgetAllocationItemService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ProjectServiceImpl implements ProjectService {

	@Autowired
	private ProjectRepository projectRepository;

	@Autowired
	private MonthlyTargetRepository monthlyTargetRepository;

	@Autowired
	private BudgetAllocationItemRepository budgetAllocationItemRepository;

	@Autowired
	private BudgetAllocationItemService budgetAllocationItemService;

	@Autowired
	private NGORepository ngoRepository;

	@Autowired
	private UserRepository userRepository;

	@Override
	@Transactional
	public Project createProjectWithDetails(ProjectCreateRequest request) {
		// Create basic project entity
		Project project = new Project();
		project.setProjectType(request.getProjectType());
		project.setProjectHead(request.getProjectHead());
		project.setProjectName(request.getProjectName());
		project.setProjectTheme(request.getProjectTheme());
		project.setProjectNgoPartner(request.getProjectNgoPartner());
		project.setExpectedBeneficiaries(request.getExpectedBeneficiaries());
		project.setProjectLocation(request.getProjectLocation());
		project.setProjectStartDate(request.getProjectStartDate());
		project.setProjectEndDate(request.getProjectEndDate());
		project.setProjectDescription(request.getProjectDescription());
		project.setProjectObjectives(request.getProjectObjectives());
		project.setProjectdpr(request.getProjectdpr());
		project.setProjectStatus(request.getProjectStatus());

		// Set embedded objects for backward compatibility
		if (request.getBudget() != null) {
			project.setBudget(request.getBudget());
		}

		if (request.getWorkPlan() != null) {
			project.setWorkPlan(request.getWorkPlan());
		}

		// Save project first
		Project savedProject = projectRepository.save(project);

		// Convert and save budget allocation items with monthly targets
		if (request.getBudgetAllocationItems() != null && !request.getBudgetAllocationItems().isEmpty()) {
			List<BudgetAllocationItem> savedBudgetItems = createBudgetItemsWithTargets(
					request.getBudgetAllocationItems(), savedProject, request.getMonthlyTargetItems());
			savedProject.setBudgetAllocationItems(savedBudgetItems);

			// Calculate and update budget summary
			Budget budget = savedProject.getBudget() != null ? savedProject.getBudget() : new Budget();
			budget.calculateTotalsFromItems(savedBudgetItems);
			savedProject.setBudget(budget);

			// Save project with updated budget summary
			savedProject = projectRepository.save(savedProject);
		}

		return savedProject;
	}

	@Transactional
	private List<BudgetAllocationItem> createBudgetItemsWithTargets(List<BudgetAllocationItemDTO> budgetItemDTOs,
			Project project, List<MonthlyTargetItemDTO> monthlyTargetDTOs) {

		List<BudgetAllocationItem> savedBudgetItems = new ArrayList<>();

		// Create map of monthly targets by budget item srNo for quick lookup
		Map<String, List<MonthlyTargetItemDTO>> targetsByBudgetItem = new HashMap<>();
		if (monthlyTargetDTOs != null && !monthlyTargetDTOs.isEmpty()) {
			for (MonthlyTargetItemDTO dto : monthlyTargetDTOs) {
				String srNo = dto.getBudgetAllocationItemSrNo();
				targetsByBudgetItem.computeIfAbsent(srNo, k -> new ArrayList<>()).add(dto);
			}
		}

		for (BudgetAllocationItemDTO dto : budgetItemDTOs) {
			// Convert DTO to entity and save
			BudgetAllocationItem budgetItem = convertBudgetAllocationDTO(dto, project);
			BudgetAllocationItem savedBudgetItem = budgetAllocationItemService.createBudgetItem(budgetItem);

			// Create monthly targets for this budget item
			List<MonthlyTarget> monthlyTargets = createMonthlyTargetsForBudgetItem(savedBudgetItem, project,
					targetsByBudgetItem.get(dto.getSrNo()));

			// Save monthly targets
			if (!monthlyTargets.isEmpty()) {
				List<MonthlyTarget> savedTargets = monthlyTargetRepository.saveAll(monthlyTargets);
				savedBudgetItem.setMonthlyTargets(savedTargets);
			}

			savedBudgetItems.add(savedBudgetItem);
		}

		return savedBudgetItems;
	}

	private BudgetAllocationItem convertBudgetAllocationDTO(BudgetAllocationItemDTO dto, Project project) {
		BudgetAllocationItem entity = new BudgetAllocationItem();
		entity.setSrNo(dto.getSrNo());
		entity.setItemName(dto.getItemName());
		entity.setDescription(dto.getDescription());
		entity.setUnits(dto.getUnits());
		entity.setUnitCost(dto.getUnitCost());
		entity.setCmsContribution(dto.getCmsContribution());
		entity.setNgoContribution(dto.getNgoContribution());
		entity.setGovernmentContribution(dto.getGovernmentContribution());
		entity.setBeneficiaryContribution(dto.getBeneficiaryContribution());
		entity.setBudgetType(dto.getBudgetType());
		entity.setProject(project);
		return entity;
	}

	private List<MonthlyTarget> createMonthlyTargetsForBudgetItem(BudgetAllocationItem budgetItem, Project project,
			List<MonthlyTargetItemDTO> targetDTOs) {

		List<MonthlyTarget> monthlyTargets = new ArrayList<>();

		// Generate all months between project start and end date
		LocalDate startDate = project.getProjectStartDate();
		LocalDate endDate = project.getProjectEndDate();

		if (startDate == null || endDate == null) {
			return monthlyTargets; // Return empty list if dates are not set
		}

		YearMonth start = YearMonth.from(startDate);
		YearMonth end = YearMonth.from(endDate);

		// Create map of provided targets by month for quick lookup
		Map<LocalDate, MonthlyTargetItemDTO> providedTargets = new HashMap<>();
		if (targetDTOs != null && !targetDTOs.isEmpty()) {
			for (MonthlyTargetItemDTO dto : targetDTOs) {
				providedTargets.put(dto.getTargetMonth(), dto);
			}
		}

		// Create monthly target for each month in project lifecycle
		for (YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)) {
			LocalDate targetMonth = month.atDay(1);
			MonthlyTargetItemDTO providedTarget = providedTargets.get(targetMonth);

			MonthlyTarget monthlyTarget = new MonthlyTarget();
			monthlyTarget.setTargetMonth(targetMonth);
			monthlyTarget.setBudgetAllocationItem(budgetItem);
			monthlyTarget.setProject(project);

			if (providedTarget != null) {
				// Use provided target data
				monthlyTarget.setPlannedTarget(providedTarget.getPlannedTarget());
				monthlyTarget.setTargetDescription(providedTarget.getTargetDescription());
			} else {
				// Set default values for months without specific targets
				monthlyTarget.setPlannedTarget(0);
				monthlyTarget
						.setTargetDescription("No specific target set for " + month.getMonth() + " " + month.getYear());
			}

			monthlyTargets.add(monthlyTarget);
		}

		return monthlyTargets;
	}

	@Override
	public Project createProject(Project project) {
		return projectRepository.save(project);
	}

	@Override
	public List<Project> getAllProjects() {
		return projectRepository.findAll();
	}

	@Override
	public Optional<Project> getProjectById(Long id) {
		return projectRepository.findById(id);
	}

	@Override
	@Transactional
	public Project updateProject(Long id, Project project) {
		Project existing = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

		// Update basic project fields
		if (project.getProjectType() != null) {
			existing.setProjectType(project.getProjectType());
		}
		if (project.getProjectHead() != null) {
			existing.setProjectHead(project.getProjectHead());
		}
		if (project.getProjectName() != null) {
			existing.setProjectName(project.getProjectName());
		}
		if (project.getProjectTheme() != null) {
			existing.setProjectTheme(project.getProjectTheme());
		}
		if (project.getProjectNgoPartner() != null) {
			existing.setProjectNgoPartner(project.getProjectNgoPartner());
		}
		if (project.getExpectedBeneficiaries() != null) {
			existing.setExpectedBeneficiaries(project.getExpectedBeneficiaries());
		}
		if (project.getProjectLocation() != null) {
			existing.setProjectLocation(project.getProjectLocation());
		}
		if (project.getProjectStartDate() != null) {
			existing.setProjectStartDate(project.getProjectStartDate());
		}
		if (project.getProjectEndDate() != null) {
			existing.setProjectEndDate(project.getProjectEndDate());
		}
		if (project.getProjectDescription() != null) {
			existing.setProjectDescription(project.getProjectDescription());
		}
		if (project.getProjectObjectives() != null) {
			existing.setProjectObjectives(project.getProjectObjectives());
		}
		if (project.getBudget() != null) {
			existing.setBudget(project.getBudget());
		}
		if (project.getWorkPlan() != null) {
			existing.setWorkPlan(project.getWorkPlan());
		}
		if (project.getProjectStatus() != null) {
			existing.setProjectStatus(project.getProjectStatus());
		}

		return projectRepository.save(existing);
	}

	// ✅ NEW: Enhanced updateProjectWithDetails method with proper UPDATE logic
	@Override
	@Transactional
	public Project updateProjectWithDetails(Long id, ProjectCreateRequest request) {
		Project existing = projectRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

		// Update basic fields
		updateBasicProjectFields(existing, request);

		// ✅ UPDATE existing budget allocation items instead of recreating
		if (request.getBudgetAllocationItems() != null && !request.getBudgetAllocationItems().isEmpty()) {
			updateExistingBudgetItems(existing, request.getBudgetAllocationItems(), request.getMonthlyTargetItems());
		}

		// Update budget summary
		Budget budget = existing.getBudget() != null ? existing.getBudget() : new Budget();
		budget.calculateTotalsFromItems(existing.getBudgetAllocationItems());
		existing.setBudget(budget);

		return projectRepository.save(existing);
	}

	// ✅ NEW METHOD: Update existing budget items instead of recreating

	// ✅ UPDATE existing budget item fields
	private void updateBudgetItemFromDTO(BudgetAllocationItem existing, BudgetAllocationItemDTO dto) {
		existing.setItemName(dto.getItemName());
		existing.setDescription(dto.getDescription());
		existing.setUnits(dto.getUnits());
		existing.setUnitCost(dto.getUnitCost());
		existing.setCmsContribution(dto.getCmsContribution());
		existing.setNgoContribution(dto.getNgoContribution());
		existing.setGovernmentContribution(dto.getGovernmentContribution());
		existing.setBeneficiaryContribution(dto.getBeneficiaryContribution());
		existing.setBudgetType(dto.getBudgetType());
		// Don't change srNo or project reference
	}

	// ✅ UPDATE monthly targets for existing budget item

	
	@Transactional
	private void updateMonthlyTargetsForBudgetItem(BudgetAllocationItem budgetItem, Project project, List<MonthlyTargetItemDTO> targetDTOs) {
	    System.out.println("=== UPDATE MONTHLY TARGETS FOR BUDGET ITEM ===");
	    System.out.println("Budget Item ID: " + budgetItem.getId());
	    System.out.println("Budget Item Sr No: " + budgetItem.getSrNo());
	    System.out.println("Target DTOs received: " + (targetDTOs != null ? targetDTOs.size() : 0));
	    
	    if (targetDTOs != null) {
	        for (MonthlyTargetItemDTO dto : targetDTOs) {
	            System.out.println("Target DTO: Month=" + dto.getTargetMonth() + ", Planned=" + dto.getPlannedTarget() + ", Description=" + dto.getTargetDescription());
	        }
	    }
	    
	    // CRITICAL: Get existing targets DIRECTLY from database, not from entity collection
	    List<MonthlyTarget> existingTargets = monthlyTargetRepository.findByBudgetAllocationItemId(budgetItem.getId());
	    if (existingTargets == null) {
	        existingTargets = new ArrayList<>();
	    }
	    System.out.println("Existing targets from DB: " + existingTargets.size());

	    // Create map of provided targets by month
	    Map<LocalDate, MonthlyTargetItemDTO> providedTargets = new HashMap<>();
	    if (targetDTOs != null) {
	        for (MonthlyTargetItemDTO dto : targetDTOs) {
	            providedTargets.put(dto.getTargetMonth(), dto);
	        }
	    }

	    // Generate all months between project start and end date
	    LocalDate startDate = project.getProjectStartDate();
	    LocalDate endDate = project.getProjectEndDate();
	    if (startDate == null || endDate == null) {
	        System.out.println("Project dates are null, skipping monthly target updates");
	        return;
	    }

	    YearMonth start = YearMonth.from(startDate);
	    YearMonth end = YearMonth.from(endDate);

	    // Create map of existing targets by month for quick lookup
	    Map<LocalDate, MonthlyTarget> existingTargetMap = existingTargets.stream()
	            .collect(Collectors.toMap(MonthlyTarget::getTargetMonth, Function.identity()));

	    // UPDATE existing targets and CREATE missing ones
	    List<MonthlyTarget> targetsToSave = new ArrayList<>();
	    
	    for (YearMonth month = start; !month.isAfter(end); month = month.plusMonths(1)) {
	        LocalDate targetMonth = month.atDay(1);
	        MonthlyTargetItemDTO providedTarget = providedTargets.get(targetMonth);
	        MonthlyTarget existingTarget = existingTargetMap.get(targetMonth);

	        if (existingTarget != null) {
	            // UPDATE existing monthly target
	            if (providedTarget != null) {
	                existingTarget.setPlannedTarget(providedTarget.getPlannedTarget());
	                existingTarget.setTargetDescription(providedTarget.getTargetDescription());
	                
	                // 🚨 ADD ALL THESE MISSING UPDATES:
	                existingTarget.setAchievedTarget(providedTarget.getAchievedTarget());
	                existingTarget.setDeviation(providedTarget.getDeviation());
	                existingTarget.setAchievementPercentage(providedTarget.getAchievementPercentage());
	                existingTarget.setDeviationReason(providedTarget.getDeviationReason());
	                existingTarget.setMitigationPlan(providedTarget.getMitigationPlan());
	            } else {
	                // Set default values
	                existingTarget.setPlannedTarget(0);
	                existingTarget.setTargetDescription("No specific target set...");
	                
	                // 🚨 ADD DEFAULT VALUES FOR MISSING FIELDS:
	                existingTarget.setAchievedTarget(0);
	                existingTarget.setDeviation(0);
	                existingTarget.setAchievementPercentage(0.0);
	                existingTarget.setDeviationReason(null);
	                existingTarget.setMitigationPlan(null);
	            }
	            targetsToSave.add(existingTarget);
	        } else {
	            // CREATE new monthly target
	            MonthlyTarget newTarget = new MonthlyTarget();
	            newTarget.setTargetMonth(targetMonth);
	            newTarget.setBudgetAllocationItem(budgetItem);
	            newTarget.setProject(project);
	            
	            if (providedTarget != null) {
	                newTarget.setPlannedTarget(providedTarget.getPlannedTarget());
	                newTarget.setTargetDescription(providedTarget.getTargetDescription());
	                
	                // 🚨 ADD ALL MISSING FIELD ASSIGNMENTS:
	                newTarget.setAchievedTarget(providedTarget.getAchievedTarget());
	                newTarget.setDeviation(providedTarget.getDeviation());
	                newTarget.setAchievementPercentage(providedTarget.getAchievementPercentage());
	                newTarget.setDeviationReason(providedTarget.getDeviationReason());
	                newTarget.setMitigationPlan(providedTarget.getMitigationPlan());
	            } else {
	                // Set defaults for new target
	                newTarget.setPlannedTarget(0);
	                newTarget.setTargetDescription("No specific target set...");
	                newTarget.setAchievedTarget(0);
	                newTarget.setDeviation(0);
	                newTarget.setAchievementPercentage(0.0);
	                newTarget.setDeviationReason(null);
	                newTarget.setMitigationPlan(null);
	            }
	            targetsToSave.add(newTarget);
	        }
	    }

	    // CRITICAL FIX: Save all updated/new targets to database
	    if (!targetsToSave.isEmpty()) {
	        System.out.println("Saving " + targetsToSave.size() + " targets to database");
	        List<MonthlyTarget> savedTargets = monthlyTargetRepository.saveAll(targetsToSave);
	        System.out.println("Successfully saved " + savedTargets.size() + " targets");
	        
	        // VERIFY what was actually saved
	        for (MonthlyTarget saved : savedTargets) {
	            System.out.println("Saved target: ID=" + saved.getId() + ", Month=" + saved.getTargetMonth() + ", Planned=" + saved.getPlannedTarget());
	        }
	        
	        // Update the budgetItem's monthly targets collection with SAVED entities
	        budgetItem.setMonthlyTargets(savedTargets);
	        System.out.println("Updated budget item collection with " + savedTargets.size() + " targets");
	    }

	    // DELETE targets that are outside the new project timeline
	    List<MonthlyTarget> targetsToDelete = existingTargets.stream()
	            .filter(target -> target.getTargetMonth().isBefore(startDate) || 
	                            target.getTargetMonth().isAfter(endDate))
	            .collect(Collectors.toList());
	    
	    if (!targetsToDelete.isEmpty()) {
	        monthlyTargetRepository.deleteAll(targetsToDelete);
	        System.out.println("DELETED " + targetsToDelete.size() + " outdated monthly targets");
	    }
	    
	    System.out.println("=== END UPDATE MONTHLY TARGETS ===");
	}
	

	
	
	@Transactional
	private void updateExistingBudgetItems(Project project, List<BudgetAllocationItemDTO> budgetItemDTOs,
			List<MonthlyTargetItemDTO> monthlyTargetDTOs) {
		// Create lookup maps for quick access
		Map<String, BudgetAllocationItemDTO> newItemsBySrNo = budgetItemDTOs.stream()
				.collect(Collectors.toMap(BudgetAllocationItemDTO::getSrNo, Function.identity()));

		Map<String, List<MonthlyTargetItemDTO>> targetsByBudgetItem = new HashMap<>();
		if (monthlyTargetDTOs != null) {
			for (MonthlyTargetItemDTO dto : monthlyTargetDTOs) {
				targetsByBudgetItem.computeIfAbsent(dto.getBudgetAllocationItemSrNo(), k -> new ArrayList<>()).add(dto);
			}
		}

		// Get existing budget items
		List<BudgetAllocationItem> existingItems = project.getBudgetAllocationItems();
		if (existingItems == null) {
			existingItems = new ArrayList<>();
			project.setBudgetAllocationItems(existingItems);
		}

		// UPDATE existing items and track which ones to keep
		Set<String> processedSrNos = new HashSet<>();

		for (BudgetAllocationItem existingItem : existingItems) {
			String srNo = existingItem.getSrNo();
			BudgetAllocationItemDTO updateDTO = newItemsBySrNo.get(srNo);

			if (updateDTO != null) {
				// UPDATE existing budget item
				updateBudgetItemFromDTO(existingItem, updateDTO);

				// CRITICAL FIX: Save the updated budget item first
				BudgetAllocationItem savedItem = budgetAllocationItemService.updateBudgetItem(existingItem.getId(),
						existingItem);

				// UPDATE monthly targets for this budget item (FIXED METHOD CALL)
				updateMonthlyTargetsForBudgetItem(savedItem, project, targetsByBudgetItem.get(srNo));
				processedSrNos.add(srNo);
			}
		}

		// CREATE new budget items that don't exist
		for (BudgetAllocationItemDTO dto : budgetItemDTOs) {
			if (!processedSrNos.contains(dto.getSrNo())) {
				BudgetAllocationItem newItem = convertBudgetAllocationDTO(dto, project);
				BudgetAllocationItem savedItem = budgetAllocationItemService.createBudgetItem(newItem);

				// Create monthly targets for new item
				List<MonthlyTarget> monthlyTargets = createMonthlyTargetsForBudgetItem(savedItem, project,
						targetsByBudgetItem.get(dto.getSrNo()));

				if (!monthlyTargets.isEmpty()) {
					List<MonthlyTarget> savedTargets = monthlyTargetRepository.saveAll(monthlyTargets);
					savedItem.setMonthlyTargets(savedTargets);
				}

				existingItems.add(savedItem);
			}
		}

		// REMOVE budget items that are no longer in the request
		List<BudgetAllocationItem> itemsToRemove = existingItems.stream()
				.filter(item -> !newItemsBySrNo.containsKey(item.getSrNo())).collect(Collectors.toList());

		// DELETE removed items from database
		for (BudgetAllocationItem itemToRemove : itemsToRemove) {
			budgetAllocationItemService.deleteBudgetItem(itemToRemove.getId());
		}

		// Remove from the project's collection
		existingItems.removeAll(itemsToRemove);
	}

	private void updateBasicProjectFields(Project existing, ProjectCreateRequest request) {
		if (request.getProjectType() != null) {
			existing.setProjectType(request.getProjectType());
		}
		if (request.getProjectHead() != null) {
			existing.setProjectHead(request.getProjectHead());
		}
		if (request.getProjectName() != null) {
			existing.setProjectName(request.getProjectName());
		}
		if (request.getProjectTheme() != null) {
			existing.setProjectTheme(request.getProjectTheme());
		}
		if (request.getProjectNgoPartner() != null) {
			existing.setProjectNgoPartner(request.getProjectNgoPartner());
		}
		if (request.getExpectedBeneficiaries() != null) {
			existing.setExpectedBeneficiaries(request.getExpectedBeneficiaries());
		}
		if (request.getProjectLocation() != null) {
			existing.setProjectLocation(request.getProjectLocation());
		}
		if (request.getProjectStartDate() != null) {
			existing.setProjectStartDate(request.getProjectStartDate());
		}
		if (request.getProjectEndDate() != null) {
			existing.setProjectEndDate(request.getProjectEndDate());
		}
		if (request.getProjectDescription() != null) {
			existing.setProjectDescription(request.getProjectDescription());
		}
		if (request.getProjectObjectives() != null) {
			existing.setProjectObjectives(request.getProjectObjectives());
		}
		if (request.getProjectdpr() != null) {
			existing.setProjectdpr(request.getProjectdpr());
		}
		if (request.getProjectStatus() != null) {
			existing.setProjectStatus(request.getProjectStatus());
		}
		if (request.getBudget() != null) {
			existing.setBudget(request.getBudget());
		}
		if (request.getWorkPlan() != null) {
			existing.setWorkPlan(request.getWorkPlan());
		}
	}

	// ✅ NEW API Methods for Frontend Integration
	@Override
	public List<BudgetAllocationItem> getBudgetAllocationItemsByProject(Long projectId) {
		return budgetAllocationItemService.getBudgetItemsByProject(projectId);
	}

	@Override
	public List<BudgetAllocationItem> getBudgetAllocationItemsByProjectAndBudgetHead(Long projectId,
			BudgetHead budgetHead) {
		return budgetAllocationItemService.getBudgetItemsByProjectAndHead(projectId, budgetHead);
	}

	@Override
	public Map<BudgetHead, Double> getBudgetSummaryByProject(Long projectId) {
		return budgetAllocationItemService.getBudgetSummaryByProject(projectId);
	}

	@Override
	public List<MonthlyTarget> getMonthlyTargetsByProject(Long projectId) {
		Optional<Project> project = projectRepository.findById(projectId);
		return project.map(Project::getMonthlyTargets).orElse(new ArrayList<>());
	}

	@Override
	public List<MonthlyTarget> getMonthlyTargetsByBudgetItem(Long budgetItemId) {
		return monthlyTargetRepository.findByBudgetAllocationItemId(budgetItemId);
	}

	@Override
	public Map<String, Object> getProjectPerformanceMetrics(Long projectId) {
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		if (projectOpt.isPresent()) {
			Project project = projectOpt.get();
			Map<String, Object> metrics = new HashMap<>();

			metrics.put("totalBudget", project.getTotalBudgetFromItems());
			metrics.put("budgetByHead", project.getBudgetSummaryByHead());
			metrics.put("overallPerformance", project.getOverallAchievementPercentage());
			metrics.put("performanceByHead", project.getPerformanceByBudgetHead());
			metrics.put("totalMonthlyTargets",
					project.getMonthlyTargets() != null ? project.getMonthlyTargets().size() : 0);
			metrics.put("budgetItemsCount",
					project.getBudgetAllocationItems() != null ? project.getBudgetAllocationItems().size() : 0);

			return metrics;
		}
		throw new RuntimeException("Project not found with id: " + projectId);
	}

	@Override
	public void deleteProject(Long id) {
		if (projectRepository.existsById(id)) {
			projectRepository.deleteById(id);
		} else {
			throw new RuntimeException("Project not found with id: " + id);
		}
	}

	// ✅ FIXED: Method names to match controller expectations
	@Override
	public Double getTotalBudget() {
		Double total = projectRepository.getTotalBudgetAllProjects();
		return total != null ? total : 0.0;
	}

	@Override
	public Double getBudgetUtilized() {
		Double utilized = projectRepository.getTotalBudgetUtilized();
		return utilized != null ? utilized : 0.0;
	}

	@Override
	public Integer getTotalBeneficiaries() {
		Integer total = projectRepository.getTotalBeneficiaries();
		return total != null ? total : 0;
	}

	@Override
	public List<Project> searchProjectByString(String searchTerm) {
		return projectRepository.searchProjectsByString(searchTerm);
	}

	@Override
	public List<Project> getProjectByStatus(String status) {
		return projectRepository.findByProjectStatus(status);
	}

	@Override
	public List<Project> getProjectByTheme(String theme) {
		return projectRepository.findByProjectTheme(theme);
	}

	@Override
	public List<Project> getProjectByMonth(int month, int year) {
		return projectRepository.getProjectsByMonth(month, year);
	}

	@Override
	public Map<String, Object> getProjectAnalytics() {
		Map<String, Object> analytics = new HashMap<>();
		analytics.put("totalProjects", projectRepository.count());
		analytics.put("totalBudget", getTotalBudget());
		analytics.put("budgetUtilized", getBudgetUtilized());
		analytics.put("totalBeneficiaries", getTotalBeneficiaries());
		analytics.put("projectsByStatus", getProjectCountByStatus());
		analytics.put("projectsByTheme", getProjectCountByTheme());
		return analytics;
	}

	@Override
	public List<Project> getProjectByPerformance(String performanceCategory) {
		switch (performanceCategory.toLowerCase()) {
		case "excellent":
			return projectRepository.getProjectsByPerformance(80.0, 100.0);
		case "good":
			return projectRepository.getProjectsByPerformance(50.0, 79.9);
		case "attention":
			return projectRepository.getProjectsByPerformance(30.0, 49.9);
		case "critical":
			return projectRepository.getProjectsByPerformance(0.0, 29.9);
		default:
			return getAllProjects();
		}
	}

	@Override
	public List<Project> getProjectByNGO(String ngoPartner) {
		return projectRepository.findByNgoPartnerNgoName(ngoPartner);
	}

	@Override
	public List<Project> getAllIVDPProjects() {
		return projectRepository.getAllIVDPProjects();
	}

	@Override
	public List<Project> getAllThematicProjects() {
		return projectRepository.getAllThematicProjects();
	}

	@Override
	public Map<String, Long> getProjectCountByStatus() {
		List<Object[]> results = projectRepository.getProjectCountByStatus();
		Map<String, Long> statusCount = new HashMap<>();
		for (Object[] result : results) {
			statusCount.put((String) result[0], (Long) result[1]);
		}
		return statusCount;
	}

	@Override
	public Map<String, Long> getProjectCountByTheme() {
		List<Object[]> results = projectRepository.getProjectCountByTheme();
		Map<String, Long> themeCount = new HashMap<>();
		for (Object[] result : results) {
			themeCount.put((String) result[0], (Long) result[1]);
		}
		return themeCount;
	}

	// Method to recalculate budget when budget items are updated
	@Override
	@Transactional
	public Project recalculateBudgetSummary(Long projectId) {
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		if (projectOpt.isPresent()) {
			Project project = projectOpt.get();

			// Recalculate budget summary
			if (project.getBudget() == null) {
				project.setBudget(new Budget());
			}

			project.getBudget().calculateTotalsFromItems(project.getBudgetAllocationItems());
			return projectRepository.save(project);
		}
		throw new RuntimeException("Project not found with id: " + projectId);
	}

	// ✅ NEW: Get projects by NGO
	@Override
	public List<Project> getProjectsByNGOId(Long ngoId) {
		return projectRepository.findByNgoPartnerId(ngoId);
	}

	@Override
	public List<Project> getProjectsByNGOName(String ngoName) {
		return projectRepository.findByNgoPartnerNgoName(ngoName);
	}

	// ✅ NEW: Get projects by manager
	@Override
	public List<Project> getProjectsByManager(Long userId) {
		return projectRepository.findByProjectManagerId(userId);
	}

	// ✅ NEW: Create project with NGO assignment
	@Override
	@Transactional
	public Project createProjectWithNGO(ProjectCreateRequest request) {
		Project project = createProjectWithDetails(request);

		// Assign NGO if specified
		if (request.getNgoPartnerId() != null) {
			Optional<NGO> ngoOpt = ngoRepository.findById(request.getNgoPartnerId());
			if (ngoOpt.isPresent()) {
				project.setNgoPartner(ngoOpt.get());
			}
		}

		// Assign Project Manager if specified
		if (request.getProjectManagerId() != null) {
			Optional<User> managerOpt = userRepository.findById(request.getProjectManagerId());
			if (managerOpt.isPresent()) {
				project.setProjectManager(managerOpt.get());
			}
		}

		return projectRepository.save(project);
	}

	// ✅ NEW: Assign NGO to existing project
	@Override
	@Transactional
	public Project assignNGOToProject(Long projectId, Long ngoId) {
		Project project = projectRepository.findById(projectId)
				.orElseThrow(() -> new RuntimeException("Project not found"));

		NGO ngo = ngoRepository.findById(ngoId).orElseThrow(() -> new RuntimeException("NGO not found"));

		project.setNgoPartner(ngo);
		return projectRepository.save(project);
	}

	// ✅ NEW: Get NGO performance for project
	@Override
	public Map<String, Object> getProjectNGOPerformance(Long projectId) {
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		if (projectOpt.isPresent() && projectOpt.get().getNgoPartner() != null) {
			Project project = projectOpt.get();
			NGO ngo = project.getNgoPartner();

			Map<String, Object> performance = new HashMap<>();
			performance.put("ngoName", ngo.getNgoName());
			performance.put("ngoId", ngo.getId());
			performance.put("projectPerformance", project.getCurrentAchievementPercentage());
			performance.put("ngoOverallMetrics", ngo.getNGOPerformanceMetrics());
			performance.put("financialPerformance", project.getFinancialPerformance());

			return performance;
		}
		throw new RuntimeException("Project not found or no NGO assigned");
	}

	// ✅ NEW: Get NGO project dashboard
	@Override
	public Map<String, Object> getNGOProjectDashboard(Long ngoId) {
		NGO ngo = ngoRepository.findById(ngoId).orElseThrow(() -> new RuntimeException("NGO not found"));

		List<Project> projects = projectRepository.findByNgoPartnerId(ngoId);

		Map<String, Object> dashboard = new HashMap<>();
		dashboard.put("ngo", ngo);
		dashboard.put("totalProjects", projects.size());
		dashboard.put("activeProjects",
				projects.stream().filter(p -> "ACTIVE".equalsIgnoreCase(p.getProjectStatus())).count());
		dashboard.put("completedProjects",
				projects.stream().filter(p -> "COMPLETED".equalsIgnoreCase(p.getProjectStatus())).count());
		dashboard.put("totalBudget", projects.stream().mapToDouble(Project::getTotalBudgetFromItems).sum());
		dashboard.put("avgPerformance",
				projects.stream().mapToDouble(Project::getCurrentAchievementPercentage).average().orElse(0.0));
		dashboard.put("projects", projects);

		return dashboard;
	}

	@Override
	public Map<String, Object> getProjectFinancialPerformance(Long projectId) {
		Optional<Project> projectOpt = projectRepository.findById(projectId);
		if (projectOpt.isPresent()) {
			Project project = projectOpt.get();
			return project.getFinancialPerformance();
		}
		throw new RuntimeException("Project not found with id: " + projectId);
	}
}
