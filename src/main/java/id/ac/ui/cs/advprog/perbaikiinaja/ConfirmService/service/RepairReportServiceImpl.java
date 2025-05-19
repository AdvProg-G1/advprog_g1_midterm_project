package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.UUID;

@Service
public class RepairReportServiceImpl implements RepairReportService {
    private final RepairReportRepository reportRepo;
    private final RepairOrderRepository orderRepo;

    public RepairReportServiceImpl(RepairReportRepository reportRepo,
                                   RepairOrderRepository orderRepo) {
        this.reportRepo = reportRepo;
        this.orderRepo  = orderRepo;
    }

    @Override
    public RepairReport getReportsByOrderId(String orderId) {
        return reportRepo.getReportsByOrderId(orderId);
    }

    @Override
    public RepairReport createRepairReport(String orderId, String details) {
        ServiceOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        String technicianId = order.getTechnicianId();
        if (!"completed".equals(order.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot report on order not in COMPLETED state.");

        RepairReport rpt = RepairReport.builder()
                .orderId(orderId)
                .technicianId(technicianId)
                .details(details)
                .createdAt(new Date())
                .build();

        return reportRepo.save(rpt);
    }
}