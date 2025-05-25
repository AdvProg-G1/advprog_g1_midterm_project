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
                // disable CSRF on our API & actuator endpoints
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers(
                                "/actuator/**",
                                "/auth/register",
                                "/auth/login",
                                "/auth/logout",
                                "/api/reviews/**",
                                "/api/auth/**",
                                "/api/users/**",
                                "/api/orders/**",
                                "/api/repair/**",
                                "/api/report/**",
                                "/api/coupons/**",
                                "/api/payments/**"
                        )
                )

                // session management
                .sessionManagement(sm -> sm
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // authorization rules
                .authorizeHttpRequests(auth -> auth
                        // public assets & auth pages
                        .requestMatchers("/", "/auth/**", "/assets/**", "/favicon.ico").permitAll()

                        // actuator endpoints
                        .requestMatchers("/actuator/**").permitAll()

                        // role-scoped areas
                        .requestMatchers("/user/**")       .hasAuthority("USER")
                        .requestMatchers("/technician/**") .hasAuthority("TECHNICIAN")
                        .requestMatchers("/admin/**")      .hasAuthority("ADMIN")

                        // everything else must be authenticated
                        .anyRequest().authenticated()
                )

                // disable default login form & HTTP Basic
                .formLogin(f -> f.disable())
                .httpBasic(b -> b.disable())

                // add our JWT filter before the standard UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)

                // logout configuration
                .logout(logout -> logout
                        .logoutRequestMatcher(new AntPathRequestMatcher("/auth/logout", "GET"))
                        .logoutSuccessUrl("/auth/login.html")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                );

        return http.build();
    }
}
