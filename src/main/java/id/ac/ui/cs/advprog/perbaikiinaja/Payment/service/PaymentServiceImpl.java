package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {
        List<Payment> existingPayments = paymentRepository.findAll();

        // Check if payment with the same name exists, if yes, cancel creation
        boolean exists = existingPayments.stream().anyMatch(payment ->
            payment.getPaymentName().equals(request.getPaymentName())
        );
        if (exists) {
            throw new IllegalStateException("The payment already exists. Creation cancelled.");
        }

        Payment payment = new Payment();
        payment.setPaymentId(java.util.UUID.randomUUID().toString());
        payment.setPaymentName(request.getPaymentName());
        payment.setPaymentBankNumber(String.valueOf(request.getPaymentBankNumber()));

        Payment saved = paymentRepository.save(payment);
        return mapToResponse(saved);
    }

    @Override
    public PaymentResponse updatePaymentName(String id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment name cannot be empty.");
        }
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            throw new IllegalArgumentException("Payment not found.");
        }
        Payment payment = paymentOpt.get();
        payment.setPaymentName(newName);
        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    @Override
    public PaymentResponse updatePaymentBankNumber(String id, String newBankNumber) {
        if (!newBankNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Bank number must contain only digits.");
        }
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) {
            throw new IllegalArgumentException("Payment not found.");
        }
        Payment payment = paymentOpt.get();
        payment.setPaymentBankNumber(newBankNumber);
        Payment updated = paymentRepository.save(payment);
        return mapToResponse(updated);
    }

    @Override
    public PaymentResponse findById(String id) {
        Optional<Payment> paymentOpt = paymentRepository.findById(id);
        if (!paymentOpt.isPresent()) return null;
        return mapToResponse(paymentOpt.get());
    }

    @Override
    public List<PaymentResponse> findAllPayment() {
        return paymentRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public PaymentResponse updatePayment(String paymentId, PaymentRequest request) {
        Optional<Payment> existingOpt = paymentRepository.findById(paymentId);
        if (!existingOpt.isPresent()) {
            throw new IllegalArgumentException("Payment not found.");
        }
        Payment existing = existingOpt.get();
        existing.setPaymentName(request.getPaymentName());
        existing.setPaymentBankNumber(String.valueOf(request.getPaymentBankNumber()));
        Payment updated = paymentRepository.save(existing);
        return mapToResponse(updated);
    }

    @Override
    public void deletePayment(String paymentId) {
        paymentRepository.deleteById(paymentId);
    }

    private PaymentResponse mapToResponse(Payment payment) {
        return new PaymentResponse(
                payment.getPaymentId(),
                payment.getPaymentName(),
                payment.getPaymentBankNumber()
        );
    }
}