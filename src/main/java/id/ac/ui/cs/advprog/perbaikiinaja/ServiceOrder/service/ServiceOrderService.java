package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.service;

import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.dto.CreateServiceOrderRequest;
import id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.model.ServiceOrder;

public interface ServiceOrderService {
    ServiceOrder createOrder(CreateServiceOrderRequest request);
}