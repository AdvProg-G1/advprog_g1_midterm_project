package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Repository
public interface RepairOrderRepository extends JpaRepository<ServiceOrder, UUID> {
    List<ServiceOrder> findByStatusIn(Collection<String> statuses);
}
