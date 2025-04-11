package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.model.RepairOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/repair")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;

    public RepairOrderController(RepairOrderService repairOrderService) {
        this.repairOrderService = repairOrderService;
    }

    @PostMapping("/confirm/{id}")
    public ResponseEntity<RepairOrder> confirmOrder(@PathVariable("id") Long orderId,
                                                    @RequestParam("duration") int estimatedDuration,
                                                    @RequestParam("cost") double estimatedCost) {
        RepairOrder confirmed = repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);
        return ResponseEntity.ok(confirmed);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<RepairOrder> rejectOrder(@PathVariable("id") Long orderId) {
        RepairOrder rejected = repairOrderService.rejectRepairOrder(orderId);
        return ResponseEntity.ok(rejected);
    }
}
