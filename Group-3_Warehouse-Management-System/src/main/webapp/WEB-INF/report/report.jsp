<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .main-content {
            padding: 24px;
            max-width: 1280px;
            margin: auto;
        }

        .report-type {
            display: inline-flex;
            align-items: center;
            justify-content: center;
            min-width: 120px;
            padding: 10px 16px;
            border: 1px solid #d8deea;
            border-radius: 8px;
            text-decoration: none;
            color: #334155;
            font-weight: 600;
            background: #f8fafc;
            transition: all .2s ease;
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

        .panel {
            background-color: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0,0,0,0.1);
            margin-bottom: 20px;
        }

        .panel-title {
            font-size: 18px;
            font-weight: 600;
            margin-bottom: 15px;
            color: #1e293b;
        }

        .filter-grid {
            display: flex;
            gap: 12px;
            flex-wrap: wrap;
            align-items: flex-end;
        }

        .filter-grid > div {
            display: flex;
            flex-direction: column;
            gap: 5px;
        }

        .table-wrap {
            overflow-x: auto;
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
            <c:otherwise>Report</c:otherwise>
        </c:choose>
    </h2>

    <!-- 1. YEAR SEARCH -->
    <div class="panel">
        <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
            <div>
                <label class="small font-weight-bold text-muted">Year</label>
                <input type="number" name="year" class="form-control" style="min-width: 120px;"
                       placeholder="Input year" value="${year}">
            </div>
            <button type="submit" class="btn btn-primary">Search</button>
        </form>
    </div>

    <!-- 2. OVERALL COMPARE CHART (Year Overview) -->
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
            <a href="?type=import&year=${year}"
               class="report-type ${requestScope.type eq 'import' ? 'active' : ''}">
                Import
            </a>
            <a href="?type=export&year=${year}"
               class="report-type ${requestScope.type eq 'export' ? 'active' : ''}">
                Export
            </a>
            <a href="?type=inventory&year=${year}"
               class="report-type ${requestScope.type eq 'inventory' ? 'active' : ''}">
                Inventory
            </a>
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

    <!-- IMPORT REPORT -->
    <c:if test="${type eq 'import'}">
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
                                    <td>${item.productName}</td>
                                    <td class="text-center">${item.quantity}</td>
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
    </c:if>

    <!-- EXPORT REPORT -->
    <c:if test="${type eq 'export'}">
        <!-- 4. TREND CHART -->
        <div class="panel">
            <h3 class="panel-title">Quarter Export Trend (Q${quarter}/${year})</h3>
            <div style="position: relative; height: 300px;">
                <canvas id="trendChart"></canvas>
            </div>
        </div>

        <!-- 5. QUARTER & FILTER -->
        <div class="panel">
            <h3 class="panel-title">Export Filters</h3>
            <form method="get" action="${pageContext.request.contextPath}/report" class="filter-grid">
                <input type="hidden" name="type" value="export"/>
                <input type="hidden" name="year" value="${year}"/>
                <div>
                    <label class="small font-weight-bold text-muted">Quarter</label>
                    <select name="quarter" class="form-control">
                        <option value="1" ${quarter == 1 ? 'selected' : ''}>Q1 (Jan - Mar)</option>
                        <option value="2" ${quarter == 2 ? 'selected' : ''}>Q2 (Apr - Jun)</option>
                        <option value="3" ${quarter == 3 ? 'selected' : ''}>Q3 (Jul - Sep)</option>
                        <option value="4" ${quarter == 4 ? 'selected' : ''}>Q4 (Oct - Dec)</option>
                    </select>
                </div>
                <div>
                    <label class="small font-weight-bold text-muted">Order Code</label>
                    <input type="text" name="searchCode" class="form-control"
                           placeholder="Search order code" value="${searchCode}">
                </div>
                <button type="submit" class="btn btn-primary">Apply</button>
                <a href="${pageContext.request.contextPath}/report?type=export&year=${year}"
                   class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <!-- 6. TABLE -->
        <div class="panel">
            <h3 class="panel-title">
                Export History - Q${quarter}/${year}
                <small class="text-muted">(${fromDate} to ${toDate})</small>
            </h3>
            <div class="table-wrap">
                <table class="table table-bordered table-hover mb-0">
                    <thead>
                    <tr>
                        <th style="width: 70px;">No.</th>
                        <th>Order Code</th>
                        <th>Customer</th>
                        <th style="min-width: 170px;">Processed Date</th>
                        <th>Warehouse Staff</th>
                        <th style="width: 110px;" class="text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty orders}">
                            <c:forEach items="${orders}" var="order" varStatus="loop">
                                <tr>
                                    <td class="text-center">${(pageNo - 1) * 10 + loop.index + 1}</td>
                                    <td class="font-weight-bold">${order.orderCode}</td>
                                    <td>${order.customerName}</td>
                                    <td>
                                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${order.processedAt}"/>
                                    </td>
                                    <td>${order.processedBy != null ? order.processedBy.fullName : '-'}</td>
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/inventory/export-history/detail?orderId=${order.id}"
                                           class="btn btn-sm btn-info">View</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="6" class="text-center text-muted py-4">No export history found for Q${quarter}/${year}.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>

            <!-- PAGINATION -->
            <c:if test="${totalPages > 1}">
                <nav class="mt-3">
                    <ul class="pagination justify-content-center mb-0">
                        <li class="page-item ${pageNo <= 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/report?type=export&year=${year}&quarter=${quarter}&searchCode=${searchCode}&pageNo=${pageNo - 1}">Previous</a>
                        </li>

                        <c:forEach begin="1" end="${totalPages}" var="i">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/report?type=export&year=${year}&quarter=${quarter}&searchCode=${searchCode}&pageNo=${i}">${i}</a>
                            </li>
                        </c:forEach>

                        <li class="page-item ${pageNo >= totalPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/report?type=export&year=${year}&quarter=${quarter}&searchCode=${searchCode}&pageNo=${pageNo + 1}">Next</a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </c:if>

    <!-- INVENTORY REPORT -->
    <c:if test="${type eq 'inventory'}">
        <!-- 4. TREND CHART -->
        <c:if test="${not empty chartData}">
            <div class="panel">
                <h3 class="panel-title">Inventory Trend by Month (${year})</h3>
                <div style="position: relative; height: 300px;">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>
        </c:if>

        <!-- 5. MONTH FILTER -->
        <div class="panel">
            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="inventory"/>
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
            <h3 class="panel-title">Inventory Details - ${month}/${year}</h3>
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
                                    <td>${item.productName}</td>
                                    <td class="text-center">${item.quantity}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" class="text-center text-muted py-4">No inventory data found for ${month}/${year}.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>
    </c:if>

</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const reportType = '${type}';
        const chartSummaryRaw = '${chartSummaryData}';
        const trendChartRaw = '${chartData}';
        const quarterTrendRaw = '${quarterTrendData}';

        // 1. Overall Compare Chart (All three report types)
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
                                {
                                    label: 'Import',
                                    data: importData,
                                    backgroundColor: 'rgba(59, 130, 246, 0.8)',
                                    borderColor: 'rgba(59, 130, 246, 1)',
                                    borderWidth: 1
                                },
                                {
                                    label: 'Export',
                                    data: exportData,
                                    backgroundColor: 'rgba(239, 68, 68, 0.8)',
                                    borderColor: 'rgba(239, 68, 68, 1)',
                                    borderWidth: 1
                                }
                            ]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {precision: 0}
                                }
                            },
                            plugins: {
                                legend: {position: 'top'},
                                title: {
                                    display: true,
                                    text: 'Annual Overview - ${year}'
                                }
                            }
                        }
                    });
                }
            } catch (e) {
                console.error('Error parsing chartSummaryData:', e);
            }
        }

        // 2. Trend Charts for Import & Inventory
        if ((reportType === 'import' || reportType === 'inventory') && trendChartRaw) {
            try {
                const trendData = JSON.parse(trendChartRaw);
                const trendCanvas = document.getElementById('trendChart');
                if (trendCanvas) {
                    new Chart(trendCanvas, {
                        type: 'line',
                        data: {
                            labels: ['January', 'February', 'March', 'April', 'May', 'June',
                                'July', 'August', 'September', 'October', 'November', 'December'],
                            datasets: [{
                                label: reportType === 'inventory' ? 'Inventory Quantity' : 'Imported Quantity',
                                data: trendData,
                                backgroundColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 0.2)' 
                                    : 'rgba(59, 130, 246, 0.2)',
                                borderColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 1)' 
                                    : 'rgba(59, 130, 246, 1)',
                                borderWidth: 2,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 1)' 
                                    : 'rgba(59, 130, 246, 1)',
                                pointRadius: 4
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {precision: 0}
                                }
                            },
                            plugins: {
                                legend: {position: 'top'}
                            }
                        }
                    });
                }
            } catch (e) {
                console.error('Error parsing trendChartRaw:', e);
            }
        }

        // 3. Trend Chart for Export
        if (reportType === 'export' && quarterTrendRaw) {
            try {
                const quarterTrend = JSON.parse(quarterTrendRaw);
                const trendCanvas = document.getElementById('trendChart');
                if (trendCanvas) {
                    new Chart(trendCanvas, {
                        type: 'line',
                        data: {
                            labels: quarterTrend.labels || [],
                            datasets: [{
                                label: 'Exported Items',
                                data: quarterTrend.data || [],
                                borderColor: '#2563eb',
                                backgroundColor: 'rgba(37, 99, 235, 0.18)',
                                pointBackgroundColor: '#1d4ed8',
                                pointRadius: 4,
                                borderWidth: 2,
                                fill: true,
                                tension: 0.32
                            }]
                        },
                        options: {
                            responsive: true,
                            maintainAspectRatio: false,
                            plugins: {
                                legend: {position: 'top'}
                            },
                            scales: {
                                y: {
                                    beginAtZero: true,
                                    ticks: {precision: 0}
                                }
                            }
                        }
                    });
                }
            } catch (e) {
                console.error('Error parsing quarterTrendRaw:', e);
            }
        }
    });
</script>

</body>

</html>