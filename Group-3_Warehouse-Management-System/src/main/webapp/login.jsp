<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/9/2026
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%
    String ctx = request.getContextPath();
%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8"  language="java" %>
<%--<%@ page session="false" %>--%>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="<%=ctx%>/static/css/login.css">
</head>
<body>
<div class="login-container">
    <div class="login-left">
        <h1>📦 Laptop Warehouse</h1>
        <p>Comprehensive inventory management system for laptop distribution and tracking</p>
        <ul class="feature-list">
            <li>Real-time inventory tracking</li>
            <li>Order management</li>
            <li>Supplier coordination</li>
            <li>Sales analytics & reporting</li>
            <li>Multi-user access control</li>
        </ul>
    </div>

    <div class="login-right">
        <div class="login-header">
            <h2>Welcome Back</h2>
            <p>Sign in to access your dashboard</p>
        </div>

        <!-- Error message (JSP will populate this) -->
        <%
            String error = (String) request.getAttribute("error");
            if (error != null && !error.isEmpty()) {
        %>
        <div class="alert alert-danger">
            <%= error %>
        </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/login" method="post">
            <div class="form-group">
                <label for="email">Email Address</label>
                <input type="email" id="email" name="email" placeholder="your.email@company.com" required autofocus>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>

            <button type="submit" class="btn-login">Sign In</button>
        </form>

    </div>
</div>

</body>
</html>

