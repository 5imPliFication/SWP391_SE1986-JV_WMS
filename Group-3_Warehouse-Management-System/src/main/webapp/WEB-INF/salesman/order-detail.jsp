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

<!-- Only allow editing if DRAFT -->
<c:if test="${order.status == 'DRAFT'}">
    <h3>Add Item</h3>
    <form action="${pageContext.request.contextPath}/salesman/order/item/add" method="post">
        <input type="hidden" name="orderId" value="${order.id}"/>

        Product ID:
        <input type="number" name="productId" required/>

        Quantity:
        <input type="number" name="quantity" required/>

        <button type="submit">Add</button>
    </form>

    <form action="${pageContext.request.contextPath}/salesman/order/submit" method="post">
        <input type="hidden" name="orderId" value="${order.id}"/>
        <button type="submit">Submit Order</button>
    </form>
</c:if>

<c:if test="${order.status != 'DRAFT'}">
    <p><i>Order is locked and cannot be modified.</i></p>
</c:if>
</body>
</html>
