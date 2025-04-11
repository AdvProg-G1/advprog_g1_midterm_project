package id.ac.ui.cs.advprog.perbaikiinaja.Payment.model;

import lombok.Getter;
import java.util.UUID;

@Getter
public class Payment {
    private String paymentId;
    private String paymentName;
    private String accountNumber;

    public Payment() {
        this.paymentId = UUID.randomUUID().toString();
    }

    public Payment(String paymentName, String accountNumber) {
        this.paymentId = UUID.randomUUID().toString();
        setPaymentName(paymentName);
        setAccountNumber(accountNumber);
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public void setPaymentName(String paymentName) {
        if (paymentName == null || paymentName.isBlank()) {
            throw new IllegalArgumentException("Payment name cannot be null or empty.");
        }
        this.paymentName = paymentName;
    }

    public void setAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.isBlank()) {
            throw new IllegalArgumentException("Account number cannot be null or empty.");
        }
        this.accountNumber = accountNumber;
    }
}
