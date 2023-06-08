package com.es.phoneshop.web.servlet;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dto.IncludeType;
import com.es.phoneshop.service.ProductsTrackingHistoryService;
import com.es.phoneshop.service.ValidateParameterService;
import com.es.phoneshop.service.impl.DefaultProductsTrackingHistoryService;
import com.es.phoneshop.service.impl.DefaultValidateParameterService;
import com.es.phoneshop.web.constant.ServletConstant;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class AdvancedSearchPageServlet extends HttpServlet {

    private ProductDao productDao;
    private ProductsTrackingHistoryService productsTrackingHistory;
    private ValidateParameterService validateParameterService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        productsTrackingHistory = DefaultProductsTrackingHistoryService.getInstance();
        validateParameterService = DefaultValidateParameterService.getInstance();
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String description = request.getParameter(ServletConstant.RequestParameterName.DESCRIPTION);
        String includeType = request.getParameter(ServletConstant.RequestParameterName.INCLUDE_TYPE);
        String minPrice = request.getParameter(ServletConstant.RequestParameterName.MIN_PRICE);
        String maxPrice = request.getParameter(ServletConstant.RequestParameterName.MAX_PRICE);
        Map<String, String> errors = new HashMap<>();
        request.setAttribute(
                ServletConstant.RequestParameterName.PRODUCT_HISTORY,
                productsTrackingHistory.getProductHistory(request.getSession()
                ).getProducts());
        request.setAttribute(ServletConstant.RequestParameterName.PRODUCTS, productDao.advancedSearchProducts(
                description,
                Optional.ofNullable(includeType).map(IncludeType::valueOf).orElse(null),
                validateParameterService.validatePrice(errors, minPrice, ServletConstant.RequestParameterName.MIN_PRICE),
                validateParameterService.validatePrice(errors, maxPrice, ServletConstant.RequestParameterName.MAX_PRICE)));
        request.setAttribute(ServletConstant.RequestParameterName.ERRORS, errors);
        request.getRequestDispatcher(ServletConstant.PagesLocation.ADVANCED_SEARCH_PAGE).forward(request, response);
    }

}