package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private PaymentRequest sampleRequest;
    private PaymentResponse sampleResponse;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        sampleRequest = new PaymentRequest("GoPay", "1234567890");

        sampleResponse = new PaymentResponse("id-01", "GoPay", "1234567890");
        sampleResponse.setPaymentId("id-01");
        sampleResponse.setPaymentName("GoPay");
        sampleResponse.setPaymentBankNumber("1234567890");
    }

@Test
void testGetAllPayments() {
    when(paymentService.findAllPayment()).thenReturn(List.of(sampleResponse));

    List<PaymentResponse> result = paymentController.getAll().getBody();

    assertEquals(1, Objects.requireNonNull(result).size());
    assertEquals("id-01", result.get(0).getPaymentId());
    assertEquals("GoPay", result.get(0).getPaymentName());
}

    @Test
    void testCreatePayment() {
        when(paymentService.createPayment(any(PaymentRequest.class))).thenReturn(sampleResponse);

        PaymentResponse result = paymentController.create(sampleRequest).getBody();

        assertNotNull(result);
        assertEquals("id-01", result.getPaymentId());
        assertEquals("GoPay", result.getPaymentName());
    }

    @Test
    void testUpdatePayment() {
        when(paymentService.updatePayment(eq("id-01"), any(PaymentRequest.class))).thenReturn(sampleResponse);

        PaymentRequest updateRequest = new PaymentRequest("DANA", "999999999");

        PaymentResponse result = paymentController.update("id-01", updateRequest).getBody();

        assertEquals("id-01", result.getPaymentId());
        assertEquals("GoPay", result.getPaymentName()); // Assuming mock returns unchanged name
        assertEquals("1234567890", result.getPaymentBankNumber()); // Assuming mock returns unchanged bank number
    }

    @Test
    void testFindById() {
        when(paymentService.findById("id-01")).thenReturn(sampleResponse);

        PaymentResponse result = paymentController.getById("id-01").getBody();

        assertEquals("id-01", result.getPaymentId());
        assertEquals("GoPay", result.getPaymentName());
    }

    @Test
    void testDeletePayment() {
        // no return value to check, just verify call
        doNothing().when(paymentService).deletePayment("id-01");

        paymentController.delete("id-01");

        verify(paymentService).deletePayment("id-01");
    }
}