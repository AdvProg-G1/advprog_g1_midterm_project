package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateServiceOrderRequest {
    @NotBlank(message = "Item name must not be blank")
    private String itemName;

    @NotBlank(message = "Condition must not be blank")
    private String condition;

    @NotBlank(message = "Problem description must not be blank")
    private String problemDescription;

    @FutureOrPresent(message = "Service date cannot be in the past")
    private LocalDate serviceDate;

    private String technicianId;

    @NotBlank(message = "Payment method must not be blank")
    private String paymentMethod;

    private boolean couponApplied;

    // New fields added for update
    private String estimatedCompletionTime;  // e.g. "2 days"

    private Integer estimatedPrice;          // e.g. 120000

    private String status;                   // e.g. "WAITING_CONFIRMATION"
}
