package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairReportRepository extends JpaRepository<RepairReport, String> {
    RepairReport getReportsByOrderId(String orderId);
}
