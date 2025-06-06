package id.ac.ui.cs.advprog.perbaikiinaja.Auth.config;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import id.ac.ui.cs.advprog.perbaikiinaja.Config.AuthConfig;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringJUnitConfig  // Meta‐annotation for @ExtendWith(SpringExtension.class) + @ContextConfiguration
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
@ContextConfiguration(classes = AuthConfig.class)
class AuthConfigTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    void passwordEncoderBeanExists() {
        assertNotNull(passwordEncoder, "passwordEncoder bean should be present");
        assertTrue(passwordEncoder instanceof BCryptPasswordEncoder,
                "passwordEncoder should be an instance of BCryptPasswordEncoder");
    }

    @Test
    void bcryptEncoderEncodesAndMatches() {
        String raw = "mySecretPwd";
        String hash = passwordEncoder.encode(raw);

        assertNotNull(hash, "Encoded password should not be null");
        assertNotEquals(raw, hash, "Encoded password should not equal raw password");
        assertTrue(passwordEncoder.matches(raw, hash),
                "matches() should return true for correct raw vs hash");
        assertFalse(passwordEncoder.matches("wrongPwd", hash),
                "matches() should return false for incorrect raw vs hash");
    }
}