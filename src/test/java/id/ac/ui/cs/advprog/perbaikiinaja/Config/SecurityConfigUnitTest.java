// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Config/SecurityConfigUnitTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Config;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SecurityConfigUnitTest {

    @Test
    void securityFilterChain_buildsChainAndInvokesAllConfigurers() throws Exception {
        // Arrange
        JwtAuthenticationFilter jwtFilter = mock(JwtAuthenticationFilter.class);
        SecurityConfig config = new SecurityConfig(jwtFilter);

        HttpSecurity http = mock(HttpSecurity.class);
        when(http.csrf(any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.formLogin(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);
        when(http.addFilterBefore(eq(jwtFilter), eq(UsernamePasswordAuthenticationFilter.class))).thenReturn(http);
        when(http.logout(any())).thenReturn(http);

        DefaultSecurityFilterChain expectedChain = mock(DefaultSecurityFilterChain.class);
        when(http.build()).thenReturn(expectedChain);

        // Act
        SecurityFilterChain actualChain = config.securityFilterChain(http);

        // Assert
        assertSame(expectedChain, actualChain);
        verify(http).csrf(any());
        verify(http).sessionManagement(any());
        verify(http).authorizeHttpRequests(any());
        verify(http).formLogin(any());
        verify(http).httpBasic(any());
        verify(http).addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        verify(http).logout(any());
        verify(http).build();
    }
}