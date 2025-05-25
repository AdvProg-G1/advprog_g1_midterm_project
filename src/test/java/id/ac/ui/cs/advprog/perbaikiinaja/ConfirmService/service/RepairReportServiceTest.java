package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
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

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
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

    @Mock
    private UserRepository userRepo;

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
                "Cannot report on order not in IN_PROGRESS state.",
                ex.getReason()
        );
        verify(orderRepo).findById(UUID.fromString(ORDER_ID));
        verifyNoInteractions(reportRepo);
    }

    @Test
    void testCreateRepairReportSuccess() {
        ServiceOrder ord = new ServiceOrder();
        ord.setId(UUID.fromString(ORDER_ID));
        ord.setStatus("IN_PROGRESS");
        ord.setTechnicianId(TECH_ID);
        ord.setEstimatedPrice(200);

        when(orderRepo.findById(UUID.fromString(ORDER_ID)))
                .thenReturn(Optional.of(ord));
        when(orderRepo.save(any(ServiceOrder.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        when(reportRepo.save(any(RepairReport.class)))
                .thenAnswer(inv -> {
                    RepairReport rpt = inv.getArgument(0);
                    rpt.setId("report-999");
                    return rpt;
                });

        User tech = new User();
        tech.setId(TECH_ID);
        tech.setTotalSalary(null);
        tech.setTotalWork(null);

        when(userRepo.findById(TECH_ID))
                .thenReturn(Optional.of(tech));
        when(userRepo.save(any(User.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        RepairReport result = service.createRepairReport(ORDER_ID, "Fixed bathroom");

        ArgumentCaptor<RepairReport> reportCap = ArgumentCaptor.forClass(RepairReport.class);
        verify(reportRepo).save(reportCap.capture());
        RepairReport savedRpt = reportCap.getValue();

        assertEquals(ORDER_ID,       savedRpt.getOrderId());
        assertEquals(TECH_ID,        savedRpt.getTechnicianId());
        assertEquals("Fixed bathroom", savedRpt.getDetails());
        assertNotNull(savedRpt.getCreatedAt());

        // service returns the same object with its new id
        assertEquals("report-999", result.getId());
        assertSame(savedRpt,      result);

        // --- assert technician update ------------------
        ArgumentCaptor<User> userCap = ArgumentCaptor.forClass(User.class);
        verify(userRepo).save(userCap.capture());
        User savedTech = userCap.getValue();

        assertEquals(200, savedTech.getTotalSalary());
        assertEquals(1,   savedTech.getTotalWork());

        assertEquals("COMPLETED", ord.getStatus());
        verify(orderRepo).save(ord);
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
    
    @Test
    void testGetAllReports() {
        RepairReport r1 = RepairReport.builder()
                .id("1")
                .orderId("00000000-0000-0000-0000-000000000001")
                .technicianId("tech-1")
                .details("Fixed screen")
                .createdAt(new Date())
                .build();

        RepairReport r2 = RepairReport.builder()
                .id("2")
                .orderId("00000000-0000-0000-0000-000000000002")
                .technicianId("tech-2")
                .details("Replaced battery")
                .createdAt(new Date())
                .build();

        when(reportRepo.findAll()).thenReturn(Arrays.asList(r1, r2));

        User tech1 = new User();
        tech1.setId("tech-1");
        tech1.setFullName("Alice Cooper");

        User tech2 = new User();
        tech2.setId("tech-2");
        tech2.setFullName("Bob Dylan");

        when(userRepo.findById("tech-1")).thenReturn(Optional.of(tech1));
        when(userRepo.findById("tech-2")).thenReturn(Optional.of(tech2));

        ServiceOrder order1 = new ServiceOrder();
        order1.setId(UUID.fromString("00000000-0000-0000-0000-000000000001"));
        order1.setItemName("Laptop");

        ServiceOrder order2 = new ServiceOrder();
        order2.setId(UUID.fromString("00000000-0000-0000-0000-000000000002"));
        order2.setItemName("Phone");

        when(orderRepo.findById(order1.getId())).thenReturn(Optional.of(order1));
        when(orderRepo.findById(order2.getId())).thenReturn(Optional.of(order2));

        List<Map<String, Object>> result = service.getAllReports();

        assertEquals(2, result.size());

        Map<String, Object> report1 = result.get(0);
        assertEquals("Alice Cooper", report1.get("technicianName"));
        assertEquals("Laptop", report1.get("itemName"));
        assertEquals("Fixed screen", report1.get("details"));
        assertTrue(report1.containsKey("createdAt"));

        Map<String, Object> report2 = result.get(1);
        assertEquals("Bob Dylan", report2.get("technicianName"));
        assertEquals("Phone", report2.get("itemName"));
        assertEquals("Replaced battery", report2.get("details"));
        assertTrue(report2.containsKey("createdAt"));

        verify(reportRepo).findAll();
        verify(userRepo).findById("tech-1");
        verify(userRepo).findById("tech-2");
        verify(orderRepo).findById(order1.getId());
        verify(orderRepo).findById(order2.getId());
    }

    
    
}
