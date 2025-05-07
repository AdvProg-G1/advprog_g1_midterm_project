package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;


import java.time.LocalDate;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceOrderController.class)
public class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceOrderService serviceOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderReturns200() throws Exception {
        CreateServiceOrderRequest request = CreateServiceOrderRequest.builder()
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .serviceDate(LocalDate.now())
                .technicianId("tech123")
                .paymentMethod("BANK")
                .couponApplied(false)
                .build();

        ServiceOrder mockOrder = ServiceOrder.builder()
                .id(UUID.randomUUID())
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .technicianId("tech123")
                .userId("user1")
                .serviceDate(request.getServiceDate())
                .paymentMethod("BANK")
                .couponApplied(false)
                .status("pending")
                .build();

        Mockito.when(serviceOrderService.createOrder(Mockito.any())).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Laptop"))
                .andExpect(jsonPath("$.condition").value("Damaged"))
                .andExpect(jsonPath("$.problemDescription").value("Screen cracked"))
                .andExpect(jsonPath("$.technicianId").value("tech123"));
    }
}
