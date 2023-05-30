<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart">
    <p>
        Cart: ${cart.items}
    </p>
    <c:if test="${not empty  param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty errors}">
        <div class="error">
            There was some error while updating quantity
        </div>
    </c:if>
    <form method="post">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>Description
                </td>
                <td>
                    Quantity
                </td>
                <td>Price</td>
                <td>Delete</td>
            </tr>
            </thead>
            <c:forEach var="item" items="${cart.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/products/${item.product.id}">${item.product.description}</a>
                    </td>
                    <td>

                        <input class="align-right" value="${item.quantity}" name="quantity"/>
                        <c:set var="error" value="${errors[item.product.id]}"/>
                        <c:if test="${not empty error}">
                            <br>
                            <span class="error">
                                    ${error}
                            </span>
                        </c:if>
                        <input type="hidden" value="${item.product.id}" name="productId">
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                    <td>
                        <button form="deleteCartItem" formaction="${pageContext.servletContext.contextPath}/cart/deleteCartItem/${item.product.id}">Delete</button>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Total Price</td>
                <td class="price"><fmt:formatNumber value="${cart.totalCost}" type="currency"
                                      currencySymbol="${cart.currency.symbol}"/></td>
                <td></td>
            </tr>
        </table>
        <p>
            <button type="submit">Update</button>
        </p>
    </form>
    <form action="${pageContext.request.contextPath}/checkout">
        <p>
            <button>Order</button>
        </p>
    </form>
    <form id="deleteCartItem" method="post"></form>
</tags:master>