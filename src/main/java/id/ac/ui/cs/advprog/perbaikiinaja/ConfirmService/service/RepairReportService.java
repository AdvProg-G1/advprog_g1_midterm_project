package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RepairReportService {
    ServiceOrder createRepairReport(Long orderId, Long technicianId, String details);
    List<ServiceOrder> getReportsByOrderId(Long orderId);
}