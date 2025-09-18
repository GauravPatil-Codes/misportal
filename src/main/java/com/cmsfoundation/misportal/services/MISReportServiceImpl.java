package com.cmsfoundation.misportal.services;

import com.cmsfoundation.misportal.entities.MISReport;
import com.cmsfoundation.misportal.repositories.MISReportRepository;
import com.cmsfoundation.misportal.services.MISReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class MISReportServiceImpl implements MISReportService {
    
    @Autowired
    private MISReportRepository misReportRepository;
    
    @Override
    public MISReport createMISReport(MISReport misReport) {
        return misReportRepository.save(misReport);
    }
    
    @Override
    public List<MISReport> getAllMISReports() {
        return misReportRepository.findAll();
    }
    
    @Override
    public Optional<MISReport> getMISReportById(Long id) {
        return misReportRepository.findById(id);
    }
    
    @Override
    public MISReport updateMISReport(Long id, MISReport misReport) {
        if (misReportRepository.existsById(id)) {
            misReport.setId(id);
            return misReportRepository.save(misReport);
        }
        throw new RuntimeException("MIS Report not found with id: " + id);
    }
    
    @Override
    public void deleteMISReport(Long id) {
        if (misReportRepository.existsById(id)) {
            misReportRepository.deleteById(id);
        } else {
            throw new RuntimeException("MIS Report not found with id: " + id);
        }
    }
    
    @Override
    public List<MISReport> getReportsByProject(String project) {
        return misReportRepository.findByProject(project);
    }
    
    @Override
    public List<MISReport> getReportsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return misReportRepository.getReportsByDateRange(startDate, endDate);
    }
    
    @Override
    public List<MISReport> getReportsByAchievementRange(double minPercentage, double maxPercentage) {
        return misReportRepository.getReportsByAchievementRange(minPercentage, maxPercentage);
    }
    
    @Override
    public Long countReportsByProject(String project) {
        return misReportRepository.countReportsByProject(project);
    }
}