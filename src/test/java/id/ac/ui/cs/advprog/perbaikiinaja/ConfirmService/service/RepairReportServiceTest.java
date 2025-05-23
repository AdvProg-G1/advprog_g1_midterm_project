package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairOrderRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.repository.RepairReportRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.TestPropertySource;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@Import(TestSecurityConfig.class)
class RepairReportServiceTest {
	@MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

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
        when(orderRepo.findById(UUID.fromString(ORDER_ID)))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.createRepairReport(ORDER_ID, "details")
        );

        assertTrue(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals("Order not found", ex.getReason());
        verify(orderRepo).findById(UUID.fromString(ORDER_ID));
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportNotCompleted() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(UUID.fromString(ORDER_ID));
        ord.setStatus("PENDING");

        when(orderRepo.findById(UUID.fromString(ORDER_ID)))
                .thenReturn(Optional.of(ord));

        ResponseStatusException ex = assertThrows(
                ResponseStatusException.class,
                () -> service.createRepairReport(ORDER_ID, "details")
        );

        assertFalse(ex.getMessage().contains("404 NOT_FOUND"));
        assertEquals(
                "Cannot report on order not in COMPLETED state.",
                ex.getReason()
        );
        verify(orderRepo).findById(UUID.fromString(ORDER_ID));
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportSuccess() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(UUID.fromString(ORDER_ID));
        ord.setStatus("completed");
        ord.setTechnicianId(TECH_ID);

        when(orderRepo.findById(UUID.fromString(ORDER_ID)))
                .thenReturn(Optional.of(ord));
        when(reportRepo.save(any(RepairReport.class)))
                .thenAnswer(inv -> {
                    RepairReport r = inv.getArgument(0);
                    r.setId("report-999");
                    return r;
                });

        RepairReport result = service.createRepairReport(ORDER_ID, "Fixed bathroom");

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
    void testGetReportByOrderId() {
        RepairReport rpt = RepairReport.builder()
                .id("1")
                .orderId(ORDER_ID)
                .details("A")
                .createdAt(new Date())
                .build();

        when(reportRepo.getReportsByOrderId(ORDER_ID))
                .thenReturn(rpt);

        RepairReport actual = service.getReportsByOrderId(ORDER_ID);

        assertSame(rpt, actual);
        verify(reportRepo).getReportsByOrderId(ORDER_ID);
    }
}
