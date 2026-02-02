<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:55 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Order Queue</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="mb-4">
        <h2 class="font-weight-bold text-dark">
            <i class="fas fa-tasks mr-2"></i>Order Queue
        </h2>
        <p class="text-muted mb-0">Review and process incoming orders</p>
    </div>

    <!-- Stats Row -->
    <div class="row mb-4">
        <div class="col-md-4 mb-3">
            <div class="card shadow-sm border-left-warning h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h3 class="font-weight-bold text-warning mb-1">12</h3>
                            <p class="text-muted mb-0 small">Submitted Orders</p>
                        </div>
                        <div class="text-warning">
                            <i class="fas fa-clock fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card shadow-sm border-left-primary h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h3 class="font-weight-bold text-primary mb-1">5</h3>
                            <p class="text-muted mb-0 small">Processing</p>
                        </div>
                        <div class="text-primary">
                            <i class="fas fa-spinner fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <div class="col-md-4 mb-3">
            <div class="card shadow-sm border-left-success h-100">
                <div class="card-body">
                    <div class="d-flex justify-content-between">
                        <div>
                            <h3 class="font-weight-bold text-success mb-1">89</h3>
                            <p class="text-muted mb-0 small">Approved Today</p>
                        </div>
                        <div class="text-success">
                            <i class="fas fa-check-circle fa-2x"></i>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <!-- Orders Table -->
    <div class="card shadow-sm">
        <div class="card-header bg-dark text-white">
            <h5 class="mb-0"><i class="fas fa-list mr-2"></i>Orders</h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Status</th>
                        <th class="py-3 px-4 text-center">Actions</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${orders}" var="o">
                        <tr>
                            <td class="px-4 align-middle font-weight-bold text-primary">
                                    ${o.orderCode}
                            </td>
                            <td class="px-4 align-middle">${o.customerName}</td>
                            <td class="px-4 align-middle">
                                <c:choose>
                                    <c:when test="${o.status == 'SUBMITTED'}">
                                            <span class="badge badge-warning">
                                                <i class="fas fa-clock mr-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'PROCESSING'}">
                                            <span class="badge badge-info">
                                                <i class="fas fa-spinner mr-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'APPROVED'}">
                                            <span class="badge badge-success">
                                                <i class="fas fa-check-circle mr-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'FLAGGED'}">
                                            <span class="badge badge-danger">
                                                <i class="fas fa-flag mr-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary">${o.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="px-4 align-middle text-center">
                                <div class="btn-group" role="group">
                                    <a href="${pageContext.request.contextPath}/warehouse/order/detail?id=${o.id}"
                                       class="btn btn-sm btn-outline-primary">
                                        <i class="fas fa-eye mr-1"></i>View
                                    </a>

                                    <c:if test="${o.status == 'SUBMITTED'}">
                                        <form action="${pageContext.request.contextPath}/warehouse/order/process"
                                              method="post"
                                              class="d-inline"
                                              onsubmit="return confirm('Start processing this order?');">
                                            <input type="hidden" name="orderId" value="${o.id}"/>
                                            <button type="submit" class="btn btn-sm btn-success">
                                                <i class="fas fa-play-circle mr-1"></i>Process
                                            </button>
                                        </form>
                                    </c:if>
                                </div>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="4" class="text-center py-5">
                                <div class="text-muted">
                                    <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                    <h5>No Orders in Queue</h5>
                                    <p class="mb-0">All orders have been processed</p>
                                </div>
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
