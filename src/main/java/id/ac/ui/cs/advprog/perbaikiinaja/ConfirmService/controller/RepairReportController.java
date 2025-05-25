package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairReportServiceImpl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("api/report")
public class RepairReportController {
    @Autowired
    private RepairReportServiceImpl service;

    @PostMapping("/{orderId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<RepairReport> createReport(
            @PathVariable String orderId,
            @RequestBody String details
    ) {
        log.debug("POST api/report/{} details=\"{}\"", orderId, details);
        RepairReport created = service.createRepairReport(orderId, details);

        log.info("Created report id={} for order={} by tech={}  details=\"{}\"",
                created.getId(), created.getOrderId(),
                created.getTechnicianId(), created.getDetails());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping("/all")
    public ResponseEntity<List<Map<String, Object>>> getAllReports() {

        return ResponseEntity.ok(service.getAllReports());
    }
}
