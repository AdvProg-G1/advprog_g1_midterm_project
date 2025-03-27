// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/model/ServiceOrder.java
package id.ac.ui.cs.advprog.perbaikiinaja.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceOrder {
    private Long id;
    private User user;
    private String itemName;
    private String itemCondition;
    private String repairDescription;
    private LocalDate serviceDate;
    private String paymentMethod;
    private String couponCode;       // Optional promo coupon field
    private Technician assignedTechnician; // Selected randomly by default (or manually if implemented)
    private String status;           // E.g., "Pending", "Accepted", "In Progress", "Completed"
    private String technicianEstimate; // Estimated time and price given by the technician
}