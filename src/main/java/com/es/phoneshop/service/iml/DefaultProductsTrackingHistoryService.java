package com.es.phoneshop.service.iml;

import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductsTrackingHistoryService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class DefaultProductsTrackingHistoryService implements ProductsTrackingHistoryService {

    public static final String PRODUCT_HISTORY_LIST_SESSION = ProductsHistory.class.getName() + ".class";
    public static final int MAX_SIZE = 3;

    private volatile static DefaultProductsTrackingHistoryService instance;

    private DefaultProductsTrackingHistoryService() {
    }

    public static DefaultProductsTrackingHistoryService getInstance() {
        if (instance == null) {
            synchronized (DefaultProductsTrackingHistoryService.class) {
                if (instance == null) {
                    instance = new DefaultProductsTrackingHistoryService();
                }
            }
        }
        return instance;
    }

    @Override
    public ProductsHistory getProductHistory(HttpSession session) {
        synchronized (session.getId().intern()) {
            ProductsHistory productHistory = (ProductsHistory) session.getAttribute(PRODUCT_HISTORY_LIST_SESSION);
            if (productHistory == null) {
                session.setAttribute(PRODUCT_HISTORY_LIST_SESSION, productHistory = new ProductsHistory());
            }
            return productHistory;
        }
    }

    @Override
    public void addToViewed(ProductsHistory productHistory, Product product, String sessionId) {
        synchronized (productHistory) {
            List<Product> viewedProducts = productHistory.getProducts();
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
}
