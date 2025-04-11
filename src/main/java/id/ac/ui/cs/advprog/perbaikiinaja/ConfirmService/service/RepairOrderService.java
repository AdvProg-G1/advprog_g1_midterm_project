package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;

public interface RepairOrderService {
    RepairOrder confirmRepairOrder(Long orderId, int estimatedDuration, double estimatedCost);
    RepairOrder rejectRepairOrder(Long orderId);
}
