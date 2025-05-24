package id.ac.ui.cs.advprog.perbaikiinaja.Payment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PaymentRequest {
    @NotNull(message = "Payment name is required.")
    private String paymentName;

    @NotNull(message = "Bank Number is required")
    private String paymentBankNumber;
}
