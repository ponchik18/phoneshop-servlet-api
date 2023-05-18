package com.es.phoneshop.service;

import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpSession;

public interface ProductsTrackingHistoryService {
    ProductsHistory getProductHistory(HttpSession session);

    void addToViewed(ProductsHistory productHistoryList, Product product, String sessionId);
}
