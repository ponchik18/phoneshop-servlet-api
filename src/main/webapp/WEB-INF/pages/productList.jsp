<%@ page import="javafx.util.Pair" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<jsp:useBean id="productHistory" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty  param.message}">
        <div class="success">
                ${param.message}
        </div>
    </c:if>
    <c:if test="${not empty param.error}">
        <div class="error">
            There was some error while updating quantity
        </div>
    </c:if>
    <form>
        <input type="text" name="search" value="${param.search}">
        <button type="submit">Search</button>
        <p>Count of product: ${products.size()}<p>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>Description
                <tags:sortLink order="asc" sort="description"/>
                <tags:sortLink order="desc" sort="description"/>
            </td>
            <td>Quantity</td>
            <td>Price
                    <tags:sortLink order="asc" sort="price"/>
                    <tags:sortLink order="desc" sort="price"/>
            <td></td>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td><a href="${pageContext.request.contextPath}/products/${product.id}">${product.description}</a></td>
                <td>
                    <input class="align-right" value="1" name="quantity" form="addItemToCart/${product.id}">
                    <c:if test="${not empty param.error and param.productIdWithError eq product.id}">
                        <br>
                        <span class="error">
                                ${param.error}
                        </span>
                    </c:if>
                </td>
                <td class="price" onclick="showPriceHistory(this)">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                    <div class="popup">
                        <div class="popup-content">
                            <h2 style="text-align: center;">Price history</h2>
                            <c:choose>
                                <c:when test="${product.priceHistories.size()==0}">
                                    <p>empty</p>
                                    <br/>
                                </c:when>
                                <c:otherwise>
                                    <c:forEach var="priceHistory" items="${product.priceHistories}">
                                        <fmt:formatDate value="${priceHistory.date}" type="date" pattern="dd MMM yyyy"/>
                                        <fmt:formatNumber value="${priceHistory.price}" type="currency"
                                                          currencySymbol="${product.currency.symbol}"/>
                                        <br/>
                                    </c:forEach>
                                    <br/>
                                </c:otherwise>
                            </c:choose>
                        </div>
                    </div>
                </td>
                <td>
                    <button form="addItemToCart/${product.id}"
                            formaction="${pageContext.request.contextPath}/add-to-cart/${product.id}?sort=${param.sort}&order=${param.order}&search=${param.search}">
                        Add to cart
                    </button>
                </td>
            </tr>
            <form id="addItemToCart/${product.id}" method="post"></form>
        </c:forEach>
    </table>
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
    <script>
        let modal;

        function showPriceHistory(element) {
            modal = element.querySelector('.popup')
            modal.style.display = "flex";
        }

        window.addEventListener("click", function (event) {

            if (event.target === modal) {
                modal.style.display = "none";
            }
        })
    </script>
</tags:master>