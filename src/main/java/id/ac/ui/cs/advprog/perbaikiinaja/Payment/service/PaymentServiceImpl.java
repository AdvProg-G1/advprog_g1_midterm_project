package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
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
        if (paymentRepository.existsByPaymentNameIgnoreCase(request.getPaymentName().trim())) {
            throw new IllegalStateException("Payment method with this name already exists.");
        }

        Payment payment = new Payment();
        payment.setPaymentId(UUID.randomUUID().toString());
        payment.setPaymentName(request.getPaymentName().trim());
        payment.setPaymentBankNumber(request.getPaymentBankNumber().trim());

        Payment saved = paymentRepository.save(payment);

        return mapToResponse(saved);
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

        // format input name for validation
        String newName = request.getPaymentName().trim();

        boolean nameExists = paymentRepository.existsByPaymentNameIgnoreCaseAndPaymentIdNot(newName, paymentId);
        if (nameExists) {
            throw new IllegalStateException("Payment method with this name already exists.");
        }

        Payment existing = existingOpt.get();
        existing.setPaymentName(newName);
        existing.setPaymentBankNumber(request.getPaymentBankNumber().trim());
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