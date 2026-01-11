<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Role Detail</title>
</head>
<body>

<h2>Role Detail</h2>

<table border="1" cellpadding="8" cellspacing="0">
    <tr>
        <th>ID</th>
        <td>${role.id}</td>
    </tr>

    <tr>
        <th>Name</th>
        <td>${role.name}</td>
    </tr>

    <tr>
        <th>Description</th>
        <td>${role.description}</td>
    </tr>

    <tr>
        <th>Status</th>
        <td>
            <c:choose>
                <c:when test="${role.status}">Active</c:when>
                <c:otherwise>Inactive</c:otherwise>
            </c:choose>
        </td>
    </tr>
</table>

<br>
<a href="${pageContext.request.contextPath}/admin/role?action=edit&id=${role.id}">
    Update Role
</a>


<a href="${pageContext.request.contextPath}/admin/role">
    Back to Role List
</a>

</body>
</html>

