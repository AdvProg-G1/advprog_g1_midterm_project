// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/model/ServiceOrderTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.model;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceOrderTest {

    @Test
    public void testServiceOrderCreation() {
        User user = new User(1L, "John Doe", "john@example.com", "08123456789", "password", "Jakarta");
        ServiceOrder order = new ServiceOrder(null, user, "Laptop", "Good", "Screen flickering",
                LocalDate.now(), "Bank A", null, null, "Pending", null);

        assertNotNull(order);
        assertEquals("Laptop", order.getItemName());
        assertEquals("Pending", order.getStatus());
    }
}