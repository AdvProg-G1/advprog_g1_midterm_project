package id.ac.ui.cs.advprog.perbaikiinaja;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

/**
 * Smoke test: can Spring start the whole application context?
 *
 * Two @MockBean declarations shadow real beans; we therefore allow
 * bean-definition overriding via a test-only property.
 */
@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class PerbaikiinAjaApplicationTests {

    @MockBean
    UserRepository userRepository;

    @MockBean
    AuthStrategy authStrategy;

    @Test
    void contextLoads() {
        // if we reach this line the context started successfully
    }
}