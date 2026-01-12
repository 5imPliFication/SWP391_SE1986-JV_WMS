<%-- 
    Document   : sidebar
    Created on : Jan 11, 2026, 11:49:22 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<%--            dashboard--%>
            <li>
                <a href="${pageContext.request.contextPath}/home" 
                   class="<%= (uri.endsWith("home") || uri.contains("home")) ? "active" : "" %>">
                    <span>Dashboard</span>
                </a>
            </li>
    <%--            view user profile--%>
            <c:if test="${sessionScope.user.hasPermission('READ_USER')}">
                <li>
                    <a href="${pageContext.request.contextPath}/user-list" 
                       class="<%= (uri.contains("user")) ? "active" : "" %>">
                        <span>User</span>
                    </a>
                </li>
            </c:if>
            <c:if test="${sessionScope.user.hasPermission('READ_ROLE')}">
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
