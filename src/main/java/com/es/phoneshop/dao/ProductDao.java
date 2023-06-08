package com.es.phoneshop.dao;

import com.es.phoneshop.dto.IncludeType;
import com.es.phoneshop.dto.SortField;
import com.es.phoneshop.dto.SortOrder;
import com.es.phoneshop.model.product.Product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao extends DAO<Product> {
    List<Product> findProducts(String search, SortField field, SortOrder order);
    List<Product> advancedSearchProducts(String description, IncludeType includeType,
                                         BigDecimal minPrice, BigDecimal maxPrice);
}
