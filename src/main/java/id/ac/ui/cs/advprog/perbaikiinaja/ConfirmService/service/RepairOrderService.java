package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;

import java.util.List;

public interface RepairOrderService {
    RepairOrder confirmRepairOrder(Long orderId, int estimatedDuration, double estimatedCost);
    RepairOrder rejectRepairOrder(Long orderId);
    List<RepairOrder> getAllRepairOrders();
    RepairOrder createRepairOrder(RepairOrder repairOrder);
}
