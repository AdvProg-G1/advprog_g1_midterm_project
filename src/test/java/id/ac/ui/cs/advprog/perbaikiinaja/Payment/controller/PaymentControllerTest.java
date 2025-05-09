package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment samplePayment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        samplePayment = new Payment();
        samplePayment.setPaymentId("PAY123");
        samplePayment.setPaymentName("Bank BCA");
        samplePayment.setPaymentBankNumber("1234567890");
    }

    @Test
    void testGetAllPayments() {
        when(paymentService.findAllPayment()).thenReturn(List.of(samplePayment));

        List<Payment> result = paymentController.getAllPayments();

        assertEquals(1, result.size());
        assertEquals(samplePayment, result.get(0));
    }

    @Test
    void testCreatePayment() {
        when(paymentService.createPayment(any(Payment.class))).thenReturn(samplePayment);

        Payment result = paymentController.createPayment(samplePayment);

        assertEquals(samplePayment, result);
        verify(paymentService).createPayment(samplePayment);
    }

    @Test
    void testUpdatePayment() {
        when(paymentService.updatePayment(any(Payment.class))).thenReturn(samplePayment);

        Payment result = paymentController.updatePayment(samplePayment);

        assertEquals(samplePayment, result);
        verify(paymentService).updatePayment(samplePayment);
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentService).deletePayment("PAY123");

        ResponseEntity<Void> response = paymentController.deletePayment("PAY123");

        assertEquals(204, response.getStatusCodeValue());
        verify(paymentService).deletePayment("PAY123");
    }

    @Test
    void testGetPaymentById() {
        when(paymentService.findById("PAY123")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentById("PAY123");

        assertEquals(samplePayment, result);
    }

    @Test
    void testGetPaymentByName() {
        when(paymentService.findByName("Bank BCA")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentByName("Bank BCA");

        assertEquals(samplePayment, result);
    }

    @Test
    void testGetPaymentByBankNumber() {
        when(paymentService.findByBankNumber("1234567890")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentByBankNumber("1234567890");

        assertEquals(samplePayment, result);
    }
}