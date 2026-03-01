<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Salesman Dashboard</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <div>
            <h2 class="font-weight-bold text-dark">
                <i class="fas fa-chart-line mr-2"></i>My Dashboard
            </h2>
        </div>
    </div>

    <!-- Statistics Cards -->
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
                            <i class="fas fa-calendar-day fa-2x text-gray-300"></i>
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
                                Draft Orders
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${draftOrders}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-edit fa-2x text-gray-300"></i>
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
                                Submitted Orders
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${submittedOrders}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-paper-plane fa-2x text-gray-300"></i>
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
                                Completed Orders
                            </div>
                            <div class="h5 mb-0 font-weight-bold text-gray-800">${completedOrders}</div>
                        </div>
                        <div class="col-auto">
                            <i class="fas fa-check-circle fa-2x text-gray-300"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Today's Orders -->
    <div class="card shadow mb-4">
        <div class="card-header py-3 bg-primary text-white d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold">
                <i class="fas fa-calendar-day mr-2"></i>Today's Orders
            </h6>
            <span class="badge badge-light">${todayOrders.size()} orders</span>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Status</th>
                        <th class="py-3 px-4">Time</th>
                        <th class="py-3 px-4 text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${todayOrders}" var="order">
                        <tr>
                            <td class="px-4 align-middle font-weight-bold text-primary">
                                ${order.orderCode}
                            </td>
                            <td class="px-4 align-middle">
                                <div class="font-weight-bold">${order.customerName}</div>
                                <small class="text-muted">${order.customerPhone}</small>
                            </td>
                            <td class="px-4 align-middle">
                                <c:choose>
                                    <c:when test="${order.status == 'DRAFT'}">
                                        <span class="badge badge-secondary badge-pill">
                                            <i class="fas fa-edit mr-1"></i>DRAFT
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'SUBMITTED'}">
                                        <span class="badge badge-warning badge-pill">
                                            <i class="fas fa-paper-plane mr-1"></i>SUBMITTED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'PROCESSING'}">
                                        <span class="badge badge-info badge-pill">
                                            <i class="fas fa-spinner mr-1"></i>PROCESSING
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge badge-success badge-pill">
                                            <i class="fas fa-check-circle mr-1"></i>COMPLETED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <span class="badge badge-danger badge-pill">
                                            <i class="fas fa-times-circle mr-1"></i>CANCELLED
                                        </span>
                                    </c:when>
                                </c:choose>
                            </td>
                            <td class="px-4 align-middle text-muted">
                                <i class="fas fa-clock mr-1"></i>
                                <fmt:formatDate value="${order.createdAt}" pattern="HH:mm"/>
                            </td>
                            <td class="px-4 align-middle text-center">
                                <a href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}" 
                                   class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-eye mr-1"></i>View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                    <c:if test="${empty todayOrders}">
                        <tr>
                            <td colspan="5" class="text-center py-5 text-muted">
                                <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                <h5>No orders created today</h5>
                                <a href="${pageContext.request.contextPath}/salesman/order/create" class="btn btn-primary mt-3">
                                    <i class="fas fa-plus-circle mr-2"></i>Create Your First Order
                                </a>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Recent Orders -->
    <div class="card shadow mb-4">
        <div class="card-header py-3 bg-dark text-white d-flex justify-content-between align-items-center">
            <h6 class="m-0 font-weight-bold">
                <i class="fas fa-history mr-2"></i>Recent Orders (Last 7 Days)
            </h6>
            <a href="${pageContext.request.contextPath}/salesman/orders" class="btn btn-sm btn-light">
                View All <i class="fas fa-arrow-right ml-1"></i>
            </a>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Status</th>
                        <th class="py-3 px-4">Created</th>
                        <th class="py-3 px-4 text-center">Action</th>
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
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM, HH:mm"/>
                            </td>
                            <td class="px-4 align-middle text-center">
                                <a href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}" 
                                   class="btn btn-sm btn-outline-primary">
                                    <i class="fas fa-eye mr-1"></i>View
                                </a>
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
