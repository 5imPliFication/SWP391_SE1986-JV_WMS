<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Import Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        :root {
            --report-bg: linear-gradient(180deg, #f3f8ff 0%, #f8fbff 48%, #ffffff 100%);
            --panel-border: #dbe7f5;
            --panel-shadow: 0 18px 36px rgba(15, 23, 42, 0.08);
            --text-main: #12304a;
            --text-soft: #5f7388;
            --accent: #2563eb;
            --accent-soft: rgba(37, 99, 235, 0.12);
            --success-soft: rgba(34, 197, 94, 0.12);
        }

        body {
            background: var(--report-bg);
        }

        .main-content {
            padding: 24px;
            max-width: 1280px;
            margin: auto;
        }

        .report-shell {
            display: flex;
            flex-direction: column;
            gap: 20px;
        }

        .hero-panel,
        .panel {
            background: rgba(255, 255, 255, 0.96);
            border: 1px solid var(--panel-border);
            border-radius: 24px;
            box-shadow: var(--panel-shadow);
        }

        .hero-panel {
            padding: 28px;
            background:
                radial-gradient(circle at top right, rgba(37, 99, 235, 0.16), transparent 30%),
                linear-gradient(135deg, #ffffff 0%, #eef6ff 100%);
        }

        .panel {
            padding: 24px;
        }

        .eyebrow {
            display: inline-flex;
            align-items: center;
            gap: 8px;
            padding: 6px 12px;
            border-radius: 999px;
            background: var(--accent-soft);
            color: var(--accent);
            font-size: 12px;
            font-weight: 700;
            letter-spacing: 0.06em;
            text-transform: uppercase;
        }

        .hero-title {
            margin: 16px 0 10px;
            color: var(--text-main);
            font-size: 34px;
            font-weight: 700;
        }

        .hero-subtitle,
        .section-copy {
            color: var(--text-soft);
            margin-bottom: 0;
        }

        .context-chip {
            padding: 14px 18px;
            border-radius: 18px;
            background: rgba(255, 255, 255, 0.78);
            border: 1px solid rgba(37, 99, 235, 0.16);
            min-width: 220px;
        }

        .context-chip .label {
            display: block;
            font-size: 12px;
            font-weight: 700;
            color: var(--text-soft);
            text-transform: uppercase;
            letter-spacing: 0.06em;
            margin-bottom: 6px;
        }

        .context-chip .value {
            color: var(--text-main);
            font-size: 16px;
            font-weight: 700;
        }

        .panel-header {
            display: flex;
            justify-content: space-between;
            align-items: flex-start;
            flex-wrap: wrap;
            gap: 16px;
            margin-bottom: 18px;
        }

        .panel-title {
            margin: 0 0 6px;
            font-size: 22px;
            font-weight: 700;
            color: var(--text-main);
        }

        .report-type-group {
            display: flex;
            flex-wrap: wrap;
            gap: 10px;
        }

        .report-type {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 126px;
            padding: 12px 18px;
            border: 1px solid #d6e3f3;
            border-radius: 14px;
            text-decoration: none;
            color: var(--text-soft);
            font-weight: 700;
            background: #f8fbff;
            transition: all 0.2s ease;
        }

        .report-type:hover {
            text-decoration: none;
            color: var(--accent);
            border-color: #b8cff2;
            transform: translateY(-1px);
        }

        .report-type.active {
            background: linear-gradient(135deg, #2563eb, #3b82f6);
            border-color: #2563eb;
            color: #fff;
            box-shadow: 0 14px 24px rgba(37, 99, 235, 0.24);
        }

        .filter-grid {
            display: flex;
            flex-wrap: wrap;
            align-items: flex-end;
            gap: 14px;
        }

        .filter-grid > div {
            display: flex;
            flex-direction: column;
            gap: 6px;
        }

        .filter-grid label {
            font-size: 12px;
            font-weight: 700;
            letter-spacing: 0.05em;
            color: var(--text-soft);
            text-transform: uppercase;
        }

        .filter-grid .form-control {
            min-width: 140px;
            border-radius: 12px;
            border-color: #cbd9ea;
            min-height: 44px;
        }

        .btn {
            border-radius: 12px;
            min-height: 44px;
            font-weight: 700;
            padding: 0 18px;
        }

        .stats-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(190px, 1fr));
            gap: 16px;
        }

        .stat-card {
            border-radius: 18px;
            padding: 18px 20px;
            border: 1px solid #dce7f4;
            background: linear-gradient(180deg, #ffffff 0%, #f8fbff 100%);
            min-height: 120px;
        }

        .stat-card .label {
            font-size: 12px;
            font-weight: 700;
            color: var(--text-soft);
            text-transform: uppercase;
            letter-spacing: 0.05em;
            margin-bottom: 8px;
        }

        .stat-card .value {
            font-size: 28px;
            line-height: 1.1;
            font-weight: 700;
            color: var(--text-main);
            margin-bottom: 6px;
        }

        .stat-card .meta {
            color: var(--text-soft);
            font-size: 13px;
        }

        .stat-card.accent {
            background: linear-gradient(135deg, rgba(37, 99, 235, 0.10), rgba(59, 130, 246, 0.02));
            border-color: rgba(37, 99, 235, 0.24);
        }

        .stat-card.success {
            background: linear-gradient(135deg, rgba(34, 197, 94, 0.10), rgba(255, 255, 255, 0.9));
            border-color: rgba(34, 197, 94, 0.18);
        }

        .chart-shell {
            position: relative;
            height: 320px;
            padding: 14px;
            border-radius: 20px;
            background: linear-gradient(180deg, #f8fbff 0%, #ffffff 100%);
            border: 1px solid #e3edf8;
        }

        .table-shell {
            border: 1px solid #dce7f4;
            border-radius: 20px;
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
            background: #d8ebfb;
            color: var(--text-main);
            border-bottom: 0;
            font-weight: 700;
        }

        .table tbody td {
            vertical-align: middle;
            border-color: #ebf1f7;
        }

        .quantity-link {
            font-weight: 700;
            color: var(--accent);
            text-decoration: none;
        }

        .quantity-link:hover {
            color: #1d4ed8;
            text-decoration: none;
        }

        .empty-state {
            padding: 52px 16px;
            text-align: center;
            color: var(--text-soft);
        }

        @media (max-width: 767px) {
            .main-content {
                padding: 16px;
            }

            .hero-panel,
            .panel {
                padding: 18px;
                border-radius: 20px;
            }

            .hero-title {
                font-size: 28px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <div class="report-shell">
        <c:set var="totalQuantity" value="0"/>
        <c:forEach items="${reportItems}" var="item">
            <c:set var="totalQuantity" value="${totalQuantity + item.quantity}"/>
        </c:forEach>

        <div class="hero-panel">
            <div class="d-flex justify-content-between align-items-start flex-wrap" style="gap: 18px;">
                <div>
                    <span class="eyebrow">Warehouse analytics</span>
                    <h1 class="hero-title">Import Report</h1>
                    <p class="hero-subtitle">Review monthly inbound volume with the same clean summary-first flow used in the inventory dashboard.</p>
                </div>
                <div class="context-chip">
                    <span class="label">Current focus</span>
                    <span class="value">
                        <c:choose>
                            <c:when test="${not empty month}">${month}/${year}</c:when>
                            <c:otherwise>Year ${year}</c:otherwise>
                        </c:choose>
                    </span>
                </div>
            </div>
        </div>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Select Report Type</h2>
                    <p class="section-copy">Switch between inventory, import, and export views without losing the same layout rhythm.</p>
                </div>
                <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                    <input type="hidden" name="type" value="import"/>
                    <div>
                        <label>Year</label>
                        <input type="number" name="year" class="form-control" placeholder="Input year" value="${year}">
                    </div>
                    <button type="submit" class="btn btn-primary">Apply Year</button>
                </form>
            </div>

            <div class="report-type-group">
                <a href="?type=inventory&year=${year}" class="report-type">Inventory</a>
                <a href="?type=import&year=${year}" class="report-type active">Import</a>
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
            <c:remove var="message" scope="session"/>
            <c:remove var="messageType" scope="session"/>
        </c:if>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Import Snapshot</h2>
                    <p class="section-copy">A quick read on product coverage and inbound quantity before diving into the detail table.</p>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card accent">
                    <div class="label">Selected Year</div>
                    <div class="value">${year}</div>
                    <div class="meta">Annual context for the report</div>
                </div>
                <div class="stat-card success">
                    <div class="label">Total Imported Qty</div>
                    <div class="value"><fmt:formatNumber value="${totalQuantity}" type="number"/></div>
                    <div class="meta">Sum of products in the current result set</div>
                </div>
                <div class="stat-card">
                    <div class="label">Products Listed</div>
                    <div class="value">${fn:length(reportItems)}</div>
                    <div class="meta">Distinct rows shown for this period</div>
                </div>
                <div class="stat-card">
                    <div class="label">Selected Month</div>
                    <div class="value">
                        <c:choose>
                            <c:when test="${not empty month}">${month}</c:when>
                            <c:otherwise>All</c:otherwise>
                        </c:choose>
                    </div>
                    <div class="meta">Filter scope inside the selected year</div>
                </div>
            </div>
        </div>

        <c:if test="${not empty chartSummaryData}">
            <div class="panel">
                <div class="panel-header">
                    <div>
                        <h2 class="panel-title">Annual Overview</h2>
                        <p class="section-copy">Compare inbound and outbound movement across the full year before checking the monthly trend.</p>
                    </div>
                </div>
                <div class="chart-shell">
                    <canvas id="overviewChart"></canvas>
                </div>
            </div>
        </c:if>

        <c:if test="${not empty chartData}">
            <div class="panel">
                <div class="panel-header">
                    <div>
                        <h2 class="panel-title">Import Trend by Month (${year})</h2>
                        <p class="section-copy">The line chart keeps the same visual tone as the inventory view while focusing on inbound volume only.</p>
                    </div>
                </div>
                <div class="chart-shell">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>
        </c:if>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Import Detail Filters</h2>
                    <p class="section-copy">Refine the current year by month to inspect the exact inbound quantity per product.</p>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="import"/>
                <input type="hidden" name="year" value="${year}"/>
                <div>
                    <label>Month</label>
                    <input type="number" name="month" class="form-control" placeholder="Input month" value="${month}" min="1" max="12">
                </div>
                <button type="submit" class="btn btn-primary">Apply Filter</button>
                <a href="${pageContext.request.contextPath}/report?type=import&year=${year}" class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Import Details</h2>
                    <p class="section-copy">
                        <c:choose>
                            <c:when test="${not empty month}">Inbound quantity for each product in ${month}/${year}.</c:when>
                            <c:otherwise>Inbound quantity for each product in year ${year}.</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="table-shell">
                <div class="table-wrap">
                    <table class="table table-hover mb-0">
                        <thead>
                        <tr>
                            <th style="width: 80px;">No.</th>
                            <th>Product</th>
                            <th style="width: 180px;" class="text-center">Quantity</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty reportItems}">
                                <c:forEach items="${reportItems}" var="item" varStatus="loop">
                                    <tr>
                                        <td class="text-center">${loop.index + 1}</td>
                                        <td class="font-weight-bold">${item.productName}</td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/stock-history?year=${year}&month=${month}&productName=${item.productName}&type=IMPORT" class="quantity-link">
                                                <fmt:formatNumber value="${item.quantity}" type="number"/>
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="3" class="empty-state">No import data found for the selected period.</td>
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
                                { label: 'Import', data: importData, backgroundColor: 'rgba(59, 130, 246, 0.8)', borderColor: 'rgba(59, 130, 246, 1)', borderWidth: 1, borderRadius: 8 },
                                { label: 'Export', data: exportData, backgroundColor: 'rgba(239, 68, 68, 0.8)', borderColor: 'rgba(239, 68, 68, 1)', borderWidth: 1, borderRadius: 8 }
                            ]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: { beginAtZero: true, ticks: { precision: 0 } },
                                x: { grid: { display: false } }
                            },
                            plugins: {
                                legend: { position: 'top' },
                                title: { display: true, text: 'Annual Overview - ${year}' }
                            }
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
                                label: 'Imported Quantity',
                                data: trendData,
                                backgroundColor: 'rgba(59, 130, 246, 0.18)',
                                borderColor: 'rgba(37, 99, 235, 1)',
                                borderWidth: 3,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: 'rgba(37, 99, 235, 1)',
                                pointRadius: 4
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: { beginAtZero: true, ticks: { precision: 0 } },
                                x: { grid: { display: false } }
                            },
                            plugins: { legend: { position: 'top' } }
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
