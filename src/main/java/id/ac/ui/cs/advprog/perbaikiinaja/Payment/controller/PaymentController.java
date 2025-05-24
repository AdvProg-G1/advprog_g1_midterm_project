package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<PaymentResponse> createPayment(@RequestBody PaymentRequest request) {
        return ResponseEntity.status(201).body(paymentService.createPayment(request));
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> getById(@PathVariable ("paymentId") String paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }

    @PutMapping("/{paymentId}")
    public ResponseEntity<PaymentResponse> updatePayment(
            @PathVariable String paymentId,
            @RequestBody PaymentRequest paymentRequest
    ) {
        try {
            PaymentResponse updated = paymentService.updatePayment(paymentId, paymentRequest);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<PaymentResponse> getAllPayments() {
        return paymentService.findAllPayment();
    }
}
