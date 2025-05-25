// src/test/java/id/ac/ui/cs/advprog/perbaikiinaja/Payment/controller/PaymentControllerTest.java
package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

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
    }

    @Test
    void create_returns201AndLocation() {
        when(paymentService.createPayment(any(PaymentRequest.class)))
                .thenReturn(sampleResponse);

        ResponseEntity<PaymentResponse> resp = paymentController.create(sampleRequest);

        assertEquals(HttpStatus.CREATED, resp.getStatusCode());
        assertEquals(URI.create("/api/payments/id-01"), resp.getHeaders().getLocation());
        assertSame(sampleResponse, resp.getBody());
    }

    @Test
    void getAll_returns200AndList() {
        when(paymentService.findAllPayment())
                .thenReturn(List.of(sampleResponse));

        ResponseEntity<List<PaymentResponse>> resp = paymentController.getAll();

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        var body = resp.getBody();
        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals("id-01", body.get(0).getPaymentId());
    }

    @Test
    void getById_returns200AndItem() {
        when(paymentService.findById("id-01"))
                .thenReturn(sampleResponse);

        ResponseEntity<PaymentResponse> resp = paymentController.getById("id-01");

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(sampleResponse, resp.getBody());
    }

    @Test
    void update_success_returns200() {
        when(paymentService.updatePayment(eq("id-01"), any(PaymentRequest.class)))
                .thenReturn(sampleResponse);

        ResponseEntity<?> resp = paymentController.update("id-01", sampleRequest);

        assertEquals(HttpStatus.OK, resp.getStatusCode());
        assertSame(sampleResponse, resp.getBody());
    }

    @Test
    void update_duplicate_throwsConflict() {
        when(paymentService.updatePayment(eq("id-01"), any()))
                .thenThrow(new IllegalStateException("Duplicate"));

        ResponseEntity<?> resp = paymentController.update("id-01", sampleRequest);

        assertEquals(HttpStatus.CONFLICT, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof Map);
        assertEquals("Duplicate", ((Map<?,?>)resp.getBody()).get("error"));
    }

    @Test
    void update_notFound_throws404() {
        when(paymentService.updatePayment(eq("id-01"), any()))
                .thenThrow(new IllegalArgumentException("Not found"));

        ResponseEntity<?> resp = paymentController.update("id-01", sampleRequest);

        assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
        assertTrue(resp.getBody() instanceof Map);
        assertEquals("Not found", ((Map<?,?>)resp.getBody()).get("error"));
    }

    @Test
    void delete_returns204() {
        doNothing().when(paymentService).deletePayment("id-01");

        ResponseEntity<Void> resp = paymentController.delete("id-01");

        assertEquals(HttpStatus.NO_CONTENT, resp.getStatusCode());
        assertNull(resp.getBody());
        verify(paymentService).deletePayment("id-01");
    }
}