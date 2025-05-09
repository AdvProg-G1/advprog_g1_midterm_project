package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RepairReportRepository extends JpaRepository<RepairReport, String> {
    RepairReport createRepairReport(RepairReport report);
    List<RepairReport> getReportsByOrderId(UUID orderId);
}
