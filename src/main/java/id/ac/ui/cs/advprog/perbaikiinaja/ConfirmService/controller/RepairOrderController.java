package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;

    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<ServiceOrder> confirmOrder(
            @PathVariable("id") String orderId,
            @RequestParam("duration") int estimatedDuration,
            @RequestParam("cost") int estimatedCost) {
        ServiceOrder confirmed = repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);
        return ResponseEntity.ok(confirmed);
    }

    @GetMapping("/list")
    public ResponseEntity<List<ServiceOrder>> incomingOrderList() {
        List<ServiceOrder> waiting = repairOrderService.findByStatus(List.of("WAITING_CONFIRMATION"));
        return ResponseEntity.ok(waiting);
    }

    @GetMapping("/history")
    public ResponseEntity<List<ServiceOrder>> orderHistory() {
        List<ServiceOrder> allOrders = repairOrderService.findByStatus(List.of("IN_PROGRESS", "COMPLETED"));
        return ResponseEntity.ok(allOrders);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceOrder> getOrder(@PathVariable("id") String id) {
        ServiceOrder order = repairOrderService.findById(id);
        if (order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }

    @PostMapping("/user/reject/{id}")
    public ResponseEntity<Void> userRejectOrder(@PathVariable("id") String orderId) {
        repairOrderService.userRejectOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/user/accept/{id}")
    public ResponseEntity<Void> userAcceptOrder(@PathVariable("id") String orderId) {
        repairOrderService.userAcceptOrder(orderId);
        return ResponseEntity.noContent().build();
    }}



