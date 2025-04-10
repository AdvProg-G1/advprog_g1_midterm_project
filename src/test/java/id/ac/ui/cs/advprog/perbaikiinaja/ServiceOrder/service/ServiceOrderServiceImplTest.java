package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ServiceOrderServiceImplTest {

    private ServiceOrderServiceImpl serviceOrderService;
    private ServiceOrder sampleOrder;

    @BeforeEach
    void setUp() {
        serviceOrderService = new ServiceOrderServiceImpl();

        sampleOrder = ServiceOrder.builder()
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Broken screen")
                .technicianId("tech123")
                .userId("user123")
                .serviceDate(LocalDate.now())
                .paymentMethod("BANK")
                .couponApplied(false)
                .status("pending")
                .estimatedPrice(500)
                .estimatedCompletionTime("3 days")
                .build();
    }

    @Test
    void testCreate() {
        ServiceOrder created = serviceOrderService.create(sampleOrder);

        assertNotNull(created.getId());
        assertEquals(sampleOrder.getItemName(), created.getItemName());
    }

    @Test
    void testFindAll() {
        serviceOrderService.create(sampleOrder);
        List<ServiceOrder> orders = serviceOrderService.findAll();

        assertEquals(1, orders.size());
    }

    @Test
    void testFindById() {
        ServiceOrder created = serviceOrderService.create(sampleOrder);
        Optional<ServiceOrder> result = serviceOrderService.findById(created.getId());

        assertTrue(result.isPresent());
        assertEquals(created.getId(), result.get().getId());
    }

    @Test
    void testUpdateSuccess() {
        ServiceOrder created = serviceOrderService.create(sampleOrder);
        created.setStatus("completed");

        Optional<ServiceOrder> updated = serviceOrderService.update(created.getId(), created);

        assertTrue(updated.isPresent());
        assertEquals("completed", updated.get().getStatus());
    }

    @Test
    void testUpdateFailure() {
        UUID fakeId = UUID.randomUUID();
        Optional<ServiceOrder> updated = serviceOrderService.update(fakeId, sampleOrder);

        assertTrue(updated.isEmpty());
    }

    @Test
    void testDeleteSuccess() {
        ServiceOrder created = serviceOrderService.create(sampleOrder);
        boolean deleted = serviceOrderService.delete(created.getId());

        assertTrue(deleted);
        assertTrue(serviceOrderService.findById(created.getId()).isEmpty());
    }

    @Test
    void testDeleteFailure() {
        UUID fakeId = UUID.randomUUID();
        boolean deleted = serviceOrderService.delete(fakeId);

        assertFalse(deleted);
    }

    // [RED] Test getAllOrders returns all service orders
    @Test
    void testGetAllOrders_ShouldReturnAllOrders() {
        ServiceOrder order1 = ServiceOrder.builder()
                .itemName("AC")
                .condition("Rusak Berat")
                .build();
        ServiceOrder order2 = ServiceOrder.builder()
                .itemName("Laptop")
                .condition("Mati Total")
                .build();

        serviceOrderService.create(order1);
        serviceOrderService.create(order2);

        List<ServiceOrder> orders = serviceOrderService.getAllOrders();
        assertEquals(2, orders.size());
        assertTrue(orders.stream().anyMatch(o -> o.getItemName().equals("AC")));
        assertTrue(orders.stream().anyMatch(o -> o.getItemName().equals("Laptop")));
    }

    // [RED] Test getOrderById returns correct service order by ID
    @Test
    void testGetOrderById_ShouldReturnCorrectOrder() {
        ServiceOrder order = ServiceOrder.builder()
                .itemName("Printer")
                .condition("Tinta Bocor")
                .build();

        ServiceOrder createdOrder = serviceOrderService.create(order);
        ServiceOrder foundOrder = serviceOrderService.getOrderById(createdOrder.getId());

        assertNotNull(foundOrder);
        assertEquals("Printer", foundOrder.getItemName());
    }

    // [RED] Test getOrderById returns null if not found
    @Test
    void testGetOrderById_WhenNotFound_ShouldReturnNull() {
        UUID randomId = UUID.randomUUID();
        ServiceOrder result = serviceOrderService.getOrderById(randomId);

        assertNull(result);
    }
}
