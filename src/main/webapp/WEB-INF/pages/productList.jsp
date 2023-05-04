<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
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
        <td >Price
            <tags:sortLink order="asc" sort="price"/>
            <tags:sortLink order="desc" sort="price"/>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td><a href="${pageContext.request.contextPath}/products/${product.id}">${product.description}</a></td>
        <td class="price" onclick="showPriceHistory()">
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          <div id="priceHistoryPopup" class="popup">
            <div class="popup-content">
              <h2 style="text-align: center;">Price history</h2>
              <c:choose>
                <c:when test="${product.pricesHistory.size()==0}">
                  <p>empty</p>
                  <br />
                </c:when>
                <c:otherwise>
                  <c:forEach var="entry" items="${product.pricesHistory}">
                    <fmt:formatDate value="${entry.key}" type="date" pattern="dd MMM yyyy"/>
                    <fmt:formatNumber value="${entry.value}" type="currency" currencySymbol="${product.currency.symbol}"/>
                    <br />
                  </c:forEach>
                  <br />
                </c:otherwise>
              </c:choose>
            </div>
          </div>
        </td>
      </tr>
    </c:forEach>
  </table>
  <script>
    function showPriceHistory() {
      const popup = document.getElementById("priceHistoryPopup");
      popup.style.display = "flex";
    }

    function closePopup() {
      const popup = document.getElementById("priceHistoryPopup");
      popup.style.display = "none";
    }
    window.addEventListener("click", function(event) {
      const popup = document.getElementById("priceHistoryPopup");
      if (event.target === popup) {
        popup.style.display = "none";
      }
    })
  </script>
</tags:master>