package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class RepairReportServiceImpl implements RepairReportService {
    private final RepairReportRepository reportRepo;
    private final RepairOrderRepository orderRepo;
    private final UserRepository userRepo;

    public RepairReportServiceImpl(RepairReportRepository reportRepo,
                                   RepairOrderRepository orderRepo,
                                   UserRepository userRepo) {
        this.reportRepo = reportRepo;
        this.orderRepo  = orderRepo;
        this.userRepo = userRepo;
    }

    @Override
    public RepairReport getReportsByOrderId(String orderId) {
        return reportRepo.getReportsByOrderId(orderId);
    }

    @Override
    public RepairReport createRepairReport(String orderId, String details) {
        ServiceOrder order = orderRepo.findById(UUID.fromString(orderId)).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Order not found"));
        String technicianId = order.getTechnicianId();

        if (!"IN_PROGRESS".equals(order.getStatus()))
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot report on order not in IN_PROGRESS state.");

        RepairReport rpt = RepairReport.builder()
                .orderId(orderId)
                .technicianId(technicianId)
                .details(details)
                .createdAt(new Date())
                .build();

        order.setStatus("COMPLETED");
        orderRepo.save(order);

        User tech = userRepo.findById(order.getTechnicianId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Technician not found"));

        int prevSalary = tech.getTotalSalary() != null ? tech.getTotalSalary() : 0;
        int prevWork   = tech.getTotalWork()   != null ? tech.getTotalWork()   : 0;

        tech.setTotalSalary(prevSalary + (int)order.getEstimatedPrice());
        tech.setTotalWork  (prevWork   + 1);

        userRepo.save(tech);
        return reportRepo.save(rpt);
    }
    
    @Override
    public List<Map<String, Object>> getAllReports() {
        return new ArrayList<>();
    }

}