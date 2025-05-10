package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.Role;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class MethodAuthorizationConfigTest {

    @Autowired
    private RoleHierarchy roleHierarchy;

    @Test
    void adminHasCustomerAccess() {
        var adminAuth = new SimpleGrantedAuthority("ROLE_ADMIN");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(adminAuth));
        assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }

    @Test
    void customerHasNoAdminAccess() {
        var customerAuth = new SimpleGrantedAuthority("ROLE_CUSTOMER");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(customerAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void userAuthoritiesMatchRole() {
        User customer = new User("id-02", "Shopper", "iliketoshop@gmail.com", "ILoveShop123", "012345679", "Shop Town", "ROLE_CUSTOMER");
        User admin = new User("id-01", "Admin", "adminisfun@gmail.com", "admintime123", "0666777888", "Admin Place", "ROLE_ADMIN");

        assertTrue(customer.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
        assertFalse(customer.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        assertTrue(admin.getAuthorities().contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }

    @Test
    void nullRoleUserThrows() {
        User brokenUser = new User("id-04", "Ghost", "ghost@gmail.com", "boo123", "000", "Nowhere", null);
        assertThrows(NullPointerException.class, () -> brokenUser.getAuthorities());
    }

    @Test
    void invalidRoleHasNoAccess() {
        var invalidAuth = new SimpleGrantedAuthority("ROLE_HACKER");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(invalidAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_CUSTOMER")));
    }

    @Test
    void anonymousHasNoRoles() {
        var anonymous = Collections.<GrantedAuthority>emptyList();
        assertTrue(anonymous.isEmpty());
    }

    @Test
    void technicianHasNoAdminAccess() {
        var techAuth = new SimpleGrantedAuthority("ROLE_TECHNICIAN");
        var authorities = roleHierarchy.getReachableGrantedAuthorities(Collections.singleton(techAuth));
        assertFalse(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));
    }
}