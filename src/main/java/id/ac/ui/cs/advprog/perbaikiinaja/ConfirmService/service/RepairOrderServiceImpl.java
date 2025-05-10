package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final RepairOrderRepository repo;

    public RepairOrderServiceImpl(ServiceOrderRepository serviceOrderRepository, RepairOrderRepository repo) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.repo = repo;
    }

    @Override
    public ServiceOrder confirmRepairOrder(String id, int duration, int cost) {
        ServiceOrder order = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RepairOrder not found with ID: " + id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in PENDING state");
        }

        order.setStatus("ACCEPTED");
        order.setEstimatedCompletionTime(String.valueOf(duration));
        order.setEstimatedPrice(cost);
        order.setServiceDate(LocalDate.now());
        return repo.save(order);
    }

    @Override
    public void rejectRepairOrder(String id) {
        ServiceOrder order = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RepairOrder not found with ID: " + id));

        if (!"PENDING".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in PENDING state");
        }

        repo.delete(order);
    }

    @Override
    public ServiceOrder findById(ServiceOrder id) {
        return serviceOrderRepository.findById(id.getId()).orElse(null);
    }

    @Override
    public void deleteById(ServiceOrder order) {
        ServiceOrder toDelete = findById(order);
        repo.delete(toDelete);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return repo.findAll();
    }
}
