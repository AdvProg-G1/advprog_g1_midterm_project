// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/filter/JwtAuthenticationFilterTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.util.JwtUtil;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class JwtAuthenticationFilterTest {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private JwtAuthenticationFilter filter;
    private MockHttpServletRequest request;
    private MockHttpServletResponse response;
    private FilterChain chain;

    @BeforeEach
    void setUp() {
        jwtUtil = mock(JwtUtil.class);
        userRepository = mock(UserRepository.class);
        filter = new JwtAuthenticationFilter(jwtUtil, userRepository);
        request = new MockHttpServletRequest();
        response = new MockHttpServletResponse();
        chain = mock(FilterChain.class);
        SecurityContextHolder.clearContext();
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void doFilterInternal_noAuthorizationHeader_doesNotAuthenticate() throws Exception {
        // no Authorization header set
        filter.doFilterInternal(request, response, chain);

        // chain should still be invoked
        verify(chain).doFilter(request, response);

        // no authentication should be set
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNull(auth, "Expected no authentication in security context");
    }

    @Test
    void doFilterInternal_invalidToken_doesNotAuthenticate() throws Exception {
        request.addHeader("Authorization", "Bearer some.token.here");
        when(jwtUtil.validateToken("some.token.here")).thenReturn(false);

        filter.doFilterInternal(request, response, chain);

        verify(jwtUtil).validateToken("some.token.here");
        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validTokenButUserNotFound_doesNotAuthenticate() throws Exception {
        request.addHeader("Authorization", "Bearer valid.token");
        when(jwtUtil.validateToken("valid.token")).thenReturn(true);

        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaims("valid.token")).thenReturn(claims);
        when(claims.get("username", String.class)).thenReturn("nonexistent");
        when(userRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        filter.doFilterInternal(request, response, chain);

        verify(jwtUtil).getClaims("valid.token");
        verify(userRepository).findByUsername("nonexistent");
        verify(chain).doFilter(request, response);
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    @Test
    void doFilterInternal_validTokenAndUserFound_setsAuthentication() throws Exception {
        request.addHeader("Authorization", "Bearer valid.token");
        when(jwtUtil.validateToken("valid.token")).thenReturn(true);

        Claims claims = mock(Claims.class);
        when(jwtUtil.getClaims("valid.token")).thenReturn(claims);
        when(claims.get("username", String.class)).thenReturn("johndoe");

        User user = mock(User.class);
        when(user.getAuthorities()).thenReturn(java.util.Collections.emptyList());
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(user));

        filter.doFilterInternal(request, response, chain);

        verify(jwtUtil).getClaims("valid.token");
        verify(userRepository).findByUsername("johndoe");
        verify(chain).doFilter(request, response);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assertNotNull(auth, "Expected authentication to be set");
        assertEquals(user, auth.getPrincipal(), "Principal should be the loaded User");
        assertTrue(auth.getAuthorities().isEmpty(), "Authorities should come from user");
    }
}