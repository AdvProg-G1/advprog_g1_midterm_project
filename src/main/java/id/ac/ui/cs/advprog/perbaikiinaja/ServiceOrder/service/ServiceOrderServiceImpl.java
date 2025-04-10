package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository serviceOrderRepository;

    private final Map<UUID, ServiceOrder> serviceOrderMap = new HashMap<>();

    @Override
    public ServiceOrder create(ServiceOrder serviceOrder) {
        UUID id = UUID.randomUUID();
        serviceOrder.setId(id);
        serviceOrderMap.put(id, serviceOrder);
        return serviceOrder;
    }

    @Override
    public List<ServiceOrder> findAll() {
        return new ArrayList<>(serviceOrderMap.values());
    }

    @Override
    public Optional<ServiceOrder> findById(UUID id) {
        return Optional.ofNullable(serviceOrderMap.get(id));
    }

    @Override
    public Optional<ServiceOrder> update(UUID id, ServiceOrder updatedOrder) {
        ServiceOrder existing = serviceOrderMap.get(id);
        if (existing != null) {
            if (!"pending".equalsIgnoreCase(existing.getStatus())) {
                throw new IllegalStateException("Only orders with status 'pending' can be updated");
            }
            updatedOrder.setId(id);
            serviceOrderMap.put(id, updatedOrder);
            return Optional.of(updatedOrder);
        }
        return Optional.empty();
    }

    @Override
    public boolean delete(UUID id) {
        ServiceOrder existing = serviceOrderMap.get(id);
        if (existing != null) {
            if (!"pending".equalsIgnoreCase(existing.getStatus())) {
                throw new IllegalStateException("Only orders with status 'pending' can be deleted");
            }
            serviceOrderMap.remove(id);
            return true;
        }
        return false;
    }

    @Override
    public ServiceOrder createOrder(ServiceOrder order) {
        return create(order);
    }

    @Override
    public List<ServiceOrder> getAllOrders() {
        return findAll();
    }

    @Override
    public ServiceOrder getOrderById(UUID id) {
        return serviceOrderMap.get(id);
    }

    @Override
    public List<ServiceOrder> findOrdersByTechnicianId(String technicianId) {
        List<ServiceOrder> result = new ArrayList<>();
        for (ServiceOrder order : serviceOrderMap.values()) {
            if (order.getTechnicianId() != null && order.getTechnicianId().equals(technicianId)) {
                result.add(order);
            }
        }
        return result;
    }
}
