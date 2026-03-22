<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Report</title>
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

        .inv-summary-head {
            background: #d8ebfb;
            color: #1f2937;
            font-weight: 700;
        }

        .inv-summary-subhead {
            background: #f4f9ff;
            color: #334155;
            font-size: 13px;
            font-weight: 700;
        }

        .inv-summary-total {
            background: #f6f6dc;
            font-weight: 700;
        }

        .inv-sticky-first {
            position: sticky;
            left: 0;
            background: #fff;
            z-index: 2;
        }

        .inv-summary-total .inv-sticky-first {
            background: #f6f6dc;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <div class="panel">
        <div class="d-flex justify-content-between align-items-start flex-wrap" style="gap: 12px;">
            <div>
                <h2 class="mb-2">
                    <c:choose>
                        <c:when test="${type eq 'import'}">Import Report</c:when>
                        <c:when test="${type eq 'export'}">Export Report</c:when>
                        <c:when test="${type eq 'inventory'}">Inventory Report</c:when>
                        <c:otherwise>Report</c:otherwise>
                    </c:choose>
                </h2>
                <p class="text-muted mb-0">Track movement trends and review detailed inventory in-out-balance at a glance.</p>
            </div>
            <div class="text-muted small">
                Primary View: <span class="font-weight-bold">Inventory In-Out-Balance</span>
            </div>
        </div>
    </div>

    <div class="panel">
        <div class="d-flex justify-content-between align-items-end flex-wrap" style="gap: 16px;">
            <div>
                <h3 class="panel-title mb-2">Select Report Type</h3>
                <div class="d-flex gap-2 flex-wrap">
                    <a href="?type=inventory&year=${year}"
                       class="report-type ${requestScope.type eq 'inventory' ? 'active' : ''}">
                        Inventory
                    </a>
                    <a href="?type=import&year=${year}"
                       class="report-type ${requestScope.type eq 'import' ? 'active' : ''}">
                        Import
                    </a>
                    <a href="?type=export&year=${year}"
                       class="report-type ${requestScope.type eq 'export' ? 'active' : ''}">
                        Export
                    </a>
                </div>
            </div>

            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="${empty type ? 'inventory' : type}"/>
                <div>
                    <label class="small font-weight-bold text-muted">Year</label>
                    <input type="number" name="year" class="form-control" style="min-width: 120px;"
                           placeholder="Input year" value="${year}">
                </div>
                <button type="submit" class="btn btn-primary">Apply Year</button>
            </form>
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
            <h3 class="panel-title">Export Trend by Month (${year})</h3>
            <div style="position: relative; height: 300px;">
                <canvas id="trendChart"></canvas>
            </div>
        </div>

        <!-- 5. MONTH FILTER -->
        <div class="panel">
            <h3 class="panel-title">Export Filters</h3>
            <form method="get" action="${pageContext.request.contextPath}/report" class="filter-grid">
                <input type="hidden" name="type" value="export"/>
                <input type="hidden" name="year" value="${year}"/>
                <div>
                    <label class="small font-weight-bold text-muted">Month</label>
                    <input type="number" name="month" class="form-control" style="min-width: 120px;"
                           placeholder="Input month" value="${month}" min="1" max="12">
                </div>
                <button type="submit" class="btn btn-primary">Filter</button>
                <a href="${pageContext.request.contextPath}/report?type=export&year=${year}"
                   class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <!-- 6. TABLE -->
        <div class="panel">
            <h3 class="panel-title">Export Details - ${month}/${year}</h3>
            <div class="table-wrap">
                <table class="table table-bordered table-hover mb-0">
                    <thead>
                    <tr>
                        <th style="width: 80px;">No.</th>
                        <th>Product</th>
                        <th style="width: 150px;" class="text-center">Quantity</th>
                        <th style="width: 180px;" class="text-center">Orders</th>
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
                                    <td class="text-center">
                                        <a href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${item.productId}&productPage=1"
                                           class="btn btn-sm btn-outline-primary">View Orders</a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="4" class="text-center text-muted py-4">No export data found for ${month}/${year}.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <c:if test="${not empty selectedProductId}">
            <div class="panel">
                <h3 class="panel-title">Orders Containing ${selectedProductName} (${month}/${year})</h3>
                <div class="table-wrap">
                    <table class="table table-bordered table-hover mb-0">
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
                                        <td class="text-center">${po.quantity}</td>
                                        <td class="text-center">
                                            <a href="${pageContext.request.contextPath}/inventory/export-history/detail?orderId=${po.orderId}"
                                               class="btn btn-sm btn-info">View</a>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:when>
                            <c:otherwise>
                                <tr>
                                    <td colspan="7" class="text-center text-muted py-4">No orders contain this product in ${month}/${year}.</td>
                                </tr>
                            </c:otherwise>
                        </c:choose>
                        </tbody>
                    </table>
                </div>

                <c:if test="${totalProductOrderPages > 1}">
                    <nav class="mt-3">
                        <ul class="pagination justify-content-center mb-0">
                            <li class="page-item ${productPageNo <= 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${productPageNo - 1}">Previous</a>
                            </li>

                            <c:forEach begin="1" end="${totalProductOrderPages}" var="i">
                                <li class="page-item ${i == productPageNo ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${i}">${i}</a>
                                </li>
                            </c:forEach>

                            <li class="page-item ${productPageNo >= totalProductOrderPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/report?type=export&year=${year}&month=${month}&productId=${selectedProductId}&productPage=${productPageNo + 1}">Next</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </c:if>
    </c:if>

    <!-- INVENTORY REPORT -->
    <c:if test="${type eq 'inventory'}">
        <!-- 4. PRIMARY FILTER -->
        <div class="panel">
            <h3 class="panel-title">Inventory Focus Filters</h3>
            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="inventory"/>
                <input type="hidden" name="year" value="${year}"/>
                <div>
                    <label class="small font-weight-bold text-muted">Month (quick pick)</label>
                    <input type="number" name="month" class="form-control" style="min-width: 120px;"
                           placeholder="Input month" value="${month}" min="1" max="12">
                </div>
                <div>
                    <label class="small font-weight-bold text-muted">From Date</label>
                    <input type="date" name="fromDate" class="form-control" style="min-width: 180px;"
                           value="${fromDate}">
                </div>
                <div>
                    <label class="small font-weight-bold text-muted">To Date</label>
                    <input type="date" name="toDate" class="form-control" style="min-width: 180px;"
                           value="${toDate}">
                </div>
                <button type="submit" class="btn btn-primary">Apply Period</button>
                <a href="${pageContext.request.contextPath}/report?type=inventory&year=${year}" class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <!-- 6. TABLE -->
        <div class="panel">
            <h3 class="panel-title">Inventory In-Out-Balance Table</h3>
            <p class="mb-2"><strong>Period:</strong> ${fromDate} to ${toDate}</p>
            <p class="text-muted mb-3">This is the primary manager view for stock movement and closing balance health.</p>

            <c:set var="cardOpeningQty" value="0"/>
            <c:set var="cardImportQty" value="0"/>
            <c:set var="cardExportQty" value="0"/>
            <c:set var="cardClosingQty" value="0"/>
            <c:set var="cardClosingValue" value="0"/>
            <c:forEach items="${movementRows}" var="r">
                <c:set var="cardOpeningQty" value="${cardOpeningQty + r.openingQty}"/>
                <c:set var="cardImportQty" value="${cardImportQty + r.importQty}"/>
                <c:set var="cardExportQty" value="${cardExportQty + r.exportQty}"/>
                <c:set var="cardClosingQty" value="${cardClosingQty + r.closingQty}"/>
                <c:set var="cardClosingValue" value="${cardClosingValue + r.closingValue}"/>
            </c:forEach>

            <div class="row mb-3">
                <div class="col-md-3 mb-2">
                    <div class="border rounded p-3 bg-light h-100">
                        <div class="text-muted small">Opening Qty</div>
                        <div class="h5 mb-0"><fmt:formatNumber value="${cardOpeningQty}" type="number"/></div>
                    </div>
                </div>
                <div class="col-md-3 mb-2">
                    <div class="border rounded p-3 bg-light h-100">
                        <div class="text-muted small">Import Qty</div>
                        <div class="h5 mb-0 text-success"><fmt:formatNumber value="${cardImportQty}" type="number"/></div>
                    </div>
                </div>
                <div class="col-md-3 mb-2">
                    <div class="border rounded p-3 bg-light h-100">
                        <div class="text-muted small">Export Qty</div>
                        <div class="h5 mb-0 text-danger"><fmt:formatNumber value="${cardExportQty}" type="number"/></div>
                    </div>
                </div>
                <div class="col-md-3 mb-2">
                    <div class="border rounded p-3 bg-light h-100">
                        <div class="text-muted small">Closing Value</div>
                        <div class="h5 mb-0 text-primary"><fmt:formatNumber value="${cardClosingValue}" type="number" maxFractionDigits="0"/></div>
                    </div>
                </div>
            </div>

            <div class="table-wrap">
                <table class="table table-bordered table-hover mb-0">
                    <thead>
                    <tr class="inv-summary-head text-center">
                        <th rowspan="2" style="min-width:120px;">Item Code</th>
                        <th rowspan="2" style="min-width:220px;">Item Name</th>
                        <th colspan="2">Opening</th>
                        <th colspan="2">Import</th>
                        <th colspan="2">Export</th>
                        <th colspan="2">Closing</th>
                    </tr>
                    <tr class="inv-summary-subhead text-center">
                        <th style="min-width:90px;">Qty</th>
                        <th style="min-width:140px;">Value</th>
                        <th style="min-width:90px;">Qty</th>
                        <th style="min-width:140px;">Value</th>
                        <th style="min-width:90px;">Qty</th>
                        <th style="min-width:140px;">Value</th>
                        <th style="min-width:90px;">Qty</th>
                        <th style="min-width:140px;">Value</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty movementRows}">
                            <c:set var="sumOpeningQty" value="0"/>
                            <c:set var="sumImportQty" value="0"/>
                            <c:set var="sumExportQty" value="0"/>
                            <c:set var="sumClosingQty" value="0"/>
                            <c:set var="sumOpeningValue" value="0"/>
                            <c:set var="sumImportValue" value="0"/>
                            <c:set var="sumExportValue" value="0"/>
                            <c:set var="sumClosingValue" value="0"/>

                            <c:forEach items="${movementRows}" var="row" varStatus="loop">
                                <c:set var="sumOpeningQty" value="${sumOpeningQty + row.openingQty}"/>
                                <c:set var="sumImportQty" value="${sumImportQty + row.importQty}"/>
                                <c:set var="sumExportQty" value="${sumExportQty + row.exportQty}"/>
                                <c:set var="sumClosingQty" value="${sumClosingQty + row.closingQty}"/>

                                <c:set var="sumOpeningValue" value="${sumOpeningValue + row.openingValue}"/>
                                <c:set var="sumImportValue" value="${sumImportValue + row.importValue}"/>
                                <c:set var="sumExportValue" value="${sumExportValue + row.exportValue}"/>
                                <c:set var="sumClosingValue" value="${sumClosingValue + row.closingValue}"/>

                                <tr>
                                    <td class="font-weight-bold text-primary inv-sticky-first">
                                        SP<fmt:formatNumber value="${row.productId}" pattern="000000"/>
                                    </td>
                                    <td>${row.productName}</td>

                                    <td class="text-right"><fmt:formatNumber value="${row.openingQty}" type="number"/></td>
                                    <td class="text-right"><fmt:formatNumber value="${row.openingValue}" type="number" maxFractionDigits="0"/></td>

                                    <td class="text-right text-success"><fmt:formatNumber value="${row.importQty}" type="number"/></td>
                                    <td class="text-right text-success"><fmt:formatNumber value="${row.importValue}" type="number" maxFractionDigits="0"/></td>

                                    <td class="text-right text-danger"><fmt:formatNumber value="${row.exportQty}" type="number"/></td>
                                    <td class="text-right text-danger"><fmt:formatNumber value="${row.exportValue}" type="number" maxFractionDigits="0"/></td>

                                    <td class="text-right font-weight-bold"><fmt:formatNumber value="${row.closingQty}" type="number"/></td>
                                    <td class="text-right font-weight-bold text-primary"><fmt:formatNumber value="${row.closingValue}" type="number" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>

                            <tr class="inv-summary-total">
                                <td colspan="2" class="inv-sticky-first">Total Items: ${fn:length(movementRows)}</td>

                                <td class="text-right"><fmt:formatNumber value="${sumOpeningQty}" type="number"/></td>
                                <td class="text-right"><fmt:formatNumber value="${sumOpeningValue}" type="number" maxFractionDigits="0"/></td>

                                <td class="text-right text-success"><fmt:formatNumber value="${sumImportQty}" type="number"/></td>
                                <td class="text-right text-success"><fmt:formatNumber value="${sumImportValue}" type="number" maxFractionDigits="0"/></td>

                                <td class="text-right text-danger"><fmt:formatNumber value="${sumExportQty}" type="number"/></td>
                                <td class="text-right text-danger"><fmt:formatNumber value="${sumExportValue}" type="number" maxFractionDigits="0"/></td>

                                <td class="text-right"><fmt:formatNumber value="${sumClosingQty}" type="number"/></td>
                                <td class="text-right text-primary"><fmt:formatNumber value="${sumClosingValue}" type="number" maxFractionDigits="0"/></td>
                            </tr>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="10" class="text-center text-muted py-4">No inventory movement data found in selected period.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </div>

        <div class="panel">
            <h3 class="panel-title">How To Read This Table</h3>
            <div class="row small text-muted">
                <div class="col-md-4 mb-2"><strong>Opening</strong>: Stock before the selected period starts.</div>
                <div class="col-md-4 mb-2"><strong>Import/Export</strong>: Net movement inside the selected period.</div>
                <div class="col-md-4 mb-2"><strong>Closing</strong>: Opening + Import - Export.</div>
            </div>
        </div>

        <c:if test="${not empty chartData}">
            <div class="panel">
                <h3 class="panel-title">Secondary Insight: Inventory Trend by Month (${year})</h3>
                <div style="position: relative; height: 280px;">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>
        </c:if>
    </c:if>

    <c:if test="${not empty chartSummaryData}">
        <div class="panel">
            <h3 class="panel-title">Annual Overview - Import vs Export</h3>
            <p class="text-muted mb-3">Year-level context for planning, while daily operations should focus on the table above.</p>
            <div style="position: relative; height: 300px;">
                <canvas id="overviewChart"></canvas>
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

        // 2. Trend Charts for Import, Export, Inventory
        if ((reportType === 'import' || reportType === 'export' || reportType === 'inventory') && trendChartRaw) {
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
                                label: reportType === 'inventory' ? 'Inventory Quantity' : (reportType === 'export' ? 'Exported Quantity' : 'Imported Quantity'),
                                data: trendData,
                                backgroundColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 0.2)'
                                    : (reportType === 'export' ? 'rgba(239, 68, 68, 0.2)' : 'rgba(59, 130, 246, 0.2)'),
                                borderColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 1)'
                                    : (reportType === 'export' ? 'rgba(239, 68, 68, 1)' : 'rgba(59, 130, 246, 1)'),
                                borderWidth: 2,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: reportType === 'inventory' 
                                    ? 'rgba(34, 197, 94, 1)'
                                    : (reportType === 'export' ? 'rgba(239, 68, 68, 1)' : 'rgba(59, 130, 246, 1)'),
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
    });
</script>

</body>

</html>