package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.jupiter.api.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepairOrderServiceTest {

    private RepairOrderServiceImpl service;
    private ServiceOrderRepository serviceOrderRepo;
    private RepairOrderRepository repairOrderRepo;

    @BeforeEach
    void setUp() {
        serviceOrderRepo = mock(ServiceOrderRepository.class);
        repairOrderRepo  = mock(RepairOrderRepository.class);
        service = new RepairOrderServiceImpl(serviceOrderRepo, repairOrderRepo);
    }

    @Test
    void testConfirmOrderNotFound() {
        when(repairOrderRepo.findById("100"))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmRepairOrder("100", 5, 100)
        );
        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: 100", ex.getReason());
    }

    @Test
    void testConfirmOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .status("TECHNICIAN_ACCEPTED")
                .build();
        when(repairOrderRepo.findById("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmRepairOrder("01f93c11-ded7-4776-a9ab-b77b097ddf63", 5, 100)
        );
        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("Cannot confirm an order that is not in WAITING_CONFIRMATION state", ex.getReason());
    }

    @Test
    void testConfirmOrderSuccess() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .status("WAITING_CONFIRMATION")
                .build();
        when(repairOrderRepo.findById("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any(ServiceOrder.class)))
                .thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.confirmRepairOrder("01f93c11-ded7-4776-a9ab-b77b097ddf63", 5, 100);

        assertEquals("TECHNICIAN_ACCEPTED", result.getStatus());
        // completion time is stored as String
        assertEquals("5", result.getEstimatedCompletionTime());
        assertEquals(100, result.getEstimatedPrice());
        assertNotNull(result.getServiceDate());
        assertEquals(LocalDate.now(), result.getServiceDate());
    }

    @Test
    void testRejectOrderNotFound() {
        when(repairOrderRepo.findById("42"))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.rejectRepairOrder("42")
        );
        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: 42", ex.getReason());
    }

    @Test
    void testRejectOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .status("TECHNICIAN_ACCEPTED")
                .build();
        when(repairOrderRepo.findById("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.rejectRepairOrder("01f93c11-ded7-4776-a9ab-b77b097ddf63")
        );
        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("Cannot confirm an order that is not in WAITING_CONFIRMATION state", ex.getReason());
    }

    @Test
    void testRejectOrderDeletes() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .status("WAITING_CONFIRMATION")
                .build();
        when(repairOrderRepo.findById("01f93c11-ded7-4776-a9ab-b77b097ddf63"))
                .thenReturn(Optional.of(ord));

        service.rejectRepairOrder("01f93c11-ded7-4776-a9ab-b77b097ddf63");

        verify(repairOrderRepo).delete(ord);
    }

    @Test
    void testFindAll() {
        ServiceOrder o1 = ServiceOrder.builder().id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63")).status("WAITING_CONFIRMATION").build();
        ServiceOrder o2 = ServiceOrder.builder().id(UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf66")).status("TECHNICIAN_ACCEPTED").build();
        List<ServiceOrder> list = List.of(o1, o2);

        when(repairOrderRepo.findAll()).thenReturn(list);

        List<ServiceOrder> got = service.findAll();
        assertSame(list, got);
        verify(repairOrderRepo).findAll();
    }
}
