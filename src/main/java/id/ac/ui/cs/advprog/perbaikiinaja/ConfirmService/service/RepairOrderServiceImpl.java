package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service
public class RepairOrderServiceImpl implements RepairOrderService {

    private final ServiceOrderRepository serviceOrderRepository;
    private final RepairOrderRepository repo;

    private static final String WAITING_CONFIRMATION   = "WAITING CONFIRMATION";
    private static final String TECHNICIAN_ACCEPTED    = "TECHNICIAN ACCEPTED";
    private static final String TECHNICIAN_REJECTED    = "TECHNICIAN REJECTED";
    private static final String IN_PROGRESS            = "IN PROGRESS";
    private static final String CANCELLED              = "CANCELLED";

    public RepairOrderServiceImpl(ServiceOrderRepository serviceOrderRepository, RepairOrderRepository repo) {
        this.serviceOrderRepository = serviceOrderRepository;
        this.repo = repo;
    }

    private ServiceOrder fetchOrderOrThrow(String id) {
        return serviceOrderRepository.findById(UUID.fromString(id))
        .orElseThrow(() ->
            new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "RepairOrder not found with ID: " + id
            )
        );
    }

    @Override
    public ServiceOrder confirmRepairOrder(String id, int duration, int cost) {
        ServiceOrder order = fetchOrderOrThrow(id);
        if (! WAITING_CONFIRMATION.equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot confirm an order that is not in waiting_confirmation state"
            );
        }
        order.setStatus(TECHNICIAN_ACCEPTED);
        order.setEstimatedCompletionTime(String.valueOf(duration));
        order.setEstimatedPrice(cost);
        order.setServiceDate(LocalDate.now());
        return repo.save(order);
    }

    @Override
    public ServiceOrder rejectRepairOrder(String id) {
        ServiceOrder order = fetchOrderOrThrow(id);
        if (! WAITING_CONFIRMATION.equals(order.getStatus())) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Cannot confirm an order that is not in waiting_confirmation state"
            );
        }

        order.setStatus(TECHNICIAN_REJECTED);
        return repo.save(order);
    }

    @Override
    public ServiceOrder findById(String id) {
        return repo.findById(UUID.fromString(id))
                .orElse(null);
    }

    @Override
    public List<ServiceOrder> findAll() {
        return repo.findAll();
    }

    @Override
    public List<ServiceOrder> findByStatus(List<String> statuses) {
        if (statuses == null || statuses.isEmpty()) {
            return Collections.emptyList();
        }
        return repo.findByStatusIn(statuses);
    }

    @Override
    public void userRejectOrder(String id) {
        ServiceOrder order = fetchOrderOrThrow(id);

        if (!TECHNICIAN_ACCEPTED.equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in technician_accepted state");
        }

        order.setStatus(CANCELLED);
        repo.save(order);
    }

    @Override
    public void userAcceptOrder(String id) {
        ServiceOrder order = fetchOrderOrThrow(id);
        if (!TECHNICIAN_ACCEPTED.equals(order.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cannot confirm an order that is not in technician_accepted state");
        }

        order.setStatus(IN_PROGRESS);
        repo.save(order);
    }
}
