package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    Product getProduct(Long id) throws ProductNotFoundException;

    List<Product> findProducts(String search, SortField field, SortOrder order);

    void save(Product product);

    void delete(Long id);
}
