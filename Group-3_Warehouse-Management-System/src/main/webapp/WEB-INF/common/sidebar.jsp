<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<% String uri = request.getRequestURI(); %>

<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">
    <title>JSP Page</title>
</head>
<aside class="sidebar">
    <div class="sidebar-header">
        <h1>Warehouse</h1>
        <p>Management System</p>
    </div>

    <ul class="sidebar-menu">

        <!-- Dashboard luôn hiển thị -->
        <li>
            <a href="${pageContext.request.contextPath}/home"
               class="<%= (uri.endsWith("home") || uri.contains("home")) ? "active" : "" %>">
                <span>Dashboard</span>
            </a>
        </li>

        <!-- USER -->
        <c:if test="${sessionScope.user != null
                         and sessionScope.user.role != null
                         and sessionScope.user.role.active
                         and fn:contains(sessionScope.userPermissions, 'READ_USER')}">
            <li>
                <a href="${pageContext.request.contextPath}/user-list"
                   class="<%= (uri.contains("user")) ? "active" : "" %>">
                    <span>User</span>
                </a>
            </li>
        </c:if>

        <!-- ROLE -->
        <c:if test="${sessionScope.user != null
                         and sessionScope.user.role != null
                         and sessionScope.user.role.active
                         and fn:contains(sessionScope.userPermissions, 'READ_ROLE')}">
            <li>
                <a href="${pageContext.request.contextPath}/roles"
                   class="<%= (uri.contains("roles")) ? "active" : "" %>">
                    <span>Role</span>
                </a>
            </li>
        </c:if>

    </ul>
</aside>
</html>