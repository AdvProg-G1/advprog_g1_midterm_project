package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class RepairOrderServiceTest {

    private RepairOrderService repairOrderService;
    private RepairOrderRepository repository;
    private RepairOrder order; // We'll reuse the same RepairOrder in each test

    @BeforeEach
    void setUp() {
        // Mock the repository so there's no real DB access
        repository = Mockito.mock(RepairOrderRepository.class);

        // Use your actual service implementation
        repairOrderService = new RepairOrderServiceImpl(repository);

        // Prepare a sample order with PENDING status
        order = new RepairOrder();
        order.setId(1L);
        order.setStatus("PENDING");

        // Whenever save(...) is called, just return the same object that was passed in
        when(repository.save(any(RepairOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Whenever findById(1L) is called, return our 'order'
        when(repository.findById(1L)).thenReturn(order);
    }

    @Test
    public void testConfirmOrderInvalidStatus() {
        // Make the order's status not "PENDING" so confirmation fails
        order.setStatus("ACCEPTED");
        when(repository.findById(1L)).thenReturn(order);

        // We expect an exception because the order isn't in PENDING status
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> repairOrderService.confirmRepairOrder(1L, 5, 100.0)
        );
        // Check if the exception message matches what your service throws
        assertEquals("Cannot confirm an order that is not in PENDING state.", exception.getMessage());
    }

    @Test
    public void testConfirmOrderAccepted() {
        // Reset the order to PENDING
        order.setStatus("PENDING");
        when(repository.findById(1L)).thenReturn(order);

        RepairOrder confirmed = repairOrderService.confirmRepairOrder(1L, 5, 100.0);
        assertEquals("ACCEPTED", confirmed.getStatus());
        assertEquals(5, confirmed.getEstimatedDuration());
        assertEquals(100.0, confirmed.getEstimatedCost(), 0.001);
    }

    @Test
    public void testRejectOrder() {
        // Ensure the order is PENDING
        order.setStatus("PENDING");
        when(repository.findById(1L)).thenReturn(order);

        RepairOrder rejected = repairOrderService.rejectRepairOrder(1L);
        assertEquals("REJECTED", rejected.getStatus());
    }
}
