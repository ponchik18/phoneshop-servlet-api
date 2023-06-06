package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    private final CheckoutPageServlet servlet = new CheckoutPageServlet();
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    @Mock
    private CartService cartService;

    @Mock
    private OrderService orderService;

    @Before
    public void setup() throws ServletException, NoSuchFieldException, IllegalAccessException {
        servlet.init(config);

        Field cartPageServletFieldCartService = servlet.getClass().getDeclaredField("cartService");
        cartPageServletFieldCartService.setAccessible(true);
        cartPageServletFieldCartService.set(servlet, cartService);

        Field cartPageServletFieldOrderService = servlet.getClass().getDeclaredField("orderService");
        cartPageServletFieldOrderService.setAccessible(true);
        cartPageServletFieldOrderService.set(servlet, orderService);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(cartService.getCart(request.getSession())).thenReturn(new Cart());
        Cart cart = cartService.getCart(request.getSession());
        when(orderService.getOrder(cart)).thenReturn(new Order());
        when(orderService.getPaymentMethods()).thenReturn(new ArrayList<>());
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        Order order = orderService.getOrder(cartService.getCart(request.getSession()));
        List<PaymentMethod> methods = orderService.getPaymentMethods();

        servlet.doGet(request, response);

        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.ORDER), eq(order));
        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.PAYMENT_METHODS), eq(methods));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoPostSuccessfulCreateOrder() throws ServletException, IOException, OutOfStockException {
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");
        when(request.getParameter("deliveryAddress")).thenReturn("test");
        when(request.getParameter("phone")).thenReturn("(333) 333-3333");
        when(request.getParameter("paymentMethod")).thenReturn(PaymentMethod.CASH.toString());
        when(request.getParameter("deliveryDate")).thenReturn(LocalDate.now().toString());

        servlet.doPost(request, response);

        verify(response).sendRedirect(any());
        verify(request, never()).setAttribute(eq(ServletConstant.RequestParameterName.ERRORS), any());
    }

    @Test
    public void testDoPostUnsuccessfulCreateOrderInvalidFormat()
            throws ServletException, IOException, OutOfStockException {
        when(request.getParameter("firstname")).thenReturn("test");
        when(request.getParameter("lastname")).thenReturn("test");
        when(request.getParameter("deliveryAddress")).thenReturn("test");
        when(request.getParameter("phone")).thenReturn("456789");
        when(request.getParameter("paymentMethod")).thenReturn(PaymentMethod.CASH.toString());
        when(request.getParameter("deliveryDate")).thenReturn(LocalDate.now().toString());

        servlet.doPost(request, response);

        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq(ServletConstant.RequestParameterName.ERRORS), any());
    }

}