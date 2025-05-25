package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ServiceOrderWithTechnicianNameTest {

    private ServiceOrder mockOrder;
    private UUID        id;
    private LocalDate  serviceDate;
    private final String TECH_NAME = "Alice Technician";

    @BeforeEach
    void setUp() {
        mockOrder = mock(ServiceOrder.class);

        id = UUID.randomUUID();
        serviceDate = LocalDate.of(2025, 5, 25);

        when(mockOrder.getId()).thenReturn(id);
        when(mockOrder.getItemName()).thenReturn("Smartphone");
        when(mockOrder.getCondition()).thenReturn("Used");
        when(mockOrder.getProblemDescription()).thenReturn("Battery drains fast");
        when(mockOrder.getTechnicianId()).thenReturn("tech-789");
        when(mockOrder.getUserId()).thenReturn("user-456");
        when(mockOrder.getPaymentMethod()).thenReturn("CASH");
        when(mockOrder.isCouponApplied()).thenReturn(true);
        when(mockOrder.getStatus()).thenReturn("IN_PROGRESS");
        when(mockOrder.getEstimatedCompletionTime()).thenReturn("3 days");
        when(mockOrder.getEstimatedPrice()).thenReturn(150);
        when(mockOrder.getServiceDate()).thenReturn(serviceDate);
    }

    @Test
    void dtoCopiesAllFieldsCorrectly() {
        ServiceOrderWithTechnicianName dto =
                new ServiceOrderWithTechnicianName(mockOrder, TECH_NAME);

        assertEquals(id.toString(),                     dto.getId());
        assertEquals("Smartphone",                       dto.getItemName());
        assertEquals("Used",                             dto.getCondition());
        assertEquals("Battery drains fast",              dto.getProblemDescription());
        assertEquals("tech-789",                         dto.getTechnicianId());
        assertEquals(TECH_NAME,                          dto.getTechnicianName());
        assertEquals("user-456",                         dto.getUserId());
        assertEquals("CASH",                             dto.getPaymentMethod());
        assertTrue(dto.isCouponApplied(),                "couponApplied should be true");
        assertEquals("IN_PROGRESS",                      dto.getStatus());
        assertEquals("3 days",                           dto.getEstimatedCompletionTime());
        assertEquals(150,                                dto.getEstimatedPrice());
        assertEquals(serviceDate.toString(),             dto.getServiceDate());
    }

    @Test
    void dtoGettersReturnExpectedValues() {
        ServiceOrderWithTechnicianName dto =
                new ServiceOrderWithTechnicianName(mockOrder, TECH_NAME);

        // Just sanity-check a couple getters again
        assertNotNull(dto.getId());
        assertNotNull(dto.getServiceDate());
        assertEquals(TECH_NAME, dto.getTechnicianName());
    }
}