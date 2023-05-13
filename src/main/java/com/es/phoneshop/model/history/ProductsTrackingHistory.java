package com.es.phoneshop.model.history;

import com.es.phoneshop.model.product.Product;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class ProductsTrackingHistory {

    private static final String PRODUCT_HISTORY_LIST_SESSION = ProductHistoryList.class.getName() + ".class";
    private static final int MAX_SIZE = 3;

    private static ProductsTrackingHistory instance;

    private ProductsTrackingHistory() {
    }

    public static ProductsTrackingHistory getInstance() {
        if (instance == null) {
            synchronized (ProductsTrackingHistory.class) {
                if (instance == null) {
                    instance = new ProductsTrackingHistory();
                }
            }
        }
        return instance;
    }

    public synchronized ProductHistoryList getProductHistoryList(HttpServletRequest request) {
        ProductHistoryList productHistoryList = (ProductHistoryList) request.getSession().getAttribute(PRODUCT_HISTORY_LIST_SESSION);
        if (productHistoryList == null) {
            request.getSession().setAttribute(PRODUCT_HISTORY_LIST_SESSION, productHistoryList = new ProductHistoryList());
        }
        return productHistoryList;
    }

    public synchronized void addToViewed(ProductHistoryList productHistoryList, Product product) {
        List<Product> viewedProducts = productHistoryList.getProducts();
        viewedProducts.stream()
                .filter(viewedProduct -> product.getId().equals(viewedProduct.getId()))
                .findAny()
                .ifPresent(viewedProducts::remove);

        if (viewedProducts.size() == MAX_SIZE) {
            viewedProducts.remove(0);
        }
        viewedProducts.add(product);
    }
}
