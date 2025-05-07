package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "report")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RepairReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_report_order")
    )
    private ServiceOrder orderId;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "technician_id",
            referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_report_technician")
    )
    private ServiceOrder technicianId;

    private String details;

    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}

