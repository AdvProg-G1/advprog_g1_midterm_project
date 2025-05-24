// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/ServiceOrder/controller/ServiceOrderController.java
package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ServiceOrderController.class)
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@Import(TestSecurityConfig.class)
class ServiceOrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ServiceOrderService serviceOrderService;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderReturns201() throws Exception {
        User mockUser = new User();
        mockUser.setId("user1");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .serviceDate(LocalDate.now())
                .technicianId("tech123")
                .paymentMethod("BANK")
                .couponApplied(false)
                .build();

        ServiceOrder fakeOrder = ServiceOrder.builder()
                .id(UUID.randomUUID())
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .technicianId(req.getTechnicianId())
                .userId(mockUser.getId())
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .status("pending")
                .build();

        Mockito.doReturn(fakeOrder)
                .when(serviceOrderService)
                .createOrder(ArgumentMatchers.any(ServiceOrder.class));

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemName").value("Laptop"))
                .andExpect(jsonPath("$.condition").value("Damaged"))
                .andExpect(jsonPath("$.problemDescription").value("Screen cracked"))
                .andExpect(jsonPath("$.technicianId").value("tech123"));
    }

}