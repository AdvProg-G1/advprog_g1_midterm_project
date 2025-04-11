package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;

import java.util.Date;

public class RepairOrderServiceImpl implements RepairOrderService {

    private final RepairOrderRepository repairOrderRepository;

    public RepairOrderServiceImpl(RepairOrderRepository repairOrderRepository) {
        this.repairOrderRepository = repairOrderRepository;
    }

    // Follows Template Method Pattern
    @Override
    public RepairOrder confirmRepairOrder(Long orderId, int estimatedDuration, double estimatedCost) {
        RepairOrder order = repairOrderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("RepairOrder not found with ID: " + orderId);
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot confirm an order that is not in PENDING state.");
        }

        order.setStatus("ACCEPTED");
        order.setEstimatedDuration(estimatedDuration);
        order.setEstimatedCost(estimatedCost);
        order.setConfirmationDate(new Date());
        return repairOrderRepository.save(order);
    }

    @Override
    public RepairOrder rejectRepairOrder(Long orderId) {
        RepairOrder order = repairOrderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("RepairOrder not found with ID: " + orderId);
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot reject an order that is not in PENDING state.");
        }

        order.setStatus("REJECTED");
        order.setConfirmationDate(new Date());
        return repairOrderRepository.save(order);
    }
}
