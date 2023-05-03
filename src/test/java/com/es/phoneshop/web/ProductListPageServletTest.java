package com.es.phoneshop.web;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import jakarta.servlet.ServletConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProductListPageServletTest {
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

    private ProductListPageServlet servlet = new ProductListPageServlet();

    private ArrayList<Product> products = new ArrayList<>();

    @Before
    public void setup() throws ServletException {

        servlet.init(config);
        servlet.setProductDao(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        products.add(new Product("sfold", "Samsung Galaxy Fold", new BigDecimal(100), Currency.getInstance("USD"), 100, "urlForImage"));
        products.add(new Product("iphone14", "IPhone 14", new BigDecimal(256), Currency.getInstance("USD"), 100, "urlForImage"));
        when(productDao.findProducts()).thenReturn(products);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("products"), eq(products));
    }

}