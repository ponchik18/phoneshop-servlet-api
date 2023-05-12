<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="${product.description}">
  <p>
    Welcome to Expert-Soft training!
  </p>
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
          <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
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
      </tr>
    </tbody>
  </table>
</tags:master>