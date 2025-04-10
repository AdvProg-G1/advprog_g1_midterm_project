package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ServiceOrderServiceImpl implements ServiceOrderService {

    @Autowired
    private ServiceOrderRepository repository;

    @Override
    public ServiceOrder createOrder(ServiceOrder order) {
        order.setStatus("pending");
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
}
