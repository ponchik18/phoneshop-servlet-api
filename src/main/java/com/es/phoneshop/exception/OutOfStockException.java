package com.es.phoneshop.exception;

import com.es.phoneshop.model.product.Product;

public class OutOfStockException extends Exception{
    private final Product product;
    private final int quantity;
    private final int availableStock;

    public OutOfStockException(Product product, int quantity, int availableStock) {
        this.product = product;
        this.quantity = quantity;
        this.availableStock = availableStock;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getAvailableStock() {
        return availableStock;
    }
}
