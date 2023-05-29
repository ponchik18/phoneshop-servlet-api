package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DeleteCartItemServletTest {
    private static final Long productId = 1L;
    private final DeleteCartItemServlet servlet = new DeleteCartItemServlet();
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
    public void testDoPostDeleteCartItem() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/" + productId);
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());

        servlet.doPost(request, response);

        verify(response).sendRedirect(request.getContextPath() + "/cart?message=Cart item removed successfully");
    }


}