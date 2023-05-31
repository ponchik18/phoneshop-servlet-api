package com.es.phoneshop.web.constant;

public interface ServletConstant {
    interface PagesLocation {
        String PRODUCTS_LIST_PAGE = "/WEB-INF/pages/productList.jsp";
        String PRODUCT_DETAIL_PAGE = "/WEB-INF/pages/productDetails.jsp";
        String MINI_CART_PAGE = "/WEB-INF/pages/minicart.jsp";
        String CART_PAGE = "/WEB-INF/pages/cart.jsp";
        String CHECKOUT_PAGE = "/WEB-INF/pages/checkout.jsp";
        String OVERVIEW_PAGE = "/WEB-INF/pages/orderOverview.jsp";
    }

    interface RequestParameterName {
        String PAYMENT_METHODS = "paymentMethods";
        String PAYMENT_METHOD = "paymentMethod";
        String FIRST_NAME = "firstname";
        String LAST_NAME = "lastname";
        String PRODUCTS = "products";
        String ERROR = "error";
        String PRODUCT = "product";
        String CART = "cart";
        String ERRORS = "errors";
        String ORDER = "order";
        String PHONE = "phone";
        String DELIVERY_ADDRESS = "deliveryAddress";
        String DELIVERY_DATE = "deliveryDate";
        String QUANTITY = "quantity";
        String PRODUCT_ID = "productId";
        String SORT = "sort";
        String PRODUCT_HISTORY = "productHistory";
        String PRODUCT_ID_WITH_ERROR = "productIdWithError";
        String SEARCH = "search";
    }

    interface Message {
        String ERROR_NOT_A_NUMBER = "Not a number!";
        String ERROR_VALUE_IS_REQUIRED = "Value is required!";
        String ERROR_INVALID_FORMAT = "Invalid format!";
        String ERROR_INVALID_DATE = "Please, don't select previous date!";
    }

    interface Regex {
        String PHONE_REGEX = "\\(\\d{3}\\) \\d{3}-\\d{4}";
    }
}
