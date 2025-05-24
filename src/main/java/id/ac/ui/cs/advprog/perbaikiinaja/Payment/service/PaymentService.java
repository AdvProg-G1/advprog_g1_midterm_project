package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto.PaymentResponse;

import java.util.List;

public interface PaymentService {

    PaymentResponse createPayment(PaymentRequest request);

    PaymentResponse updatePaymentName(String paymentId, String newName);

    PaymentResponse updatePaymentBankNumber(String paymentId, String newBankNumber);

    PaymentResponse findById(String paymentId);

    List<PaymentResponse> findAllPayment();

    PaymentResponse updatePayment(String paymentId, PaymentRequest request);

    void deletePayment(String paymentId);
}