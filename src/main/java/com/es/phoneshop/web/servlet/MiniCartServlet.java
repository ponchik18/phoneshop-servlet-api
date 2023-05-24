package com.es.phoneshop.web.servlet;

import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.impl.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

public class MiniCartServlet extends HttpServlet {

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
        request.setAttribute("cart", cartService.getCart(request.getSession()));

        request.getRequestDispatcher("/WEB-INF/pages/minicart.jsp").include(request, response);
    }

}