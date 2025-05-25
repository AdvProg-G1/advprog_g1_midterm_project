// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/controller/AuthPageControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.UserResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.TestSecurityConfig;
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


@WebMvcTest(
        controllers = AuthPageController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = JwtAuthenticationFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(TestSecurityConfig.class)
class AuthPageControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AuthStrategy authStrategy;

    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void current_whenNotAuthenticated_returns401() throws Exception {
        SecurityContextHolder.clearContext();

        mvc.perform(get("/api/auth/current"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void current_whenAuthenticated_returnsUserResponseWithRole() throws Exception {
        // Arrange principal
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

        // Act & Assert
        mvc.perform(get("/api/auth/current")
                        .accept(MediaType.APPLICATION_JSON))
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

        mvc.perform(get("/api/users/{id}", "u2")
                        .accept(MediaType.APPLICATION_JSON))
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
    void getUserById_whenNotFound_returns404() throws Exception {
        when(userRepository.findById("missing")).thenReturn(Optional.empty());

        mvc.perform(get("/api/users/{id}", "missing"))
                .andExpect(status().isNotFound());
    }
}
