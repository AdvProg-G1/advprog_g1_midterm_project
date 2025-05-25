package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> create(@RequestBody PaymentRequest request) {
        PaymentResponse response = paymentService.createPayment(request);

        URI location = URI.create("/api/payments/" + response.getPaymentId());
        return ResponseEntity
                .created(location)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<PaymentResponse>> getAll() {
        return ResponseEntity.ok(paymentService.findAllPayment());
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable ("paymentId") String paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<?> update(
            @PathVariable String paymentId,
            @RequestBody PaymentRequest paymentRequest
    ) {
        try {
            PaymentResponse updated = paymentService.updatePayment(paymentId, paymentRequest);
            return ResponseEntity.ok(updated);
        } catch (IllegalStateException e) {
            // Duplicate name validation error
            return ResponseEntity
                    .status(HttpStatus.CONFLICT)
                    .body(Map.of("error", e.getMessage()));
        } catch (IllegalArgumentException e) {
            // Payment not found
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> delete(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @RestControllerAdvice
    public static class GlobalExceptionHandler {

        @ExceptionHandler(IllegalStateException.class)
        public ResponseEntity<Map<String, String>> handleIllegalState(IllegalStateException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.badRequest().body(error);
        }

        @ExceptionHandler(IllegalArgumentException.class)
        public ResponseEntity<Map<String, String>> handleIllegalArgument(IllegalArgumentException ex) {
            Map<String, String> error = new HashMap<>();
            error.put("message", ex.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
    }
}
