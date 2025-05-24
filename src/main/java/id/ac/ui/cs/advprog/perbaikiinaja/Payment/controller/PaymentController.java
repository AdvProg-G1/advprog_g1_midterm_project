package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
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
    public Payment createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
        return payment;
    }

    @GetMapping("/{paymentId}")
    public ResponseEntity<Payment> getById(@PathVariable ("paymentId") String paymentId) {
        return ResponseEntity.ok(paymentService.findById(paymentId));
    }


    @PutMapping("/{paymentId}")
    public ResponseEntity<Payment> updatePayment(
            @PathVariable String paymentId,
            @RequestBody Payment payment
    ) {
        try {
            Payment updated = paymentService.updatePayment(paymentId, payment);
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
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayment();
    }
}
