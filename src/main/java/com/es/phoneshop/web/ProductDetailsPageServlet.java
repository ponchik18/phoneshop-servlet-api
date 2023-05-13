package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.DefaultCartService;
import com.es.phoneshop.model.history.ProductHistoryList;
import com.es.phoneshop.model.history.ProductsTrackingHistory;
import com.es.phoneshop.model.product.Product;
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
    private ProductsTrackingHistory productsTrackingHistory;

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        productsTrackingHistory = ProductsTrackingHistory.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        Product product = productDao.getProduct(productId);
        request.setAttribute("product", product);
        request.setAttribute("cart", cartService.getCart(request));
        ProductHistoryList productHistoryList = productsTrackingHistory.getProductHistoryList(request);
        request.setAttribute("productHistoryList", productHistoryList.getProducts());
        productsTrackingHistory.addToViewed(productHistoryList, product);
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        int quantity = 0;
        try {
            NumberFormat format = NumberFormat.getInstance(request.getLocale());
            quantity = format.parse(request.getParameter("quantity")).intValue();
        } catch (ParseException exception) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        try {
            Cart cart = cartService.getCart(request);
            cartService.add(cart, productId, quantity);
        } catch (OutOfStockException exception) {
            request.setAttribute("error", "Invalid quantity " + exception.getQuantity() + ". Available only " + exception.getAvailableStock());
            doGet(request, response);
            return;
        }
        response.sendRedirect(request.getContextPath() + "/products/" + productId + "?message=Product added to cart successfully");
    }
}