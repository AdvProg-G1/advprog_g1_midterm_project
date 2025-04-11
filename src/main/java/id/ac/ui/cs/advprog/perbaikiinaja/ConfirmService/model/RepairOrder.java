package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairOrder {
    private Long id;
    private Long userId;
    private Long technicianId;
    private String itemDescription;
    private String status; // e.g., "PENDING", "ACCEPTED", "REJECTED"
    private int estimatedDuration;
    private double estimatedCost;
    private Date confirmationDate;
}
