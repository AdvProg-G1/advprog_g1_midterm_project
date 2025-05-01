package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RepairOrderRepository {
    RepairOrder findById (Long id);
    List<RepairOrder> getAllRepairOrders();
    RepairOrder createRepairOrder(RepairOrder repairOrder);
    void deleteById(Long id);
}
