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
            <p>Your request will be sent to the administrator. The administrator will send a reset password link to your email after confirming your request.</p>
        </div>

        <input type="submit" value="Send Request" class="btn-submit">
    </form>

    <div class="back-link">
        Remember your password? <a href="${pageContext.request.contextPath}/login">Back to Login</a>
    </div>

    <c:if test="${not empty error}">
        <div class="alert alert-danger">
                ${error}
        </div>
    </c:if>
    <c:if test="${not empty status}">
        <div class="alert alert-success">
                ${status}
        </div>
    </c:if>
</div>
</body>
</html>
