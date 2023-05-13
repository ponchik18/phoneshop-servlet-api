package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductHistoryList {
    private static final int MAX_SIZE = 3;
    private final List<Product> products;

    public ProductHistoryList() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

}
