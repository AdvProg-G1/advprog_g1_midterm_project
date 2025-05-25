package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import lombok.RequiredArgsConstructor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private static final Logger log = LoggerFactory.getLogger(PaymentController.class);

    private final PaymentService paymentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) {
        log.info("Received request to create payment: name={}, accountNumber={}", request.getPaymentName(), request.getPaymentBankNumber());

        PaymentResponse response = paymentService.createPayment(request);

        URI location = URI.create("/api/payments/" + response.getPaymentId());
        log.debug("Created payment with ID={}", response.getPaymentId());

        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'CUSTOMER')")
    public ResponseEntity<List<PaymentResponse>> getAll() {
        log.info("Fetching all payments");

        List<PaymentResponse> payments = paymentService.findAllPayment();
        log.debug("Total payments fetched: {}", payments.size());

        return ResponseEntity.ok(payments);
    }

    @GetMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'CUSTOMER')")
    public ResponseEntity<PaymentResponse> getById(@PathVariable ("paymentId") String paymentId) {
        log.info("Fetching payment with ID={}", paymentId);

        PaymentResponse response = paymentService.findById(paymentId);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<?> update(
            @PathVariable String paymentId,
            @RequestBody PaymentRequest paymentRequest
    ) {
        log.info("Attempting to update payment ID={} with new data: name={}, accountNumber={}",
                paymentId, paymentRequest.getPaymentName(), paymentRequest.getPaymentBankNumber());

        try {
            PaymentResponse updated = paymentService.updatePayment(paymentId, paymentRequest);
            log.debug("Successfully updated payment ID={}", updated.getPaymentId());
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            // Duplicate name validation error
            log.warn("Duplicate payment name '{}' while updating ID={}", paymentRequest.getPaymentName(), paymentId);
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            // Payment not found
            log.warn("Payment ID '{}' not found during update", paymentId);
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{paymentId}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable String paymentId) {
        log.warn("Deleting payment with ID={}", paymentId);
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    // For structured error handling and output on frontend
    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
            log.error("IllegalStateException occurred: {}", ex.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
            log.error("IllegalArgumentException occurred: {}", ex.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
