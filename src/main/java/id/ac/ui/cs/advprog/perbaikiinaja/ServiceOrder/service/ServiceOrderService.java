package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ServiceOrderService {
    ServiceOrder create(ServiceOrder serviceOrder);

    List<ServiceOrder> findAll();

    Optional<ServiceOrder> findById(UUID id);

    Optional<ServiceOrder> update(UUID id, ServiceOrder updatedOrder);

    boolean delete(UUID id);

    ServiceOrder createOrder(ServiceOrder order);
    List<ServiceOrder> getAllOrders();
    ServiceOrder getOrderById(UUID id);

    List<ServiceOrder> findOrdersByTechnicianId(String technicianId);
}