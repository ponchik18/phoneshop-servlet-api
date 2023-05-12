package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.model.cart.DefaultCartService;
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
import static org.mockito.Mockito.*;

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

    @Mock
    private DefaultCartService cartService;

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        servlet.setProductDao(productDao);
        servlet.setCartService(cartService);
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

    @Test
    public void testDoPostSuccess() throws ServletException, IOException, OutOfStockException {
        int initialQuantity = 5;
        String redirectLink = request.getContextPath()+"/products/"+String.valueOf(productId)+"?message=Product added to cart successfully";
        when(request.getPathInfo()).thenReturn("/"+productId);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(initialQuantity));

        servlet.doPost(request,response);

        verify(cartService).add(productId, initialQuantity);
        verify(response).sendRedirect(redirectLink);
        verify(request, never()).setAttribute(eq("error"), any());
    }

    @Test
    public void testDoPostInvalidQuantity() throws ServletException, IOException, OutOfStockException {
        when(request.getPathInfo()).thenReturn("/"+productId);
        when(request.getParameter("quantity")).thenReturn(String.valueOf("qwer"));

        servlet.doPost(request,response);

        verify(request).setAttribute("error", "Not a number");
    }

}