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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.mockito.Mockito.when;
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

    @Test
    @DisplayName("POST /auth/login → redirects USER to /user/home.html")
    void login_successfulUser_redirectsToUserHome() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("jdoe");
        mockUser.setRole("USER");

        when(authStrategy.login("jdoe", "secret")).thenReturn(mockUser);

        mvc.perform(post("/auth/login")
                        .param("username", "jdoe")
                        .param("password", "secret"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/home.html"));
    }

    @Test
    @DisplayName("POST /auth/login → redirects TECHNICIAN to /technician/home.html")
    void login_successfulTechnician_redirectsToTechnicianHome() throws Exception {
        User technician = new User();
        technician.setUsername("techie");
        technician.setRole("TECHNICIAN");

        when(authStrategy.login("techie", "pw123")).thenReturn(technician);

        mvc.perform(post("/auth/login")
                        .param("username", "techie")
                        .param("password", "pw123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/technician/home.html"));
    }

    @Test
    @DisplayName("POST /auth/login → redirects back to login page on failure")
    void login_failure_redirectsBackWithFlashMessage() throws Exception {
        when(authStrategy.login("jdoe", "wrongpass"))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mvc.perform(post("/auth/login")
                        .param("username", "jdoe")
                        .param("password", "wrongpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void login_withUserRole_redirectsToUserHome() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("user1");
        mockUser.setPassword("password");
        mockUser.setRole("USER");

        when(authStrategy.login("user1", "password")).thenReturn(mockUser);

        mvc.perform(post("/auth/login")
                        .param("username", "user1")
                        .param("password", "password"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/user/home.html"));
    }

    @Test
    void login_withTechnicianRole_redirectsToTechnicianHome() throws Exception {
        User mockUser = new User();
        mockUser.setUsername("techie");
        mockUser.setPassword("pw123");
        mockUser.setRole("TECHNICIAN");

        when(authStrategy.login("techie", "pw123")).thenReturn(mockUser);

        mvc.perform(post("/auth/login")
                        .param("username", "techie")
                        .param("password", "pw123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/technician/home.html"));
    }


    @Test
    void login_withAdminRole_redirectsToAdminHome() throws Exception {
        User admin = new User();
        admin.setUsername("admin1");
        admin.setPassword("adminpass");
        admin.setRole("ADMIN");

        when(authStrategy.login("admin1", "adminpass")).thenReturn(admin);

        mvc.perform(post("/auth/login")
                        .param("username", "admin1")
                        .param("password", "adminpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/admin/home.html"));
    }

    @Test
    void login_invalidCredentials_redirectsBackWithError() throws Exception {
        when(authStrategy.login("baduser", "badpass"))
                .thenThrow(new RuntimeException("Invalid credentials"));

        mvc.perform(post("/auth/login")
                        .param("username", "baduser")
                        .param("password", "badpass"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/login"));
    }

    @Test
    void rootEndpoint_shouldForwardToWelcomePage() throws Exception {
        mvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/auth/welcome.html"));
    }

    @Test
    void showRegister_shouldForwardToRegisterPage() throws Exception {
        mvc.perform(get("/auth/register"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/auth/register.html"));
    }

    @Test
    void showLogin_shouldForwardToLoginPage() throws Exception {
        mvc.perform(get("/auth/login"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/auth/login.html"));
    }

    @Test
    void showWelcome_shouldForwardToWelcomePage() throws Exception {
        mvc.perform(get("/auth/welcome"))
                .andExpect(status().isOk())
                .andExpect(forwardedUrl("/auth/welcome.html"));
    }

    @Test
    void register_withValidationErrors_redirectsToRegister() throws Exception {
        mvc.perform(post("/auth/register")
                        .param("username", "") // Invalid input
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/register"))
                .andExpect(flash().attributeExists("errors"));
    }

    @Test
    void register_withValidationErrors_redirectsBackToRegisterWithErrors() throws Exception {
        mvc.perform(post("/auth/register")
                        .param("username", "")
                        .param("email", "")
                        .param("password", ""))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/auth/register"))
                .andExpect(flash().attributeExists("errors"));
    }

}