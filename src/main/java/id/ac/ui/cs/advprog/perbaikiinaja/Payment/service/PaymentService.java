package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;

import java.util.List;

public interface PaymentService {

    void createPayment(Payment payment);

    void updatePaymentName(String paymentId, String newName);

    void updatePaymentBankNumber(String paymentId, String newBankNumber);

    Payment findById(String paymentId);

    Payment findByName(String paymentName);

    Payment findByBankNumber(String accountNumber);

    List<Payment> findAllPayment();
}