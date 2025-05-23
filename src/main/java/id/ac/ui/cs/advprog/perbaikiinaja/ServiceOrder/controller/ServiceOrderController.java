package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.ServiceOrderWithTechnicianName;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.UUID;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;
    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<ServiceOrder> createOrder(
            @Valid @RequestBody CreateServiceOrderRequest req,
            @AuthenticationPrincipal User user
    ) {
        if (user == null) {
            return ResponseEntity.status(401).build();
        }

        ServiceOrder order = ServiceOrder.builder()
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .technicianId(req.getTechnicianId())
                .userId(user.getId())
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .status("WAITING_CONFIRMATION")
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
                .estimatedCompletionTime(req.getEstimatedCompletionTime())
                .estimatedPrice(req.getEstimatedPrice())
                .status(req.getStatus())
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

    // Fetch all technicians
    @GetMapping("/technicians")
    public ResponseEntity<List<User>> getAllTechnicians() {
        List<User> technicians = userRepository.findAll().stream()
                .filter(u -> "TECHNICIAN".equals(u.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(technicians);
    }

    // Fetch order history for logged-in user with technician names
    @GetMapping("/user/history")
    public ResponseEntity<List<ServiceOrderWithTechnicianName>> getOrderHistoryForUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (!(principal instanceof User)) {
            return ResponseEntity.status(401).build();
        }

        User loggedInUser = (User) principal;
        String userId = loggedInUser.getId();

        List<ServiceOrder> userOrders = service.findOrdersByUserId(userId);

        List<ServiceOrderWithTechnicianName> response = userOrders.stream()
                .map(order -> {
                    String techName = userRepository.findById(order.getTechnicianId())
                            .map(User::getFullName)
                            .orElse("");
                    return new ServiceOrderWithTechnicianName(order, techName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }
}
