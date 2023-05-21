package com.es.phoneshop.web;

import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.exception.ProductNotFoundException;
import com.es.phoneshop.service.iml.DefaultCartService;
import com.es.phoneshop.model.product.Product;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;

import java.util.Locale;

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

    private HttpSession session;
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
        when(request.getLocale()).thenReturn(Locale.ENGLISH);
        when(request.getSession()).thenReturn(session);
        when(session.getId()).thenReturn("sessionId");
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
        String redirectLink = request.getContextPath() + "/products/" + productId + "?message=Product added to cart successfully";
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(initialQuantity));

        servlet.doPost(request, response);

        verify(cartService).add(cartService.getCart(request.getSession()), productId, initialQuantity);
        verify(response).sendRedirect(redirectLink);
        verify(request, never()).setAttribute(eq("error"), any());
    }

    @Test
    public void testDoPostInvalidQuantity() throws ServletException, IOException, OutOfStockException {
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter("quantity")).thenReturn("qwer");

        servlet.doPost(request, response);

        verify(request).setAttribute("error", "Not a number");
    }

}