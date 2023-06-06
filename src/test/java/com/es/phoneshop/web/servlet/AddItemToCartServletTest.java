package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.service.CartService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class AddItemToCartServletTest {
    private static final UUID productId = UUID.randomUUID();
    private final AddItemToCartServlet servlet = new AddItemToCartServlet();
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
    private CartService cartService;

    @Before
    public void setup() throws ServletException, NoSuchFieldException, IllegalAccessException {
        servlet.init(config);

        Field cartPageServletField = servlet.getClass().getDeclaredField("cartService");
        cartPageServletField.setAccessible(true);
        cartPageServletField.set(servlet, cartService);

        when(request.getSession()).thenReturn(session);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getLocale()).thenReturn(new Locale("ru"));
        when(request.getParameter("sort")).thenReturn(StringUtils.EMPTY);
        when(request.getParameter("order")).thenReturn(StringUtils.EMPTY);
    }


    @Test
    public void testDoPostSuccess() throws ServletException, IOException, OutOfStockException {
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter("quantity")).thenReturn(String.valueOf(1));
        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() +
                "/products" +
                "?message=Product added to cart successfully" +
                any());
        verify(request, never()).setAttribute(eq("error"), any());
    }

    @Test
    public void testDoPostInvalidQuantity() throws ServletException, IOException, OutOfStockException {
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(request.getParameter("quantity")).thenReturn("qwer");

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() +
                "/products" +
                "?productIdWithError=" +
                productId +
                "&error=" +
                any());
    }

}