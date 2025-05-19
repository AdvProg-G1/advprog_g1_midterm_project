package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.stereotype.Service;

@Service
public interface RepairReportService {
    RepairReport createRepairReport(String orderId, String details);
    RepairReport getReportsByOrderId(String orderId);
}