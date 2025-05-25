package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;


import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/repair")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;

    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @PostMapping("/confirm/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<ServiceOrder> confirmOrder(
            @PathVariable("id") String orderId,
            @RequestParam("duration") int estimatedDuration,
            @RequestParam("cost") int estimatedCost) {
        log.debug("→ POST /api/repair/confirm/{}  duration={}  cost={}",
                orderId, estimatedDuration, estimatedCost);
        ServiceOrder confirmed = repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);

        log.info("Order confirmed id={}  status={}",
                confirmed.getId(), confirmed.getStatus());
        return ResponseEntity.ok(confirmed);
    }

    @PostMapping("/reject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<ServiceOrder> rejectOrder(
            @PathVariable("id") String orderId
    ) {
        log.debug("→ POST /api/repair/reject/{}", orderId);

        ServiceOrder rejected = repairOrderService.rejectRepairOrder(orderId);
        log.info("Order rejected id={}  status={}",
                rejected.getId(), rejected.getStatus());
        return ResponseEntity.ok(rejected);
    }

    @GetMapping("/list")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<List<ServiceOrder>> incomingOrderList() {
        log.debug("→ GET /api/repair/list");

        List<ServiceOrder> waiting = repairOrderService.findByStatus(List.of("WAITING CONFIRMATION"));
        log.info("Fetched {} incoming orders", waiting.size());

        return ResponseEntity.ok(waiting);
    }

    @GetMapping("/history")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN')")
    public ResponseEntity<List<ServiceOrder>> orderHistory() {
        log.debug("→ GET /api/repair/history");

        List<ServiceOrder> allOrders = repairOrderService.findByStatus(List.of("IN PROGRESS", "COMPLETED"));
        log.info("Fetched {} order history", allOrders.size());

        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'TECHNICIAN', 'CUSTOMER')")
    public ResponseEntity<ServiceOrder> getOrder(@PathVariable("id") String id) {
        log.debug("→ GET /api/repair/{}", id);

        ServiceOrder order = repairOrderService.findById(id);
        if (order == null) {
            log.warn("Order not found id={}", id);

            return ResponseEntity.notFound().build();
        }

        log.info("Fetched order id={} status={}", order.getId(), order.getStatus());

        return ResponseEntity.ok(order);
    }

    @PostMapping("/user/reject/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> userRejectOrder(@PathVariable("id") String orderId) {
        log.debug("→ POST /api/repair/user/reject/{}", orderId);

        repairOrderService.userRejectOrder(orderId);
        log.info("User rejected order id={}", orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/accept/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'CUSTOMER')")
    public ResponseEntity<Void> userAcceptOrder(@PathVariable("id") String orderId) {
        log.debug("→ POST /api/repair/user/accept/{}", orderId);

        repairOrderService.userAcceptOrder(orderId);
        log.info("User accepted order id={}", orderId);

        return ResponseEntity.noContent().build();
    }
}



