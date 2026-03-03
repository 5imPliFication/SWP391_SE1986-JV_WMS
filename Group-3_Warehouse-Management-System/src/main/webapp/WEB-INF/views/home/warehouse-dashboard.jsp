<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Warehouse Dashboard</title>
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
            <i class="fas fa-warehouse mr-2"></i>Warehouse Dashboard
        </h2>
        <p class="text-muted mb-0">Order fulfillment and inventory management</p>
    </div>

    <!-- Statistics Cards -->
    <div class="row mb-4">
        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-warning shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-warning text-uppercase mb-1">
                                Pending Orders
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${submittedCount}</div>
                            <small class="text-muted">Awaiting processing</small>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-clock fa-2x text-gray-300"></i>
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
                                In Progress
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${processingCount}</div>
                            <small class="text-muted">Currently processing</small>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-spinner fa-2x text-gray-300"></i>
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
                                Completed Today
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${completedToday}</div>
                            <small class="text-muted">Orders fulfilled</small>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-check-circle fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <div class="col-xl-3 col-md-6 mb-4">
            <div class="card border-left-danger shadow h-100 py-2">
                <div class="card-body">
                    <div class="row no-gutters align-items-center">
                        <div class="col mr-2">
                            <div class="text-xs font-weight-bold text-danger text-uppercase mb-1">
                                Low Stock Items
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${lowStockCount}</div>
                            <small class="text-muted">Need attention</small>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-exclamation-triangle fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <!-- Quick Links -->
    <div class="row">
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-list fa-3x text-warning mb-3"></i>
                    <h5>Order Queue</h5>
                    <p class="text-muted">View all orders in the system</p>
                    <a href="${pageContext.request.contextPath}/warehouse/orders" class="btn btn-warning">
                        <i class="fas fa-arrow-right mr-1"></i>Go to Queue
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-boxes fa-3x text-info mb-3"></i>
                    <h5>Inventory</h5>
                    <p class="text-muted">Check and manage product stock</p>
                    <a href="${pageContext.request.contextPath}/products" class="btn btn-info">
                        <i class="fas fa-arrow-right mr-1"></i>View Inventory
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-4">
            <div class="card shadow h-100">
                <div class="card-body text-center">
                    <i class="fas fa-chart-bar fa-3x text-success mb-3"></i>
                    <h5>Statistics</h5>
                    <p class="text-muted">View fulfillment reports</p>
                    <a href="${pageContext.request.contextPath}/warehouse/reports" class="btn btn-success">
                        <i class="fas fa-arrow-right mr-1"></i>View Reports
                    </a>
                </div>
            </div>
        </div>
    </div>
    <!-- Pending Orders - High Priority -->
    <div class="card shadow mb-4">
        <div class="card-header py-3 bg-warning text-dark d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold">
                <i class="fas fa-exclamation-circle mr-2"></i>Pending Orders (Submitted - Action Required)
            </h6>
            <span class="badge badge-dark">${pendingOrders.size()} orders</span>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Salesman</th>
                        <th class="py-3 px-4">Submitted</th>
                        <th class="py-3 px-4 text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${pendingOrders}" var="order">
                        <tr class="table-warning">
                            <td class="px-4 align-middle font-weight-bold text-primary">
                                ${order.orderCode}
                            </td>
                            <td class="px-4 align-middle">
                                <div class="font-weight-bold">${order.customerName}</div>
                                <small class="text-muted">${order.customerPhone}</small>
                            </td>
                            <td class="px-4 align-middle">
                                ${order.createdBy != null ? order.createdBy.fullName : 'N/A'}
                            </td>
                            <td class="px-4 align-middle text-muted">
                                <i class="fas fa-calendar mr-1"></i>
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM, HH:mm"/>
                            </td>
                            <td class="px-4 align-middle text-center">
                                <a href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}" 
                                   class="btn btn-sm btn-warning">
                                    <i class="fas fa-play-circle mr-1"></i>Process Now
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty pendingOrders}">
                        <tr>
                            <td colspan="5" class="text-center py-5 text-muted">
                                <i class="fas fa-check-circle fa-3x text-success mb-3 d-block"></i>
                                <h5 class="text-success">All Caught Up!</h5>
                                <p class="mb-0">No pending orders at the moment</p>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Orders In Progress -->
    <div class="card shadow mb-4">
        <div class="card-header py-3 bg-info text-white d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold">
                <i class="fas fa-cog fa-spin mr-2"></i>Orders In Progress
            </h6>
            <span class="badge badge-light">${processingOrders.size()} orders</span>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Started</th>
                        <th class="py-3 px-4 text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${processingOrders}" var="order">
                        <tr>
                            <td class="px-4 align-middle font-weight-bold text-primary">
                                ${order.orderCode}
                            </td>
                            <td class="px-4 align-middle">${order.customerName}</td>
                            <td class="px-4 align-middle text-muted">
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM, HH:mm"/>
                            </td>
                            <td class="px-4 align-middle text-center">
                                <a href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}" 
                                   class="btn btn-sm btn-info">
                                    <i class="fas fa-check-circle mr-1"></i>Complete
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty processingOrders}">
                        <tr>
                            <td colspan="4" class="text-center py-4 text-muted">
                                No orders currently being processed
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Low Stock Alert -->
    <c:if test="${not empty lowStockProducts}">
        <div class="card shadow mb-4 border-danger">
            <div class="card-header py-3 bg-danger text-white">
                <h6 class="m-0 font-weight-bold">
                    <i class="fas fa-exclamation-triangle mr-2"></i>Low Stock Alert
                </h6>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="thead-light">
                        <tr>
                            <th class="py-3 px-4">Product Name</th>
                            <th class="py-3 px-4 text-center">Available Quantity</th>
                            <th class="py-3 px-4 text-center">Status</th>
                        </tr>
                        </thead>
                        <tbody>
                        <c:forEach items="${lowStockProducts}" var="product">
                            <tr>
                                <td class="px-4 align-middle font-weight-bold">
                                    ${product.name}
                                </td>
                                <td class="px-4 align-middle text-center">
                                    <span class="badge badge-danger badge-pill px-3 py-2">
                                        ${product.totalQuantity} units
                                    </span>
                                </td>
                                <td class="px-4 align-middle text-center">
                                    <c:choose>
                                        <c:when test="${product.totalQuantity == 0}">
                                            <span class="badge badge-danger">OUT OF STOCK</span>
                                        </c:when>
                                        <c:when test="${product.totalQuantity <= 5}">
                                            <span class="badge badge-danger">CRITICAL</span>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge badge-warning">LOW</span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </c:if>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
