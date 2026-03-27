<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inventory Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        body {
            background-color: #f8f9fa;
        }

        .main-content {
            padding: 24px;
        }

        .report-shell {
            display: flex;
            flex-direction: column;
            gap: 16px;
        }

        .hero-panel,
        .panel {
            background: #fff;
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 16px;
        }

        .hero-title,
        .panel-title {
            margin: 0;
            font-weight: 600;
            color: #212529;
        }

        .hero-title {
            margin-top: 8px;
            margin-bottom: 8px;
            font-size: 1.75rem;
        }

        .hero-subtitle,
        .section-copy,
        .card-copy {
            color: #6c757d;
            margin-bottom: 0;
        }

        .eyebrow,
        .context-chip .label,
        .action-card .label {
            font-size: 0.75rem;
            color: #6c757d;
            font-weight: 600;
            text-transform: uppercase;
            letter-spacing: 0.04em;
        }

        .context-chip,
        .action-card,
        .helper-card {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            padding: 12px;
            background: #fff;
        }

        .context-chip .value,
        .action-card .value {
            font-size: 1rem;
            font-weight: 600;
            color: #212529;
        }

        .panel-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            flex-wrap: wrap;
            gap: 12px;
        }

        .report-type-group {
            display: flex;
            flex-wrap: wrap;
            gap: 8px;
            margin-top: 12px;
        }

        .report-type {
            display: inline-block;
            padding: 0.375rem 0.75rem;
            border: 1px solid #ced4da;
            border-radius: 0.375rem;
            background: #fff;
            color: #495057;
            text-decoration: none;
            font-weight: 500;
        }

        .report-type:hover {
            color: #0d6efd;
            border-color: #0d6efd;
            text-decoration: none;
        }

        .report-type.active {
            color: #fff;
            background: #0d6efd;
            border-color: #0d6efd;
        }

        .filter-grid {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-end;
            gap: 12px;
        }

        .filter-grid > div {
            display: flex;
            flex-direction: column;
            gap: 4px;
        }

        .filter-grid label {
            margin-bottom: 0;
            font-size: 0.8rem;
            color: #6c757d;
            font-weight: 600;
        }

        .chart-shell {
            position: relative;
            height: 320px;
        }

        .table-shell {
            border: 1px solid #dee2e6;
            border-radius: 8px;
            overflow: hidden;
            background: #fff;
        }

        .table-wrap {
            overflow-x: auto;
        }

        .table {
            margin-bottom: 0;
        }

        .table thead th {
            background-color: #f1f3f5;
            border-bottom: 1px solid #dee2e6;
            color: #212529;
            font-weight: 600;
            vertical-align: middle;
        }

        .table tbody td {
            vertical-align: middle;
        }

        .inv-summary-head,
        .inv-summary-subhead,
        .inv-summary-total {
            background-color: #f1f3f5;
        }

        .inv-sticky-first {
            position: sticky;
            left: 0;
            background: #fff;
            z-index: 2;
        }

        .empty-state {
            padding: 40px 12px;
            text-align: center;
            color: #6c757d;
        }

        .action-grid,
        .helper-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 12px;
        }

        .action-link {
            display: inline-block;
            padding: 0.375rem 0.75rem;
            border-radius: 0.375rem;
            background-color: #0d6efd;
            color: #fff;
            text-decoration: none;
            font-weight: 500;
        }

        .action-link:hover {
            color: #fff;
            text-decoration: none;
            background-color: #0b5ed7;
        }

        @media (max-width: 767px) {
            .main-content {
                padding: 16px;
            }

            .hero-panel,
            .panel {
                padding: 12px;
            }

            .hero-title {
                font-size: 1.4rem;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <div class="report-shell">
        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Select Report Type</h2>
                </div>
                <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                    <input type="hidden" name="type" value="inventory"/>
                    <div>
                        <label>Year</label>
                        <input type="number" name="year" class="form-control" placeholder="Input year" value="${year}">
                    </div>
                    <button type="submit" class="btn btn-primary">Apply Year</button>
                </form>
            </div>

            <div class="report-type-group">
                <a class="report-type"
                   href="${pageContext.request.contextPath}/report?type=inventory&view=in-out-stock&year=${year}&month=${month}">In-Out-Stock</a>
                <a href="?type=inventory&year=${year}" class="report-type active">Inventory</a>
                <a href="?type=import&year=${year}" class="report-type">Import</a>
                <a href="?type=export&year=${year}" class="report-type">Export</a>
            </div>
        </div>

        <c:if test="${not empty message}">
            <div class="alert alert-${messageType} alert-dismissible fade show mb-0" role="alert">
                    ${message}
                <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
        </c:if>

        <!-- 4. TREND CHART -->
        <c:if test="${not empty chartData}">
            <div class="panel">
                <h3 class="panel-title">Inventory Trend by Month (${year})</h3>
                <div style="position: relative; height: 300px;">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>
        </c:if>
        <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
            <input type="hidden" name="type" value="inventory"/>
            <input type="hidden" name="view" value="in-out-stock"/>
            <input type="hidden" name="year" value="${year}"/>
            <input type="hidden" name="fromDate" id="fromDateHidden" value="${fromDate}"/>
            <input type="hidden" name="toDate" id="toDateHidden" value="${toDate}"/>

            <div>
                <label>Month</label>
                <select name="month" id="monthSelect" class="form-control" style="min-width: 160px;">
                    <option value="">-- Select Month --</option>
                    <c:forEach begin="1" end="12" var="m">
                        <option value="${m}" ${month == m ? 'selected' : ''}>
                            <c:choose>
                                <c:when test="${m == 1}">January</c:when>
                                <c:when test="${m == 2}">February</c:when>
                                <c:when test="${m == 3}">March</c:when>
                                <c:when test="${m == 4}">April</c:when>
                                <c:when test="${m == 5}">May</c:when>
                                <c:when test="${m == 6}">June</c:when>
                                <c:when test="${m == 7}">July</c:when>
                                <c:when test="${m == 8}">August</c:when>
                                <c:when test="${m == 9}">September</c:when>
                                <c:when test="${m == 10}">October</c:when>
                                <c:when test="${m == 11}">November</c:when>
                                <c:when test="${m == 12}">December</c:when>
                            </c:choose>
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div>
                <label>From Date</label>
                <input type="text" id="fromDateDisplay" class="form-control" value="${fromDate}" readonly/>
            </div>
            <div>
                <label>To Date</label>
                <input type="text" id="toDateDisplay" class="form-control" value="${toDate}" readonly/>
            </div>

            <button type="submit" class="btn btn-primary">Apply Period</button>
            <a href="${pageContext.request.contextPath}/report?type=inventory&view=in-out-stock&year=${year}" class="btn btn-outline-secondary">Reset</a>
        </form>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Inventory Details</h2>
                </div>
            </div>

            <div class="table-shell">
                <div class="table-wrap">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th style="width: 80px;">No.</th>
                            <th>Product</th>
                            <th style="width: 160px;" class="text-center">Quantity</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty reportItems}">
                                <c:forEach items="${reportItems}" var="item" varStatus="loop">
                                    <tr>
                                        <td class="text-center">${loop.index + 1}</td>
                                        <td class="font-weight-bold">${item.productName}</td>
                                        <td class="text-center"><fmt:formatNumber value="${item.quantity}"
                                                                                  type="number"/></td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="empty-state">No inventory data found for the selected
                                        period.
                                    </td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </div>
</main>
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const trendChartRaw = '${chartData}';
        if (trendChartRaw) {
            try {
                const trendData = JSON.parse(trendChartRaw);
                const trendCanvas = document.getElementById('trendChart');
                if (trendCanvas) {
                    new Chart(trendCanvas, {
                        type: 'line',
                        data: {
                            labels: ['January', 'February', 'March', 'April', 'May', 'June', 'July', 'August', 'September', 'October', 'November', 'December'],
                            datasets: [{
                                label: 'Inventory Quantity',
                                data: trendData,
                                backgroundColor: 'rgba(34, 197, 94, 0.2)',
                                borderColor: 'rgba(34, 197, 94, 1)',
                                borderWidth: 2,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: 'rgba(34, 197, 94, 1)',
                                pointRadius: 4
                            }]
                        },
                        options: {
                            responsive: true, maintainAspectRatio: false,
                            scales: {y: {beginAtZero: true, ticks: {precision: 0}}},
                            plugins: {legend: {position: 'top'}}
                        }
                    });
                }
            } catch (e) {
                console.error('Error parsing trendChartRaw:', e);
            }
        }
    });
</script>
</body>
</html>


