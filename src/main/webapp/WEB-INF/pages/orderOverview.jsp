<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order Overview">

    <h1>Your older</h1>
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
            </tr>
            </thead>
            <c:forEach var="item" items="${order.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.request.contextPath}/products/${item.product.id}">${item.product.description}</a>
                    </td>
                    <td>
                        <p class="align-right">
                                ${item.quantity}
                        </p>
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td></td>
                <td></td>
                <td>Subtotal</td>
                <td class="price"><fmt:formatNumber value="${order.subtotal}" type="currency"
                                      currencySymbol="${order.currency.symbol}"/></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Delivery Cost</td>
                <td class="price"><fmt:formatNumber value="${order.deliveryCost}" type="currency"
                                                    currencySymbol="${order.currency.symbol}"/></td>
            </tr>
            <tr>
                <td></td>
                <td></td>
                <td>Total Cost</td>
                <td class="price"><fmt:formatNumber value="${order.totalCost}" type="currency"
                                                    currencySymbol="${order.currency.symbol}"/></td>
            </tr>
        </table>
    <form method="post">
        <h2>Your details</h2>
        <table>
            <tags:orderOverviewRow name="firstname" label="First name" order="${order}" />
            <tags:orderOverviewRow name="lastname" label="Last name" order="${order}" />
            <tags:orderOverviewRow name="phone" label="Phone" order="${order}"/>
            <tags:orderOverviewRow name="deliveryDate" label="Delivery date" order="${order}"/>
            <tags:orderOverviewRow name="deliveryAddress" label="Delivery Address" order="${order}" />
            <tags:orderOverviewRow name="paymentMethod" label="Payment method" order="${order}" />
        </table>
    </form>
</tags:master>