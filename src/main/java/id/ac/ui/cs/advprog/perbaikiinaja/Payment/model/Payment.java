package id.ac.ui.cs.advprog.perbaikiinaja.Payment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter @Setter
public class Payment {

    private String paymentId;
    private String paymentName;
    private String paymentBankNumber;

    public Payment() {
        this.paymentId = UUID.randomUUID().toString();
    }

    public Payment(String paymentName, String paymentBankNumber) {
        this.paymentId = UUID.randomUUID().toString();
        setPaymentName(paymentName);
        setPaymentBankNumber(paymentBankNumber);
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }
    
    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentName(String paymentName) {
        if (paymentName == null || paymentName.isBlank()) {
            throw new IllegalArgumentException("Payment name cannot be null or empty.");
        }
        this.paymentName = paymentName;
    }

    public String getPaymentName() {
        return paymentName;
    }

    public void setPaymentBankNumber(String paymentBankNumber) {
        if (paymentBankNumber == null || paymentBankNumber.isBlank()) {
            throw new IllegalArgumentException("Bank number cannot be null or empty.");
        }
        this.paymentBankNumber = paymentBankNumber;
    }

    public String getPaymentBankNumber() {
        return paymentBankNumber;
    }


}
