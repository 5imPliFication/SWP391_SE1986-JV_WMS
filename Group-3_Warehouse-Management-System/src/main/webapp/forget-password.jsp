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
</head>
<body>
    <form method="POST" action="${pageContext.request.contextPath}/forget-password">
        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required><br>
        <p>Mật khẩu mới sẽ được admin cấp qua email của bạn</p><br>
        <input type="submit" value="Send Request">
    </form>
</body>
</html>
