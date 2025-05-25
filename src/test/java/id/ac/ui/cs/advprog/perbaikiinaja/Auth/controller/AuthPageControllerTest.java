// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/controller/AuthPageControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Config.SecurityConfig;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * MVC-slice tests for {@link AuthPageController}.
 * <p>
 *  ▸ real controller<br>
 *  ▸ mock AuthStrategy & UserRepository<br>
 *  ▸ built-in security filters and the real SecurityConfig are excluded
 *    so the slice can start without its transitive dependencies.
 */
@WebMvcTest(
        controllers = AuthPageController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = {SecurityConfig.class, JwtAuthenticationFilter.class}
        )
)
@AutoConfigureMockMvc(addFilters = false)   // disable the servlet-filter chain entirely
@Import(TestSecurityConfig.class)           // replace it with a minimal “permitAll” chain
class AuthPageControllerTest {

    @Autowired
    MockMvc mvc;

    @Autowired
    ObjectMapper mapper;

    @MockBean
    AuthStrategy authStrategy;

    @MockBean
    UserRepository userRepository;

    // ──────────────────────────────────────────────────────────────────────

    @Test
    @DisplayName("GET /api/auth/current → 401 when not logged in")
    void current_whenNotAuthenticated_returns401() throws Exception {
        SecurityContextHolder.clearContext();

        mvc.perform(get("/api/auth/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("GET /api/auth/current → 200 + UserResponse when logged in")
    void current_whenAuthenticated_returnsUserResponseWithRole() throws Exception {
        User u = new User();
        u.setId("u1");
        u.setUsername("jdoe");
        u.setFullName("John Doe");
        u.setEmail("jd@example.com");
        u.setPhone("08123456789");
        u.setAddress("123 Main St");
        u.setRole("USER");

        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(u, null, u.getAuthorities())
        );

        mvc.perform(get("/api/auth/current").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("u1"))
                .andExpect(jsonPath("$.username").value("jdoe"))
                .andExpect(jsonPath("$.fullName").value("John Doe"))
                .andExpect(jsonPath("$.email").value("jd@example.com"))
                .andExpect(jsonPath("$.phone").value("08123456789"))
                .andExpect(jsonPath("$.address").value("123 Main St"))
                .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @DisplayName("GET /api/users/{id} → 200 + UserResponse when found")
    void getUserById_whenFound_returnsUserResponseWithRole() throws Exception {
        User u2 = new User();
        u2.setId("u2");
        u2.setUsername("alice");
        u2.setFullName("Alice A");
        u2.setEmail("alice@example.com");
        u2.setPhone("08987654321");
        u2.setAddress("Wonderland");
        u2.setRole("TECHNICIAN");

        when(userRepository.findById("u2")).thenReturn(Optional.of(u2));

        mvc.perform(get("/api/users/{id}", "u2").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value("u2"))
                .andExpect(jsonPath("$.username").value("alice"))
                .andExpect(jsonPath("$.fullName").value("Alice A"))
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.phone").value("08987654321"))
                .andExpect(jsonPath("$.address").value("Wonderland"))
                .andExpect(jsonPath("$.role").value("TECHNICIAN"));
    }

    @Test
    @DisplayName("GET /api/users/{id} → 404 when not found")
    void getUserById_whenNotFound_returns404() throws Exception {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        mvc.perform(get("/api/users/{id}", "missing"))
                .andExpect(status().isNotFound());
    }
}