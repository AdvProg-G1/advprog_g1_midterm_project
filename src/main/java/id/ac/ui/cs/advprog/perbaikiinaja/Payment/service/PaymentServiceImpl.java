package id.ac.ui.cs.advprog.perbaikiinaja.Payment.service;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository.PaymentRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Payment createPayment(Payment payment) {return null;}

    @Override
    public Payment updatePaymentName(String paymentId, String newName) {return null;}

    @Override
    public Payment updatePaymentBankNumber(String paymentId, String newBankNumber) {return null;}

    @Override
    public Payment findById(String paymentId) {return null;}

    @Override
    public Payment findByName(String paymentName) {return null;}

    @Override
    public Payment findByBankNumber(String accountNumber) {return null;}

}