package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RepairOrderServiceTest {

    private RepairOrderServiceImpl service;
    private ServiceOrderRepository serviceOrderRepo;
    private RepairOrderRepository repairOrderRepo;

    private static final UUID VALID_ID = UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63");
    private static final UUID OTHER_ID = UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf66");

    @BeforeEach
    void setUp() {
        serviceOrderRepo = mock(ServiceOrderRepository.class);
        repairOrderRepo  = mock(RepairOrderRepository.class);
        service = new RepairOrderServiceImpl(serviceOrderRepo, repairOrderRepo);
    }

    @Test
    void testConfirmOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(repairOrderRepo.findById(missingId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmRepairOrder(missingId.toString(), 5, 100)
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: " + missingId, ex.getReason());
    }

    @Test
    void testConfirmOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("technician_accepted")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.confirmRepairOrder(VALID_ID.toString(), 5, 100)
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals(
                "Cannot confirm an order that is not in waiting_confirmation state",
                ex.getReason()
        );
    }

    @Test
    void testConfirmOrderSuccess() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("waiting_confirmation")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any(ServiceOrder.class))).thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.confirmRepairOrder(VALID_ID.toString(), 5, 100);

        assertEquals("technician_accepted", result.getStatus());
        assertEquals("5", result.getEstimatedCompletionTime());
        assertEquals(100, result.getEstimatedPrice());
        assertNotNull(result.getServiceDate());
        assertEquals(LocalDate.now(), result.getServiceDate());
    }

    @Test
    void testRejectOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(repairOrderRepo.findById(missingId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.rejectRepairOrder(missingId.toString())
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: " + missingId, ex.getReason());
    }

    @Test
    void testRejectOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("technician_accepted")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.rejectRepairOrder(VALID_ID.toString())
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals(
                "Cannot confirm an order that is not in waiting_confirmation state",
                ex.getReason()
        );
    }

    @Test
    void testRejectOrderDeletes() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("waiting_confirmation")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        service.rejectRepairOrder(VALID_ID.toString());

        verify(repairOrderRepo).deleteById(VALID_ID);
    }


    @Test
    void testFindAll() {
        ServiceOrder o1 = ServiceOrder.builder()
                .id(VALID_ID)
                .status("waiting_confirmation")
                .build();
        ServiceOrder o2 = ServiceOrder.builder()
                .id(OTHER_ID)
                .status("technician_accepted")
                .build();
        List<ServiceOrder> list = List.of(o1, o2);

        when(repairOrderRepo.findAll()).thenReturn(list);

        List<ServiceOrder> got = service.findAll();
        assertSame(list, got);
        verify(repairOrderRepo).findAll();
    }
}
