// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Auth/model/UserRoleTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Auth.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class UserRoleTest {

    @Test
    void getAuthorityReturnsExactText() {
        assertEquals("USER",       Role.USER.getAuthority());
        assertEquals("TECHNICIAN", Role.TECHNICIAN.getAuthority());
        assertEquals("ADMIN",      Role.ADMIN.getAuthority());
    }

    @Test
    void fromAuthorityParsesExactCase() {
        assertEquals(Role.USER,       Role.fromAuthority("USER"));
        assertEquals(Role.TECHNICIAN, Role.fromAuthority("TECHNICIAN"));
        assertEquals(Role.ADMIN,      Role.fromAuthority("ADMIN"));
    }

    @Test
    void fromAuthorityIsCaseInsensitive() {
        assertEquals(Role.USER, Role.fromAuthority("user"));
    }

    @Test
    void fromAuthorityThrowsOnUnknown() {
        assertThrows(IllegalArgumentException.class, () -> Role.fromAuthority("UNKNOWN"));
    }
}