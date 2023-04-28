package com.es.phoneshop.model.product;

public class ProductNotFoundException extends Exception{
    @Override
    public String toString() {
        return "Product doesn't exist!";
    }
}
