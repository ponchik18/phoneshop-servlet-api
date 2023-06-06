package com.es.phoneshop.service;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;

import java.util.List;
import java.util.UUID;

public interface OrderService {
    Order getOrder(Cart cart);

    Order getOrder(UUID id);


    List<PaymentMethod> getPaymentMethods();

    void placeOrder(Order order);


}
