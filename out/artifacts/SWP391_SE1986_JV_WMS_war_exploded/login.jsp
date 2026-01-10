<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/9/2026
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page session="false" %>
<!DOCTYPE html>
<html>
<head>
    <title>Login</title>
    <link rel="stylesheet" href="static/css/login.css">
    <link rel="stylesheet" href="static/css/bootstrap.min.css">
</head>
<body>

<h2>Login</h2>
<form action="${pageContext.request.contextPath}/login" method="post">
    Email: <input type="text" name="email" required /> <br/>
    Password: <input type="password" name="password" required /> <br/>
    <button type="submit">Login</button>
</form>

<c:if test="${not empty error}">
    <p style="color:red">${error}</p>
</c:if>

</body>
</html>

