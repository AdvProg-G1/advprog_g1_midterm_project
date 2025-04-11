package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @PostMapping
    public ResponseEntity<ServiceOrder> createOrder(@RequestBody CreateServiceOrderRequest request) {
        ServiceOrder order = ServiceOrder.builder()
                .itemName(request.getItemName())
                .condition(request.getCondition())
                .problemDescription(request.getProblemDescription())
                .technicianId(request.getTechnicianId())
                .userId("user1") // Temp hardcoded
                .serviceDate(request.getServiceDate())
                .paymentMethod(request.getPaymentMethod())
                .couponApplied(request.isCouponApplied())
                .build();

        return ResponseEntity.ok(serviceOrderService.createOrder(order));
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrder>> getAllOrders() {
        return ResponseEntity.ok(serviceOrderService.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrder> getOrderById(@PathVariable UUID id) {
        ServiceOrder order = serviceOrderService.getOrderById(id);
        return order != null ? ResponseEntity.ok(order) : ResponseEntity.notFound().build();
    }
}
