package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig(MethodAuthorizationConfig.class)
class MethodAuthorizationConfigTest {

    @Autowired
    private RoleHierarchyImpl roleHierarchy;

    @Test
    void roleHierarchyBeanExists() {
        assertNotNull(roleHierarchy, "RoleHierarchyImpl bean should be present");
    }

    @Test
    void hierarchyBeanIsRoleHierarchyImpl() {
        assertTrue(roleHierarchy instanceof RoleHierarchyImpl,
                "Bean should be instance of RoleHierarchyImpl");
    }

    @Test
    void adminCanReachTechnicianAndCustomer() {
        // Starting from ADMIN
        Collection<GrantedAuthority> reachable = roleHierarchy.getReachableGrantedAuthorities(
                List.of(new SimpleGrantedAuthority("ADMIN"))
        ).stream().collect(Collectors.toSet());

        assertTrue(authoritiesContain(reachable, "ADMIN"),      "Should contain ADMIN");
        assertTrue(authoritiesContain(reachable, "TECHNICIAN"), "ADMIN should reach TECHNICIAN");
        assertTrue(authoritiesContain(reachable, "CUSTOMER"),   "ADMIN should reach CUSTOMER");
    }

    @Test
    void technicianCanReachCustomerOnly() {
        Collection<GrantedAuthority> reachable = roleHierarchy.getReachableGrantedAuthorities(
                List.of(new SimpleGrantedAuthority("TECHNICIAN"))
        ).stream().collect(Collectors.toSet());

        assertFalse(authoritiesContain(reachable, "ADMIN"),     "TECHNICIAN should not reach ADMIN");
        assertTrue(authoritiesContain(reachable, "TECHNICIAN"), "Should contain TECHNICIAN");
        assertTrue(authoritiesContain(reachable, "CUSTOMER"),   "TECHNICIAN should reach CUSTOMER");
    }

    @Test
    void customerReachesOnlyItself() {
        Collection<GrantedAuthority> reachable = roleHierarchy.getReachableGrantedAuthorities(
                List.of(new SimpleGrantedAuthority("CUSTOMER"))
        ).stream().collect(Collectors.toSet());

        assertEquals(1, reachable.size(), "CUSTOMER should only reach itself");
        assertTrue(authoritiesContain(reachable, "CUSTOMER"));
    }

    // Helper to check if a collection of GrantedAuthority contains a specific role
    private boolean authoritiesContain(Collection<GrantedAuthority> auths, String role) {
        return auths.stream()
                .map(GrantedAuthority::getAuthority)
                .anyMatch(role::equals);
    }
}