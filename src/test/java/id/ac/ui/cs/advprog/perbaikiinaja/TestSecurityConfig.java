// src/test/java/.../TestSecurityConfig.java
package id.ac.ui.cs.advprog.perbaikiinaja;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;

@Configuration
@EnableWebSecurity
public class TestSecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    	http
    	  .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        User mockUser = new User();
        mockUser.setId("user1");

        var auth = new TestingAuthenticationToken(mockUser, null);
        auth.setAuthenticated(true);
        SecurityContextHolder.getContext().setAuthentication(auth);

        return http.build();

    }
}

