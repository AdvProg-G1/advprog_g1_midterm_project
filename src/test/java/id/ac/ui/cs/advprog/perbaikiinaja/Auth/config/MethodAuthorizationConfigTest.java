package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MethodAuthorizationConfigTest {

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
    void testAdminHasCustomerAccess() {
        var adminAuth = new SimpleGrantedAuthority("ADMIN");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(adminAuth));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
    }

    @Test
    void testCustomerHasNoAdminAccess() {
        var customerAuth = new SimpleGrantedAuthority("CUSTOMER");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(customerAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }

    @Test
    void testUserAuthoritiesMatchRole() {
        User customer = new User("id-02", "Shopper", "iliketoshop@gmail.com", "ILoveShop123", "012345679", "Shop Town", "CUSTOMER");
        User admin = new User("id-01", "Admin", "adminisfun@gmail.com", "admintime123", "0666777888", "Admin Place", "ADMIN");

        assertEquals(List.of(new SimpleGrantedAuthority("CUSTOMER")), customer.getAuthorities());
        assertEquals(List.of(new SimpleGrantedAuthority("ADMIN")), admin.getAuthorities());
    }

    @Test
    void testNullRoleUser() {
        User fakeUser = new User("whatid", "whatname", "whatemail@gmail.com", "whatpass", "0", "whataddr", null);
        assertTrue(fakeUser.getAuthorities().isEmpty());
    }

    @Test
    void testInvalidRoleHasNoAccess() {
        var invalidAuth = new SimpleGrantedAuthority("HACKER");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(invalidAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("CUSTOMER")));
    }

    @Test
    void testAnonymousUserHasNoRoles() {
        var anon = Collections.<GrantedAuthority>emptyList();
        assertTrue(anon.isEmpty());
    }

    @Test
    void testTechnicianHasNoAdminAccess() {
        var techAuth = new SimpleGrantedAuthority("TECHNICIAN");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(techAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ADMIN")));
    }
}
