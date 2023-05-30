package com.es.phoneshop.web.servlet;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.PaymentMethod;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.OrderService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultOrderService;
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

    private final static String PHONE_REGEX = "\\(\\d{3}\\) \\d{3}-\\d{4}";
    private final static String ERROR_VALUE_IS_REQUIRED = "Value is required!";

    private final static String ERROR_INVALID_FORMAT = "Invalid format!";

    private final static String ERROR_INVALID_DATE = "Please, don't select previous date!";
    private CartService cartService;
    private OrderService orderService;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

        request.setAttribute("paymentMethods", paymentMethods);
        request.setAttribute("order", order);
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        Order order = orderService.getOrder(cart);
        Map<String, String> errors = new HashMap<>();

        setRequiredStringParameter(errors, "firstname", request, order::setFirstname);
        setRequiredStringParameter(errors, "lastname", request, order::setLastname);
        setRequiredStringParameter(errors, "deliveryAddress", request, order::setDeliveryAddress);
        setRequiredPhoneParameter(errors, request, order);
        setRequiredDeliveryDateParameter(errors, request, order);
        setRequiredPaymentMethodParameter(errors, request, order);

        handleError(request, response, errors, order);
    }

    private void handleError(HttpServletRequest request, HttpServletResponse response, Map<String,
            String> errors, Order order)
            throws ServletException, IOException {
        if (errors.size() > 0) {
            List<PaymentMethod> paymentMethods = orderService.getPaymentMethods();

            request.setAttribute("paymentMethods", paymentMethods);
            request.setAttribute("errors", errors);
            request.setAttribute("order", order);
            request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
        } else {
            orderService.placeOrder(order);
            cartService.destroyCart(request.getSession());
            response.sendRedirect(request.getContextPath() +
                    "/overview/" + order.getId());
        }
    }

    private void setRequiredStringParameter(Map<String, String> errors, String parameterName,
                                            HttpServletRequest request, Consumer<String> consumer) {
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ERROR_VALUE_IS_REQUIRED);
        } else {
            consumer.accept(parameter.trim());
        }
    }

    private void setRequiredPhoneParameter(Map<String, String> errors, HttpServletRequest request, Order order) {
        String parameterName = "phone";
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ERROR_VALUE_IS_REQUIRED);
        } else if (parameter.trim().matches(PHONE_REGEX)) {
            order.setPhone(parameter);
        } else {
            errors.put(parameterName, ERROR_INVALID_FORMAT);
        }
    }

    private void setRequiredDeliveryDateParameter(Map<String, String> errors, HttpServletRequest request, Order order) {
        String parameterName = "deliveryDate";
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ERROR_VALUE_IS_REQUIRED);
        } else {
            try {
                LocalDate deliveryDate = LocalDate.parse(parameter);
                LocalDate compareDate = LocalDate.now().minusDays(1);
                if (deliveryDate.isBefore(compareDate)) {
                    errors.put(parameterName, ERROR_INVALID_DATE);
                }
                order.setDeliveryDate(deliveryDate);
            } catch (DateTimeParseException exception) {
                errors.put(parameterName, ERROR_INVALID_FORMAT);
            }
        }
    }

    private void setRequiredPaymentMethodParameter(Map<String, String> errors,
                                                   HttpServletRequest request, Order order) {
        String parameterName = "paymentMethod";
        String parameter = request.getParameter(parameterName);
        if (Objects.isNull(parameter) || parameter.trim().isEmpty()) {
            errors.put(parameterName, ERROR_VALUE_IS_REQUIRED);
        } else {
            PaymentMethod paymentMethod = PaymentMethod.valueOf(parameter);
            order.setPaymentMethod(paymentMethod);
        }
    }
}