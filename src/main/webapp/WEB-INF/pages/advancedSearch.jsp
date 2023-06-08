<%@ page import="javafx.util.Pair" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">

    <h2>Advanced search</h2>
    <form>
        <table class="border-none">
            <tr >
                <td>Description</td>
                <td><input name = "description" value="${param.description}"></td>
                <td>
                    <select name="include_type">
                        <option value="ALL_WORDS" selected>all words</option>
                        <option value="ANY_WORDS" >any words</option>
                        <c:if test="${not empty order.paymentMethod}">
                            <option selected>${order.paymentMethod}</option>
                        </c:if>
                    </select>
                </td>
            </tr>
            <tr>
                <td>Min price</td>
                <td>
                    <input name = "min_price" value="${param.min_price}">
                    <c:if test="${not empty errors['min_price']}">
                        <div class="error">
                                ${errors['min_price']}
                        </div>
                    </c:if>
                </td>
                <td></td>
            </tr>
            <tr>
                <td>Max price</td>
                <td>
                    <input name="max_price" value="${param.max_price}">
                    <c:if test="${not empty errors['max_price']}">
                        <div class="error">
                                ${errors['max_price']}
                        </div>
                    </c:if>
                </td>
                <td></td>
            </tr>
        </table>
        <p>
            <button type="submit">Search</button>
        </p>
    </form>
    <c:if test="${products.size()>0}">
        <table>
            <thead>
                <tr>
                    <td>Image</td>
                    <td>Description</td>
                    <td>Price</td>
                </tr>
            </thead>
            <c:forEach var="product" items="${products}">
                <tr>
                    <td>
                        <img class="product-tile" src="${product.imageUrl}">
                    </td>
                    <td><a href="${pageContext.request.contextPath}/products/${product.id}">${product.description}</a></td>

                    <td class="price">
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
    <c:if test="${productHistory.size()>0}">
        <table>
            <h3>Recently viewed</h3>
            <c:forEach var="product" items="${productHistory}">
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