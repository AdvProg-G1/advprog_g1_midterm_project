package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import org.springframework.http.HttpStatus;
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
    public ResponseEntity<ServiceOrder> confirmOrder(@PathVariable("id") Long orderId,
                                                    @RequestParam("duration") int estimatedDuration,
                                                    @RequestParam("cost") double estimatedCost) {
        ServiceOrder confirmed = repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);
        return ResponseEntity.ok(confirmed);
    }

    @PostMapping("/reject/{id}")
    public ResponseEntity<ServiceOrder> rejectOrder(@PathVariable("id") Long orderId) {
        ServiceOrder rejected = repairOrderService.rejectRepairOrder(orderId);
        return ResponseEntity.ok(rejected);
    }
}
