package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;

import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairReport;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairReportServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.is;
@WebMvcTest(RepairReportController.class)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@Import(TestSecurityConfig.class)
class RepairReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepairReportServiceImpl service;
    
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String ORDER_ID = UUID.randomUUID().toString();
    private static final String DETAILS  = "Fixed the leaking pipe";

    @Test
    void createReport_Success() throws Exception {
        RepairReport stub = RepairReport.builder()
                .id(UUID.randomUUID().toString())
                .orderId(ORDER_ID)
                .technicianId("tech-123")
                .details(DETAILS)
                .createdAt(new Date())
                .build();

        when(service.createRepairReport(eq(ORDER_ID), eq(DETAILS)))
                .thenReturn(stub);

        mockMvc.perform(post("/api/report/{orderId}", ORDER_ID)
                        .content(DETAILS)
                        .contentType(MediaType.TEXT_PLAIN)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(stub.getId()))
                .andExpect(jsonPath("$.orderId").value(ORDER_ID))
                .andExpect(jsonPath("$.technicianId").value(stub.getTechnicianId()))
                .andExpect(jsonPath("$.details").value(DETAILS))
                .andExpect(jsonPath("$.createdAt").exists());
    }

    @Test
    void createReport_OrderNotFound() throws Exception {
        when(service.createRepairReport(eq(ORDER_ID), eq(DETAILS)))
                .thenThrow(new ResponseStatusException(
                        org.springframework.http.HttpStatus.NOT_FOUND,
                        "Order not found"));

        mockMvc.perform(post("/api/report/{orderId}", ORDER_ID)
                        .content(DETAILS)
                        .contentType(MediaType.TEXT_PLAIN))
                .andExpect(status().isNotFound());
    }
    
    @Test
    void getAllReports_Success() throws Exception {
        Map<String, Object> report1 = new HashMap<>();
        report1.put("technicianName", "Alice Cooper");
        report1.put("itemName", "Laptop");
        report1.put("details", "Fixed screen");
        report1.put("createdAt", new Date());

        Map<String, Object> report2 = new HashMap<>();
        report2.put("technicianName", "Bob Dylan");
        report2.put("itemName", "Washing Machine");
        report2.put("details", "Replaced belt");
        report2.put("createdAt", new Date());

        List<Map<String, Object>> reports = Arrays.asList(report1, report2);

        when(service.getAllReports()).thenReturn(reports);

        mockMvc.perform(
                org.springframework.test.web.servlet.request.MockMvcRequestBuilders
                        .get("/api/report/all")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(2)))
                .andExpect(jsonPath("$[0].technicianName", is("Alice Cooper")))
                .andExpect(jsonPath("$[1].itemName", is("Washing Machine")))
                .andExpect(jsonPath("$[1].details", is("Replaced belt")));
    }


    
}
