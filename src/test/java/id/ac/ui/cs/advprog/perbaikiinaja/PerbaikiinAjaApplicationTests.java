package id.ac.ui.cs.advprog.perbaikiinaja;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(properties = {
        "spring.main.allow-bean-definition-overriding=true"
})
@TestPropertySource(properties = {
        "jwt.secret=TEST_SECRET_12345678901234567890123456789012",
        "jwt.expiration-ms=3600000"
})
class PerbaikiinAjaApplicationTests {

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthStrategy authStrategy;

    @Test
    void contextLoads() {
        // Intentionally left empty: this test ensures the
        // Spring Boot application context loads successfully.
    }
}