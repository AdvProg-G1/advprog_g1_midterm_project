package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;

@Configuration
public class MethodAuthorizationConfig {

    @Bean
    public RoleHierarchyImpl roleHierarchy() {
        var hierarchy = new RoleHierarchyImpl();
        // ADMIN > TECHNICIAN > CUSTOMER
        hierarchy.setHierarchy("""
            ADMIN > TECHNICIAN
            TECHNICIAN > CUSTOMER
        """);
        return hierarchy;
    }
}