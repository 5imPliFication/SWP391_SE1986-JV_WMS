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

<h2>Order Queue</h2>

<table border="1" width="100%">
    <tr>
        <th>Order Code</th>
        <th>Customer</th>
        <th>Status</th>
        <th>Action</th>
    </tr>

    <c:forEach items="${orders}" var="o">
        <tr>
            <td>${o.orderCode}</td>
            <td>${o.customerName}</td>
            <td>${o.status}</td>
            <td>
                <a href="${pageContext.request.contextPath}/warehouse/order/detail?id=${o.id}">
                    View
                </a>

                <c:if test="${o.status == 'SUBMITTED'}">
                    <form action="${pageContext.request.contextPath}/warehouse/order/process" method="post" style="display:inline">
                        <input type="hidden" name="orderId" value="${o.id}" />
                        <button type="submit">Process</button>
                    </form>
                </c:if>
            </td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
