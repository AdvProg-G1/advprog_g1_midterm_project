package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;

public interface PaymentService {

    Payment createPayment(Payment payment);

    Payment updatePaymentName(String paymentId, String newName);

    Payment updatePaymentBankNumber(String paymentId, String newBankNumber);

    Payment findById(String paymentId);

    Payment findByName(String paymentName);

    Payment findByBankNumber(String accountNumber);

}