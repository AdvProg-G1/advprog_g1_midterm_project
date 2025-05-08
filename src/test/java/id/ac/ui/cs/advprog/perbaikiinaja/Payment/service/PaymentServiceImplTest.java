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
        payment1.setPaymentBankNumber("124567890");

        Payment payment2 = new Payment();
        payment2.setPaymentId("id-02");
        payment2.setPaymentName("OVO");
        payment2.setPaymentBankNumber("070707070");

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
        newPayment.setPaymentBankNumber("1234567890");

        assertDoesNotThrow(() -> paymentService.createPayment(newPayment));
        assertEquals(3, paymentRepository.findAll().size());
    }

    // unhappy: create existing payment
    @Test
    void createPaymentInvalid() {
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId("id-01");
        existingPayment.setPaymentName("GoPay");
        existingPayment.setPaymentBankNumber("124567890");

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
        paymentToUpdate.setPaymentBankNumber("1111111111");

        assertDoesNotThrow(() -> paymentService.updatePaymentBankNumber(paymentToUpdate.getPaymentId(), "1111111111"));
        assertEquals("1111111111", paymentRepository.findById(paymentToUpdate.getPaymentId()).getPaymentBankNumber());
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
        Payment payment = paymentService.findById(idToFind);

        Payment result = paymentService.findById(payment.getPaymentId());
        assertEquals(idToFind, result.getPaymentId());
    }

    // unhappy
    @Test
    void findByIdNonexistent() {
        String idToFind = "onetwothree";
        Payment payment = paymentService.findById(idToFind);

        Payment result = paymentService.findById(payment.getPaymentId());
        assertNull(paymentService.findById(result.getPaymentId()));
    }

    // happy
    @Test
    void findByName() {
        String nameToFind = "GoPay";
        Payment payment = paymentService.findByName(nameToFind);

        Payment result = paymentService.findByName(payment.getPaymentName());
        assertEquals(nameToFind, result.getPaymentName());
    }

    // unhappy
    @Test
    void findByNameInvalid() {
        String nameToFind = "helloworld";
        Payment payment = paymentService.findByName(nameToFind);

        Payment result = paymentService.findByName(payment.getPaymentName());
        assertNull(paymentService.findByName(result.getPaymentName()));
    }

    // happy
    @Test
    void findByBankNumber() {
        String bankNumberToFind = "124567890";
        Payment payment = paymentService.findByBankNumber(bankNumberToFind);

        Payment result = paymentService.findByBankNumber(payment.getPaymentBankNumber());
        assertEquals(bankNumberToFind, result.getPaymentBankNumber());
    }

    // unhappy
    @Test
    void findByBankNumberInvalid() {
        String bankNumberToFind = "whatisthis";
        Payment payment = paymentService.findByBankNumber(bankNumberToFind);

        Payment result = paymentService.findByBankNumber(payment.getPaymentBankNumber());
        assertNull(paymentService.findByBankNumber(result.getPaymentBankNumber()));
    }


}