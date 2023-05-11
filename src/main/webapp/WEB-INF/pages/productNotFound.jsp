<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<%--<jsp:useBean id="exeption" type="com.es.phoneshop.model.product.exception.ProductNotFoundException" scope="request"/>--%>
<tags:master pageTitle="Product Not Found">
  <h1>404 Product ${pageContext.exception.id} Not Found </h1>
</tags:master>