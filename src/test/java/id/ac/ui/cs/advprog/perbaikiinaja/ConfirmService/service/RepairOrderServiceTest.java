package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class RepairOrderServiceTest {

    private RepairOrderServiceImpl service;
    private ServiceOrderServiceImpl serviceOrderService;
    private ServiceOrder order;

    @BeforeEach
    void setUp() {
        serviceOrderService = Mockito.mock(ServiceOrderServiceImpl.class);
        service = new ServiceOrderServiceImpl(serviceOrderService);

        order = new ServiceOrder();
        order.setId(1L);
        order.setStatus("PENDING");

        // stub common service calls
        when(serviceOrderService.findById(1L)).thenReturn(order);
        when(serviceOrderService.create(any(ServiceOrder.class)))
                .thenAnswer(inv -> inv.getArgument(0));
    }

    @Test
    void testConfirmOrderInvalidStatus() {
        // change status away from PENDING
        order.setStatus("ACCEPTED");
        when(serviceOrderService.findById(1L)).thenReturn(order);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.confirmRepairOrder(1L, 5, 100.0)
        );
        assertEquals("Cannot confirm an order that is not in PENDING state.", ex.getMessage());
    }

    @Test
    void testConfirmOrderAccepted() {
        // PENDING -> should be accepted
        order.setStatus("PENDING");
        when(serviceOrderService.findById(1L)).thenReturn(order);

        ServiceOrder confirmed = service.confirmRepairOrder(1L, 5, 100.0);
        assertEquals("ACCEPTED", confirmed.getStatus());
        assertEquals(5, confirmed.getEstimatedCompletionTime());
        assertEquals(100.0, confirmed.getEstimatedPrice(), 0.001);
    }

    @Test
    void testConfirmOrderInvalidId() {
        when(serviceOrderService.findById(100L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.confirmRepairOrder(100L, 5, 100.0)
        );
        assertEquals("RepairOrder not found with ID: 100", ex.getMessage());
    }

    @Test
    void testRejectOrderDeletesEntry() {
        order.setStatus("PENDING");
        when(serviceOrderService.findById(1L)).thenReturn(order);

        service.rejectRepairOrder(1L);
        verify(service).deleteById(1L);
    }

    @Test
    void testRejectOrderInvalidStatus() {
        order.setStatus("ACCEPTED");
        when(serviceOrderService.findById(1L)).thenReturn(order);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.rejectRepairOrder(1L)
        );
        assertEquals("Cannot reject an order that is not in PENDING state.", ex.getMessage());
    }

    @Test
    void testRejectOrderInvalidId() {
        when(serviceOrderService.findById(100L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.rejectRepairOrder(100L)
        );
        assertEquals("RepairOrder not found with ID: 100", ex.getMessage());
    }

    @Test
    void testGetAllRepairOrders() {
        ServiceOrder o1 = ServiceOrder.builder().id(1L).status("PENDING").build();
        ServiceOrder o2 = ServiceOrder.builder().id(2L).status("ACCEPTED").build();
        List<ServiceOrder> mockList = List.of(o1, o2);

        when(serviceOrderService.findAll()).thenReturn(mockList);

        List<ServiceOrder> result = service.findAll();

        assertSame(mockList, result);
        verify(serviceOrderService).findAll();
    }
}
