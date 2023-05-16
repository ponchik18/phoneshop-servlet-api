package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpSession session);

    void add(Cart cart, Long productId, int quantity, String sessionId) throws OutOfStockException;

}
