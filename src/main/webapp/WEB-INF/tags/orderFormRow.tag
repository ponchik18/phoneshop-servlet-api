<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order"%>
<%@ attribute name="errors" required="true" type="java.util.Map"%>
<%@ attribute name="inputType" required="false"%>
<%@ attribute name="placeHolderValue" required="false"  %>

<tr>
    <c:set var="error" value="${errors[name]}"/>
    <td>${label}<span style="color: red">*</span></td>
    <td><input name="${name}" type="${not empty inputType? inputType: 'text'}" placeholder="${not empty placeHolderValue? placeHolderValue: ''}"
           value="${not empty error? param[name]: order[name]}"/>
        <c:if test="${not empty error}">
            <div class="error">
                ${error}
            </div>
        </c:if>
    </td>
</tr>