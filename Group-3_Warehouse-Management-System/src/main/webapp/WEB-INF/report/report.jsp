<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Inventory Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">

    <style>
        .main-content {
            padding: 20px;
            max-width: 1200px;
            margin: auto;
        }

        .report-type {
            cursor: pointer;
            padding: 10px 20px;
            border: 1px solid #ccc;
            margin-right: 10px;
            border-radius: 5px;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <h2 class="mb-4">Import Report</h2>

    <%--input year--%>
    <form action="${pageContext.request.contextPath}/report" method="get" class="mb-3">
        <div class="d-flex gap-2 mb-2">
            <input type="number" name="year" class="form-control w-auto"
                   placeholder="Input year"
                   value="${param.year}">
            <button type="submit" class="btn btn-primary">Search</button>
        </div>
    </form>

    <div class="mb-3">
        Biểu đồ kết hợp xuất, nhập, tồn (hiển thị đủ 12 tháng trong năm) (Alo Tùng à e)
    </div>

    <%--  select type  --%>
    <div class="d-flex mb-3">
        <a href="?type=import&year=${param.year}"
           class="report-type ${type == 'import' ? 'active' : ''}">
            Import
        </a>

        <a href="?type=export&year=${param.year}"
           class="report-type ${type == 'export' ? 'active' : ''}">
            Export
        </a>

        <a href="?type=inventory&year=${param.year}"
           class="report-type ${type == 'inventory' ? 'active' : ''}">
            Inventory
        </a>
    </div>

    <%--chart 12 months for each type--%>
    <div class="mb-4">
        <c:choose>
            <c:when test="${not empty chartData}">
                <div style="background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
                    <canvas id="importChart" width="400" height="150"></canvas>
                </div>
            </c:when>
        </c:choose>
    </div>

    <%--select month--%>
    <form action="${pageContext.request.contextPath}/report" method="get" class="mb-3">

        <input type="hidden" name="type" value="${param.type}">
        <input type="hidden" name="year" value="${param.year}">

        <div class="d-flex gap-2 align-items-center">
            <input type="number" name="month"
                   class="form-control w-auto" placeholder="Input month"
                   value="${param.month}">
            <button type="submit" class="btn btn-primary">Search</button>
        </div>
    </form>

    <%-- message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>

        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>


    <%--table detail--%>
    <div class="table-card"> <div class="table-scroll"> <table class="modern-table">
        <thead>
        <tr>
            <th style="width: 80px;" class="text-center">No.</th>
            <th>Product</th>
            <th style="width: 150px;" class="text-center">Quantity</th>
        </tr>
        </thead>
        <tbody>
        <c:choose>
            <c:when test="${not empty reportItems}">
                <c:forEach items="${reportItems}" var="item" varStatus="loop">
                    <tr>
                        <td class="text-center">
                                ${loop.index + 1}
                        </td>
                        <td>
                                ${item.productName}
                        </td>
                        <td class="text-center">
                                ${item.quantity}
                        </td>
                    </tr>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <tr>
                    <td colspan="3" style="text-align: center; padding: 30px; color: #999;">
                        No data available.
                    </td>
                </tr>
            </c:otherwise>
        </c:choose>
        </tbody>
    </table>

    </div>
    </div>

    <jsp:include page="/WEB-INF/common/table.jsp"/>

</main>

<!-- Chart.js -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    <c:if test="${not empty chartData}">
    document.addEventListener("DOMContentLoaded", function () {
        const ctx = document.getElementById('importChart').getContext('2d');
        const data = JSON.parse('${chartData}');

        new Chart(ctx, {
            type: 'line',
            data: {
                labels: ['January', 'February', 'March', 'April', 'May', 'June',
                    'July', 'August', 'September', 'October', 'November', 'December'],
                datasets: [{
                    label: 'Quantity',
                    data: data,
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.3,
                    pointBackgroundColor: 'rgba(54, 162, 235, 1)'
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true
                    }
                },
                plugins: {
                    legend: {
                        position: 'top',
                    },
                    title: {
                        display: true,
                        text: 'Chart of import product in year ${param.year != null ? param.year : year}'
                    }
                }
            }
        });
    });
    </c:if>
</script>

</body>

</html>