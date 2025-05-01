package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RepairReportServiceTest {

    @Mock
    private RepairOrderRepository orderRepo;

    @Mock
    private RepairReportRepository reportRepo;

    @InjectMocks
    private RepairReportServiceImpl service;

    @Test
    void testCreateRepairReportNotFound() {
        when(orderRepo.findById(42L)).thenReturn(null);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> service.createRepairReport(42L, 7L, "details")
        );
        assertEquals("RepairOrder not found with ID: 42", ex.getMessage());

        verify(orderRepo).findById(42L);
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportNotCompleted() {
        RepairOrder order = new RepairOrder();
        order.setId(1L);
        order.setStatus("PENDING");
        when(orderRepo.findById(1L)).thenReturn(order);

        IllegalStateException ex = assertThrows(
                IllegalStateException.class,
                () -> service.createRepairReport(1L, 5L, "details")
        );
        assertEquals("Cannot report on order not in COMPLETED state.", ex.getMessage());

        verify(orderRepo).findById(1L);
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportSuccess() {
        RepairOrder order = new RepairOrder();
        order.setId(100L);
        order.setStatus("COMPLETED");
        when(orderRepo.findById(100L)).thenReturn(order);

        when(reportRepo.createRepairReport(any(RepairReport.class))).thenAnswer(invocation -> {
            RepairReport r = invocation.getArgument(0);
            r.setId(999L);
            return r;
        });

        RepairReport result = service.createRepairReport(100L, 55L, "Fixed bathroom");

        ArgumentCaptor<RepairReport> captor = ArgumentCaptor.forClass(RepairReport.class);
        verify(reportRepo).createRepairReport(captor.capture());
        RepairReport passed = captor.getValue();
        assertEquals(100L, passed.getOrderId());
        assertEquals(55L, passed.getTechnicianId());
        assertEquals("Fixed bathroom", passed.getDetails());
        assertNotNull(passed.getCreatedAt());

        assertEquals(999L, result.getId());
        assertSame(passed, result);
    }

    @Test
    void testGetReportsByOrderId() {
        RepairReport r1 = RepairReport.builder().id(1L).orderId(5L).details("A").createdAt(new Date()).build();
        RepairReport r2 = RepairReport.builder().id(2L).orderId(5L).details("B").createdAt(new Date()).build();
        List<RepairReport> list = Arrays.asList(r1, r2);
        when(reportRepo.getReportsByOrderId(5L)).thenReturn(list);

        List<RepairReport> result = service.getReportsByOrderId(5L);

        assertSame(list, result);
        verify(reportRepo).getReportsByOrderId(5L);
    }
}
