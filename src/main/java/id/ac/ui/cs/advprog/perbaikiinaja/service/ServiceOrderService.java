// src/main/java/id/ac/ui/cs/advprog/perbaikiinaja/service/ServiceOrderService.java
package id.ac.ui.cs.advprog.perbaikiinaja.service;

import id.ac.ui.cs.advprog.perbaikiinaja.model.ServiceOrder;
import java.util.List;
import java.util.Optional;

public interface ServiceOrderService {
    ServiceOrder createServiceOrder(ServiceOrder order);
    List<ServiceOrder> getAllServiceOrders();
    Optional<ServiceOrder> getServiceOrderById(Long id);
    ServiceOrder updateServiceOrder(ServiceOrder order);
    void deleteServiceOrder(Long id);
}