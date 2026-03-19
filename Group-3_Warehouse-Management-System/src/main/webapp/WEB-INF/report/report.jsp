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
            max-width: 80%;
            margin: auto;
        }

        .report-type {
            cursor: pointer;
            padding: 10px 20px;
            border: 1px solid #ccc;
            margin-right: 10px;
            border-radius: 5px;
        }

        .report-type.active {
            background-color: #007bff;
            color: white;
            border-color: #007bff;
            font-weight: bold;
        }

        .report-type:hover {
            background-color: #0cf304;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <h2 class="mb-4">
        <c:choose>
            <c:when test="${type eq 'import'}">Import Report</c:when>
            <c:when test="${type eq 'export'}">Export Report</c:when>
            <c:when test="${type eq 'inventory'}">Inventory Report</c:when>
            <c:otherwise>Inventory Report</c:otherwise>
        </c:choose>
    </h2>

    <%--input year--%>
    <form action="${pageContext.request.contextPath}/report" method="get" class="mb-3">
        <div class="d-flex gap-2 mb-2">
            <input type="number" name="year" class="form-control w-auto"
                   placeholder="Input year"
                   value="${param.year}">
            <button type="submit" class="btn btn-primary">Search</button>
        </div>
    </form>

    <%--Total Compare Chart--%>
    <div class="mb-3">
        <div style="background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
            <canvas id="overviewChart" width="400" height="150"></canvas>
        </div>
    </div>

    <%--  select type  --%>
    <div class="d-flex mb-3">
        <a href="?type=export&year=${param.year}"
           class="report-type ${requestScope.type eq 'export' ? 'active' : ''}">
            Export
        </a>

        <a href="?type=import&year=${param.year}"
           class="report-type ${requestScope.type eq 'import' ? 'active' : ''}">
            Import
        </a>

        <a href="?type=inventory&year=${param.year}"
           class="report-type ${requestScope.type eq 'inventory' ? 'active' : ''}">
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
    <div class="table-card">
        <div class="table-scroll">
            <table class="modern-table">
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
                    label: ' Quantity',
                    data: data,
                    backgroundColor: '${type eq 'inventory' ? 'rgba(75, 192, 192, 0.2)' : 'rgba(54, 162, 235, 0.2)'}',
                    borderColor: '${type eq 'inventory' ? 'rgba(75, 192, 192, 1)' : 'rgba(54, 162, 235, 1)'}',
                    borderWidth: 2,
                    fill: true,
                    tension: 0.3,
                    pointBackgroundColor: '${type eq 'inventory' ? 'rgba(75, 192, 192, 1)' : 'rgba(54, 162, 235, 1)'}'
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
                        text: 'Chart of ${type} product in year ${year != null ? year : (param.year != null ? param.year : year)}'
                    }
                }
            }
        });
    });
    </c:if>

    <c:if test="${not empty chartSummaryData}">
    document.addEventListener("DOMContentLoaded", function () {

        const rawData = JSON.parse('${chartSummaryData}');

        const importData = rawData.map(item => item.importQuantity);
        const exportData = rawData.map(item => item.exportQuantity);

        const ctx = document.getElementById('overviewChart').getContext('2d');

        new Chart(ctx, {
            type: 'bar',
            data: {
                labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                datasets: [
                    {
                        label: 'Import',
                        data: importData,
                        backgroundColor: 'rgb(38, 10, 247)',
                        borderWidth: 1
                    },
                    {
                        label: 'Export',
                        data: exportData,
                        backgroundColor: 'rgb(222, 10, 10)',
                        borderWidth: 1
                    }
                ]
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
                        text: 'Compare quantity of import product vs export product in year ${year != null ? year : (param.year != null ? param.year : year)}'
                    }
                }
            }
        });

    });
    </c:if>

</script>

</body>

</html>