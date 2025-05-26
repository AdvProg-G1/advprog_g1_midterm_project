package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository repository;

    @Override
    public ServiceOrder create(ServiceOrder serviceOrder) {
        return repository.save(serviceOrder);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceOrder> findById(UUID id) {
        return repository.findById(id);
    }

    @Override
    public Optional<ServiceOrder> update(UUID id, ServiceOrder updatedOrder) {
        return repository.findById(id).map(existingOrder -> {
            existingOrder.setItemName(updatedOrder.getItemName());
            existingOrder.setCondition(updatedOrder.getCondition());
            existingOrder.setProblemDescription(updatedOrder.getProblemDescription());
            existingOrder.setServiceDate(updatedOrder.getServiceDate());
            existingOrder.setCouponApplied(updatedOrder.isCouponApplied());
            existingOrder.setPaymentMethod(updatedOrder.getPaymentMethod());
            existingOrder.setTechnicianId(updatedOrder.getTechnicianId());
            existingOrder.setEstimatedCompletionTime(updatedOrder.getEstimatedCompletionTime());
            existingOrder.setEstimatedPrice(updatedOrder.getEstimatedPrice());
            existingOrder.setStatus(updatedOrder.getStatus());
            return repository.save(existingOrder);
        });
    }

    @Override
    public boolean delete(UUID id) {
        return repository.findById(id).map(order -> {
            repository.delete(order);
            return true;
        }).orElse(false);
    }

    @Override
    public ServiceOrder createOrder(ServiceOrder order) {
        // Initialize status to WAITING_CONFIRMATION if null
        if (order.getStatus() == null) {
            order.setStatus("WAITING CONFIRMATION");
        }
        return repository.save(order);
    }

    @Override
    public List<ServiceOrder> getAllOrders() {
        return repository.findAll();
    }

    @Override
    public ServiceOrder getOrderById(UUID id) {
        return repository.findById(id).orElse(null);
    }

    @Override
    public List<ServiceOrder> findOrdersByTechnicianId(String technicianId) {
        return repository.findByTechnicianId(technicianId);
    }

    @Override
    public List<ServiceOrder> findOrdersByUserId(String userId) {
        return repository.findByUserId(userId);
    }

}
