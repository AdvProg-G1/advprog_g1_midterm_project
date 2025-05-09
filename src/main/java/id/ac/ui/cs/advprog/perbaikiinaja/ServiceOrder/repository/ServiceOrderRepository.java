package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ServiceOrderRepository extends JpaRepository<ServiceOrder, UUID> {
    List<ServiceOrder> findByTechnicianId(String technicianId);
}