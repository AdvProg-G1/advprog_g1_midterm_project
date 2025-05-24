package id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentResponse {
    private String paymentId;
    private String paymentName;
    private String paymentBankNumber;
}