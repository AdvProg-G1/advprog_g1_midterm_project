package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.http.MediaType;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@RestController
@RequestMapping("/test-error")
class DummyErrorController {

    @GetMapping("/illegal-state")
    public String throwIllegalState() {
        throw new IllegalStateException("Illegal state occurred");
    }

    @GetMapping("/illegal-argument")
    public String throwIllegalArgument() {
        throw new IllegalArgumentException("Illegal argument provided");
    }
}

@WebMvcTest(controllers = DummyErrorController.class,
        excludeFilters = @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = JwtAuthenticationFilter.class))
@Import(PaymentController.GlobalExceptionHandler.class)
class PaymentControllerExceptionTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void whenIllegalStateExceptionThrown_thenReturnsBadRequest() throws Exception {
        mockMvc.perform(get("/test-error/illegal-state"))
                .andExpect(status().isBadRequest())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Illegal state occurred"));
    }

    @Test
    @WithMockUser
    void whenIllegalArgumentExceptionThrown_thenReturnsNotFound() throws Exception {
        mockMvc.perform(get("/test-error/illegal-argument"))
                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value("Illegal argument provided"));
    }
}


