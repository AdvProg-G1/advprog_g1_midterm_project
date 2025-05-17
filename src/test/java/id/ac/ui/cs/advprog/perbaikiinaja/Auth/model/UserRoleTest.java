package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserRoleTest {

    @Test
    void testGetAuthority() {
        assertEquals("CUSTOMER", Role.CUSTOMER.getAuthority());
        assertEquals("TECHNICIAN", Role.TECHNICIAN.getAuthority());
        assertEquals("ADMIN", Role.ADMIN.getAuthority());
    }

    @Test
    void testFromAuthority() {
        assertEquals(Role.CUSTOMER, Role.fromAuthority("CUSTOMER"));
        assertEquals(Role.TECHNICIAN, Role.fromAuthority("TECHNICIAN"));
        assertEquals(Role.ADMIN, Role.fromAuthority("ADMIN"));
    }

    @Test
    void testFromAuthorityIsCaseInsensitive() {
        assertEquals(Role.CUSTOMER, Role.fromAuthority("customer"));
    }

    @Test
    void testFromAuthorityThrowsExceptionOnInvalidInput() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromAuthority("UNKNOWN"));
    }
}