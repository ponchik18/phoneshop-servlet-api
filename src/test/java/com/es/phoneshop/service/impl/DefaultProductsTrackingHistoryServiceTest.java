package com.es.phoneshop.service.impl;

import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DefaultProductsTrackingHistoryServiceTest {
    private static final int MAX_STOCK = 10;
    private static final int MAX_SIZE_HISTORY_LIST = DefaultProductsTrackingHistoryService.MAX_SIZE;
    private final DefaultProductsTrackingHistoryService historyService =
            DefaultProductsTrackingHistoryService.getInstance();
    private final ProductsHistory productHistory = new ProductsHistory();
    private final List<Product> testProducts = new ArrayList<>();

    @Before
    public void setup() {
        for (int i = 0; i < 5; i++) {
            Product product = new Product("test" + i, "Test Product " + i, new BigDecimal(100), Currency.getInstance("USD"), MAX_STOCK, "https://example.com/test.jpg", null);
            product.setId(Integer.toUnsignedLong(i));
            testProducts.add(product);
        }
    }

    @Test
    public void testAddToViewed() {
        List<Product> expectedList = new LinkedList<>();
        for (int i = testProducts.size() - MAX_SIZE_HISTORY_LIST; i < testProducts.size(); i++) {
            expectedList.add(testProducts.get(i));
        }

        testProducts.forEach(testProducts ->
                historyService.addToViewed(productHistory, testProducts, "sessionId"));

        assertEquals(expectedList, productHistory.getProducts());
    }

}

