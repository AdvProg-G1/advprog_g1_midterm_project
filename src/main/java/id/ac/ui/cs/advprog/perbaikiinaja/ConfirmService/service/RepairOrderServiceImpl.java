package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        ServiceOrder order = repo.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RepairOrder not found with ID: " + id));

        if (!"waiting_confirmation".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in waiting_confirmation state");
        }

        order.setStatus("technician_accepted");
        order.setEstimatedCompletionTime(String.valueOf(duration));
        order.setEstimatedPrice(cost);
        order.setServiceDate(LocalDate.now());
        return repo.save(order);
    }

    @Override
    public void rejectRepairOrder(String id) {
        ServiceOrder order = repo.findById(UUID.fromString(id))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "RepairOrder not found with ID: " + id));

        if (!"waiting_confirmation".equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in waiting_confirmation state");
        }

        repo.deleteById(UUID.fromString(id));
    }

    @Override
    public ServiceOrder findById(String id) {
        return repo.findById(UUID.fromString(id))
                .orElse(null);
    }

    @Override
    public void deleteById(String id) {
        ServiceOrder toDelete = findById(id);
        repo.delete(toDelete);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return repo.findAll();
    }

    @Override
    public List<ServiceOrder> findByStatus(List<String> statuses) {
        List<String> upper = statuses.stream()
                .map(String::toLowerCase)
                .collect(Collectors.toList());
        return repo.findByStatusIn(upper);
    }
}
