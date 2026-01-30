<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

<h2>Order Detail</h2>

<p><b>Order Code:</b> ${order.orderCode}</p>
<p><b>Customer:</b> ${order.customerName}</p>
<p><b>Status:</b> ${order.status}</p>

<h3>Items</h3>
<table border="1">
    <tr>
        <th>Product</th>
        <th>Quantity</th>
    </tr>

    <c:forEach items="${items}" var="i">
        <tr>
            <td>${i.productName}</td>
            <td>${i.quantity}</td>
        </tr>
    </c:forEach>
</table>

<c:if test="${order.status == 'PROCESSING'}">
    <form action="${pageContext.request.contextPath}/warehouse/order/approve" method="post">
        <input type="hidden" name="orderId" value="${order.id}" />
        <button type="submit">Approve</button>
    </form>

    <form action="${pageContext.request.contextPath}/warehouse/order/flag" method="post">
        <input type="hidden" name="orderId" value="${order.id}" />
        Reason:
        <input type="text" name="note" required />
        <button type="submit">Flag</button>
    </form>
</c:if>

</body>
</html>
