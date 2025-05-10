package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    void testGetAuthority() {
        assertEquals("ROLE_CUSTOMER", Role.CUSTOMER.getAuthority());
        assertEquals("ROLE_TECHNICIAN", Role.TECHNICIAN.getAuthority());
        assertEquals("ROLE_ADMIN", Role.ADMIN.getAuthority());
    }

    @Test
    void testFromAuthority() {
        assertEquals(Role.CUSTOMER, Role.fromAuthority("ROLE_CUSTOMER"));
        assertEquals(Role.TECHNICIAN, Role.fromAuthority("ROLE_TECHNICIAN"));
        assertEquals(Role.ADMIN, Role.fromAuthority("ROLE_ADMIN"));
    }

    @Test
    void testFromAuthorityIsCaseInsensitive() {
        assertEquals(Role.CUSTOMER, Role.fromAuthority("role_customer"));
    }

    @Test
    void testFromAuthorityThrowsExceptionOnInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromAuthority("ROLE_UNKNOWN"));
    }
}