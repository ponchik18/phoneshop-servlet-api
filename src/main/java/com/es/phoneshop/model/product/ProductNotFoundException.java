package com.es.phoneshop.model.product;

public class ProductNotFoundException extends RuntimeException{
    @Override
    public String toString() {
        return "Product doesn't exist!";
    }
}
