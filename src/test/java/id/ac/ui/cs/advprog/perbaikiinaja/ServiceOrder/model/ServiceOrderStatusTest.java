package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ServiceOrderStatusTest {

    @Test
    void allEnumConstantsPresent() {
        ServiceOrderStatus[] statuses = ServiceOrderStatus.values();
        assertEquals(6, statuses.length, "There should be exactly 6 statuses");
        assertArrayEquals(new ServiceOrderStatus[]{
                ServiceOrderStatus.WAITING_CONFIRMATION,
                ServiceOrderStatus.TECHNICIAN_ACCEPTED,
                ServiceOrderStatus.TECHNICIAN_REJECTED,
                ServiceOrderStatus.IN_PROGRESS,
                ServiceOrderStatus.COMPLETED,
                ServiceOrderStatus.CANCELLED
        }, statuses, "Enum constants should match the defined order");
    }

    @Test
    void valueOfValidNames() {
        assertEquals(ServiceOrderStatus.WAITING_CONFIRMATION,
                ServiceOrderStatus.valueOf("WAITING_CONFIRMATION"));
        assertEquals(ServiceOrderStatus.TECHNICIAN_ACCEPTED,
                ServiceOrderStatus.valueOf("TECHNICIAN_ACCEPTED"));
        assertEquals(ServiceOrderStatus.TECHNICIAN_REJECTED,
                ServiceOrderStatus.valueOf("TECHNICIAN_REJECTED"));
        assertEquals(ServiceOrderStatus.IN_PROGRESS,
                ServiceOrderStatus.valueOf("IN_PROGRESS"));
        assertEquals(ServiceOrderStatus.COMPLETED,
                ServiceOrderStatus.valueOf("COMPLETED"));
        assertEquals(ServiceOrderStatus.CANCELLED,
                ServiceOrderStatus.valueOf("CANCELLED"));
    }

    @Test
    void valueOfInvalidNameThrows() {
        assertThrows(IllegalArgumentException.class, () -> {
            ServiceOrderStatus.valueOf("UNKNOWN_STATUS");
        }, "valueOf should throw IllegalArgumentException for invalid names");
    }
}