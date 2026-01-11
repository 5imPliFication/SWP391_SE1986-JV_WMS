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
    <title>Change Password</title>
</head>
<body>
    <form method="POST" action="${pageContext.request.contextPath}/change-password">

        <label for="currentPassword">Mật khẩu hiện tại:</label>
        <input type="text" id="currentPassword" name="currentPassword" required><br>

        <label for="newPassword">Mật khẩu mới:</label>
        <input type="text" id="newPassword" name="newPassword" required><br>

        <input type="submit" value="Change Password">

        <c:if test="${not empty error}">
            <div>${error}</div>
        </c:if>

    </form>
</body>
</html>
