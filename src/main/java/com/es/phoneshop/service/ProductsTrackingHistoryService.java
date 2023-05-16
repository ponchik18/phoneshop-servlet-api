package com.es.phoneshop.service;

import com.es.phoneshop.model.history.ProductHistoryList;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpSession;

public interface ProductsTrackingHistoryService {
    ProductHistoryList getProductHistoryList(HttpSession session);

    void addToViewed(ProductHistoryList productHistoryList, Product product, String sessionId);
}
