package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
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
import java.util.Optional;

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
        PaymentRequest request = new PaymentRequest("DANA", "9876543210");

        Payment savedPayment = new Payment();
        savedPayment.setPaymentId("id-03");
        savedPayment.setPaymentName("DANA");
        savedPayment.setPaymentBankNumber("9876543210");

        when(paymentRepository.findAll()).thenReturn(payments);
        when(paymentRepository.save(any(Payment.class))).thenReturn(savedPayment);

        PaymentResponse response = paymentService.createPayment(request);

        assertEquals("id-03", response.getPaymentId());
        assertEquals("DANA", response.getPaymentName());
        verify(paymentRepository).save(any(Payment.class));
    }

    // unhappy
    @Test
    void createPaymentInvalid() {
        PaymentRequest request = new PaymentRequest("OVO", "070707070");

        when(paymentRepository.findAll()).thenReturn(payments);

        assertThrows(IllegalStateException.class, () -> paymentService.createPayment(request));
        verify(paymentRepository, never()).save(any());
    }

    // happy
    @Test
    void findById() {
        Payment payment = payments.get(0);
        when(paymentRepository.findById("id-01")).thenReturn(Optional.ofNullable(payment));

        PaymentResponse response = paymentService.findById("id-01");

        assertEquals("id-01", response.getPaymentId());
        assertEquals("GoPay", response.getPaymentName());
    }


    // happy
    @Test
    void findAllPayment() {
        doReturn(payments).when(paymentRepository).findAll();

        List<PaymentResponse> result = paymentService.findAllPayment();

        assertEquals(2, result.size());
        assertEquals("id-01", result.get(0).getPaymentId());
        assertEquals("id-02", result.get(1).getPaymentId());

        verify(paymentRepository).findAll();
    }

    // happy
    @Test
    void testUpdatePayment() {
        Payment existing = payments.get(0); // GoPay

        PaymentRequest updateRequest = new PaymentRequest("Dana", "999999999");

        when(paymentRepository.findById("id-01")).thenReturn(Optional.ofNullable(existing));
        when(paymentRepository.save(any(Payment.class))).thenReturn(existing);

        PaymentResponse response = paymentService.updatePayment("id-01", updateRequest);

        assertEquals("Dana", response.getPaymentName());
        assertEquals("999999999", response.getPaymentBankNumber());
        assert existing != null;
        verify(paymentRepository).save(existing);
    }

    // happy
    @Test
    void testDeletePaymentSuccess() {

        paymentService.deletePayment("id-02");
        assertNull(paymentService.findById("id-02"));
    }
}