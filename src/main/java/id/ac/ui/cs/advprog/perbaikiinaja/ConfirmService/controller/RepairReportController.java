package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairReportServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/report")
public class RepairReportController {
    @Autowired
    private RepairReportServiceImpl service;

    @PostMapping("/{orderId}")
    public ResponseEntity<RepairReport> createReport(
            @PathVariable String orderId,
            @RequestBody String details
    ) {
        RepairReport created = service.createRepairReport(orderId, details);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllReports() {
        return ResponseEntity.ok(new ArrayList<>());
    }

}
