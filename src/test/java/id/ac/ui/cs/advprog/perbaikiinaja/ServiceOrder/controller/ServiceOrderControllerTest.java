package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceOrderService serviceOrderService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderReturns200() throws Exception {
        // Arrange: build your request DTO
        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .serviceDate(LocalDate.now())
                .technicianId("tech123")
                .paymentMethod("BANK")
                .couponApplied(false)
                .build();

        // Fake the entity your service will return
        ServiceOrder fakeOrder = ServiceOrder.builder()
                .id(UUID.randomUUID())
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .technicianId(req.getTechnicianId())
                .userId("user1")
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .status("pending")
                .build();

        // Stub the service call
        doReturn(fakeOrder)
                .when(serviceOrderService)
                .createOrder(ArgumentMatchers.any(ServiceOrder.class));

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemName").value("Laptop"))
                .andExpect(jsonPath("$.condition").value("Damaged"))
                .andExpect(jsonPath("$.problemDescription").value("Screen cracked"))
                .andExpect(jsonPath("$.technicianId").value("tech123"));
    }
}
