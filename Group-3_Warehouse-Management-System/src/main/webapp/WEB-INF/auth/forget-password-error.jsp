<%--
  Created by IntelliJ IDEA.
  User: nguye
  Date: 1/9/2026
  Time: 10:26 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Forget Password Error</title>
    <link rel="stylesheet" href="${pageContext.request.getContextPath()}/static/css/forget-password.css">
</head>
<div class="forgot-container">
    <hr class="error-container">

    <h2>Account Not Found</h2>

    <div class="error-message">
        <p>We couldn't find an account associated with this email address. Password reset cannot be processed.</p>
    </div>

    <a href="${pageContext.request.contextPath}/login" class="btn-back">
        Back to Login
    </a>
    <div class="help-text">
        <strong>Need help?</strong>
        If you believe this is an error, please contact your system administrator or create a new account if you
        don't have one yet.
    </div>
</div>
</html>
