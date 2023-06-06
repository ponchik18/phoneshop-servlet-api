package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.ValidateParameterService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
import com.es.phoneshop.service.impl.DefaultValidateParameterService;
import com.es.phoneshop.web.constant.ServletConstant;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {

    private CartService cartService;
    private OrderService orderService;

    private ValidateParameterService validateParameterService;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
        validateParameterService = DefaultValidateParameterService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

        request.setAttribute(ServletConstant.RequestParameterName.PAYMENT_METHODS, paymentMethods);
        request.setAttribute(ServletConstant.RequestParameterName.ORDER, order);
        request.getRequestDispatcher(ServletConstant.PagesLocation.CHECKOUT_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        validateParameterService.setRequiredStringParameter(errors, ServletConstant.RequestParameterName.FIRST_NAME,
                request, order::setFirstname);
        validateParameterService.setRequiredStringParameter(errors, ServletConstant.RequestParameterName.LAST_NAME,
                request, order::setLastname);
        validateParameterService.setRequiredStringParameter(errors, ServletConstant.RequestParameterName.DELIVERY_ADDRESS,
                request, order::setDeliveryAddress);
        validateParameterService.setRequiredPhoneParameter(errors, request, order::setPhone);
        validateParameterService.setRequiredDeliveryDateParameter(errors, request, order::setDeliveryDate);
        validateParameterService.setRequiredPaymentMethodParameter(errors, request, order::setPaymentMethod);

        handleError(request, response, errors, order);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Map<String,
            String> errors, Order order)
            throws ServletException, IOException {
        if (errors.size() > 0) {
            List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

            request.setAttribute(ServletConstant.RequestParameterName.PAYMENT_METHODS, paymentMethods);
            request.setAttribute(ServletConstant.RequestParameterName.ERRORS, errors);
            request.setAttribute(ServletConstant.RequestParameterName.ORDER, order);
            request.getRequestDispatcher(ServletConstant.PagesLocation.CHECKOUT_PAGE).forward(request, response);
        } else {
            orderService.placeOrder(order);
            cartService.destroyCart(request.getSession());
            response.sendRedirect(request.getContextPath() +
                    "/overview/" + order.getId());
        }
    }
}