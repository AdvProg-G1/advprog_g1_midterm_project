package id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ConfirmService.service.RepairOrderService;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/repair")
public class RepairOrderController {

    private final RepairOrderService repairOrderService;
    private final ServiceOrderService serviceOrderService;

    public RepairOrderController(RepairOrderService repairOrderService, ServiceOrderService serviceOrderService) {
        this.repairOrderService = repairOrderService;
        this.serviceOrderService = serviceOrderService;
    }

    @PostMapping("/confirm/{id}")
    @ResponseStatus(HttpStatus.OK)
    public ServiceOrder confirmOrder(@PathVariable("id") String orderId,
                                                    @RequestParam("duration") int estimatedDuration,
                                                    @RequestParam("cost") int estimatedCost) {
        ServiceOrder confirmed = repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);
        return repairOrderService.confirmRepairOrder(orderId, estimatedDuration, estimatedCost);
    }

    @DeleteMapping("/reject/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void rejectOrder(@PathVariable("id") String orderId) {
        repairOrderService.rejectRepairOrder(orderId);
    }

    @GetMapping("/list")
    public String repairPage(Model model) {
        List<ServiceOrder> allOrders = serviceOrderService.findAll();
        model.addAttribute("orders", allOrders);
        return "technician/incoming_orders";
    }
}



