// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/PerbaikiinAjaApplicationTest.java
package id.ac.ui.cs.advprog.perbaikiinaja;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(
        classes = PerbaikiinAjaApplication.class,
        properties = "spring.main.allow-bean-definition-overriding=true"
)
class PerbaikiinAjaApplicationTest {

    @Test
    void contextLoads() {
        // If the application context fails to start, this will fail.
    }

}