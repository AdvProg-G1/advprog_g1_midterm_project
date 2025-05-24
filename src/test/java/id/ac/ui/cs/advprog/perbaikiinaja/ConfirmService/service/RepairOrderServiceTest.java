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
    void testTechnicianConfirmOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("TECHNICIAN_ACCEPTED")
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
    void testTechnicianConfirmOrderSuccess() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("WAITING_CONFIRMATION")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any(ServiceOrder.class))).thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.confirmRepairOrder(VALID_ID.toString(), 5, 100);

        assertEquals("TECHNICIAN_ACCEPTED", result.getStatus());
        assertEquals("5", result.getEstimatedCompletionTime());
        assertEquals(100, result.getEstimatedPrice());
        assertNotNull(result.getServiceDate());
        assertEquals(LocalDate.now(), result.getServiceDate());
    }

    @Test
    void testUserRejectOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(repairOrderRepo.findById(missingId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.userRejectOrder(missingId.toString())
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: " + missingId, ex.getReason());
    }

    @Test
    void testUserRejectOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("WAITING_CONFIRMATION")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.userRejectOrder(VALID_ID.toString())
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals(
                "Cannot confirm an order that is not in technician_accepted state",
                ex.getReason()
        );
    }

    @Test
    void testUserRejectOrderSuccess() {
        ServiceOrder order = ServiceOrder.builder()
                .id(VALID_ID)
                .status("TECHNICIAN_ACCEPTED")
                .build();
        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(order));
        when(repairOrderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.userRejectOrder(String.valueOf(VALID_ID));

        assertEquals("CANCELLED", order.getStatus());
        verify(repairOrderRepo).save(order);
    }

    @Test
    void testFindAll() {
        ServiceOrder o1 = ServiceOrder.builder()
                .id(VALID_ID)
                .status("WAITING_CONFIRMATION")
                .build();
        ServiceOrder o2 = ServiceOrder.builder()
                .id(OTHER_ID)
                .status("TECHNICIAN_ACCEPTED")
                .build();
        List<ServiceOrder> list = List.of(o1, o2);

        when(repairOrderRepo.findAll()).thenReturn(list);

        List<ServiceOrder> got = service.findAll();
        assertSame(list, got);
        verify(repairOrderRepo).findAll();
    }

    @Test
    void testFindById() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(VALID_ID);

        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        ServiceOrder result = service.findById(String.valueOf(VALID_ID));

        assertSame(ord, result);
    }

    @Test
    void testFindById_NotFound() {
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.empty());

        assertNull(service.findById(String.valueOf(VALID_ID)));
    }

    @Test
    void testFindByStatus() {
        List<String> requested = List.of("pending", "Completed", "IN_PROGRESS");
        List<String> expectedArg = List.of("PENDING", "COMPLETED", "IN_PROGRESS");

        List<ServiceOrder> returned = List.of(new ServiceOrder(), new ServiceOrder());
        when(repairOrderRepo.findByStatusIn(expectedArg)).thenReturn(returned);

        List<ServiceOrder> result = service.findByStatus(requested);

        assertSame(returned, result);
    }

    @Test
    void testUserAcceptOrderSuccess() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(VALID_ID);
        ord.setStatus("TECHNICIAN_ACCEPTED");

        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.userAcceptOrder(String.valueOf(VALID_ID));

        assertEquals("IN_PROGRESS", ord.getStatus());
        verify(repairOrderRepo).save(ord);
    }

    @Test
    void testUserAcceptOrderInvalidStatus() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(VALID_ID);
        ord.setStatus("PENDING");

        when(repairOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.userAcceptOrder(String.valueOf(VALID_ID))
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals(
                "Cannot confirm an order that is not in technician_accepted state",
                ex.getReason()
        );
    }

    @Test
    void testUserAcceptOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(repairOrderRepo.findById(missingId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.userAcceptOrder(missingId.toString())
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: " + missingId, ex.getReason());
    }
}
