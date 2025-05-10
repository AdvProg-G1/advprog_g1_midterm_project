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

    @PostMapping("/create")
    public Payment createPayment(@RequestBody Payment payment) {
        paymentService.createPayment(payment);
        return payment;
    }

    @GetMapping("/history/id/{paymentId}")
    public ResponseEntity<Payment> getById(@PathVariable String paymentId) {
        return Optional.ofNullable(paymentService.findById(paymentId))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history/name/{paymentName}")
    public ResponseEntity<Payment> getByName(@PathVariable String paymentName) {
        return Optional.ofNullable(paymentService.findByName(paymentName))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/history/bankNumber/{bankNumber}")
    public ResponseEntity<Payment> getByBankNumber(@PathVariable String bankNumber) {
        return Optional.ofNullable(paymentService.findByBankNumber(bankNumber))
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{paymentId}")
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

    @DeleteMapping("/delete/{paymentId}")
    public ResponseEntity<Void> deletePayment(@PathVariable String paymentId) {
        paymentService.deletePayment(paymentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/history")
    public List<Payment> getAllPayments() {
        return paymentService.findAllPayment();
    }
}
