package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model;

import lombok.*;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    private String itemName;
    private String condition;
    private String problemDescription;
    private String technicianId; // null if assigned randomly
    private String userId;

    private LocalDate serviceDate;
    private String paymentMethod;
    private boolean couponApplied;

    private String status; // pending, accepted, rejected, completed
    private Integer estimatedPrice;
    private String estimatedCompletionTime;
}
