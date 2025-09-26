package com.cmsfoundation.misportal.controllers;

import com.cmsfoundation.misportal.entities.FinancialTracking;
import com.cmsfoundation.misportal.services.FinancialTrackingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/financial-tracking")
//@CrossOrigin(origins = "*")
public class FinancialTrackingController {
    
    @Autowired
    private FinancialTrackingService financialTrackingService;
    
    // CRUD Operations
    
    @PostMapping
    public ResponseEntity<FinancialTracking> createFinancialTracking(@RequestBody FinancialTracking financialTracking) {
        try {
            FinancialTracking savedFinancialTracking = financialTrackingService.createFinancialTracking(financialTracking);
            return new ResponseEntity<>(savedFinancialTracking, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<FinancialTracking>> getAllFinancialTracking() {
        List<FinancialTracking> financialTrackingList = financialTrackingService.getAllFinancialTracking();
        return new ResponseEntity<>(financialTrackingList, HttpStatus.OK);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<FinancialTracking> getFinancialTrackingById(@PathVariable Long id) {
        Optional<FinancialTracking> financialTracking = financialTrackingService.getFinancialTrackingById(id);
        return financialTracking.map(ft -> new ResponseEntity<>(ft, HttpStatus.OK))
                               .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<FinancialTracking> updateFinancialTracking(@PathVariable Long id, @RequestBody FinancialTracking financialTracking) {
        try {
            FinancialTracking updatedFinancialTracking = financialTrackingService.updateFinancialTracking(id, financialTracking);
            return new ResponseEntity<>(updatedFinancialTracking, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFinancialTracking(@PathVariable Long id) {
        try {
            financialTrackingService.deleteFinancialTracking(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    // Business Operations
    
    @GetMapping("/thematic")
    public ResponseEntity<List<FinancialTracking>> getFinanceByThematic() {
        List<FinancialTracking> thematicFinance = financialTrackingService.getFinanceByThematic();
        return new ResponseEntity<>(thematicFinance, HttpStatus.OK);
    }
    
    @GetMapping("/ivdp")
    public ResponseEntity<List<FinancialTracking>> getFinanceByIVDP() {
        List<FinancialTracking> ivdpFinance = financialTrackingService.getFinanceByIVDP();
        return new ResponseEntity<>(ivdpFinance, HttpStatus.OK);
    }
    
    @GetMapping("/ivdp/{id}")
    public ResponseEntity<FinancialTracking> getIVDPFinanceById(@PathVariable Long id) {
        Optional<FinancialTracking> ivdpFinance = financialTrackingService.getIVDPFinanceById(id);
        return ivdpFinance.map(ft -> new ResponseEntity<>(ft, HttpStatus.OK))
                         .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/thematic/{id}")
    public ResponseEntity<FinancialTracking> getThematicFinanceById(@PathVariable Long id) {
        Optional<FinancialTracking> thematicFinance = financialTrackingService.getThematicFinanceById(id);
        return thematicFinance.map(ft -> new ResponseEntity<>(ft, HttpStatus.OK))
                             .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    @GetMapping("/budget-utilized")
    public ResponseEntity<Map<String, Double>> getOverallBudgetUtilized() {
        Map<String, Double> budgetInfo = financialTrackingService.getOverallBudgetUtilized();
        return new ResponseEntity<>(budgetInfo, HttpStatus.OK);
    }
    
    @GetMapping("/performance")
    public ResponseEntity<Map<String, Object>> getFinancialPerformance() {
        Map<String, Object> performance = financialTrackingService.getFinancialPerformance();
        return new ResponseEntity<>(performance, HttpStatus.OK);
    }
}