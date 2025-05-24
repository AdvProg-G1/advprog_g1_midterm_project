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
        PaymentRequest request = new PaymentRequest();
        request.setPaymentName("DANA");
        request.setPaymentBankNumber("9876543210");

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
        PaymentRequest request = new PaymentRequest();
        request.setPaymentName("OVO");
        request.setPaymentBankNumber("070707070");

        when(paymentRepository.findAll()).thenReturn(payments);

        assertThrows(IllegalArgumentException.class, () -> paymentService.createPayment(request));
        verify(paymentRepository, never()).save(any());
    }

    // happy
    @Test
    void updatePaymentName() {
        Payment existing = payments.get(0); // GoPay

        when(paymentRepository.findById("id-01")).thenReturn(existing);
        when(paymentRepository.save(any(Payment.class))).thenReturn(existing);

        PaymentResponse response = paymentService.updatePaymentName("id-01", "ShopeePay");

        assertEquals("ShopeePay", response.getPaymentName());
        assertEquals("id-01", response.getPaymentId());
        verify(paymentRepository).save(existing);
    }

    // happy
    @Test
    void updatePaymentBankNumber() {
        Payment existing = payments.get(1); // OVO

        when(paymentRepository.findById("id-02")).thenReturn(existing);
        when(paymentRepository.save(any(Payment.class))).thenReturn(existing);

        PaymentResponse response = paymentService.updatePaymentBankNumber("id-02", "7777777777");

        assertEquals("7777777777", response.getPaymentBankNumber());
        assertEquals("id-02", response.getPaymentId());
        verify(paymentRepository).save(existing);
    }


    // happy
    @Test
    void findById() {
        Payment payment = payments.get(0);
        when(paymentRepository.findById("id-01")).thenReturn(payment);

        PaymentResponse response = paymentService.findById("id-01");

        assertEquals("id-01", response.getPaymentId());
        assertEquals("GoPay", response.getPaymentName());
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

    // happy
    @Test
    void testUpdatePayment() {
        Payment existing = payments.get(0); // GoPay

        PaymentRequest updateRequest = new PaymentRequest();
        updateRequest.setPaymentName("Dana");
        updateRequest.setPaymentBankNumber("999999999");

        when(paymentRepository.findById("id-01")).thenReturn(existing);
        when(paymentRepository.save(any(Payment.class))).thenReturn(existing);

        PaymentResponse response = paymentService.updatePayment("id-01", updateRequest);

        assertEquals("Dana", response.getPaymentName());
        assertEquals("999999999", response.getPaymentBankNumber());
        verify(paymentRepository).save(existing);
    }

    // unhappy
    @Test
    void testUpdatePaymentNotFound() {
        PaymentRequest updateRequest = new PaymentRequest();
        updateRequest.setPaymentName("Dana");
        updateRequest.setPaymentBankNumber("999999999");

        when(paymentRepository.findById("non-existent-id")).thenReturn(null);

        assertThrows(IllegalArgumentException.class, () ->
                paymentService.updatePayment("non-existent-id", updateRequest)
        );

        verify(paymentRepository, never()).save(any());
    }

    // happy
    @Test
    void testDeletePaymentSuccess() {

        paymentService.deletePayment("id-02");
        assertNull(paymentService.findById("id-02"));
    }

    // unhappy
    @Test
    void testDeletePaymentNotFound() {
        doReturn(false).when(paymentRepository).deletePayment("id-99");
        doReturn(payments).when(paymentRepository).findAll();

        paymentService.deletePayment("id-99");

        assertEquals(2, paymentService.findAllPayment().size());
        verify(paymentRepository).deletePayment("id-99");
    }
}