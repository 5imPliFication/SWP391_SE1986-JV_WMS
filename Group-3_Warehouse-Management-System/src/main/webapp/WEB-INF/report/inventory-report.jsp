<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Inventory Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        :root {
            --report-bg: linear-gradient(180deg, #f4fbf6 0%, #f9fcfa 48%, #ffffff 100%);
            --panel-border: #dbe7f5;
            --panel-shadow: 0 18px 36px rgba(15, 23, 42, 0.08);
            --text-main: #12304a;
            --text-soft: #5f7388;
            --accent: #16a34a;
            --accent-strong: #15803d;
            --accent-soft: rgba(22, 163, 74, 0.12);
            --import-soft: rgba(37, 99, 235, 0.10);
            --export-soft: rgba(220, 38, 38, 0.10);
            --summary-soft: #f8fbff;
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
                radial-gradient(circle at top right, rgba(22, 163, 74, 0.14), transparent 30%),
                linear-gradient(135deg, #ffffff 0%, #eefbf2 100%);
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
            color: var(--accent-strong);
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
            background: rgba(255, 255, 255, 0.82);
            border: 1px solid rgba(22, 163, 74, 0.16);
            min-width: 220px;
        }

        .context-chip .label,
        .stat-card .label {
            display: block;
            font-size: 12px;
            font-weight: 700;
            color: var(--text-soft);
            text-transform: uppercase;
            letter-spacing: 0.06em;
            margin-bottom: 6px;
        }

        .context-chip .value,
        .stat-card .value {
            color: var(--text-main);
            font-weight: 700;
        }

        .context-chip .value {
            font-size: 16px;
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
            color: #2563eb;
            border-color: #b8cff2;
            transform: translateY(-1px);
        }

        .report-type.active {
            background: linear-gradient(135deg, #16a34a, #22c55e);
            border-color: #16a34a;
            color: #fff;
            box-shadow: 0 14px 24px rgba(22, 163, 74, 0.24);
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

        .stat-card .value {
            font-size: 28px;
            line-height: 1.1;
            margin-bottom: 6px;
        }

        .stat-card .meta {
            color: var(--text-soft);
            font-size: 13px;
        }

        .stat-card.accent {
            background: linear-gradient(135deg, rgba(22, 163, 74, 0.10), rgba(255, 255, 255, 0.92));
            border-color: rgba(22, 163, 74, 0.18);
        }

        .stat-card.import {
            background: linear-gradient(135deg, var(--import-soft), rgba(255, 255, 255, 0.92));
            border-color: rgba(37, 99, 235, 0.18);
        }

        .stat-card.export {
            background: linear-gradient(135deg, var(--export-soft), rgba(255, 255, 255, 0.92));
            border-color: rgba(220, 38, 38, 0.18);
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
            color: var(--text-main);
            border-bottom: 0;
            font-weight: 700;
        }

        .table tbody td {
            vertical-align: middle;
            border-color: #ebf1f7;
        }

        .inv-summary-head {
            background: #dff6e7;
            color: var(--text-main);
            font-weight: 700;
        }

        .inv-summary-subhead {
            background: #f4fbf7;
            color: #365067;
            font-size: 13px;
            font-weight: 700;
        }

        .inv-summary-total {
            background: #f5f9e8;
            font-weight: 700;
        }

        .inv-sticky-first {
            position: sticky;
            left: 0;
            background: #fff;
            z-index: 2;
        }

        .inv-summary-head .inv-sticky-first {
            background: #dff6e7;
        }

        .inv-summary-total .inv-sticky-first {
            background: #f5f9e8;
        }

        .helper-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(220px, 1fr));
            gap: 16px;
        }

        .helper-card {
            padding: 18px 20px;
            border-radius: 18px;
            border: 1px solid #dce7f4;
            background: var(--summary-soft);
            color: var(--text-soft);
        }

        .helper-card strong {
            display: block;
            color: var(--text-main);
            margin-bottom: 6px;
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
        <div class="hero-panel">
            <div class="d-flex justify-content-between align-items-start flex-wrap" style="gap: 18px;">
                <div>
                    <span class="eyebrow">Warehouse analytics</span>
                    <h1 class="hero-title">Inventory Report</h1>
                    <p class="hero-subtitle">Track opening stock, movement, and closing balance in the same summary-first style used by the other report pages.</p>
                </div>
                <div class="context-chip">
                    <span class="label">Current focus</span>
                    <span class="value">
                        <c:choose>
                            <c:when test="${not empty fromDate and not empty toDate}">${fromDate} to ${toDate}</c:when>
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
                    <input type="hidden" name="type" value="${empty type ? 'inventory' : type}"/>
                    <div>
                        <label>Year</label>
                        <input type="number" name="year" class="form-control" placeholder="Input year" value="${year}">
                    </div>
                    <button type="submit" class="btn btn-primary">Apply Year</button>
                </form>
            </div>

            <div class="report-type-group">
                <a href="?type=inventory&year=${year}" class="report-type ${requestScope.type eq 'inventory' ? 'active' : ''}">
                    Inventory
                </a>
                <a href="?type=import&year=${year}" class="report-type ${requestScope.type eq 'import' ? 'active' : ''}">
                    Import
                </a>
                <a href="?type=export&year=${year}" class="report-type ${requestScope.type eq 'export' ? 'active' : ''}">
                    Export
                </a>
            </div>
        </div>

    <!-- Error/Message Display -->
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
                                <td colspan="3" class="text-center text-muted py-4">No import data found
                                    for ${month}/${year}.
                                </td>
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
                                <td colspan="4" class="text-center text-muted py-4">No export data found
                                    for ${month}/${year}.
                                </td>
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
                                    <td colspan="7" class="text-center text-muted py-4">No orders contain this product
                                        in ${month}/${year}.
                                    </td>
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

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Inventory Snapshot</h2>
                    <p class="section-copy">Start with a quick balance overview before inspecting the detailed in-out table.</p>
                </div>
            </div>

            <div class="stats-grid">
                <div class="stat-card accent">
                    <div class="label">Opening Qty</div>
                    <div class="value"><fmt:formatNumber value="${cardOpeningQty}" type="number"/></div>
                    <div class="meta">Stock available before the selected period</div>
                </div>
                <div class="stat-card import">
                    <div class="label">Import Qty</div>
                    <div class="value"><fmt:formatNumber value="${cardImportQty}" type="number"/></div>
                    <div class="meta">Inbound movement during the period</div>
                </div>
                <div class="stat-card export">
                    <div class="label">Export Qty</div>
                    <div class="value"><fmt:formatNumber value="${cardExportQty}" type="number"/></div>
                    <div class="meta">Outbound movement during the period</div>
                </div>
                <div class="stat-card">
                    <div class="label">Closing Value</div>
                    <div class="value"><fmt:formatNumber value="${cardClosingValue}" type="number" maxFractionDigits="0"/></div>
                    <div class="meta">Estimated value remaining at the end of the period</div>
                </div>
            </div>
        </div>

        <!-- 4. PRIMARY FILTER -->
        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Inventory Focus Filters</h2>
                    <p class="section-copy">Pick a month to auto-build the period, then review how opening stock, imports, exports, and closing balance changed.</p>
                </div>
            </div>
            <form action="${pageContext.request.contextPath}/report" method="get" class="filter-grid">
                <input type="hidden" name="type" value="inventory"/>
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
                <a href="${pageContext.request.contextPath}/report?type=inventory&year=${year}"
                   class="btn btn-outline-secondary">Reset</a>
            </form>
        </div>

        <script>
            (function () {
                const monthSelect  = document.getElementById('monthSelect');
                const fromHidden   = document.getElementById('fromDateHidden');
                const toHidden     = document.getElementById('toDateHidden');
                const fromDisplay  = document.getElementById('fromDateDisplay');
                const toDisplay    = document.getElementById('toDateDisplay');
                const year         = parseInt('${year}') || new Date().getFullYear();

                function pad(n) { return String(n).padStart(2, '0'); }

                function updateDates(month) {
                    if (!month) return;
                    const m       = parseInt(month);
                    const lastDay = new Date(year, m, 0).getDate();
                    const from    = year + '-' + pad(m) + '-01';
                    const to      = year + '-' + pad(m) + '-' + pad(lastDay);

                    fromHidden.value  = from;
                    toHidden.value    = to;
                    fromDisplay.value = from;
                    toDisplay.value   = to;
                }

                if (monthSelect.value) {
                    updateDates(monthSelect.value);
                }

                monthSelect.addEventListener('change', function () {
                    updateDates(this.value);
                });
            })();
        </script>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Inventory In-Out-Balance Table</h2>
                    <p class="section-copy">
                        <c:choose>
                            <c:when test="${not empty fromDate and not empty toDate}">Period: ${fromDate} to ${toDate}.</c:when>
                            <c:otherwise>Review the selected year's stock movement and closing balance.</c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </div>

            <div class="table-shell">
                <div class="table-wrap">
                <table class="table table-hover mb-0">
                    <thead>
                    <tr class="inv-summary-head text-center">
                        <th rowspan="2" class="inv-sticky-first" style="min-width:220px; z-index:3;">Product Name</th>
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
                                    <td class="inv-sticky-first font-weight-bold">${row.productName}</td>

                                    <td class="text-right"><fmt:formatNumber value="${row.openingQty}"
                                                                             type="number"/></td>
                                    <td class="text-right"><fmt:formatNumber value="${row.openingValue}" type="number"
                                                                             maxFractionDigits="0"/></td>

                                    <td class="text-right text-primary"><fmt:formatNumber value="${row.importQty}"
                                                                                          type="number"/></td>
                                    <td class="text-right text-primary"><fmt:formatNumber value="${row.importValue}"
                                                                                          type="number"
                                                                                          maxFractionDigits="0"/></td>

                                    <td class="text-right text-danger"><fmt:formatNumber value="${row.exportQty}"
                                                                                         type="number"/></td>
                                    <td class="text-right text-danger"><fmt:formatNumber value="${row.exportValue}"
                                                                                         type="number"
                                                                                         maxFractionDigits="0"/></td>

                                    <td class="text-right font-weight-bold"><fmt:formatNumber value="${row.closingQty}"
                                                                                              type="number"/></td>
                                    <td class="text-right font-weight-bold text-success"><fmt:formatNumber
                                            value="${row.closingValue}" type="number" maxFractionDigits="0"/></td>
                                </tr>
                            </c:forEach>

                            <tr class="inv-summary-total">
                                <td colspan="1" class="inv-sticky-first">Total Items: ${fn:length(movementRows)}</td>

                                <td class="text-right"><fmt:formatNumber value="${sumOpeningQty}" type="number"/></td>
                                <td class="text-right"><fmt:formatNumber value="${sumOpeningValue}" type="number"
                                                                         maxFractionDigits="0"/></td>

                                <td class="text-right text-primary"><fmt:formatNumber value="${sumImportQty}"
                                                                                      type="number"/></td>
                                <td class="text-right text-primary"><fmt:formatNumber value="${sumImportValue}"
                                                                                      type="number"
                                                                                      maxFractionDigits="0"/></td>

                                <td class="text-right text-danger"><fmt:formatNumber value="${sumExportQty}"
                                                                                     type="number"/></td>
                                <td class="text-right text-danger"><fmt:formatNumber value="${sumExportValue}"
                                                                                     type="number"
                                                                                     maxFractionDigits="0"/></td>

                                <td class="text-right"><fmt:formatNumber value="${sumClosingQty}" type="number"/></td>
                                <td class="text-right text-success"><fmt:formatNumber value="${sumClosingValue}"
                                                                                      type="number"
                                                                                      maxFractionDigits="0"/></td>
                            </tr>

                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="9" class="empty-state">No inventory movement data found for the selected period.</td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
                </div>
            </div>
        </div>

        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">How To Read This Table</h2>
                    <p class="section-copy">The summary logic matches the cards above, so each column tells one clear part of the inventory story.</p>
                </div>
            </div>
            <div class="helper-grid">
                <div class="helper-card">
                    <strong>Opening</strong>
                    Stock on hand before the selected period begins.
                </div>
                <div class="helper-card">
                    <strong>Import / Export</strong>
                    Movement recorded during the selected period.
                </div>
                <div class="helper-card">
                    <strong>Closing</strong>
                    Opening balance plus imports minus exports.
                </div>
            </div>
        </div>

        <c:if test="${not empty chartData}">
            <div class="panel">
                <div class="panel-header">
                    <div>
                        <h2 class="panel-title">Inventory Trend by Month (${year})</h2>
                        <p class="section-copy">The inventory view uses the same chart frame as the other reports, with a stock-focused green accent.</p>
                    </div>
                </div>
                <div class="chart-shell">
                    <canvas id="trendChart"></canvas>
                </div>
            </div>
        </c:if>
    </c:if>

    <c:if test="${not empty chartSummaryData}">
        <div class="panel">
            <div class="panel-header">
                <div>
                    <h2 class="panel-title">Annual Overview</h2>
                    <p class="section-copy">Keep yearly import versus export context visible while the main operational focus stays on the inventory balance table.</p>
                </div>
            </div>
            <div class="chart-shell">
                <canvas id="overviewChart"></canvas>
            </div>
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
                                {
                                    label: 'Import',
                                    data: importData,
                                    backgroundColor: 'rgba(59, 130, 246, 0.8)',
                                    borderColor: 'rgba(59, 130, 246, 1)',
                                    borderWidth: 1,
                                    borderRadius: 8
                                },
                                {
                                    label: 'Export',
                                    data: exportData,
                                    backgroundColor: 'rgba(239, 68, 68, 0.8)',
                                    borderColor: 'rgba(239, 68, 68, 1)',
                                    borderWidth: 1,
                                    borderRadius: 8
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
                                },
                                x: {
                                    grid: {display: false}
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

        if (trendChartRaw) {
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
                                label: 'Inventory Quantity',
                                data: trendData,
                                backgroundColor: 'rgba(34, 197, 94, 0.18)',
                                borderColor: 'rgba(22, 163, 74, 1)',
                                borderWidth: 3,
                                fill: true,
                                tension: 0.3,
                                pointBackgroundColor: 'rgba(22, 163, 74, 1)',
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
                                },
                                x: {
                                    grid: {display: false}
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
