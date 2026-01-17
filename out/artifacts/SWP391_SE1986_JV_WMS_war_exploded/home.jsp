<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/9/2026
  Time: 1:43 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.User" %>
<html>
<head>
    <title>Home</title>
</head>
<body>
    <%
        User user = (User) session.getAttribute("user");
    %>

<html>
<head>
    <title>Home</title>
</head>
<body>

<h2>Welcome, <%= user.getFullName() %>
</h2>

<a href="logout">Logout</a>

</body>
</html>
