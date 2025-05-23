package id.ac.ui.cs.advprog.perbaikiinaja.Payment.controller;

import lombok.RequiredArgsConstructor;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.service.PaymentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public Payment createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
        return payment;
    }

    @GetMapping
    public ResponseEntity<Payment> getById(@PathVariable String paymentId) {
        return Optional.ofNullable(paymentService.findById(paymentId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<Payment> getByName(@PathVariable String paymentName) {
        return Optional.ofNullable(paymentService.findByName(paymentName))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{Id}")
    public ResponseEntity<Payment> getByBankNumber(@PathVariable String bankNumber) {
        return Optional.ofNullable(paymentService.findByBankNumber(bankNumber))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{Id}")
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

    @DeleteMapping("/{Id}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayment();
    }
}
