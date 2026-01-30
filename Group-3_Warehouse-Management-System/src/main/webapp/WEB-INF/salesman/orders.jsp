<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>My Orders</h2>

<a href="${pageContext.request.contextPath}/salesman/create-order.jsp">
    Create New Order
</a>

<table border="1" width="100%">
    <tr>
        <th>Order Code</th>
        <th>Customer</th>
        <th>Status</th>
        <th>Created At</th>
        <th>Action</th>
    </tr>

    <c:forEach items="${orders}" var="o">
        <tr>
            <td>${o.orderCode}</td>
            <td>${o.customerName}</td>
            <td>${o.status}</td>
            <td>${o.createdAt}</td>
            <td>
                <a href="${pageContext.request.contextPath}/salesman/order/detail?id=${o.id}">
                    View
                </a>
            </td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
