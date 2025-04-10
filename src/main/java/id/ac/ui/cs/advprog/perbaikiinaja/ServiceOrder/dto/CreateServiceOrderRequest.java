package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto;

import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateServiceOrderRequest {
    private String itemName;
    private String condition;
    private String problemDescription;
    private LocalDate serviceDate;
    private String technicianId; // nullable for auto-assign
    private String paymentMethod;
    private boolean couponApplied;
}