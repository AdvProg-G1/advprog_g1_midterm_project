package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RepairReportServiceImpl implements RepairReportService {

    private final RepairOrderRepository orderRepo;
    private final RepairReportRepository reportRepo;

    public RepairReportServiceImpl(RepairOrderRepository orderRepo,
                                   RepairReportRepository reportRepo) {
        this.orderRepo  = orderRepo;
        this.reportRepo = reportRepo;
    }

    @Override
    public RepairReport createRepairReport(Long orderId, Long technicianId, String details) {
        ServiceOrder order = orderRepo.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("RepairOrder not found with ID: " + orderId);
        }
        if (!"COMPLETED".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot report on order not in COMPLETED state.");
        }

        RepairReport report = RepairReport.builder()
                .orderId(orderId)
                .technicianId(technicianId)
                .details(details)
                .createdAt(new Date())
                .build();

        return reportRepo.createRepairReport(report);
    }

    @Override
    public List<RepairReport> getReportsByOrderId(Long orderId) {
        return reportRepo.getReportsByOrderId(orderId);
    }
}