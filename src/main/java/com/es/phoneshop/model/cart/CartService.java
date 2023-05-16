package com.es.phoneshop.model.cart;

import com.es.phoneshop.exception.OutOfStockException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

public interface CartService {
    Cart getCart(HttpServletRequest request);

    void add(Cart cart,Long productId, int quantity) throws OutOfStockException;

}
