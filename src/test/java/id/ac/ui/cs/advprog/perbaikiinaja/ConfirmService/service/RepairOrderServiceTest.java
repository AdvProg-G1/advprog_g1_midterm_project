package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller.RepairOrderController;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

public class RepairOrderServiceTest {

    private RepairOrderController controller;
    private RepairOrderService service;
    private RepairOrderRepository repository;
    private RepairOrder order;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(RepairOrderRepository.class);
        service = new RepairOrderServiceImpl(repository);

        order = new RepairOrder();
        order.setId(1L);
        order.setStatus("PENDING");

        when(repository.createRepairOrder(any(RepairOrder.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(repository.findById(1L)).thenReturn(order);
    }

    @Test
    public void testConfirmOrderInvalidStatus() {
        order.setStatus("ACCEPTED");
        when(repository.findById(1L)).thenReturn(order);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.confirmRepairOrder(1L, 5, 100.0)
        );
        assertEquals("Cannot confirm an order that is not in PENDING state.", exception.getMessage());
    }

    @Test
    public void testConfirmOrderAccepted() {
        order.setStatus("PENDING");
        when(repository.findById(1L)).thenReturn(order);

        RepairOrder confirmed = service.confirmRepairOrder(1L, 5, 100.0);
        assertEquals("ACCEPTED", confirmed.getStatus());
        assertEquals(5, confirmed.getEstimatedDuration());
        assertEquals(100.0, confirmed.getEstimatedCost(), 0.001);
    }

    @Test
    public void testConfirmOrderInvalidId() {
        when(repository.findById(100L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.confirmRepairOrder(100L, 5, 100.0)
        );
        assertEquals("RepairOrder not found with ID: 100", ex.getMessage());
    }

    @Test
    void testRejectOrderDeletesEntry() {
        RepairOrder order = new RepairOrder();
        order.setId(1L);
        order.setStatus("PENDING");
        when(repository.findById(1L)).thenReturn(order);

        service.rejectRepairOrder(1L);
        verify(repository).deleteById(1L);
    }


    @Test
    public void testRejectOrderInvalidStatus() {
        order.setStatus("ACCEPTED");
        when(repository.findById(1L)).thenReturn(order);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> service.rejectRepairOrder(1L)
        );
        assertEquals("Cannot reject an order that is not in PENDING state.", exception.getMessage());
    }

    @Test
    public void testRejectOrderInvalidId() {
        when(repository.findById(100L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.rejectRepairOrder(100L)
        );
        assertEquals("RepairOrder not found with ID: 100", ex.getMessage());
    }

    @Test
    public void testGetAllRepairOrders() {
        RepairOrder o1 = RepairOrder.builder().id(1L).status("PENDING").build();
        RepairOrder o2 = RepairOrder.builder().id(2L).status("ACCEPTED").build();
        List<RepairOrder> mockList = List.of(o1, o2);

        when(repository.getAllRepairOrders()).thenReturn(mockList);

        List<RepairOrder> result = service.getAllRepairOrders();

        assertSame(mockList, result);
        verify(repository).getAllRepairOrders();
    }
}