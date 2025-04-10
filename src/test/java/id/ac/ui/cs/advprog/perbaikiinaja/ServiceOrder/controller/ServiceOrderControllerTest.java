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

import java.time.LocalDate;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

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
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .technicianId("tech123")
                .userId("user1")
                .serviceDate(LocalDate.now())
                .paymentMethod("BANK")
                .couponApplied(false)
                .build();

        Mockito.when(serviceOrderService.createOrder(Mockito.any())).thenReturn(mockOrder);

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Laptop"));
    }
}
