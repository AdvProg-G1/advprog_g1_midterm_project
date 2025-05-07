package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;

public interface RepairOrderService {
    ServiceOrder confirmRepairOrder(Long orderId, int estimatedDuration, double estimatedCost);
    ServiceOrder rejectRepairOrder(Long orderId);
}
