package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
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
        List<Payment> payments = paymentRepository.findAll();
        List<PaymentResponse> responses = new ArrayList<>(payments.size());

        for (Payment payment : payments) {
            responses.add(mapToResponse(payment));
        }

        return responses;
    }

    @Override
    public PaymentResponse updatePayment(String paymentId, PaymentRequest request) {
        Payment existing = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("Payment not found."));

        String newName = request.getPaymentName().trim();
        String newBankNumber = request.getPaymentBankNumber().trim();

        if (!existing.getPaymentName().equalsIgnoreCase(newName)) {
            boolean nameExists = paymentRepository.existsByPaymentNameIgnoreCaseAndPaymentIdNot(newName, paymentId);
            if (nameExists) {
                throw new IllegalStateException("Payment method with this name already exists.");
            }
        }

        boolean changed = false;

        if (!existing.getPaymentName().equals(newName)) {
            existing.setPaymentName(newName);
            changed = true;
        }

        if (!existing.getPaymentBankNumber().equals(newBankNumber)) {
            existing.setPaymentBankNumber(newBankNumber);
            changed = true;
        }

        if (changed) {
            existing = paymentRepository.save(existing);
        }

        return mapToResponse(existing);
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