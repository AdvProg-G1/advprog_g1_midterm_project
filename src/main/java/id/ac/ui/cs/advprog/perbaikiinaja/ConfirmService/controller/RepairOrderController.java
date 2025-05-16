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

    @DeleteMapping("/reject/{id}")
    public ResponseEntity<Void> rejectOrder(@PathVariable("id") String orderId) {
        repairOrderService.rejectRepairOrder(orderId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/list")
    public ResponseEntity<List<ServiceOrder>> orderList() {
        List<ServiceOrder> waiting = repairOrderService.findByStatus("waiting_confirmation");
        return ResponseEntity.ok(waiting);
    }
}



