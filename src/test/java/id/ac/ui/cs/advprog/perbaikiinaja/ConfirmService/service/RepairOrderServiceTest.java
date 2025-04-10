package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.BeforeEach;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class RepairOrderServiceTest {

    private ConfirmService confirmService;
    private RepairOrderRepository repository;

    @BeforeEach
    void setUp() {
        repository = new RepairOrderRepository();
        confirmService = new ConfirmServiceImpl(repository);

        // Setup sample order with PENDING status.
        RepairOrder order = new RepairOrder();
        order.setId(1L);
        order.setStatus("PENDING");
        repository.save(order);
    }

    @Test
    public void testConfirmOrderInvalidStatus() {
        RepairOrder order = repository.findById(1L);
        // Change status so it fails validation.
        order.setStatus("ACCEPTED");
        repository.save(order);
        confirmService.confirmRepairOrder(1L, 5, 100.0);
    }

    @Test
    public void testConfirmOrderAccepted() {
        RepairOrder order = repository.findById(1L);
        order.setStatus("PENDING");
        repository.save(order);
        RepairOrder confirmedOrder = confirmService.confirmRepairOrder(1L, 5, 100.0);
        assertEquals("ACCEPTED", confirmedOrder.getStatus());
        assertEquals(5, confirmedOrder.getEstimatedDuration());
        assertEquals(100.0, confirmedOrder.getEstimatedCost(), 0.001);
    }

    @Test
    public void testRejectOrder() {
        RepairOrder order = repository.findById(1L);
        order.setStatus("PENDING");
        repository.save(order);
        RepairOrder rejectedOrder = confirmService.rejectRepairOrder(1L);
        assertEquals("REJECTED", rejectedOrder.getStatus());
    }
}
