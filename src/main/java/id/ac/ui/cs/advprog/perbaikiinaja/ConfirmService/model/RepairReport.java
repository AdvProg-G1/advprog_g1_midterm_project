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

public class RepairReport {
    private Long id;
    private Long orderId;
    private Long technicianId;
    private String details;
    private Date createdAt;
}
