package com.es.phoneshop.service.impl;

import com.es.phoneshop.dao.OrderDao;
import com.es.phoneshop.dao.impl.ArrayListOrderDao;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.OrderService;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class DefaultOrderServiceTest {

    private OrderService orderService;
    private OrderDao orderDao;

    @Before
    public void setup() {
        orderService = DefaultOrderService.getInstance();
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Test
    public void testGetOrder() {
        Cart cart = new Cart();
        List<CartItem> cartItems = new ArrayList<>();
        cartItems.add(new CartItem( new Product("iphone14max", "Apple iPhone 14 Max 64gb", new BigDecimal(1568), Currency.getInstance("USD"), 100, "urlForImage", null),  2));
        cartItems.add(new CartItem( new Product("iphone14max", "Apple iPhone 14 Max 64gb", new BigDecimal(108), Currency.getInstance("USD"), 100, "urlForImage", null),  2));
        cart.setItems(cartItems);
        cart.setTotalQuantity(4);
        cart.setTotalCost(new BigDecimal(3352));

        Order order = orderService.getOrder(cart);

        assertEquals(new BigDecimal(3352), order.getSubtotal());
        assertEquals(new BigDecimal(6), order.getDeliveryCost());
        assertEquals(new BigDecimal(3358), order.getTotalCost());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(UUID.randomUUID());
        order.setTotalCost(new BigDecimal(100));
        orderDao.save(order);

        Order retrievedOrder = orderService.getOrder(order.getId());

        assertEquals(order, retrievedOrder);
    }

    @Test
    public void testGetPaymentMethods() {
        List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

        assertEquals(PaymentMethod.values().length, paymentMethods.size());
    }
}
