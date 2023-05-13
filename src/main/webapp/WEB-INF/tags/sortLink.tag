<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="order" required="true" %>
<%@ attribute name="sort" required="true" %>

<a href="?sort=${sort}&order=${order}&search=${param.search}" style="${sort eq param.sort and order eq param.order ? 'font-weight: bold;color:red;' : ''}">
  ${order eq "asc"? "&uArr;": "&dArr;"}</a>