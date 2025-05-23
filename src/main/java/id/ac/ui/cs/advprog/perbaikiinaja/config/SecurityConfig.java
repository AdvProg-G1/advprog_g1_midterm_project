// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/config/SecurityConfig.java
package id.ac.ui.cs.advprog.perbaikiinaja.config;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ─── Disable CSRF on all our API endpoints ──────────────────────────
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/api/reviews/**",
                                "/api/auth/**",
                                "/api/users/**",
                                "/api/orders/**",
                                "/api/repair/**",
                                "/api/report/**",
                                "/api/coupons/**"
                        )
                )

                // ─── Keep JSESSIONID after login ────────────────────────────────────
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // ─── Authorization rules ───────────────────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        // public assets and auth pages
                        .requestMatchers("/", "/auth/**", "/assets/**", "/favicon.ico").permitAll()

                        // role-scoped areas
                        .requestMatchers("/user/**")       .hasAuthority("USER")
                        .requestMatchers("/technician/**") .hasAuthority("TECHNICIAN")
                        .requestMatchers("/admin/**")      .hasAuthority("ADMIN")

                        // everything else (including our API) must be authenticated
                        .anyRequest().authenticated()
                )

                // ─── No default login form or HTTP Basic ───────────────────────────
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())

                // ─── Add JWT filter (if you’re ever using JWT headers) ────────────
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}