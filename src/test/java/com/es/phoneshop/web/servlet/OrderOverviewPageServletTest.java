package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
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
import java.util.UUID;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewPageServletTest {
    private final OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();
    private final UUID orderId = UUID.randomUUID();
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
    private OrderService orderService;

    @Before
    public void setup() throws ServletException, NoSuchFieldException, IllegalAccessException {
        servlet.init(config);

        Field cartPageServletFieldOrderService = servlet.getClass().getDeclaredField("orderService");
        cartPageServletFieldOrderService.setAccessible(true);
        cartPageServletFieldOrderService.set(servlet, orderService);

        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(orderService.getOrder(orderId)).thenReturn(new Order());
        when(request.getPathInfo()).thenReturn("/"+orderId);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        Order order = orderService.getOrder(orderId);

        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), eq(order));
        verify(requestDispatcher).forward(request, response);
    }

}