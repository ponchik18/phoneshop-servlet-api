package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;

import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.ProductsTrackingHistoryService;
import jakarta.servlet.RequestDispatcher;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
    private final ProductListPageServlet servlet = new ProductListPageServlet();
    private final ArrayList<Product> products = new ArrayList<>();
    private final ProductsHistory productHistory = new ProductsHistory();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;
    @Mock
    private ArrayListProductDao productDao;
    @Mock
    private ProductsTrackingHistoryService productsTrackingHistoryService;

    @Before
    public void setup() throws ServletException {

        servlet.init(config);
        servlet.setProductDao(productDao);
        servlet.setProductsTrackingHistoryService(productsTrackingHistoryService);

        products.add(new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage", null));
        products.add(new Product("iphone14", "IPhone 14", new BigDecimal(256), Currency.getInstance("USD"), 100, "urlForImage", null));

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(productDao.findProducts(request.getParameter("search"), null, null)).thenReturn(products);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(productsTrackingHistoryService.getProductHistory(request.getSession())).thenReturn(productHistory);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("productHistory"), eq(productHistory.getProducts()));
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), eq(products));
    }
}