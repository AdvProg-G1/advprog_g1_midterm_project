package id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto;

import lombok.Builder;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class PaymentResponse {
    private String paymentId;
    private String paymentName;
    private String paymentBankNumber;
}