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
        .main-content {
            padding: 24px;
            max-width: 1280px;
            margin: auto;
        }

        .report-toolbar {
            display: flex;
            justify-content: space-between;
            align-items: center;
            gap: 12px;
            flex-wrap: wrap;
            margin-bottom: 16px;
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
            color: #fff;
            background: #0093ff;
            border-color: #006fff;
        }

        .report-type:hover {
            text-decoration: none;
            color: #0f172a;
            border-color: #94a3b8;
        }

        .panel {
            background: #ffffff;
            border: 1px solid #e2e8f0;
            border-radius: 12px;
            box-shadow: 0 10px 20px rgba(2, 6, 23, 0.04);
            padding: 16px;
            margin-bottom: 16px;
        }

        .panel-title {
            margin: 0 0 12px;
            font-size: 1rem;
            font-weight: 700;
            color: #0f172a;
        }

        .filter-grid {
            display: grid;
            grid-template-columns: repeat(4, minmax(160px, 1fr));
            gap: 12px;
            align-items: end;
        }

        .chart-wrap {
            height: 340px;
        }

        .chart-wrap canvas {
            width: 100% !important;
            height: 100% !important;
        }

        .sub-chart {
            height: 280px;
        }

        .table-wrap {
            overflow-x: auto;
        }

        .table thead th {
            background: #f1f5f9;
            color: #0f172a;
            border-top: none;
            font-weight: 700;
            white-space: nowrap;
        }

        @media (max-width: 992px) {
            .filter-grid {
                grid-template-columns: repeat(2, minmax(140px, 1fr));
            }
        }

        @media (max-width: 576px) {
            .main-content {
                padding: 12px;
            }

            .filter-grid {
                grid-template-columns: 1fr;
            }

            .chart-wrap {
                height: 280px;
            }
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <div class="report-toolbar">
        <h2 class="mb-0">
            <c:choose>
                <c:when test="${type eq 'export'}">Export Detail Report by Quarter</c:when>
                <c:otherwise>Import Report</c:otherwise>
            </c:choose>
        </h2>

        <div class="d-flex flex-wrap" style="gap: 8px;">
            <a href="${pageContext.request.contextPath}/report?type=export&year=${not empty year ? year : param.year}&quarter=${not empty quarter ? quarter : 1}"
               class="report-type ${type eq 'export' ? 'active' : ''}">Export</a>
            <a href="${pageContext.request.contextPath}/report?type=import&year=${not empty year ? year : param.year}"
               class="report-type ${type eq 'import' ? 'active' : ''}">Import</a>
        </div>
    </div>

    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <c:choose>
        <c:when test="${type eq 'export'}">
            <div class="panel">
                <h3 class="panel-title">Report Filters</h3>
                <form method="get" action="${pageContext.request.contextPath}/report" class="filter-grid">
                    <input type="hidden" name="type" value="export"/>

                    <div>
                        <label class="small font-weight-bold text-muted">Year</label>
                        <input type="number" name="year" class="form-control"
                               min="2000" max="2100" value="${year}">
                    </div>

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

                    <div class="d-flex" style="gap: 8px;">
                        <button type="submit" class="btn btn-primary">Apply</button>
                        <a href="${pageContext.request.contextPath}/report?type=export" class="btn btn-outline-secondary">Reset</a>
                    </div>
                </form>
            </div>

            <div class="panel">
                <h3 class="panel-title">Annual Exporting Overview (12 months)</h3>
                <div class="chart-wrap">
                    <canvas id="annualOverviewChart"></canvas>
                </div>
            </div>

            <div class="panel">
                <h3 class="panel-title">Quarter Export Trend (Q${quarter})</h3>
                <div class="sub-chart">
                    <canvas id="quarterTrendChart"></canvas>
                </div>
            </div>

            <div class="panel">
                <h3 class="panel-title">
                    Export History in Q${quarter}/${year}
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
                            <th style="width: 110px;">Detail</th>
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
                                    <td colspan="6" class="text-center text-muted py-4">No export history found for this quarter.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

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
        </c:when>

        <c:otherwise>
            <div class="panel">
                <h3 class="panel-title">Import Filter</h3>
                <form action="${pageContext.request.contextPath}/report" method="get" class="d-flex flex-wrap" style="gap: 8px;">
                    <input type="hidden" name="type" value="import"/>
                    <input type="number" name="year" class="form-control" style="max-width: 180px;"
                           placeholder="Input year" value="${param.year}">
                    <input type="number" name="month" class="form-control" style="max-width: 180px;"
                           placeholder="Input month" value="${param.month}">
                    <button type="submit" class="btn btn-primary">Search</button>
                </form>
            </div>

            <div class="panel">
                <h3 class="panel-title">Import Trend by Month</h3>
                <div class="sub-chart">
                    <canvas id="importChart"></canvas>
                </div>
            </div>

            <div class="panel">
                <h3 class="panel-title">Import Detail</h3>
                <div class="table-wrap">
                    <table class="table table-bordered table-hover mb-0">
                        <thead>
                        <tr>
                            <th style="width: 80px;">No.</th>
                            <th>Product</th>
                            <th style="width: 150px;">Quantity</th>
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
                                    <td colspan="3" class="text-center text-muted py-4">No data available.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:otherwise>
    </c:choose>
</main>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const reportType = '${type}';
        const annualChartRaw = '${annualChartData}';
        const quarterTrendRaw = '${quarterTrendData}';
        const importChartRaw = '${chartData}';

        if (reportType === 'export' && annualChartRaw && quarterTrendRaw) {
            const annualData = JSON.parse(annualChartRaw);
            const quarterTrend = JSON.parse(quarterTrendRaw);

            const annualCanvas = document.getElementById('annualOverviewChart');
            if (annualCanvas) {
                const annualCtx = annualCanvas.getContext('2d');
                new Chart(annualCtx, {
                    type: 'bar',
                    data: {
                        labels: annualData.labels,
                        datasets: [
                            {
                                label: 'In Stock',
                                data: annualData.inStock,
                                backgroundColor: 'rgba(30, 64, 175, 0.75)',
                                borderColor: 'rgba(30, 64, 175, 1)',
                                borderWidth: 1
                            },
                            {
                                label: 'Exported',
                                data: annualData.exported,
                                backgroundColor: 'rgba(220, 38, 38, 0.75)',
                                borderColor: 'rgba(220, 38, 38, 1)',
                                borderWidth: 1
                            },
                            {
                                label: 'Imported',
                                data: annualData.imported,
                                backgroundColor: 'rgba(5, 150, 105, 0.75)',
                                borderColor: 'rgba(5, 150, 105, 1)',
                                borderWidth: 1
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        interaction: { mode: 'index', intersect: false },
                        plugins: {
                            legend: { position: 'top' }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: { precision: 0 }
                            }
                        }
                    }
                });
            }

            const trendCanvas = document.getElementById('quarterTrendChart');
            if (trendCanvas) {
                const trendCtx = trendCanvas.getContext('2d');
                new Chart(trendCtx, {
                    type: 'line',
                    data: {
                        labels: quarterTrend.labels,
                        datasets: [
                            {
                                label: 'Exported Items',
                                data: quarterTrend.data,
                                borderColor: '#2563eb',
                                backgroundColor: 'rgba(37, 99, 235, 0.18)',
                                pointBackgroundColor: '#1d4ed8',
                                pointRadius: 4,
                                borderWidth: 2,
                                fill: true,
                                tension: 0.32
                            }
                        ]
                    },
                    options: {
                        responsive: true,
                        maintainAspectRatio: false,
                        plugins: {
                            legend: { position: 'top' }
                        },
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: { precision: 0 }
                            }
                        }
                    }
                });
            }
        }

        if (reportType === 'import' && importChartRaw) {
            const importCanvas = document.getElementById('importChart');
            if (importCanvas) {
                const importChartData = JSON.parse(importChartRaw);
                const importCtx = importCanvas.getContext('2d');

                new Chart(importCtx, {
                    type: 'line',
                    data: {
                        labels: ['January', 'February', 'March', 'April', 'May', 'June',
                            'July', 'August', 'September', 'October', 'November', 'December'],
                        datasets: [{
                            label: 'Imported Quantity',
                            data: importChartData,
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
                        maintainAspectRatio: false,
                        scales: {
                            y: {
                                beginAtZero: true,
                                ticks: { precision: 0 }
                            }
                        },
                        plugins: {
                            legend: {
                                position: 'top'
                            }
                        }
                    }
                });
            }
        }
    });
</script>
</body>
</html>
