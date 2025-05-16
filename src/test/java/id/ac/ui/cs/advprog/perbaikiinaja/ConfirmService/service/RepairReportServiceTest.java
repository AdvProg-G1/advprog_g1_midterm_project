package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RepairReportServiceTest {

    @Mock
    private RepairOrderRepository orderRepo;

    @Mock
    private RepairReportRepository reportRepo;

    @InjectMocks
    private RepairReportServiceImpl service;

    private final String ORDER_ID = "01f93c11-ded7-4776-a9ab-b77b097ddf63";
    private final String TECH_ID  = "01f93c11-ded7-4776-a9ab-b77b097ddf68";

    @Test
    void testCreateRepairReportNotFound() {
        when(orderRepo.findById(ORDER_ID))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.createRepairReport(ORDER_ID, TECH_ID, "details")
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("Order not found", ex.getReason());

        verify(orderRepo).findById(ORDER_ID);
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportNotCompleted() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(UUID.fromString(ORDER_ID));
        ord.setStatus("PENDING");

        when(orderRepo.findById(ORDER_ID))
                .thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.createRepairReport(ORDER_ID, TECH_ID, "details")
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("Cannot report on order not in COMPLETED state.", ex.getReason());

        verify(orderRepo).findById(ORDER_ID);
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportSuccess() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(UUID.fromString(ORDER_ID));
        ord.setStatus("COMPLETED");

        when(orderRepo.findById(ORDER_ID))
                .thenReturn(Optional.of(ord));
        when(reportRepo.save(any(RepairReport.class)))
                .thenAnswer(inv -> {
                    RepairReport r = inv.getArgument(0);
                    r.setId("report-999");
                    return r;
                });

        RepairReport result = service.createRepairReport(ORDER_ID, TECH_ID, "Fixed bathroom");

        ArgumentCaptor<RepairReport> cap = ArgumentCaptor.forClass(RepairReport.class);
        verify(reportRepo).save(cap.capture());
        RepairReport saved = cap.getValue();

        assertEquals(ORDER_ID, saved.getOrderId());
        assertEquals(TECH_ID, saved.getTechnicianId());
        assertEquals("Fixed bathroom", saved.getDetails());
        assertNotNull(saved.getCreatedAt());

        assertEquals("report-999", result.getId());
        assertSame(saved, result);
    }

    @Test
    void testGetReportsByOrderId() {
        UUID uuid = UUID.randomUUID();

        RepairReport r1 = RepairReport.builder()
                .id("1")
                .orderId(uuid.toString())
                .details("A")
                .createdAt(new Date())
                .build();
        RepairReport r2 = RepairReport.builder()
                .id("2")
                .orderId(uuid.toString())
                .details("B")
                .createdAt(new Date())
                .build();

        List<RepairReport> fakeList = List.of(r1, r2);
        when(reportRepo.getReportsByOrderId(String.valueOf(uuid)))
                .thenReturn(fakeList);

        List<RepairReport> actual = service.getReportsByOrderId(String.valueOf(uuid));

        assertSame(fakeList, actual);
        verify(reportRepo).getReportsByOrderId(String.valueOf(uuid));
    }
}
