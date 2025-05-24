package id.ac.ui.cs.advprog.perbaikiinaja.Payment.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "payment")
@Data
public class Payment {

    @Id
    @Column(name = "id")
    private String paymentId;

    @Column(name = "name")
    private String paymentName;

    @Column(name = "bank_number")
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
