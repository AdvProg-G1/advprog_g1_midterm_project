package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RepairOrderController.class)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@Import(TestSecurityConfig.class)
class RepairOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RepairOrderService repairOrderService;
    
    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void technicianConfirmOrder_Success() throws Exception {
        UUID orderId = UUID.randomUUID();
        ServiceOrder stub = new ServiceOrder();
        stub.setId(orderId);

        when(repairOrderService.confirmRepairOrder(eq(orderId.toString()), eq(3), eq(200)))
                .thenReturn(stub);

        mockMvc.perform(post("/api/repair/confirm/{id}", orderId)
                        .param("duration", "3")
                        .param("cost", "200")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId.toString()));

        verify(repairOrderService).confirmRepairOrder(orderId.toString(), 3, 200);
    }

    @Test
    void technicianRejectOrder_Success() throws Exception {
        String orderId = UUID.randomUUID().toString();
        ServiceOrder stub = new ServiceOrder();
        stub.setId(UUID.fromString(orderId));

        when(repairOrderService.rejectRepairOrder(orderId))
                .thenReturn(stub);

        mockMvc.perform(post("/api/repair/reject/{id}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(orderId));

        verify(repairOrderService).rejectRepairOrder(orderId);
    }

    @Test
    void userAcceptOrder() throws Exception {
        String orderId = UUID.randomUUID().toString();

        doNothing()
                .when(repairOrderService)
                .userAcceptOrder(orderId);

        mockMvc.perform(post("/api/repair/user/accept/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(repairOrderService).userAcceptOrder(orderId);
    }

    @Test
    void UserRejectOrder() throws Exception {
        UUID orderId = UUID.randomUUID();
        doNothing().when(repairOrderService).userRejectOrder(orderId.toString());

        mockMvc.perform(post("/api/repair/user/reject/{id}", orderId))
                .andExpect(status().isNoContent());

        verify(repairOrderService).userRejectOrder(orderId.toString());
    }

    @Test
    void incomingOrderList() throws Exception {
        ServiceOrder o1 = new ServiceOrder(); o1.setId(UUID.randomUUID());
        ServiceOrder o2 = new ServiceOrder(); o2.setId(UUID.randomUUID());
        when(repairOrderService.findByStatus(List.of("WAITING CONFIRMATION")))
                .thenReturn(List.of(o1, o2));

        mockMvc.perform(get("/api/repair/list")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(o1.getId().toString()))
                .andExpect(jsonPath("$[1].id").value(o2.getId().toString()));

        verify(repairOrderService).findByStatus(List.of("WAITING CONFIRMATION"));
    }

    @Test
    void orderHistory() throws Exception {
        ServiceOrder h = new ServiceOrder(); h.setId(UUID.randomUUID());
        when(repairOrderService.findByStatus(List.of("IN PROGRESS", "COMPLETED")))
                .thenReturn(Collections.singletonList(h));

        mockMvc.perform(get("/api/repair/history")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(h.getId().toString()));

        verify(repairOrderService).findByStatus(List.of("IN PROGRESS", "COMPLETED"));
    }

    @Test
    void getOrder_Success() throws Exception {
        UUID orderId = UUID.randomUUID();
        ServiceOrder found = new ServiceOrder(); found.setId(orderId);
        when(repairOrderService.findById(orderId.toString()))
                .thenReturn(found);

        mockMvc.perform(get("/api/repair/{id}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(orderId.toString()));

        verify(repairOrderService).findById(orderId.toString());
    }

    @Test
    void getOrder_NotFound() throws Exception {
        UUID orderId = UUID.randomUUID();
        when(repairOrderService.findById(orderId.toString()))
                .thenReturn(null);

        mockMvc.perform(get("/api/repair/{id}", orderId)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(repairOrderService).findById(orderId.toString());
    }
}
