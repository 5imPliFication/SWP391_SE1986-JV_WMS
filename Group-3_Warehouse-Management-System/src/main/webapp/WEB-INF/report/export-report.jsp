<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Export Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        :root {
            --report-bg: linear-gradient(180deg, #f7faff 0%, #fbfdff 48%, #ffffff 100%);
            --panel-border: #dbe7f5;
            --panel-shadow: 0 18px 36px rgba(15, 23, 42, 0.08);
            --text-main: #12304a;
            --text-soft: #5f7388;
            --accent: #2563eb;
            --danger: #dc2626;
            --accent-soft: rgba(37, 99, 235, 0.12);
            --danger-soft: rgba(220, 38, 38, 0.10);
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
                radial-gradient(circle at top right, rgba(220, 38, 38, 0.12), transparent 28%),
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
            background: var(--danger-soft);
            color: var(--danger);
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
            background: rgba(255, 255, 255, 0.8);
            border: 1px solid rgba(220, 38, 38, 0.14);
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
            background: linear-gradient(135deg, #dc2626, #ef4444);
            border-color: #dc2626;
            color: #fff;
            box-shadow: 0 14px 24px rgba(220, 38, 38, 0.22);
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
            background: linear-gradient(135deg, rgba(220, 38, 38, 0.10), rgba(255, 255, 255, 0.92));
            border-color: rgba(220, 38, 38, 0.18);
        }

        .stat-card.info {
            background: linear-gradient(135deg, rgba(37, 99, 235, 0.10), rgba(255, 255, 255, 0.92));
            border-color: rgba(37, 99, 235, 0.18);
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

        .action-link {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 110px;
            padding: 8px 14px;
            border-radius: 10px;
            border: 1px solid rgba(37, 99, 235, 0.24);
            color: var(--accent);
            font-weight: 700;
            text-decoration: none;
            background: #f8fbff;
        }

        .action-link:hover {
            text-decoration: none;
            color: #1d4ed8;
            background: #edf4ff;
        }

        .pagination .page-link {
            border-radius: 10px;
            margin: 0 4px;
            border-color: #d6e3f3;
            color: var(--text-main);
        }

        .pagination .page-item.active .page-link {
            background-color: var(--accent);
            border-color: var(--accent);
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
                    <h1 class="hero-title">Export Report</h1>
                    <p class="hero-subtitle">Match the inventory dashboard vibe while keeping export actions and order drill-downs clear and connected.</p>
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
                    <p class="section-copy">Use the same navigation pattern as inventory so switching reports feels seamless.</p>
                </div>
                <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                    <input type="hidden" name="type" value="export"/>
                    <div>
                        <label>Year</label>
                        <input type="number" name="year" class="form-control" placeholder="Input year" value="${year}">
                    </div>
                    <button type="submit" class="btn btn-primary">Apply Year</button>
                </form>
            </div>

            <div class="report-type-group">
                <a href="?type=inventory&year=${year}" class="report-type">Inventory</a>
                <a href="?type=import&year=${year}" class="report-type">Import</a>
                <a href="?type=export&year=${year}" class="report-type active">Export</a>
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
                    <h2 class="panel-title">Export Snapshot</h2>
                    <p class="section-copy">Start with the same summary-first glance: period, volume, and product coverage before reviewing orders.</p>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card accent">
                    <div class="label">Selected Year</div>
                    <div class="value">${year}</div>
                    <div class="meta">Annual context for outgoing goods</div>
                </div>
                <div class="stat-card info">
                    <div class="label">Total Exported Qty</div>
                    <div class="value"><fmt:formatNumber value="${totalQuantity}" type="number"/></div>
                    <div class="meta">Sum of products in the current result set</div>
                </div>
                <div class="stat-card">
                    <div class="label">Products Listed</div>
                    <div class="value">${fn:length(reportItems)}</div>
                    <div class="meta">Distinct export rows for the selected period</div>
                </div>
                <div class="stat-card">
                    <div class="label">Tracked Orders</div>
                    <div class="value">${fn:length(productOrders)}</div>
                    <div class="meta">Orders shown when a product is selected</div>
                </div>
            </div>
        </div>

        <c:if test="${not empty chartSummaryData}">
            <div class="panel">
                <div class="panel-header">
                    <div>
                        <h2 class="panel-title">Annual Overview</h2>
                        <p class="section-copy">Keep import/export comparison visible so the export page still feels tied to the overall warehouse story.</p>
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
                        <h2 class="panel-title">Export Trend by Month (${year})</h2>
                        <p class="section-copy">Monthly outbound volume stays in the same chart frame as the inventory page, just with an export color tone.</p>
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
                    <h2 class="panel-title">Export Detail Filters</h2>
                    <p class="section-copy">Refine by month, then drill into the products that generated outbound orders.</p>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="export"/>
                <input type="hidden" name="year" value="${year}"/>
                <div>
                    <label>Month</label>
                    <input type="number" name="month" class="form-control" placeholder="Input month" value="${month}" min="1" max="12">
                </div>
                <button type="submit" class="btn btn-primary">Apply Filter</button>
                <a href="${pageContext.request.contextPath}/report?type=export&year=${year}" class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Export Details</h2>
                    <p class="section-copy">
                        <c:choose>
                            <c:when test="${not empty month}">Outbound quantity by product for ${month}/${year}.</c:when>
                            <c:otherwise>Outbound quantity by product for year ${year}.</c:otherwise>
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
                            <th style="width: 160px;" class="text-center">Quantity</th>
                            <th style="width: 180px;" class="text-center">Orders</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:choose>
                            <c:when test="${not empty reportItems}">
                                <c:forEach items="${reportItems}" var="item" varStatus="loop">
                                    <tr>
                                        <td class="text-center">${loop.index + 1}</td>
                                        <td class="font-weight-bold">${item.productName}</td>
                                        <td class="text-center"><fmt:formatNumber value="${item.quantity}" type="number"/></td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${item.productId}&productPage=1" class="action-link">
                                                View Orders
                                            </a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="4" class="empty-state">No export data found for the selected period.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <c:if test="${not empty selectedProductId}">
            <div class="panel">
                <div class="panel-header">
                    <div>
                        <h2 class="panel-title">Orders Containing ${selectedProductName}</h2>
                        <p class="section-copy">This drill-down keeps the same card and table treatment so the order detail still feels like part of the same page.</p>
                    </div>
                </div>

                <div class="table-shell">
                    <div class="table-wrap">
                        <table class="table table-hover mb-0">
                            <thead>
                            <tr>
                                <th style="width: 80px;">No.</th>
                                <th>Order Code</th>
                                <th>Customer</th>
                                <th style="min-width: 170px;">Processed Date</th>
                                <th>Warehouse Staff</th>
                                <th style="width: 140px;" class="text-center">Product Qty</th>
                                <th style="width: 110px;" class="text-center">Detail</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:choose>
                                <c:when test="${not empty productOrders}">
                                    <c:forEach items="${productOrders}" var="po" varStatus="loop">
                                        <tr>
                                            <td class="text-center">${(productPageNo - 1) * productOrderPageSize + loop.index + 1}</td>
                                            <td class="font-weight-bold">${po.orderCode}</td>
                                            <td>${po.customerName}</td>
                                            <td><fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${po.processedAt}"/></td>
                                            <td>${po.processedByName != null ? po.processedByName : '-'}</td>
                                            <td class="text-center"><fmt:formatNumber value="${po.quantity}" type="number"/></td>
                                            <td class="text-center">
                                                <a href="${pageContext.request.contextPath}/inventory/export-history/detail?orderId=${po.orderId}" class="action-link">
                                                    View
                                                </a>
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </c:when>
                                <c:otherwise>
                                    <tr>
                                        <td colspan="7" class="empty-state">No orders contain this product in the selected period.</td>
                                    </tr>
                                </c:otherwise>
                            </c:choose>
                            </tbody>
                        </table>
                    </div>
                </div>

                <c:if test="${totalProductOrderPages > 1}">
                    <nav class="mt-4">
                        <ul class="pagination justify-content-center mb-0">
                            <li class="page-item ${productPageNo <= 1 ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${productPageNo - 1}">Previous</a>
                            </li>

                            <c:forEach begin="1" end="${totalProductOrderPages}" var="i">
                                <li class="page-item ${i == productPageNo ? 'active' : ''}">
                                    <a class="page-link" href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <li class="page-item ${productPageNo >= totalProductOrderPages ? 'disabled' : ''}">
                                <a class="page-link" href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${productPageNo + 1}">Next</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </c:if>
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
                                label: 'Exported Quantity',
                                data: trendData,
                                backgroundColor: 'rgba(239, 68, 68, 0.16)',
                                borderColor: 'rgba(220, 38, 38, 1)',
                                borderWidth: 3,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: 'rgba(220, 38, 38, 1)',
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
