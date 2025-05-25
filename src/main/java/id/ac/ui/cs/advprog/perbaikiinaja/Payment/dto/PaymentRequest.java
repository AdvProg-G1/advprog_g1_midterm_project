package id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentRequest{
    private String paymentName;
    private String paymentBankNumber;
}