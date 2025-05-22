package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import id.ac.ui.cs.advprog.perbaikiinaja.config.SecurityConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Slice test for ServiceOrderController.
 * Excludes SecurityConfig and JwtAuthenticationFilter so JWT beans aren't required.
 */
@WebMvcTest(
        controllers = ServiceOrderController.class,
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = SecurityConfig.class
                ),
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthenticationFilter.class
                )
        }
)
@AutoConfigureMockMvc(addFilters = false)
class ServiceOrderControllerTest {

    @Autowired private MockMvc mvc;
    @MockBean private ServiceOrderService service;
    @MockBean private UserRepository userRepo;
    @Autowired private ObjectMapper mapper;

    private ServiceOrder sample;
    private UUID sampleId;

    @BeforeEach
    void setUp() {
        sampleId = UUID.randomUUID();
        sample = ServiceOrder.builder()
                .id(sampleId)
                .itemName("Phone")
                .condition("Dead Battery")
                .problemDescription("Won’t turn on")
                .technicianId("tech-1")
                .userId("user-1")
                .serviceDate(LocalDate.now().plusDays(1))
                .paymentMethod("CASH")
                .couponApplied(false)
                .status("WAITING CONFIRMATION")
                .build();
    }


    @Test
    void createOrder_valid_returnsCreated() throws Exception {
        User u = new User(); u.setId("user-1");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(u, null, List.of())
        );

        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName(sample.getItemName())
                .condition(sample.getCondition())
                .problemDescription(sample.getProblemDescription())
                .serviceDate(sample.getServiceDate())
                .technicianId(sample.getTechnicianId())
                .paymentMethod(sample.getPaymentMethod())
                .couponApplied(sample.isCouponApplied())
                .build();

        when(service.createOrder(any(ServiceOrder.class))).thenReturn(sample);

        mvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", endsWith("/api/orders/" + sampleId)))
                .andExpect(jsonPath("$.status").value("WAITING CONFIRMATION"))
                .andExpect(jsonPath("$.id").value(sampleId.toString()));
    }

    @Test
    void getAllOrders_returns200AndList() throws Exception {
        when(service.getAllOrders()).thenReturn(List.of(sample));

        mvc.perform(get("/api/orders"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(sampleId.toString()));
    }

    @Test
    void getOrderById_found_returns200() throws Exception {
        when(service.getOrderById(sampleId)).thenReturn(sample);

        mvc.perform(get("/api/orders/{id}", sampleId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Phone"));
    }

    @Test
    void getOrderById_missing_returns404() throws Exception {
        when(service.getOrderById(any())).thenReturn(null);

        mvc.perform(get("/api/orders/{id}", UUID.randomUUID()))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateOrder_found_returns200() throws Exception {
        CreateServiceOrderRequest req = CreateServiceOrderRequest.builder()
                .itemName("X")
                .condition("Y")
                .problemDescription("Z")
                .serviceDate(LocalDate.now().plusDays(2))
                .technicianId("tech-2")
                .paymentMethod("CARD")
                .couponApplied(true)
                .estimatedCompletionTime("1 day")
                .estimatedPrice(100_00)
                .status("IN_PROGRESS")
                .build();

        ServiceOrder updated = ServiceOrder.builder()
                .id(sampleId)
                .userId("user-1")
                .itemName("X")
                .condition("Y")
                .problemDescription("Z")
                .serviceDate(req.getServiceDate())
                .technicianId("tech-2")
                .paymentMethod("CARD")
                .couponApplied(true)
                .estimatedCompletionTime("1 day")
                .estimatedPrice(100_00)
                .status("IN_PROGRESS")
                .build();

        when(service.update(eq(sampleId), any()))
                .thenReturn(Optional.of(updated));

        mvc.perform(put("/api/orders/{id}", sampleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("IN_PROGRESS"));
    }


    @Test
    void deleteOrder_found_returns204() throws Exception {
        when(service.delete(sampleId)).thenReturn(true);

        mvc.perform(delete("/api/orders/{id}", sampleId))
                .andExpect(status().isNoContent());
    }

    @Test
    void deleteOrder_missing_returns404() throws Exception {
        when(service.delete(any())).thenReturn(false);

        mvc.perform(delete("/api/orders/{id}", sampleId))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllTechnicians_filtersByRole() throws Exception {
        User tech  = new User(); tech.setId("t1"); tech.setRole("TECHNICIAN");
        User other = new User(); other.setId("u2"); other.setRole("USER");
        when(userRepo.findAll()).thenReturn(List.of(tech, other));

        mvc.perform(get("/api/orders/technicians"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("t1"))
                .andExpect(jsonPath("$[?(@.id=='u2')]").doesNotExist());
    }

    @Test
    void getOrderHistory_returnsOrdersWithNames() throws Exception {
        User u = new User(); u.setId("user-1");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(u, null, List.of())
        );

        when(service.findOrdersByUserId("user-1")
        ).thenReturn(List.of(sample));

        User tech = new User(); tech.setId("tech-1"); tech.setFullName("Alice");
        when(userRepo.findById("tech-1")
        ).thenReturn(Optional.of(tech));

        mvc.perform(get("/api/orders/user/history"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].technicianName").value("Alice"))
                .andExpect(jsonPath("$[0].id").value(sampleId.toString()));
    }
}
