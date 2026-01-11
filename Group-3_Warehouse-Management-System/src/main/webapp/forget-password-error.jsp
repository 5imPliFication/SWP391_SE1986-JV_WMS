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
</head>
<body>
    <p>Không tồn tại tài khoản dành cho Email của bạn. Không thể cấp lại mật khẩu</p><br>
    <a href="${pageContext.request.contextPath}/login.jsp">Quay lại trang đăng nhập</a>
</body>
</html>
