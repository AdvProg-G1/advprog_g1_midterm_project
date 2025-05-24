package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.controller;

import id.ac.ui.cs.advprog.perbaikiinaja.Auth.AuthStrategy;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.dto.UserResponse;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.model.User;
import id.ac.ui.cs.advprog.perbaikiinaja.Auth.repository.UserRepository;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.ServiceOrderWithTechnicianName;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service.ServiceOrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class ServiceOrderController {

    private final ServiceOrderService service;
    private final UserRepository     userRepository;
    private final AuthStrategy       auth;

    @PostMapping
    public ResponseEntity<ServiceOrder> createOrder(
            @Valid @RequestBody CreateServiceOrderRequest req
    ) {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User)) {
            return ResponseEntity.status(401).build();
        }
        String userId = ((User) principal).getId();

        ServiceOrder order = ServiceOrder.builder()
                .itemName(req.getItemName())
                .condition(req.getCondition())
                .problemDescription(req.getProblemDescription())
                .technicianId(req.getTechnicianId())
                .userId(userId)
                .serviceDate(req.getServiceDate())
                .paymentMethod(req.getPaymentMethod())
                .couponApplied(req.isCouponApplied())
                .status("WAITING CONFIRMATION")
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
        return (o == null)
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
        return service.delete(id)
                ? ResponseEntity.noContent().build()
                : ResponseEntity.notFound().build();
    }

    /**
     * Raw User entities for technicians.
     */
    @GetMapping("/technicians/raw")
    public ResponseEntity<List<User>> getAllTechniciansRaw() {
        List<User> techs = userRepository.findAll().stream()
                .filter(u -> "TECHNICIAN".equals(u.getRole()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(techs);
    }

    /**
     * DTO-safe version of technician list.
     */
    @GetMapping("/technicians")
    public List<UserResponse> listTechnicians() {
        return auth.getAllTechnicians();
    }

    /**
     * Order history for the current user, with technician names.
     */
    @GetMapping("/user/history")
    public ResponseEntity<List<ServiceOrderWithTechnicianName>> getOrderHistoryForUser() {
        Object principal = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

        if (!(principal instanceof User)) {
            return ResponseEntity.status(401).build();
        }
        String userId = ((User) principal).getId();

        List<ServiceOrderWithTechnicianName> history = service
                .findOrdersByUserId(userId)
                .stream()
                .map(order -> {
                    String techName = userRepository
                            .findById(order.getTechnicianId())
                            .map(User::getFullName)
                            .orElse("");
                    return new ServiceOrderWithTechnicianName(order, techName);
                })
                .collect(Collectors.toList());

        return ResponseEntity.ok(history);
    }
}
