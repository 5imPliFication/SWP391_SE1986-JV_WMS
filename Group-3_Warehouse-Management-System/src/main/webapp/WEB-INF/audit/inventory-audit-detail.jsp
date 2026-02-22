<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inventory Audit Detail</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
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

        .product-img {
            width: 40px;
            height: 40px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">
    <h2>Inventory Audit Detail</h2>

    <%-- Audit Info--%>
    <p>AUDIT CODE: ${inventoryAudit.auditCode}</p>
    <p>CREATED BY: ${inventoryAudit.user.fullName}</p>
    <p>STATUS: ${inventoryAudit.status}</p>
    <p>CREATED AT: ${inventoryAudit.createdAt}</p>
    <p>UPDATED AT: ${inventoryAudit.updatedAt}</p>
    <br>

    <%--Table of audit item--%>
    <table>
        <thead>
        <tr>
            <th>No.</th>
            <th>Product Name</th>
            <th>System Quantity</th>
            <th>Physical Quantity</th>
            <th>Discrepancy</th>
            <th>Reason</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${inventoryAuditItems}" var="i" varStatus="status">
            <tr>
                <td>${status.index + 1}</td>
                <td>${i.product.name}</td>
                <td>${i.systemQuantity}</td>
                <td>${i.physicalQuantity == null ? "-" : i.physicalQuantity}</td>
                <td>${i.discrepancy == null ? "-" : i.discrepancy}</td>
                <td>${i.reason == null ? "-" : i.reason}</td>
            </tr>
        </c:forEach>

        <c:if test="${empty inventoryAuditItems}">
            <tr>
                <td colspan="6">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <!-- Back button -->
    <br>
    <a href="${pageContext.request.contextPath}/inventory-audits"
       class="btn btn-secondary">
        ← Back
    </a>

</div>
</div>

</body>
</html>