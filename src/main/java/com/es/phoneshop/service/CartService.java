package com.es.phoneshop.service;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import jakarta.servlet.http.HttpSession;

import java.util.UUID;

public interface CartService {
    Cart getCart(HttpSession session);

    void destroyCart(HttpSession session);

    void add(Cart cart, UUID productId, int quantity) throws OutOfStockException;

    void update(Cart cart, UUID productId, int quantity) throws OutOfStockException;

    void delete(Cart cart, UUID productId);
}
