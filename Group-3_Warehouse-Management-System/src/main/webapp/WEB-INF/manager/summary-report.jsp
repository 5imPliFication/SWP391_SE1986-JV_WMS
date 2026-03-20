<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.dto.SummaryReportDTO, java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Inventory Report</title>

        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">

        <style>
            .stat-card {
                background: #fff;
                border-radius: 12px;
                padding: 20px;
                box-shadow: 0 2px 10px rgba(0,0,0,0.08);
                margin-bottom: 20px;
            }

            .stat-title {
                font-size: 14px;
                color: #777;
            }
            .stat-value {
                font-size: 28px;
                font-weight: bold;
            }

            .table thead {
                background: #f5f5f5;
            }
            .total-row {
                font-weight: bold;
                background: #e9ecef;
            }
        </style>
    </head>

    <body>
        <div class="container mt-4">

            <h2 class="mb-4">📊 Inventory Report</h2>

            <!-- 🔥 FILTER THEO THÁNG -->
            <form method="get" action="${pageContext.request.contextPath}/summary_report" class="mb-4">
                <div class="row">
                    <div class="col-md-3">
                        <label>Month</label>
                        <input type="month" name="month" value="${month}" class="form-control">
                    </div>

                    <div class="col-md-3 d-flex align-items-end">
                        <button class="btn btn-primary mr-2">Filter</button>
                        <a href="${pageContext.request.contextPath}/summary_report" class="btn btn-secondary">Reset</a>
                    </div>
                </div>
            </form>

            <!-- 🔥 STAT CARDS TỔNG HỢP -->
            <c:if test="${not empty reportList}">
                <%
                    long totalImport = 0;
                    long totalExport = 0;
                    long totalClosing = 0;
                    for (SummaryReportDTO r : (java.util.List<SummaryReportDTO>)request.getAttribute("reportList")) {
                        totalImport += r.getTotalImport();
                        totalExport += r.getTotalExport();
                        totalClosing += r.getClosingStock();
                    }
                %>

                <div class="row">
                    <div class="col-md-4">
                        <div class="stat-card text-center">
                            <div class="stat-title">Total Import</div>
                            <div class="stat-value text-success">
                                <fmt:formatNumber value="<%=totalImport%>" pattern="#,##0"/>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="stat-card text-center">
                            <div class="stat-title">Total Export</div>
                            <div class="stat-value text-danger">
                                <fmt:formatNumber value="<%=totalExport%>" pattern="#,##0"/>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-4">
                        <div class="stat-card text-center">
                            <div class="stat-title">Current Stock</div>
                            <div class="stat-value text-primary">
                                <fmt:formatNumber value="<%=totalClosing%>" pattern="#,##0"/>
                            </div>
                        </div>
                    </div>
                </div>
            </c:if>

            <!-- 🔥 TABLE -->
            <c:if test="${not empty reportList}">
                <div class="card mt-4">
                    <div class="card-header"><b>Inventory Detail</b></div>
                    <div class="card-body p-0">
                        <table class="table table-bordered table-hover mb-0">
                            <thead>
                                <tr>
                                    <th>Product</th>
                                    <th>Opening</th>
                                    <th>Import</th>
                                    <th>Export</th>
                                    <th>Closing</th>
                                </tr>
                            </thead>
                            <tbody>
                                <c:forEach var="r" items="${reportList}">
                                    <tr>
                                        <td>${r.productName}</td>
                                        <td><fmt:formatNumber value="${r.openingStock}" pattern="#,##0"/></td>
                                        <td><fmt:formatNumber value="${r.totalImport}" pattern="#,##0"/></td>
                                        <td><fmt:formatNumber value="${r.totalExport}" pattern="#,##0"/></td>
                                        <td><fmt:formatNumber value="${r.closingStock}" pattern="#,##0"/></td>
                                    </tr>
                                </c:forEach>
                            </tbody>
                        </table>
                    </div>
                </div>
            </c:if>

            <!-- 🔥 ERROR -->
            <c:if test="${not empty error}">
                <div class="alert alert-danger mt-3">
                    ${error}
                </div>
            </c:if>

        </div>
    </body>
</html>
