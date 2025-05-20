package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class PaymentServiceImplTest {


    @InjectMocks
    PaymentServiceImpl paymentService;

    @Mock
    PaymentRepository paymentRepository;

    List<Payment> payments;

    // dummy data
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
    }

    // happy
    @Test
    void createPayment() {
        Payment newPayment = new Payment();
        newPayment.setPaymentId("id-03");
        newPayment.setPaymentName("DANA");
        newPayment.setPaymentBankNumber("9876543210");

        doReturn(payments).when(paymentRepository).findAll();
        doReturn(newPayment).when(paymentRepository).save(newPayment);

        paymentService.createPayment(newPayment);

        verify(paymentRepository).save(newPayment);
    }

    // unhappy
    @Test
    void createPaymentInvalid() {
        Payment existingPayment = new Payment();
        existingPayment.setPaymentId("id-01");
        existingPayment.setPaymentName("OVO");
        existingPayment.setPaymentBankNumber("070707070");

        doReturn(payments).when(paymentRepository).findAll();

        assertThrows(IllegalArgumentException.class,
                () -> paymentService.createPayment(existingPayment));

        verify(paymentRepository, never()).save(any());
    }

    // happy
    @Test
    void updateName() {
        Payment paymentToUpdate = payments.get(0);

        doReturn(paymentToUpdate).when(paymentRepository).findById("id-01");
        doReturn(paymentToUpdate).when(paymentRepository).save(any(Payment.class));

        paymentService.updatePaymentName("id-01", "LinkAja");

        assertEquals("LinkAja", paymentToUpdate.getPaymentName());
        verify(paymentRepository).save(paymentToUpdate);
    }

    // happy
    @Test
    void updateNameInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.updatePaymentName("id-01", "   "));

        verify(paymentRepository, never()).save(any());
    }


    // happy
    @Test
    void updateBankNumber() {
        Payment paymentToUpdate = payments.get(0);

        doReturn(paymentToUpdate).when(paymentRepository).findById("id-01");
        doReturn(paymentToUpdate).when(paymentRepository).save(any(Payment.class));

        paymentService.updatePaymentBankNumber("id-01", "9998887776");

        assertEquals("9998887776", paymentToUpdate.getPaymentBankNumber());
        verify(paymentRepository).save(paymentToUpdate);
    }

    // unhappy
    @Test
    void updateBankNumberInvalid() {
        assertThrows(IllegalArgumentException.class,
                () -> paymentService.updatePaymentBankNumber("id-01", "abc123"));

        verify(paymentRepository, never()).save(any());
    }


    // happy
    @Test
    void findById() {
        Payment payment = payments.get(0);

        doReturn(payment).when(paymentRepository).findById("id-01");

        Payment result = paymentService.findById("id-01");

        assertEquals("id-01", result.getPaymentId());
    }

    // unhappy
    @Test
    void findByIdNonexistent() {
        doReturn(null).when(paymentRepository).findById("noidhere");

        assertNull(paymentService.findById("noidhere"));
    }

    // happy
    @Test
    void findByName() {
        Payment payment = payments.get(0);

        doReturn(payment).when(paymentRepository).findByName("GoPay");

        Payment result = paymentService.findByName("GoPay");

        assertEquals("GoPay", result.getPaymentName());
    }

    // unhappy
    @Test
    void findByNameInvalid() {
        doReturn(null).when(paymentRepository).findByName("whatisaname");

        assertNull(paymentService.findByName("whatisaname"));
    }

    // happy
    @Test
    void findByBankNumber() {
        Payment payment = payments.get(0);

        doReturn(payment).when(paymentRepository).findByBankNumber("124567890");

        Payment result = paymentService.findByBankNumber("124567890");

        assertEquals("124567890", result.getPaymentBankNumber());
    }

    // unhappy: find by bank number invalid
    @Test
    void findByBankNumberInvalid() {
        doReturn(null).when(paymentRepository).findByBankNumber("notanumber");

        assertNull(paymentService.findByBankNumber("notanumber"));
    }

    // happy
    @Test
    void findAllPayment() {
        doReturn(payments).when(paymentRepository).findAll();

        List<Payment> result = paymentService.findAllPayment();

        assertEquals(2, result.size());
        assertEquals("id-01", result.get(0).getPaymentId());
        assertEquals("id-02", result.get(1).getPaymentId());

        verify(paymentRepository).findAll();
    }

    @Test
    void testUpdatePayment() {
        Payment original = payments.get(0);  // GoPay, id-01

        Payment updated = new Payment();
        updated.setPaymentId("id-01");
        updated.setPaymentName("Dana");
        updated.setPaymentBankNumber("999999999");

        doReturn(original).when(paymentRepository).findById("id-01");
        doReturn(original).when(paymentRepository).save(any(Payment.class));

        Payment result = paymentService.updatePayment("id-01", updated);

        assertEquals("Dana", result.getPaymentName());
        assertEquals("999999999", result.getPaymentBankNumber());

        verify(paymentRepository).save(original);
    }

    @Test
    void testUpdatePaymentNotFound() {
        Payment update = new Payment();
        update.setPaymentId("id-99");
        update.setPaymentName("LinkAja");
        update.setPaymentBankNumber("000000000");

        assertThrows(RuntimeException.class, () -> {
            paymentService.updatePayment("id-99", update);
        });
    }

    @Test
    void testDeletePaymentSuccess() {

        paymentService.deletePayment("id-02");
        assertNull(paymentService.findById("id-02"));
    }

    @Test
    void testDeletePaymentNotFound() {
        doReturn(false).when(paymentRepository).deletePayment("id-99");
        doReturn(payments).when(paymentRepository).findAll();

        paymentService.deletePayment("id-99");

        assertEquals(2, paymentService.findAllPayment().size());
        verify(paymentRepository).deletePayment("id-99");
    }
}