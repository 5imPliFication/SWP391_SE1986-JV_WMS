<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/8/2026
  Time: 9:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>User List</title>
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }
        th, td {
            padding: 8px;
            border: 1px solid #ccc;
            text-align: left;
        }
        th {
            background-color: #f4f4f4;
        }
    </style>
</head>
<body>

<h2>Danh sách người dùng</h2>

<table>
    <thead>
    <tr>
        <th>ID</th>
        <th>Họ tên</th>
        <th>Email</th>
        <th>Role</th>
        <th>Status</th>
    </tr>
    </thead>

    <tbody>
    <c:forEach items="${userList}" var="u">
        <tr>
            <td>${u.id}</td>
            <td>${u.fullName}</td>
            <td>${u.email}</td>
            <td>${u.role}</td>
            <td>${u.active}</td>
        </tr>
    </c:forEach>

    <c:if test="${empty userList}">
        <tr>
            <td colspan="5">Không có dữ liệu</td>
        </tr>
    </c:if>
    </tbody>
</table>

</body>
</html>

