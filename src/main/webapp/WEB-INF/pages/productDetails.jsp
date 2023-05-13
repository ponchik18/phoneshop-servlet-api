<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<jsp:useBean id="productHistoryList" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="${product.description}">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty error}">
        <div class="error">
            There was an error in adding to cart
        </div>
    </c:if>
    <p>
        Cart: ${cart.items}
    </p>
    <form method="post">
        <table>
            <tbody>
            <tr>
                <td>
                    Image
                </td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Description</td>
                <td>${product.description}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input class="align-right" name="quantity"
                           value="${ not empty param.quantity? param.quantity: "1"}">
                    <c:if test="${not empty error}">
                        <br>
                        <span class="error">
                                ${error}
                        </span>
                    </c:if>
                </td>
            </tr>
            </tbody>
        </table>
        <p>
            <button type="submit">Add to cart</button>
        </p>
    </form>
    <c:if test="${productHistoryList.size()>0}">
        <table>
            <h3>Recently viewed</h3>
            <c:forEach var="product" items="${productHistoryList}">
                <td>
                    <p>
                        <img class="product-tile" src="${product.imageUrl}">
                    </p>
                    <p>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}">${product.description}</a>

                    </p>
                    <p>
                        <a href="${pageContext.servletContext.contextPath}/products/${product.id}"></a>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </p>
                </td>
            </c:forEach>
        </table>
    </c:if>
</tags:master>