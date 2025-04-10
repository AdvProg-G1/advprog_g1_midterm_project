package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;
import java.util.List;
import java.util.UUID;

public interface ServiceOrderService {
    ServiceOrder createOrder(ServiceOrder order);
    List<ServiceOrder> getAllOrders();
    ServiceOrder getOrderById(UUID id);
}