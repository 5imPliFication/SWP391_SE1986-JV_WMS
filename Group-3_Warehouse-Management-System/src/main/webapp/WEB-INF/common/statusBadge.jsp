<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<c:choose>

    <c:when test="${param.active == '1' || param.active == 'true'}">
        <span class="badge bg-success">Active</span>
    </c:when>

    <c:otherwise>
        <span class="badge bg-danger">Inactive</span>
    </c:otherwise>

</c:choose>
