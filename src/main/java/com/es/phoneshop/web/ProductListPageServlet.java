package com.es.phoneshop.web;

import com.es.phoneshop.dao.ProductDao;
import com.es.phoneshop.dao.impl.ArrayListProductDao;
import com.es.phoneshop.dto.SortField;
import com.es.phoneshop.dto.SortOrder;
import com.es.phoneshop.model.history.ProductHistoryList;
import com.es.phoneshop.model.history.ProductsTrackingHistory;
import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Optional;

public class ProductListPageServlet extends HttpServlet {

    private ProductDao productDao;
    private ProductsTrackingHistory productsTrackingHistory;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        productsTrackingHistory = ProductsTrackingHistory.getInstance();
    }

    public void setProductDao(ProductDao productDao) {
        this.productDao = productDao;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String search = request.getParameter("search");
        String sortField = request.getParameter("sort");
        String sortOrder = request.getParameter("order");

        request.setAttribute("productHistoryList", productsTrackingHistory.getProductHistoryList(request).getProducts());
        request.setAttribute("products", productDao.findProducts(search, Optional.ofNullable(sortField).map(SortField::valueOf).orElse(null), Optional.ofNullable(sortOrder).map(SortOrder::valueOf).orElse(null)));
        request.getRequestDispatcher("/WEB-INF/pages/productList.jsp").forward(request, response);
    }
}