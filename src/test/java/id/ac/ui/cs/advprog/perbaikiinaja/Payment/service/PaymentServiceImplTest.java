package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PaymentServiceImplTest {

    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Payment> payments;

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

        this.paymentRepository = new PaymentRepository();
    }

    // unhappy
    @Test
    void createPayment() {
        Payment newPayment = new Payment();
        newPayment.setPaymentId("id-03");
        newPayment.setPaymentName("DANA");
        newPayment.setAccountNumber("1234567890");

        assertDoesNotThrow(() -> paymentService.createPayment(newPayment));
        assertEquals(3, paymentRepository.findAll().size());
    }

    // unhappy: create existing payment
    @Test
    void createPaymentInvalid() {
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId("id-01");
        existingPayment.setPaymentName("GoPay");
        existingPayment.setAccountNumber("124567890");

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(existingPayment));
    }

    // happy
    @Test
    void updateName() {
        Payment paymentToUpdate = payments.get(0);
        paymentToUpdate.setPaymentName("Gojek");

        assertDoesNotThrow(() -> paymentService.updatePaymentName(paymentToUpdate.getPaymentId(), "Gojek"));
        assertEquals("Gojek", paymentRepository.findById(paymentToUpdate.getPaymentId()).getPaymentName());
    }

    // unhappy
    @Test
    void updateNameInvalid() {
        Payment paymentToUpdate = payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentName(paymentToUpdate.getPaymentId(), ""));
    }

    // happy
    @Test
    void updateBankNumber() {
        Payment paymentToUpdate = payments.get(0);
        paymentToUpdate.setAccountNumber("1111111111");

        assertDoesNotThrow(() -> paymentService.updatePaymentBankNumber(paymentToUpdate.getPaymentId(), "1111111111"));
        assertEquals("1111111111", paymentRepository.findById(paymentToUpdate.getPaymentId()).getAccountNumber());
    }

    // unhappy
    @Test
    void updateBankNumberInvalid() {
        Payment paymentToUpdate = payments.get(0);

        assertThrows(IllegalArgumentException.class, () -> paymentService.updatePaymentBankNumber(paymentToUpdate.getPaymentId(), "invalidNumber"));
    }

    // happy
    @Test
    void findById() {
        String idToFind = "id-01";
        Optional<Payment> payment = paymentService.findById(idToFind);

        assertTrue(payment.isPresent());
        assertEquals(idToFind, payment.get().getPaymentId());
    }

    // unhappy
    @Test
    void findByIdNonexistent() {
        String idToFind = "onetwothree";
        Optional<Payment> payment = paymentService.findById(idToFind);

        assertFalse(payment.isPresent());
    }

    // happy
    @Test
    void findByName() {
        String nameToFind = "GoPay";
        Optional<Payment> payment = paymentService.findByName(nameToFind);

        assertTrue(payment.isPresent());
        assertEquals(nameToFind, payment.get().getPaymentName());
    }

    // unhappy
    @Test
    void findByNameInvalid() {
        String nameToFind = "helloworld";
        Optional<Payment> payment = paymentService.findByName(nameToFind);

        assertFalse(payment.isPresent());
    }

    // happy
    @Test
    void findByBankNumber() {
        String bankNumberToFind = "124567890";
        Optional<Payment> payment = paymentService.findByBankNumber(bankNumberToFind);

        assertTrue(payment.isPresent());
        assertEquals(bankNumberToFind, payment.get().getAccountNumber());
    }

    // unhappy
    @Test
    void findByBankNumberInvalid() {
        String bankNumberToFind = "whatisthis";
        Optional<Payment> payment = paymentService.findByBankNumber(bankNumberToFind);

        assertFalse(payment.isPresent());
    }
}