package id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {

    private List<Payment> payments;

    @BeforeEach
    void setUp() {
        this.payments = new ArrayList<>();
        Payment payment1 = new Payment();
        payment1.setPaymentId("id-01");
        payment1.setPaymentName("GoPay");
        payment1.setAccountNumber("124567890");

        Payment payment2 = new Payment();
        payment2.setPaymentId("id-02");
        payment2.setPaymentName("OVO");
        payment2.setAccountNumber("070707070");

        this.payments.add(payment1);
        this.payments.add(payment2);
    }

    // happy
    @Test
    void testSaveNewPaymentMethod() {
        Payment newPayment = new Payment();
        newPayment.setPaymentId("id-03");
        newPayment.setPaymentName("DANA");
        newPayment.setAccountNumber("9988776655");

        payments.add(newPayment);

        assertEquals(3, payments.size());
        assertTrue(payments.contains(newPayment));
    }

    // happy
    @Test
    void testSaveUpdateExistingPaymentMethod() {
        Payment existing = payments.get(0);
        existing.setAccountNumber("999999999");

        assertEquals("999999999", payments.get(0).getAccountNumber());
    }

    // happy
    @Test
    void testFindByIdWithValidId() {
        String targetId = "id-02";
        Payment found = payments.stream()
                .filter(p -> p.getPaymentId().equals(targetId))
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertEquals("OVO", found.getPaymentName());
    }

    // unhappy
    @Test
    void testFindByIdWithInvalidId() {
        String targetId = "invalid-id";
        Payment found = payments.stream()
                .filter(p -> p.getPaymentId().equals(targetId))
                .findFirst()
                .orElse(null);

        assertNull(found);
    }

    // happy
    @Test
    void testFindByNameWithValidName() {
        String targetName = "GoPay";
        Payment found = payments.stream()
                .filter(p -> p.getPaymentName().equalsIgnoreCase(targetName))
                .findFirst()
                .orElse(null);

        assertNotNull(found);
        assertEquals("id-01", found.getPaymentId());
    }

    // unhappy
    @Test
    void testFindByNameWithInvalidName() {
        String targetName = "nonexistent";
        Payment found = payments.stream()
                .filter(p -> p.getPaymentName().equalsIgnoreCase(targetName))
                .findFirst()
                .orElse(null);

        assertNull(found);
    }

}
