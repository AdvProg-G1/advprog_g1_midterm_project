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
        samplePayment.setPaymentId("id-01");
        samplePayment.setPaymentName("GoPay");
        samplePayment.setPaymentBankNumber("1234567890");
    }

    @Test
    void testGetAllPayments() {
        when(paymentService.findAllPayment()).thenReturn(List.of(samplePayment));

        List<Payment> result = paymentController.getAllPayments();

        assertEquals(1, result.size());
        assertEquals("id-01", result.get(0).getPaymentId());
    }

    @Test
    void testCreatePayment() {
        when(paymentService.createPayment(any(Payment.class))).thenReturn(samplePayment);

        Payment result = paymentController.createPayment(samplePayment);

        assertNotNull(result);
        assertEquals("GoPay", result.getPaymentName());
        verify(paymentService).createPayment(samplePayment);
    }

    @Test
    void testUpdatePayment() {
        when(paymentService.updatePayment(eq("id-01"), any(Payment.class))).thenReturn(samplePayment);

        Payment result = paymentController.updatePayment("id-01", samplePayment);

        assertEquals("id-01", result.getPaymentId());
        verify(paymentService).updatePayment("id-01", samplePayment);
    }

    @Test
    void testDeletePayment() {
        doNothing().when(paymentService).deletePayment("id-01");

        ResponseEntity<Void> response = paymentController.deletePayment("id-01");

        assertEquals(204, response.getStatusCodeValue());
        verify(paymentService).deletePayment("id-01");
    }

    @Test
    void testGetPaymentById() {
        when(paymentService.findById("id-01")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentById("id-01");

        assertEquals("id-01", result.getPaymentId());
    }

    @Test
    void testGetPaymentByName() {
        when(paymentService.findByName("GoPay")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentByName("GoPay");

        assertEquals("GoPay", result.getPaymentName());
    }

    @Test
    void testGetPaymentByBankNumber() {
        when(paymentService.findByBankNumber("1234567890")).thenReturn(samplePayment);

        Payment result = paymentController.getPaymentByBankNumber("1234567890");

        assertEquals("1234567890", result.getPaymentBankNumber());
    }
}