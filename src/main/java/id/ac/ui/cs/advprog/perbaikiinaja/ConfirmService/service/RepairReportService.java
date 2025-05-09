package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface RepairReportService {
    RepairReport createRepairReport(String orderId, String technicianId, String details);
    List<RepairReport> getReportsByOrderId(UUID orderId);
}