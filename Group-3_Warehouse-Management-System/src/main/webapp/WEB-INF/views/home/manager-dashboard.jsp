<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Manager Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="mb-4">
        <h2 class="font-weight-bold text-dark">
            <i class="fas fa-chart-line mr-2"></i>Manager Dashboard
        </h2>
        <p class="text-muted mb-0">Overview of system performance and statistics</p>
    </div>

    <!-- Statistics Cards Row 1 -->
    <div class="row mb-4">
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-primary shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-primary text-uppercase mb-1">
                                Orders Today
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${ordersToday}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-shopping-cart fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-success shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-success text-uppercase mb-1">
                                Orders This Week
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${ordersThisWeek}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-calendar-week fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-info shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-info text-uppercase mb-1">
                                Total Products
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${totalProducts}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-laptop fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                Low Stock Items
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${lowStockCount}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Order Status Statistics -->
    <div class="row mb-4">
        <div class="col-lg-6 mb-4">
            <div class="card shadow">
                <div class="card-header py-3 bg-primary text-white">
                    <h6 class="m-0 font-weight-bold">
                        <i class="fas fa-clipboard-list mr-2"></i>Order Status Overview
                    </h6>
                </div>
                <div class="card-body">
                    <div class="row mb-3">
                        <div class="col-6">
                            <div class="border-left-warning p-3">
                                <div class="text-xs text-uppercase font-weight-bold text-warning mb-1">Submitted</div>
                                <div class="h5 mb-0 font-weight-bold">${submittedOrders}</div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="border-left-info p-3">
                                <div class="text-xs text-uppercase font-weight-bold text-info mb-1">Processing</div>
                                <div class="h5 mb-0 font-weight-bold">${processingOrders}</div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-6">
                            <div class="border-left-success p-3">
                                <div class="text-xs text-uppercase font-weight-bold text-success mb-1">Completed</div>
                                <div class="h5 mb-0 font-weight-bold">${completedOrders}</div>
                            </div>
                        </div>
                        <div class="col-6">
                            <div class="border-left-danger p-3">
                                <div class="text-xs text-uppercase font-weight-bold text-danger mb-1">Cancelled</div>
                                <div class="h5 mb-0 font-weight-bold">${cancelledOrders}</div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-lg-6 mb-4">
            <div class="card shadow">
                <div class="card-header py-3 bg-success text-white">
                    <h6 class="m-0 font-weight-bold">
                        <i class="fas fa-users mr-2"></i>Active Users
                    </h6>
                </div>
                <div class="card-body">
                    <div class="mb-3">
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Salesmen</span>
                            <span class="font-weight-bold">${salesmenCount}</span>
                        </div>
                        <div class="progress mb-3" style="height: 10px;">
                            <div class="progress-bar bg-primary" role="progressbar" 
                                 style="width: ${(salesmenCount * 100) / (salesmenCount + warehouseCount)}%"></div>
                        </div>
                    </div>
                    <div>
                        <div class="d-flex justify-content-between mb-2">
                            <span class="text-muted">Warehouse Staff</span>
                            <span class="font-weight-bold">${warehouseCount}</span>
                        </div>
                        <div class="progress" style="height: 10px;">
                            <div class="progress-bar bg-success" role="progressbar" 
                                 style="width: ${(warehouseCount * 100) / (salesmenCount + warehouseCount)}%"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Quick Actions -->
    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-users fa-3x text-primary mb-3"></i>
                    <h5>Manage Users</h5>
                    <p class="text-muted">Add, edit, or remove system users</p>
                    <a href="${pageContext.request.contextPath}/user/list" class="btn btn-primary">
                        <i class="fas fa-arrow-right mr-1"></i>Go to Users
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-tag fa-3x text-success mb-3"></i>
                    <h5>Manage Coupons</h5>
                    <p class="text-muted">Create and manage discount coupons</p>
                    <a href="${pageContext.request.contextPath}/coupons" class="btn btn-success">
                        <i class="fas fa-arrow-right mr-1"></i>Go to Coupons
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-boxes fa-3x text-info mb-3"></i>
                    <h5>View Inventory</h5>
                    <p class="text-muted">Check product stock levels</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-info">
                        <i class="fas fa-arrow-right mr-1"></i>Go to Products
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!-- Recent Orders Table -->
    <div class="card shadow mb-4">
        <div class="card-header py-3 bg-dark text-white">
            <h6 class="m-0 font-weight-bold">
                <i class="fas fa-history mr-2"></i>Recent Orders (Last 10)
            </h6>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Salesman</th>
                        <th class="py-3 px-4">Status</th>
                        <th class="py-3 px-4">Created At</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${recentOrders}" var="order">
                        <tr>
                            <td class="px-4 align-middle font-weight-bold text-primary">
                                ${order.orderCode}
                            </td>
                            <td class="px-4 align-middle">${order.customerName}</td>
                            <td class="px-4 align-middle">
                                ${order.createdBy != null ? order.createdBy.fullName : 'N/A'}
                            </td>
                            <td class="px-4 align-middle">
                                <c:choose>
                                    <c:when test="${order.status == 'DRAFT'}">
                                        <span class="badge badge-secondary">DRAFT</span>
                                    </c:when>
                                    <c:when test="${order.status == 'SUBMITTED'}">
                                        <span class="badge badge-warning">SUBMITTED</span>
                                    </c:when>
                                    <c:when test="${order.status == 'PROCESSING'}">
                                        <span class="badge badge-info">PROCESSING</span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge badge-success">COMPLETED</span>
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <span class="badge badge-danger">CANCELLED</span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td class="px-4 align-middle text-muted">
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, HH:mm"/>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty recentOrders}">
                        <tr>
                            <td colspan="5" class="text-center py-4 text-muted">
                                No recent orders
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>


</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
