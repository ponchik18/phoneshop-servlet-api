package com.es.phoneshop.service.iml;

import com.es.phoneshop.model.history.ProductHistoryList;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductsTrackingHistoryService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

public class DefaultProductsTrackingHistoryService implements ProductsTrackingHistoryService {

    public static final String PRODUCT_HISTORY_LIST_SESSION = ProductHistoryList.class.getName() + ".class";
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
    public ProductHistoryList getProductHistoryList(HttpSession session) {
        synchronized (session.getId().intern()) {
            ProductHistoryList productHistoryList = (ProductHistoryList) session.getAttribute(PRODUCT_HISTORY_LIST_SESSION);
            if (productHistoryList == null) {
                session.setAttribute(PRODUCT_HISTORY_LIST_SESSION, productHistoryList = new ProductHistoryList());
            }
            return productHistoryList;
        }
    }

    @Override
    public void addToViewed(ProductHistoryList productHistoryList, Product product, String sessionId) {
        synchronized (sessionId.intern()) {
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
}
