package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairReportRepository {
    RepairReport createRepairReport(RepairReport report);
    List<RepairReport> getReportsByOrderId(Long orderId);
}
