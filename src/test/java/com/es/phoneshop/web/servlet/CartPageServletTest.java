package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.web.constant.ServletConstant;
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
import java.lang.reflect.Field;
import java.util.Locale;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CartPageServletTest {
    private final CartPageServlet servlet = new CartPageServlet();
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
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.CART), eq(cart));
        verify(requestDispatcher).forward(request, response);
        verify(request, never()).setAttribute(eq(ServletConstant.RequestParameterName.ERRORS), any());
    }

    @Test
    public void testDoPostSuccessfulUpdate() throws ServletException, IOException, OutOfStockException {
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());
        when(request.getParameterValues(ServletConstant.RequestParameterName.PRODUCT_ID)).thenReturn(new String[]{UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString()});
        when(request.getParameterValues(ServletConstant.RequestParameterName.QUANTITY)).thenReturn(new String[]{"1", "1", "1"});

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart" +
                "?message=Quantity has been changed successfully");
    }

    @Test
    public void testDoPostWithError() throws ServletException, IOException {
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());
        when(request.getParameterValues(ServletConstant.RequestParameterName.PRODUCT_ID)).thenReturn(new String[]{UUID.randomUUID().toString(),
                UUID.randomUUID().toString(), UUID.randomUUID().toString()});
        when(request.getParameterValues(ServletConstant.RequestParameterName.QUANTITY)).thenReturn(new String[]{"asd", "xcv", "qwe"});

        servlet.doPost(request, response);

        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.ERRORS), any());
    }
}