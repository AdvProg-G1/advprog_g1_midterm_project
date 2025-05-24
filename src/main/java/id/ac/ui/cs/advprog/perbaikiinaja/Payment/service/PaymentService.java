package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    void updatePaymentName(String paymentId, String newName);

    void updatePaymentBankNumber(String paymentId, String newBankNumber);

    PaymentResponse findById(String paymentId);

    List<PaymentResponse> findAllPayment();

    Payment updatePayment(String paymentId, PaymentRequest request);

    void deletePayment(String paymentId);
}