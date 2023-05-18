package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.iml.DefaultCartService;
import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.service.iml.DefaultProductsTrackingHistoryService;
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
    private DefaultProductsTrackingHistoryService productsTrackingHistory;

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
        productsTrackingHistory = DefaultProductsTrackingHistoryService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));
        Product product = productDao.getProduct(productId);
        ProductsHistory productHistory = productsTrackingHistory.getProductHistory(request.getSession());
        productsTrackingHistory.addToViewed(productHistory, product, request.getSession().getId());

        request.setAttribute("product", product);
        request.setAttribute("cart", cartService.getCart(request.getSession()));
        request.setAttribute("productHistory", productHistory.getProducts());
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.parseLong(request.getPathInfo().substring(1));

        if(isSuccessfullyAddingToCart(request, response, productId)) {
            response.sendRedirect(request.getContextPath() +
                    "/products/" +
                    productId +
                    "?message=Product added to cart successfully");
        }
    }

    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute("error", errorMessage);
        doGet(request, response);
    }

    private int getProductQuantity(HttpServletRequest request, HttpServletResponse response) throws ParseException {
        NumberFormat format = NumberFormat.getInstance(request.getLocale());
        return format.parse(request.getParameter("quantity")).intValue();
    }

    private void addToCart(HttpServletRequest request, HttpServletResponse response, Long productId, int quantity)
            throws OutOfStockException {
        Cart cart = cartService.getCart(request.getSession());
        cartService.add(cart, productId, quantity, request.getSession().getId());
    }

    private boolean isSuccessfullyAddingToCart(HttpServletRequest request, HttpServletResponse response, Long productId) throws ServletException, IOException {
        int quantity;

        try {
            quantity = getProductQuantity(request, response);
        } catch (ParseException exception) {
            setErrorAndForward(request, response, "Not a number");
            return false;
        }
        try {
            addToCart(request, response, productId, quantity);
        } catch (OutOfStockException exception) {
            setErrorAndForward(request, response, "Invalid quantity " +
                    exception.getQuantity() +
                    ". Available only " +
                    exception.getAvailableStock());
            return false;
        }
        return true;
    }

}