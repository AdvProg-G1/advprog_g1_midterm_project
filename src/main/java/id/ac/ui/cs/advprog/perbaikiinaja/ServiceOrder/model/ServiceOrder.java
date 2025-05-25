package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

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
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false)
    private String itemName;

    @Column(nullable = false)
    private String condition;

    @Column(nullable = false)
    private String problemDescription;

    // if you allow auto-assignment, this may be null until a tech picks it up
    private String technicianId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private LocalDate serviceDate;

    @Column(nullable = false)
    private String paymentMethod;

    private boolean couponApplied;

    @Column(nullable = false)
    private String status;

    private Integer estimatedPrice;
    private String estimatedCompletionTime;
}
