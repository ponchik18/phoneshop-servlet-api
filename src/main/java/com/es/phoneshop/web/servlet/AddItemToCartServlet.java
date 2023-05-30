package com.es.phoneshop.web.servlet;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.QuantityRetrieverService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultQuantityRetrieverService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public class AddItemToCartServlet extends HttpServlet {

    private CartService cartService;

    private QuantityRetrieverService quantityRetrieverService;

    public void setQuantityRetrieverService(QuantityRetrieverService quantityRetrieverService) {
        this.quantityRetrieverService = quantityRetrieverService;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
        quantityRetrieverService = DefaultQuantityRetrieverService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID productId = UUID.fromString(request.getPathInfo().substring(1));
        if (isSuccessfullyAddedToCart(request, response, productId)) {
            String sort = request.getParameter("sort");
            String order = request.getParameter("order");
            String redirectLink = request.getContextPath() +
                    "/products" +
                    "?message=Product added to cart successfully&" +
                    "search=" + request.getParameter("search") +
                    (sort.isEmpty() || order.isEmpty() ? ""
                            : "&sort=" + sort + "&order=" + order);


            response.sendRedirect(redirectLink);
        }
    }

    private void setErrorAndRedirect(HttpServletRequest request, HttpServletResponse response,
                                     UUID productId, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("productIdWithError", productId);
        request.setAttribute("error", errorMessage);
        String sort = request.getParameter("sort");
        String order = request.getParameter("order");
        String redirectLink = request.getContextPath() +
                "/products" +
                "?productIdWithError=" +
                productId +
                "&error=" +
                errorMessage +
                "&search=" + request.getParameter("search") +
                (sort.isEmpty() || order.isEmpty() ? ""
                        : "&sort=" + sort + "&order=" + order);
        response.sendRedirect(redirectLink);
    }

    private boolean isSuccessfullyAddedToCart(HttpServletRequest request, HttpServletResponse response, UUID productId)
            throws ServletException, IOException {
        int quantity;

        try {
            quantity = quantityRetrieverService.getProductQuantity(request.getParameter("quantity"),
                    request.getLocale());
        } catch (ParseException exception) {
            setErrorAndRedirect(request, response, productId, "Not a number");
            return false;
        } catch (FractionalNumberException | NegativeNumberException exception) {
            setErrorAndRedirect(request, response, productId, exception.getMessage());
            return false;
        }
        try {
            addToCart(request, response, productId, quantity);
        } catch (OutOfStockException exception) {
            setErrorAndRedirect(request, response, productId, "Invalid quantity " +
                    exception.getQuantity() +
                    ". Available only " +
                    exception.getAvailableStock());
            return false;
        }
        return true;
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, UUID productId, int quantity)
            throws OutOfStockException {
        Cart cart = cartService.getCart(request.getSession());
        cartService.add(cart, productId, quantity);
    }
}