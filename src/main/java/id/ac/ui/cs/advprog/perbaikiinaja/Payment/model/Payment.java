package id.ac.ui.cs.advprog.perbaikiinaja.payment.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class Payment {
    String paymentId;
    String paymentName;
    String accountNumber;

    public Payment(String paymentId, String paymentName, String accountNumber) {
    }

    public Payment() {
    }
}