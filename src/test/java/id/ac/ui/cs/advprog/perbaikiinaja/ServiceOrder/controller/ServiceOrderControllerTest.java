// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/ServiceOrder/controller/ServiceOrderControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
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
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

    @MockBean
    private AuthStrategy authStrategy;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateOrderReturns201() throws Exception {
        // Arrange: mock authenticated user in SecurityContext
        User mockUser = new User();
        mockUser.setId("user1");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        // Build request payload
        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Laptop")
                .condition("Damaged")
                .problemDescription("Screen cracked")
                .serviceDate(LocalDate.now())
                .technicianId("tech123")
                .paymentMethod("BANK")
                .couponApplied(false)
                .build();

        // Prepare fake returned order
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

        // Act & Assert
        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", org.hamcrest.Matchers.endsWith("/api/orders/" + fakeOrder.getId())))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.itemName").value("Laptop"))
                .andExpect(jsonPath("$.condition").value("Damaged"))
                .andExpect(jsonPath("$.problemDescription").value("Screen cracked"))
                .andExpect(jsonPath("$.technicianId").value("tech123"));
    }
    
    @Test
    void testGetAllOrdersReturns200() throws Exception {
        Mockito.when(serviceOrderService.getAllOrders())
               .thenReturn(List.of(ServiceOrder.builder().build()));

        mockMvc.perform(get("/api/orders"))
               .andExpect(status().isOk());
    }
    
    @Test
    void testGetOrderByIdFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(serviceOrderService.getOrderById(id))
               .thenReturn(ServiceOrder.builder().id(id).build());

        mockMvc.perform(get("/api/orders/" + id))
               .andExpect(status().isOk());
    }

    @Test
    void testGetOrderByIdNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(serviceOrderService.getOrderById(id))
               .thenReturn(null);

        mockMvc.perform(get("/api/orders/" + id))
               .andExpect(status().isNotFound());
    }
    
    @Test
    void testUpdateOrderSuccess() throws Exception {
        UUID id = UUID.randomUUID();

        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Phone")
                .condition("Good")
                .problemDescription("Battery issue")
                .serviceDate(LocalDate.now())
                .technicianId("tech321")
                .paymentMethod("CASH")
                .couponApplied(true)
                .estimatedCompletionTime("2 days")
                .estimatedPrice(100000)
                .status("PENDING")
                .build();

        ServiceOrder updatedOrder = ServiceOrder.builder()
                .id(id)
                .itemName("Phone")
                .build();

        Mockito.when(serviceOrderService.update(Mockito.eq(id), Mockito.any()))
               .thenReturn(java.util.Optional.of(updatedOrder));

        mockMvc.perform(put("/api/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isOk())
               .andExpect(jsonPath("$.itemName").value("Phone"));
    }

    @Test
    void testUpdateOrderNotFound() throws Exception {
        UUID id = UUID.randomUUID();

        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Phone").condition("Good").problemDescription("Issue")
                .serviceDate(LocalDate.now()).technicianId("tech321")
                .paymentMethod("CASH").couponApplied(true)
                .estimatedCompletionTime("2 days").estimatedPrice(100000)
                .status("PENDING").build();

        Mockito.when(serviceOrderService.update(Mockito.eq(id), Mockito.any()))
               .thenReturn(java.util.Optional.empty());

        mockMvc.perform(put("/api/orders/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
               .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteOrderSuccess() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(serviceOrderService.delete(id)).thenReturn(true);

        mockMvc.perform(delete("/api/orders/" + id))
               .andExpect(status().isNoContent());
    }

    @Test
    void testDeleteOrderNotFound() throws Exception {
        UUID id = UUID.randomUUID();
        Mockito.when(serviceOrderService.delete(id)).thenReturn(false);

        mockMvc.perform(delete("/api/orders/" + id))
               .andExpect(status().isNotFound());
    }
    
    @Test
    void testGetAllTechniciansRaw() throws Exception {
        User tech = new User();
        tech.setRole("TECHNICIAN");

        Mockito.when(userRepository.findAll())
               .thenReturn(List.of(tech));

        mockMvc.perform(get("/api/orders/technicians/raw"))
               .andExpect(status().isOk());
    }

    @Test
    void testListTechniciansReturnsList() throws Exception {
        Mockito.when(authStrategy.getAllTechnicians())
               .thenReturn(List.of());

        mockMvc.perform(get("/api/orders/technicians"))
               .andExpect(status().isOk());
    }

    @Test
    void testGetOrderHistoryForUser() throws Exception {
        User mockUser = new User();
        mockUser.setId("user1");

        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn(mockUser);

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        Mockito.when(serviceOrderService.findOrdersByUserId("user1"))
               .thenReturn(List.of());

        mockMvc.perform(get("/api/orders/user/history"))
               .andExpect(status().isOk());
    }

    @Test
    void testCreateOrder_UnauthorizedIfPrincipalNotUser() throws Exception {
        Authentication authentication = Mockito.mock(Authentication.class);
        Mockito.when(authentication.getPrincipal()).thenReturn("anonymousUser");

        SecurityContext context = Mockito.mock(SecurityContext.class);
        Mockito.when(context.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(context);

        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("Laptop")
                .condition("New")
                .problemDescription("No power")
                .serviceDate(LocalDate.now())
                .technicianId("tech1")
                .paymentMethod("CASH")
                .couponApplied(false)
                .build();

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isUnauthorized());
    }


    
}
