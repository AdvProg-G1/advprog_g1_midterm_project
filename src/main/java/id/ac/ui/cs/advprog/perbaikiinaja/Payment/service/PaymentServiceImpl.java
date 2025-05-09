package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public void createPayment(Payment payment) {
        List<Payment> existingPayments = paymentRepository.findAll();
        for (Payment existing : existingPayments) {
            if (existing.getPaymentId().equals(payment.getPaymentId())) {
                throw new IllegalArgumentException("Payment with ID already exists.");
            }
        }
        paymentRepository.save(payment);
    }

    @Override
    public void updatePaymentName(String id, String newName) {
        if (newName == null || newName.trim().isEmpty()) {
            throw new IllegalArgumentException("Payment name cannot be empty.");
        }
        Payment payment = paymentRepository.findById(id);
        if (payment != null) {
            payment.setPaymentName(newName);
            paymentRepository.save(payment);
        }
    }

    @Override
    public void updatePaymentBankNumber(String id, String newBankNumber) {
        if (!newBankNumber.matches("\\d+")) {
            throw new IllegalArgumentException("Bank number must contain only digits.");
        }
        Payment payment = paymentRepository.findById(id);
        if (payment != null) {
            payment.setPaymentBankNumber(newBankNumber);
            paymentRepository.save(payment);
        }
    }

    @Override
    public Payment findById(String id) {
        return paymentRepository.findById(id);
    }

    @Override
    public Payment findByName(String name) {
        return paymentRepository.findByName(name);
    }

    @Override
    public Payment findByBankNumber(String bankNumber) {
        return paymentRepository.findByBankNumber(bankNumber);
    }

    @Override
    public List<Payment> findAllPayment() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment updatePayment(String paymentId, Payment newPayment) {
        Payment existing = paymentRepository.findById(paymentId);
        if (existing == null) {
            throw new RuntimeException("Payment not found: " + paymentId);
        }

        newPayment.setPaymentId(paymentId);
        paymentRepository.save(newPayment);
        return newPayment;
    }

    @Override
    public void deletePayment(String paymentId) {
        Payment existing = paymentRepository.findById(paymentId);
        if (existing != null) {
            paymentRepository.deletePayment(String.valueOf(existing));
        }
    }
}
