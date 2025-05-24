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
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // ─── Disable CSRF on our API & auth endpoints ────────────────────
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/auth/register",
                                "/auth/login",
                                "/auth/logout",         // allow logout without CSRF
                                "/api/reviews/**",
                                "/api/auth/**",
                                "/api/users/**",
                                "/api/orders/**",
                                "/api/repair/**",
                                "/api/report/**",
                                "/api/coupons/**",
                                "api/payments/**"
                        )
                )

                // ─── Keep JSESSIONID after login ───────────────────────────────
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // ─── Authorization rules ───────────────────────────────────────
                .authorizeHttpRequests(auth -> auth
                        // public assets & auth pages
                        .requestMatchers("/", "/auth/**", "/assets/**", "/favicon.ico")
                        .permitAll()

                        // role-scoped areas
                        .requestMatchers("/user/**")       .hasAuthority("USER")
                        .requestMatchers("/technician/**") .hasAuthority("TECHNICIAN")
                        .requestMatchers("/admin/**")      .hasAuthority("ADMIN")

                        // everything else must be authenticated
                        .anyRequest().authenticated()
                )

                // ─── Disable default login form & HTTP Basic ───────────────────
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())

                // ─── JWT filter for our API calls ─────────────────────────────
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // ─── Logout configuration ────────────────────────────────────
                .logout(logout -> logout
                        // trigger logout on GET or POST /auth/logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
                        // after logout, send user here
                        .logoutSuccessUrl("/auth/login.html")
                        // kill the HTTP session
                        .invalidateHttpSession(true)
                        // remove the JSESSIONID cookie
                        .deleteCookies("JSESSIONID")
                        // allow everyone to hit this endpoint
                        .permitAll()
                );

        return http.build();
    }
}
