package id.ac.ui.cs.advprog.perbaikiinaja.Auth.util;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.*;

class JwtUtilTest {

    private JwtUtil jwtUtil;

    @BeforeEach
    void setUp() {
        jwtUtil = new JwtUtil();
        // 32-byte secret for HS256
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "0123456789abcdef0123456789abcdef");
        // 1 hour in milliseconds
        ReflectionTestUtils.setField(jwtUtil, "validityInMs", 3_600_000L);
    }

    @Test
    void testGenerateAndValidateToken() {
        String userId   = "user123";
        String username = "john_doe";
        String role     = "USER";

        String token = jwtUtil.generateToken(userId, username, role);
        assertNotNull(token, "Token should not be null");
        assertTrue(jwtUtil.validateToken(token), "Generated token should be valid");
    }

    @Test
    void testGetClaims() {
        String userId   = "user456";
        String username = "jane_doe";
        String role     = "ADMIN";

        String token = jwtUtil.generateToken(userId, username, role);
        Claims claims = jwtUtil.getClaims(token);

        assertEquals(userId,   claims.getSubject(),                   "Subject (userId) must match");
        assertEquals(username, claims.get("username", String.class), "Username claim must match");
        assertEquals(role,     claims.get("role", String.class),     "Role claim must match");
        assertNotNull(claims.getIssuedAt(),   "IssuedAt should be set");
        assertNotNull(claims.getExpiration(), "Expiration should be set");
    }

    @Test
    void testValidateInvalidToken() {
        String invalidToken = "this.is.not.a.valid.token";
        assertFalse(jwtUtil.validateToken(invalidToken), "Malformed token must be invalid");
    }

    @Test
    void testExpiredToken() {
        // Make tokens immediately expired
        ReflectionTestUtils.setField(jwtUtil, "validityInMs", -1L);

        String token = jwtUtil.generateToken("u", "u", "u");
        assertFalse(jwtUtil.validateToken(token), "Token with past expiration must be invalid");
    }
}