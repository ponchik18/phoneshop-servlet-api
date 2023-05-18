package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class Cart {
    private final List<CartItem> items;

    public Cart() {
        items = new ArrayList<>();
    }

    public List<CartItem> getItems() {
        return items;
    }

    @Override
    public String toString() {
        return "Cart[" +
                 items +
                ']';
    }
}
