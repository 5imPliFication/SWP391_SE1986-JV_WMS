<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<h2>Create Order</h2>

<form action="${pageContext.request.contextPath}/salesman/order/create" method="post">
    Customer Name:
    <input type="text" name="customerName" required/>

    <button type="submit">Create</button>
</form>

</body>
</html>
