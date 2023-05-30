<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout">

    <c:if test="${not empty errors}">
        <div class="error">
            There was some error while trying create order
        </div>
    </c:if>
    <c:if test="${not empty param.message}">
        <div class="success">
            ${param.message}
        </div>
    </c:if>
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
            <tags:orderFormRow name="firstname" label="First name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="lastname" label="Last name" order="${order}" errors="${errors}"/>
            <tags:orderFormRow name="phone" label="Phone" order="${order}" errors="${errors}" placeHolderValue="(###) ###-####"/>
            <tags:orderFormRow name="deliveryDate" label="Delivery date" order="${order}" errors="${errors}" inputType="date"/>
            <tags:orderFormRow name="deliveryAddress" label="Delivery Address" order="${order}" errors="${errors}"/>

            <tr>
                <td>Payment method<span style="color: red">*</span></td>
                <td>
                    <select name="paymentMethod">
                        <option></option>
                        <c:forEach var="paymentMethod" items="${paymentMethods}">
                            <c:choose>
                                <c:when test="${paymentMethod eq order.paymentMethod}">
                                    <selected>${paymentMethod}</selected>
                                </c:when>
                                <c:otherwise>
                                    <option>${paymentMethod}</option>
                                </c:otherwise>
                            </c:choose>
                        </c:forEach>
                        <c:if test="${not empty order.paymentMethod}">
                            <option selected>${order.paymentMethod}</option>
                        </c:if>
                    </select>
                    <c:if test="${not empty errors['paymentMethod']}">
                        <div class="error">
                                ${errors['paymentMethod']}
                        </div>

                    </c:if>
                </td>
            </tr>
        </table>
        <p>
            <button>Place order</button>
        </p>
    </form>
</tags:master>