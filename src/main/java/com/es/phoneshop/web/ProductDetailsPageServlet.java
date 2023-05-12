package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;

    private CartService cartService;

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }
    public void setCartService(CartService cartService) {this.cartService = cartService;}

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        request.setAttribute("product", productDao.getProduct(productId));
        request.setAttribute("cart", cartService.getCart());
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        int quantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(request.getParameter("quantity")).intValue();
        }
        catch (ParseException exception){
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        try {
            cartService.add(productId, quantity);
        } catch (OutOfStockException exception) {
            request.setAttribute("error", "Invalid quantity "+exception.getQuantity()+". Available only "+exception.getAvailableStock());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath()+"/products/"+ productId + "?message=Product added to cart successfully");
    }
}