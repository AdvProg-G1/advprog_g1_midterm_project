package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ServiceOrderServiceTest {

    private ServiceOrderService serviceOrderService;
    private ServiceOrderRepository serviceOrderRepository;

    @BeforeEach
    void setUp() {
        serviceOrderRepository = mock(ServiceOrderRepository.class);
        serviceOrderService = new ServiceOrderServiceImpl(serviceOrderRepository);
    }

    @Test
    void testCreateServiceOrder() {
        ServiceOrder order = ServiceOrder.builder()
                .itemName("TV LG")
                .itemCondition("rusak total")
                .issueDescription("tidak menyala")
                .serviceDate("2025-04-15")
                .paymentMethod("Bank A")
                .status("pending")
                .build();

        when(serviceOrderRepository.save(order)).thenReturn(order);

        ServiceOrder savedOrder = serviceOrderService.createOrder(order);

        assertNotNull(savedOrder);
        assertEquals("TV LG", savedOrder.getItemName());
        verify(serviceOrderRepository, times(1)).save(order);
    }

    @Test
    void testGetAllOrders() {
        List<ServiceOrder> orders = List.of(
                new ServiceOrder(),
                new ServiceOrder()
        );

        when(serviceOrderRepository.findAll()).thenReturn(orders);

        List<ServiceOrder> result = serviceOrderService.getAllOrders();

        assertEquals(2, result.size());
        verify(serviceOrderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderById() {
        UUID id = UUID.randomUUID();
        ServiceOrder order = new ServiceOrder();
        order.setId(id);

        when(serviceOrderRepository.findById(id)).thenReturn(Optional.of(order));

        ServiceOrder found = serviceOrderService.getOrderById(id);
        assertNotNull(found);
        assertEquals(id, found.getId());
    }

    @Test
    void testDeleteOrder() {
        UUID id = UUID.randomUUID();

        serviceOrderService.deleteOrder(id);

        verify(serviceOrderRepository, times(1)).deleteById(id);
    }
}
