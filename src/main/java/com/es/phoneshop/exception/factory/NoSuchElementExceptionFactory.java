package com.es.phoneshop.exception.factory;

import com.es.phoneshop.exception.NoSuchElementException;
import com.es.phoneshop.exception.NoSuchOrderException;
import com.es.phoneshop.exception.NoSuchProductException;
import com.es.phoneshop.model.UniqueItem;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.product.Product;

import java.util.UUID;

public class NoSuchElementExceptionFactory {
    public NoSuchElementException getException(Class<? extends UniqueItem> clazz, UUID id) {
        if(clazz == Product.class){
            return new NoSuchProductException(id);
        }
        else if(clazz == Order.class){
            return new NoSuchOrderException(id);
        }
        else {
            return new NoSuchElementException(id);
        }
    }
}
