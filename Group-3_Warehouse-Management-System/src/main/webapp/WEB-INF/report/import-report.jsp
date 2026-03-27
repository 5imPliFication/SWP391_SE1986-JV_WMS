<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Import Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .main-content { padding: 24px; max-width: 1280px; margin: auto; }
        .report-type { display: inline-flex; align-items: center; justify-content: center; min-width: 120px; padding: 10px 16px; border: 1px solid #d8deea; border-radius: 8px; text-decoration: none; color: #334155; font-weight: 600; background: #f8fafc; transition: all .2s ease; }
        .report-type.active { background-color: #007bff; color: white; border-color: #007bff; font-weight: bold; }
        .report-type:hover { background-color: #0cf304; }
        .panel { background-color: white; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); margin-bottom: 20px; }
        .panel-title { font-size: 18px; font-weight: 600; margin-bottom: 15px; color: #1e293b; }
        .filter-grid { display: flex; gap: 12px; flex-wrap: wrap; align-items: flex-end; }
        .filter-grid > div { display: flex; flex-direction: column; gap: 5px; }
        .table-wrap { overflow-x: auto; }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <h2 class="mb-4">Import Report</h2>

    <!-- 1. YEAR SEARCH -->
    <div class="panel">
        <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
            <div>
                <label class="small font-weight-bold text-muted">Year</label>
                <input type="number" name="year" class="form-control" style="min-width: 120px;" placeholder="Input year" value="${year}">
            </div>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>

    <!-- 2. OVERALL COMPARE CHART -->
    <c:if test="${not empty chartSummaryData}">
        <div class="panel">
            <h3 class="panel-title">Annual Overview - Import vs Export</h3>
            <div style="position: relative; height: 300px;">
                <canvas id="overviewChart"></canvas>
            </div>
        </div>
    </c:if>

    <!-- 3. REPORT TYPE SELECTOR -->
    <div class="panel">
        <h3 class="panel-title">Select Report Type</h3>
        <div class="d-flex gap-2">
            <a href="?type=import&year=${year}" class="report-type active">Import</a>
            <a href="?type=export&year=${year}" class="report-type">Export</a>
            <a href="?type=inventory&year=${year}" class="report-type">Inventory</a>
        </div>
    </div>

    <!-- Error/Message Display -->
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

    <!-- 4. TREND CHART -->
    <c:if test="${not empty chartData}">
        <div class="panel">
            <h3 class="panel-title">Import Trend by Month (${year})</h3>
            <div style="position: relative; height: 300px;">
                <canvas id="trendChart"></canvas>
            </div>
        </div>
    </c:if>

    <!-- 5. MONTH FILTER -->
    <div class="panel">
        <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
            <input type="hidden" name="type" value="import"/>
            <input type="hidden" name="year" value="${year}"/>
            <div>
                <label class="small font-weight-bold text-muted">Month</label>
                <input type="number" name="month" class="form-control" style="min-width: 120px;"
                       placeholder="Input month" value="${month}" min="1" max="12">
            </div>
            <button type="submit" class="btn btn-primary">Filter</button>
        </form>
    </div>

    <!-- 6. TABLE -->
    <div class="panel">
        <h3 class="panel-title">Import Details - ${month}/${year}</h3>
        <div class="table-wrap">
            <table class="table table-bordered table-hover mb-0">
                <thead>
                <tr>
                    <th style="width: 80px;">No.</th>
                    <th>Product</th>
                    <th style="width: 150px;" class="text-center">Quantity</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${not empty reportItems}">
                        <c:forEach items="${reportItems}" var="item" varStatus="loop">
                            <tr>
                                <td class="text-center">${loop.index + 1}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/stock-history?year=${year}&month=${month}&productName=${item.productName}&type=IMPORT" class="font-weight-bold">
                                    ${item.productName}
                                    </a>
                                </td>
                                <td class="text-center">
                                        ${item.quantity}
                                </td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="3" class="text-center text-muted py-4">No import data found for ${month}/${year}.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </div>
</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const chartSummaryRaw = '${chartSummaryData}';
        const trendChartRaw = '${chartData}';

        if (chartSummaryRaw) {
            try {
                const rawData = JSON.parse(chartSummaryRaw);
                const importData = rawData.map(item => item.importQuantity);
                const exportData = rawData.map(item => item.exportQuantity);
                const ctx = document.getElementById('overviewChart');
                if (ctx) {
                    new Chart(ctx, {
                        type: 'bar',
                        data: {
                            labels: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                            datasets: [
                                { label: 'Import', data: importData, backgroundColor: 'rgba(59, 130, 246, 0.8)', borderColor: 'rgba(59, 130, 246, 1)', borderWidth: 1 },
                                { label: 'Export', data: exportData, backgroundColor: 'rgba(239, 68, 68, 0.8)', borderColor: 'rgba(239, 68, 68, 1)', borderWidth: 1 }
                            ]
                        },
                        options: {
                            responsive: true, maintainAspectRatio: false,
                            scales: { y: { beginAtZero: true, ticks: {precision: 0} } },
                            plugins: { legend: {position: 'top'}, title: { display: true, text: 'Annual Overview - ${year}' } }
                        }
                    });
                }
            } catch (e) {
                console.error('Error parsing chartSummaryData:', e);
            }
        }

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
                                label: 'Imported Quantity', data: trendData,
                                backgroundColor: 'rgba(59, 130, 246, 0.2)', borderColor: 'rgba(59, 130, 246, 1)',
                                borderWidth: 2, fill: true, tension: 0.3, pointBackgroundColor: 'rgba(59, 130, 246, 1)', pointRadius: 4
                            }]
                        },
                        options: {
                            responsive: true, maintainAspectRatio: false,
                            scales: { y: { beginAtZero: true, ticks: {precision: 0} } },
                            plugins: { legend: {position: 'top'} }
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
