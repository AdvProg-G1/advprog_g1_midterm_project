package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RepairReportService {
    RepairReport createRepairReport(Long orderId, Long technicianId, String details);
    List<RepairReport> getReportsByOrderId(Long orderId);
}