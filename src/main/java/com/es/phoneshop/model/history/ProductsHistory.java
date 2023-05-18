package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductsHistory {
    private static final int MAX_SIZE = 3;
    private final List<Product> products;

    public ProductsHistory() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

}
