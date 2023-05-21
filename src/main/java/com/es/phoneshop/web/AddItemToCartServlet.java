package com.es.phoneshop.web;

import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.quantity.QuantityRetriever;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.iml.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;

public class AddItemToCartServlet extends HttpServlet {

    private CartService cartService;

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        if (isSuccessfullyAddingToCart(request, response, productId)) {
            response.sendRedirect(request.getContextPath() +
                    "/products" +
                    "?message=Product added to cart successfully");
        }
    }

    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, Long productId, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("productIdWithError", productId);
        request.setAttribute("error", errorMessage);
        response.sendRedirect(request.getContextPath() +
                "/products" +
                "?error=" +
                errorMessage +
                "&productIdWithError=" +
                productId);
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, Long productId, int quantity)
            throws OutOfStockException {
        Cart cart = cartService.getCart(request.getSession());
        cartService.add(cart, productId, quantity);
    }

    private boolean isSuccessfullyAddingToCart(HttpServletRequest request, HttpServletResponse response, Long productId)
            throws ServletException, IOException {
        int quantity;

        try {
            quantity = QuantityRetriever.getProductQuantity(request.getParameter("quantity"), request.getLocale());
        } catch (ParseException exception) {
            setErrorAndForward(request, response, productId, "Not a number");
            return false;
        } catch (FractionalNumberException | NegativeNumberException exception) {
            setErrorAndForward(request, response, productId, exception.getMessage());
            return false;
        }
        try {
            addToCart(request, response, productId, quantity);
        } catch (OutOfStockException exception) {
            setErrorAndForward(request, response, productId, "Invalid quantity " +
                    exception.getQuantity() +
                    ". Available only " +
                    exception.getAvailableStock());
            return false;
        }
        return true;
    }
}