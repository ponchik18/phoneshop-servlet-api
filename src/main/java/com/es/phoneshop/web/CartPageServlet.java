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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CartPageServlet extends HttpServlet {


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
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        Cart cart = cartService.getCart(request.getSession());
        request.setAttribute("cart", cart);
        request.getRequestDispatcher("/WEB-INF/pages/cart.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String[] productsId = request.getParameterValues("productId");
        String[] quantities = request.getParameterValues("quantity");
        Locale locale = request.getLocale();
        Cart cart = cartService.getCart(request.getSession());
        Map<Long, String> errors = new HashMap<>();

        for (int i = 0; i < productsId.length; i++) {
            Long productId = Long.valueOf(productsId[i]);
            validateUpdateCartItem(cart, productId, errors, locale, quantities[i]);
        }
        if (errors.size() > 0) {
            request.setAttribute("errors", errors);
            doGet(request, response);
        } else {
            response.sendRedirect(request.getContextPath() +
                    "/cart" +
                    "?message=Quantity has been changed successfully");
        }


    }

    private void validateUpdateCartItem(Cart cart, Long productId, Map<Long, String> errors, Locale locale, String quantityString) {
        try {
            int quantity = QuantityRetriever.getProductQuantity(quantityString, locale);
            cartService.update(cart, productId, quantity);
        } catch (ParseException exception) {
            errors.put(productId, "Not a number");
        } catch (OutOfStockException exception) {
            errors.put(productId, "Invalid quantity " +
                    exception.getQuantity() +
                    ". Available only " +
                    exception.getAvailableStock());
        } catch (FractionalNumberException | NegativeNumberException exception) {
            errors.put(productId, exception.getMessage());
        }
    }

}