package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.exception.FractionalNumberException;
import com.es.phoneshop.exception.NegativeNumberException;
import com.es.phoneshop.exception.OutOfStockException;
import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.history.ProductsHistory;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.service.CartService;
import com.es.phoneshop.service.QuantityRetrieverService;
import com.es.phoneshop.service.impl.DefaultCartService;
import com.es.phoneshop.service.impl.DefaultProductsTrackingHistoryService;
import com.es.phoneshop.service.impl.DefaultQuantityRetrieverService;
import com.es.phoneshop.web.constant.ServletConstant;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.text.ParseException;
import java.util.UUID;

public class ProductDetailsPageServlet extends HttpServlet {

    private ProductDao productDao;

    private CartService cartService;
    private DefaultProductsTrackingHistoryService productsTrackingHistory;

    private QuantityRetrieverService quantityRetrieverService;

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

    public void setQuantityRetrieverService(QuantityRetrieverService quantityRetrieverService) {
        this.quantityRetrieverService = quantityRetrieverService;
    }


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = DefaultCartService.getInstance();
        productsTrackingHistory = DefaultProductsTrackingHistoryService.getInstance();
        quantityRetrieverService = DefaultQuantityRetrieverService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID productId = UUID.fromString(request.getPathInfo().substring(1));
        Product product = productDao.getItem(productId);
        ProductsHistory productHistory = productsTrackingHistory.getProductHistory(request.getSession());
        productsTrackingHistory.addToViewed(productHistory, product, request.getSession().getId());

        request.setAttribute(ServletConstant.RequestParameterName.PRODUCT, product);
        request.setAttribute(ServletConstant.RequestParameterName.CART, cartService.getCart(request.getSession()));
        request.setAttribute(ServletConstant.RequestParameterName.PRODUCT_HISTORY, productHistory.getProducts());
        request.getRequestDispatcher(ServletConstant.PagesLocation.PRODUCT_DETAIL_PAGE).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        UUID productId = UUID.fromString(request.getPathInfo().substring(1));

        if (isSuccessfullyAddedToCart(request, response, productId)) {
            response.sendRedirect(request.getContextPath() +
                    "/products/" +
                    productId +
                    "?message=Product added to cart successfully");
        }
    }

    private void setErrorAndForward(HttpServletRequest request, HttpServletResponse response, String errorMessage)
            throws ServletException, IOException {
        request.setAttribute(ServletConstant.RequestParameterName.ERROR, errorMessage);
        doGet(request, response);
    }

    private void addToCart(HttpServletRequest request, UUID productId, int quantity)
            throws OutOfStockException {
        Cart cart = cartService.getCart(request.getSession());
        cartService.add(cart, productId, quantity);
    }

    private boolean isSuccessfullyAddedToCart(HttpServletRequest request, HttpServletResponse response, UUID productId)
            throws ServletException, IOException {
        int quantity;

        try {
            quantity = quantityRetrieverService.getProductQuantity(
                    request.getParameter(ServletConstant.RequestParameterName.QUANTITY),
                    request.getLocale()
            );
        } catch (ParseException exception) {
            setErrorAndForward(request, response, ServletConstant.Message.ERROR_NOT_A_NUMBER);
            return false;
        } catch (FractionalNumberException | NegativeNumberException exception) {
            setErrorAndForward(request, response, exception.getMessage());
            return false;
        }
        try {
            addToCart(request, productId, quantity);
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