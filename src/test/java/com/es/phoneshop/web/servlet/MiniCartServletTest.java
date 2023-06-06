package com.es.phoneshop.web.servlet;

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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MiniCartServletTest {
    private final MiniCartServlet servlet = new MiniCartServlet();
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
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.CART), eq(cart));
        verify(requestDispatcher).include(request, response);
    }

}