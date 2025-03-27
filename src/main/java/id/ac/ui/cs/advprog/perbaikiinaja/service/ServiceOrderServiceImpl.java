// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/service/ServiceOrderServiceImpl.java
package id.ac.ui.cs.advprog.perbaikiinaja.service;

import id.ac.ui.cs.advprog.perbaikiinaja.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    private final ServiceOrderRepository repository;

    @Autowired
    public ServiceOrderServiceImpl(ServiceOrderRepository repository) {
        this.repository = repository;
    }

    @Override
    public ServiceOrder createServiceOrder(ServiceOrder order) {
        // Secure coding: validate required fields (e.g., itemName)
        if (order.getItemName() == null || order.getItemName().isEmpty()) {
            throw new IllegalArgumentException("Item name is required");
        }
        // Additional validations can be added here
        return repository.save(order);
    }

    @Override
    public List<ServiceOrder> getAllServiceOrders() {
        return repository.findAll();
    }

    @Override
    public Optional<ServiceOrder> getServiceOrderById(Long id) {
        return repository.findById(id);
    }

    @Override
    public ServiceOrder updateServiceOrder(ServiceOrder order) {
        Optional<ServiceOrder> existing = repository.findById(order.getId());
        if (existing.isPresent()) {
            repository.delete(existing.get());
            return repository.save(order);
        } else {
            throw new IllegalArgumentException("Service order not found");
        }
    }

    @Override
    public void deleteServiceOrder(Long id) {
        Optional<ServiceOrder> existing = repository.findById(id);
        existing.ifPresent(repository::delete);
    }
}