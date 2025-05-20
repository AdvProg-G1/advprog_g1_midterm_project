// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/config/MethodAuthorizationConfigTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MethodAuthorizationConfigTest {

    private RoleHierarchyImpl roleHierarchy;

    @BeforeEach
    void setup() {
        roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("""
            ADMIN > TECHNICIAN
            TECHNICIAN > CUSTOMER
        """);
    }

    @Test
    void adminHasCustomerAccess() {
        var authorities = roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(new SimpleGrantedAuthority("ADMIN")));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
    }

    @Test
    void customerHasNoAdminAccess() {
        var authorities = roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(new SimpleGrantedAuthority("CUSTOMER")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void userAuthoritiesMatchRole() {
        User customer = new User(
                "id-02",        // id
                "shopper01",    // username
                "Shopper",      // fullName
                "iliketoshop@gmail.com",
                "ILoveShop123",
                "012345679",
                "Shop Town",
                "CUSTOMER"
        );
        User admin = new User(
                "id-01",
                "admin01",
                "Admin",
                "adminisfun@gmail.com",
                "admintime123",
                "0666777888",
                "Admin Place",
                "ADMIN"
        );

        assertEquals(List.of(new SimpleGrantedAuthority("CUSTOMER")), customer.getAuthorities());
        assertEquals(List.of(new SimpleGrantedAuthority("ADMIN")), admin.getAuthorities());
    }

    @Test
    void nullRoleUserHasNoAuthorities() {
        User nobody = new User(
                "x", "anon", "Nobody", "no@body", "pw", "-", "-", null);
        assertTrue(nobody.getAuthorities().isEmpty());
    }

    @Test
    void invalidAuthorityHasNoAccess() {
        var authorities = roleHierarchy.getReachableGrantedAuthorities(
                Collections.singleton(new SimpleGrantedAuthority("HACKER")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
    }
}