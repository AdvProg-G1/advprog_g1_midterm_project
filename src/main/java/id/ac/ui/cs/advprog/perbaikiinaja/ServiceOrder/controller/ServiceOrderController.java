package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;

    @PostMapping
    public ResponseEntity<ServiceOrder> createOrder(
            @Valid @RequestBody CreateServiceOrderRequest req
    ) {
        ServiceOrder order = ServiceOrder.builder()
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .technicianId(req.getTechnicianId())
                .userId("user1")
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .build();

        ServiceOrder created = service.createOrder(order);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(created.getId())
                .toUri();

        return ResponseEntity.created(location).body(created);
    }

    @GetMapping
    public ResponseEntity<List<ServiceOrder>> getAllOrders() {
        return ResponseEntity.ok(service.getAllOrders());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrder> getOrderById(@PathVariable UUID id) {
        ServiceOrder o = service.getOrderById(id);
        return o == null
                ? ResponseEntity.notFound().build()
                : ResponseEntity.ok(o);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceOrder> updateOrder(
            @PathVariable UUID id,
            @Valid @RequestBody CreateServiceOrderRequest req
    ) {
        ServiceOrder updated = ServiceOrder.builder()
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .technicianId(req.getTechnicianId())
                .build();

        return service.update(id, updated)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        if (service.delete(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/technician/{techId}")
    public ResponseEntity<List<ServiceOrder>> getByTechnician(
            @PathVariable("techId") String techId
    ) {
        return ResponseEntity.ok(service.findOrdersByTechnicianId(techId));
    }
}