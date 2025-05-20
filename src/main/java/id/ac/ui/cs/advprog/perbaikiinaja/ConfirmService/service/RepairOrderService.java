package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;

import java.util.List;

public interface RepairOrderService {
    ServiceOrder confirmRepairOrder(String id, int duration, int cost);
    void rejectRepairOrder(String id);
    ServiceOrder findById(String id);
    void deleteById(String id);
    List<ServiceOrder> findAll();
    List<ServiceOrder> findByStatus(List<String> status);
}
