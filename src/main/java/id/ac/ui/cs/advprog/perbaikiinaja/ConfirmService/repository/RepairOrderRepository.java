package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.springframework.stereotype.Repository;

@Repository
public interface RepairOrderRepository {
    ServiceOrder findById (Long id);
    void deleteById(Long id);
}
