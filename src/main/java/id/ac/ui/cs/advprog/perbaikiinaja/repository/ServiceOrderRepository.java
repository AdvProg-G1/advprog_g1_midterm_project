// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/repository/ServiceOrderRepository.java
package id.ac.ui.cs.advprog.perbaikiinaja.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.model.ServiceOrder;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class ServiceOrderRepository {
    private final List<ServiceOrder> serviceOrders = new ArrayList<>();
    private final AtomicLong idCounter = new AtomicLong();

    public ServiceOrder save(ServiceOrder order) {
        if (order.getId() == null) {
            order.setId(idCounter.incrementAndGet());
        }
        serviceOrders.add(order);
        return order;
    }

    public List<ServiceOrder> findAll() {
        return serviceOrders;
    }

    public Optional<ServiceOrder> findById(Long id) {
        return serviceOrders.stream().filter(o -> o.getId().equals(id)).findFirst();
    }

    public void delete(ServiceOrder order) {
        serviceOrders.remove(order);
    }
}