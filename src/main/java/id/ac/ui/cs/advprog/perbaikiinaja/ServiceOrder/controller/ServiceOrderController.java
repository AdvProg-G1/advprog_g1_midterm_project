package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/service-orders")
public class ServiceOrderController {

    @Autowired
    private ServiceOrderService serviceOrderService;

    @PostMapping("/create")
    public ServiceOrder createServiceOrder(@RequestBody ServiceOrder order) {
        return serviceOrderService.createOrder(order);
    }

    @GetMapping("/")
    public List<ServiceOrder> getAllOrders() {
        return serviceOrderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public ServiceOrder getOrderById(@PathVariable UUID id) {
        return serviceOrderService.getOrderById(id);
    }
}
