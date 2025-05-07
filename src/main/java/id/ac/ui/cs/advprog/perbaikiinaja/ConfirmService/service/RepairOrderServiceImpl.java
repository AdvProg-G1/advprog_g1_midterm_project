package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final ServiceOrderRepository serviceOrderRepository;

    public RepairOrderServiceImpl(ServiceOrderRepository serviceOrderRepository) {
        this.serviceOrderRepository = serviceOrderRepository;
    }

    @Override
    public ServiceOrder confirmRepairOrder(Long orderId, int estimatedDuration, double estimatedCost) {
        ServiceOrder order = serviceOrderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("RepairOrder not found with ID: " + orderId);
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot confirm an order that is not in PENDING state.");
        }

        order.setStatus("ACCEPTED");
        order.setEstimatedCompletionTime(estimatedDuration);
        order.setEstimatedPrice(estimatedCost);
        order.setServiceDate(new Date());
        return serviceOrderRepository.create(order);
    }

    @Override
    public ServiceOrder rejectRepairOrder(UUID orderId) {
        ServiceOrder order = serviceOrderRepository.findById(orderId);
        if (order == null) {
            throw new IllegalArgumentException("RepairOrder not found with ID: " + orderId);
        }
        if (!"PENDING".equals(order.getStatus())) {
            throw new IllegalStateException("Cannot reject an order that is not in PENDING state.");
        }

        serviceOrderRepository.deleteById(orderId);
        return serviceOrderRepository.create(order);
    }
}
