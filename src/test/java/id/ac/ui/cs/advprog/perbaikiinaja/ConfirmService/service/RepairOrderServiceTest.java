package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.repository.ServiceOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

    private static final UUID VALID_ID = UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf63");
    private static final UUID OTHER_ID = UUID.fromString("01f93c11-ded7-4776-a9ab-b77b097ddf66");

    @BeforeEach
    void setUp() {
        serviceOrderRepo = mock(ServiceOrderRepository.class);
        repairOrderRepo  = mock(RepairOrderRepository.class);
        service = new RepairOrderServiceImpl(serviceOrderRepo, repairOrderRepo);
    }

    @Test
    void testTechnicianConfirmOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(serviceOrderRepo.findById(missingId)).thenReturn(Optional.empty());

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
                .status("TECHNICIAN ACCEPTED")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

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
                .status("WAITING CONFIRMATION")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any(ServiceOrder.class))).thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.confirmRepairOrder(VALID_ID.toString(), 5, 100);

        assertEquals("TECHNICIAN ACCEPTED", result.getStatus());
        assertEquals("5", result.getEstimatedCompletionTime());
        assertEquals(100, result.getEstimatedPrice());
        assertNotNull(result.getServiceDate());
        assertEquals(LocalDate.now(), result.getServiceDate());
    }

    @Test
    void testTechnicianRejectOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(serviceOrderRepo.findById(missingId)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.rejectRepairOrder(missingId.toString())
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("RepairOrder not found with ID: " + missingId, ex.getReason());
    }

    @Test
    void testTechnicianRejectOrderInvalidStatus() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("TECHNICIAN ACCEPTED")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

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
    void testTechnicianRejectOrderSuccess() {
        ServiceOrder ord = ServiceOrder.builder()
                .id(VALID_ID)
                .status("WAITING CONFIRMATION")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any(ServiceOrder.class))).thenAnswer(i -> i.getArgument(0));

        ServiceOrder result = service.rejectRepairOrder(VALID_ID.toString());
        assertEquals("TECHNICIAN REJECTED", result.getStatus());
    }

    @Test
    void testUserRejectOrderNotFound() {
        UUID missingId = UUID.randomUUID();
        when(serviceOrderRepo.findById(missingId)).thenReturn(Optional.empty());

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
                .status("WAITING CONFIRMATION")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));

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
                .status("TECHNICIAN ACCEPTED")
                .build();
        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(order));
        when(repairOrderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.userRejectOrder(String.valueOf(VALID_ID));

        assertEquals("CANCELLED", order.getStatus());
        verify(repairOrderRepo).save(order);
    }

    @Test
    void testFindAll() {
        ServiceOrder o1 = ServiceOrder.builder()
                .id(VALID_ID)
                .status("WAITING CONFIRMATION")
                .build();
        ServiceOrder o2 = ServiceOrder.builder()
                .id(OTHER_ID)
                .status("TECHNICIAN ACCEPTED")
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
        List<String> requested = List.of("PENDING", "COMPLETED", "IN PROGRESS");
        ServiceOrder o1 = new ServiceOrder();
        ServiceOrder o2 = new ServiceOrder();
        List<ServiceOrder> stubbed = List.of(o1, o2);
        when(repairOrderRepo.findByStatusIn(requested)).thenReturn(stubbed);

        List<ServiceOrder> result = service.findByStatus(requested);
        assertSame(stubbed, result, "should return exactly what the repo returned");
        verify(repairOrderRepo).findByStatusIn(requested);
    }

    @Test
    void testUserAcceptOrderSuccess() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(VALID_ID);
        ord.setStatus("TECHNICIAN ACCEPTED");

        when(serviceOrderRepo.findById(VALID_ID)).thenReturn(Optional.of(ord));
        when(repairOrderRepo.save(any())).thenAnswer(inv -> inv.getArgument(0));

        service.userAcceptOrder(String.valueOf(VALID_ID));

        assertEquals("IN PROGRESS", ord.getStatus());
        verify(repairOrderRepo).save(ord);
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
