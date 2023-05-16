package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.product.Product;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProductDetailsPagePageServletTest {
    private static final Long invalidProductId = -45L;
    private static final Long productId = 1L;
    private final ProductDetailsPageServlet servlet = new ProductDetailsPageServlet();
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

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setProductDao(productDao);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
    }

    @Test
    public void testDoGetValidProductNumber() throws ServletException, IOException {
        Product product = new Product();
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(productDao.getProduct(productId)).thenReturn(product);

        servlet.doGet(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("product"), eq(product));
    }

    @Test(expected = ProductNotFoundException.class)
    public void testDoGetInvalidProductNumber() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/" + invalidProductId);
        when(productDao.getProduct(invalidProductId)).thenThrow(new ProductNotFoundException(invalidProductId));

        servlet.doGet(request, response);
    }
}