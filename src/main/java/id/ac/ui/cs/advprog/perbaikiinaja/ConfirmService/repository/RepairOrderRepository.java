package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;

public interface RepairOrderRepository {
    RepairOrder findById(Long id);
    RepairOrder save(RepairOrder order);
}
