package id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PaymentRepositoryTest {

    private PaymentRepository paymentRepository;

    @BeforeEach
    void setUp() {
        paymentRepository = new PaymentRepository();

        Payment payment1 = new Payment();
        payment1.setPaymentId("id-01");
        payment1.setPaymentName("GoPay");
        payment1.setPaymentBankNumber("124567890");

        Payment payment2 = new Payment();
        payment2.setPaymentId("id-02");
        payment2.setPaymentName("OVO");
        payment2.setPaymentBankNumber("070707070");

        paymentRepository.save(payment1);
        paymentRepository.save(payment2);
    }

    // happy
    @Test
    void testSaveNewPaymentMethod() {
        Payment newPayment = new Payment();
        newPayment.setPaymentId("id-03");
        newPayment.setPaymentName("DANA");
        newPayment.setPaymentBankNumber("9988776655");

        paymentRepository.save(newPayment);

        assertEquals(3, paymentRepository.findAll().size());
        assertTrue(paymentRepository.findAll().contains(newPayment));
    }

    // happy
    @Test
    void testSaveUpdateExistingPaymentMethod() {
        Payment existingPayment = paymentRepository.findById("id-01");
        existingPayment.setPaymentBankNumber("999999999");

        paymentRepository.save(existingPayment);

        assertEquals("999999999", paymentRepository.findById("id-01").getPaymentBankNumber());
    }

    // happy
    @Test
    void testFindByIdWithValidId() {
        Payment found = paymentRepository.findById("id-02");

        assertNotNull(found);
        assertEquals("OVO", found.getPaymentName());
    }

    // unhappy
    @Test
    void testFindByIdWithInvalidId() {
        Payment found = paymentRepository.findById("superfakeid");

        assertNull(found);
    }

    // happy
    @Test
    void testFindByNameWithValidName() {
        Payment found = paymentRepository.findByName("GoPay");

        assertNotNull(found);
        assertEquals("id-01", found.getPaymentId());
    }

    // unhappy
    @Test
    void testFindByNameWithInvalidName() {
        Payment found = paymentRepository.findByName("nonamehere");

        assertNull(found);
    }

    // happy
    @Test
    void testFindByBankNumber() {
        Payment found = paymentRepository.findByBankNumber("124567890");

        assertNotNull(found);
        assertEquals("id-01", found.getPaymentId());
        assertEquals("GoPay", found.getPaymentName());
    }

    // unhappy
    @Test
    void testFindByBankNumberInvalid() {
        Payment found = paymentRepository.findByBankNumber("fakenumber");

        assertNull(found);
    }

    // happy
    @Test
    void testDeleteExistingPayment() {
        boolean deleted = paymentRepository.deletePayment("id-01");

        assertTrue(deleted);
        assertNull(paymentRepository.findById("id-01"));
        assertEquals(1, paymentRepository.findAll().size());
    }

    // unhappy
    @Test
    void testDeleteNonExistingPayment() {
        boolean deleted = paymentRepository.deletePayment("superfakeid");

        assertFalse(deleted);
        assertEquals(2, paymentRepository.findAll().size());
    }
}