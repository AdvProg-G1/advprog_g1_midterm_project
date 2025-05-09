package id.ac.ui.cs.advprog.perbaikiinaja.Payment.repository;

import id.ac.ui.cs.advprog.perbaikiinaja.Payment.model.Payment;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class PaymentRepository {

    private final List<Payment> paymentMethods = new ArrayList<>();

    public Payment save(Payment payment) {

        // update
        for (int i = 0; i < paymentMethods.size(); i++) {
            if (paymentMethods.get(i).getPaymentId().equals(payment.getPaymentId())) {
                paymentMethods.set(i, payment);
                return payment;
            }
        }

        paymentMethods.add(payment);
        return payment;
    }

    public Payment findById(String paymentId) {
        for (Payment payment : paymentMethods) {
            if (payment.getPaymentId().equals(paymentId)) {
                return payment;
            }
        }
        return null;
    }

    public Payment findByName(String name) {
        for (Payment payment : paymentMethods) {
            if (payment.getPaymentName() != null &&
                    payment.getPaymentName().equalsIgnoreCase(name)) {
                return payment;
            }
        }
        return null;
    }

    public Payment findByBankNumber(String accountNumber) {
        for (Payment payment : paymentMethods) {
            if (payment.getPaymentBankNumber().equals(accountNumber)) {
                return payment;
            }
        }
        return null;
    }

    public List<Payment> findAll() {
        return new ArrayList<>(paymentMethods);
    }
}