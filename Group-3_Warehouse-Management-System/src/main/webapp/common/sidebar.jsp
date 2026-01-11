<%-- 
    Document   : sidebar
    Created on : Jan 11, 2026, 11:49:22 AM
    Author     : PC
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/home.css">

        <title>JSP Page</title>
    </head>
    <% String uri = request.getRequestURI(); %>
    <!-- Sidebar -->
    <aside class="sidebar">
        <div class="sidebar-header">
            <h1>Warehouse</h1>
            <p>Management System</p>
        </div>
        <ul class="sidebar-menu">
            <li>
                <a href="${pageContext.request.contextPath}/home" 
                   class="<%= (uri.endsWith("home") || uri.contains("home")) ? "active" : "" %>">
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/user-list" 
                   class="<%= (uri.contains("user")) ? "active" : "" %>">
                    <span>User</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/roles" 
                   class="<%= (uri.contains("roles")) ? "active" : "" %>">
                    <span>Role</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/suppliers" 
                   class="<%= (uri.contains("suppliers")) ? "active" : "" %>">
                    <span>Suppliers</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/customers" 
                   class="<%= (uri.contains("customers")) ? "active" : "" %>">
                    <span>Customers</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/reports" 
                   class="<%= (uri.contains("reports")) ? "active" : "" %>">
                    <span>Reports</span>
                </a>
            </li>
            <li>
                <a href="${pageContext.request.contextPath}/settings" 
                   class="<%= (uri.contains("settings")) ? "active" : "" %>">
                    <span>⚙ Settings</span>
                </a>
            </li>
        </ul>
    </aside>
</html>
