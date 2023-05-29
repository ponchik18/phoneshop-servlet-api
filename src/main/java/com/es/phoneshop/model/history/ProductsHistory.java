package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProductsHistory implements Serializable {
    private static final int MAX_SIZE = 3;

    private List<Product> products;

    public ProductsHistory() {
        this.products = new ArrayList<>();
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}
