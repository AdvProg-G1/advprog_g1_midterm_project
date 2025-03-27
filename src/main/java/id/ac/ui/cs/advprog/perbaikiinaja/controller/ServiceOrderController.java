// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/controller/ServiceOrderController.java
package id.ac.ui.cs.advprog.perbaikiinaja.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.service.ServiceOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/serviceOrder")
public class ServiceOrderController {

    private final ServiceOrderService serviceOrderService;

    @Autowired
    public ServiceOrderController(ServiceOrderService serviceOrderService) {
        this.serviceOrderService = serviceOrderService;
    }

    // Display the list of service orders
    @GetMapping("/list")
    public String listServiceOrders(Model model) {
        model.addAttribute("orders", serviceOrderService.getAllServiceOrders());
        return "serviceOrderList";
    }

    // Show form to create a new service order
    @GetMapping("/create")
    public String createServiceOrderForm(Model model) {
        model.addAttribute("order", new ServiceOrder());
        return "createServiceOrder";
    }

    // Process the creation form
    @PostMapping("/create")
    public String createServiceOrder(@ModelAttribute("order") ServiceOrder order) {
        serviceOrderService.createServiceOrder(order);
        return "redirect:/serviceOrder/list";
    }

    // Additional endpoints for updating and deleting orders can be added here.
}