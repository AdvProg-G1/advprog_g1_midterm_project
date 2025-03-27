// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/repository/ServiceOrderRepositoryTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.model.ServiceOrder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

public class ServiceOrderRepositoryTest {

    private ServiceOrderRepository repository;

    @BeforeEach
    public void setUp() {
        repository = new ServiceOrderRepository();
    }

    @Test
    public void testSaveAndFind() {
        User user = new User(1L, "Jane Doe", "jane@example.com", "08123456780", "password", "Bandung");
        ServiceOrder order = new ServiceOrder(null, user, "Smartphone", "Fair", "Battery issue",
                LocalDate.now(), "Bank B", null, null, "Pending", null);
        repository.save(order);

        assertNotNull(order.getId());
        assertTrue(repository.findById(order.getId()).isPresent());
    }
}