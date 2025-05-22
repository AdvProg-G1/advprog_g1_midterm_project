package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ServiceOrderWithTechnicianName {

    private final String id;
    private final String itemName;
    private final String condition;
    private final String problemDescription;
    private final String technicianId;
    private final String technicianName;
    private final String userId;
    private final String paymentMethod;
    private final boolean couponApplied;
    private final String status;
    private final String estimatedCompletionTime;
    private final Integer estimatedPrice;
    private final String serviceDate;

    public ServiceOrderWithTechnicianName(ServiceOrder order, String technicianName) {
        this.id = order.getId().toString();
        this.itemName = order.getItemName();
        this.condition = order.getCondition();
        this.problemDescription = order.getProblemDescription();
        this.technicianId = order.getTechnicianId();
        this.technicianName = technicianName;
        this.userId = order.getUserId();
        this.paymentMethod = order.getPaymentMethod();
        this.couponApplied = order.isCouponApplied();
        this.status = order.getStatus();
        this.estimatedCompletionTime = order.getEstimatedCompletionTime();
        this.estimatedPrice = order.getEstimatedPrice();
        this.serviceDate = order.getServiceDate().toString();
    }
}
