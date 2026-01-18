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
    <title>Forget Password</title>
    <link rel="stylesheet" href="${pageContext.request.getContextPath()}/static/css/forget-password.css">
</head>
<body>
<div class="forgot-container">
    <div class="forgot-header">
        <h2>Forgot Password?</h2>
    </div>

    <form method="POST" action="${pageContext.request.contextPath}/forget-password">
        <div class="form-group">
            <label for="email">Email Address</label>
            <input type="email"
                   id="email"
                   name="email"
                   placeholder="your.email@company.com"
                   required
                   autofocus>
        </div>

        <div class="info-box">
            <p>A new password will be sent to your email by the administrator. Please check your inbox after
                submitting this request.</p>
        </div>

        <input type="submit" value="Send Request" class="btn-submit">
    </form>

    <div class="back-link">
        Remember your password? <a href="${pageContext.request.contextPath}/login">Back to Login</a>
    </div>
</div>
</body>
</html>
