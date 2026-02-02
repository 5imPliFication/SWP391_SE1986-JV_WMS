<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order List - Laptop Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="fw-bold text-dark">
            <i class="bi bi-cart-check me-2"></i>My Orders
        </h2>
        <a href="${pageContext.request.contextPath}/salesman/order/create"
           class="btn btn-primary btn-lg shadow-sm">
            <i class="bi bi-plus-circle me-2"></i>Create New Order
        </a>
    </div>

    <!-- Table Container -->
    <div class="card shadow-sm border-0">
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped align-middle mb-0">
                    <thead class="table-primary">
                    <tr>
                        <th class="py-3 px-4">Order Code</th>
                        <th class="py-3 px-4">Customer</th>
                        <th class="py-3 px-4">Status</th>
                        <th class="py-3 px-4">Created At</th>
                        <th class="py-3 px-4 text-center">Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${orders}" var="o">
                        <tr>
                            <td class="px-4 fw-semibold text-primary">${o.orderCode}</td>
                            <td class="px-4">${o.customerName}</td>
                            <td class="px-4">
                                <c:choose>
                                    <c:when test="${o.status == 'Pending'}">
                                            <span class="badge bg-warning text-dark">
                                                <i class="bi bi-clock-history me-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'Processing'}">
                                            <span class="badge bg-info text-dark">
                                                <i class="bi bi-arrow-repeat me-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'Completed'}">
                                            <span class="badge bg-success">
                                                <i class="bi bi-check-circle me-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:when test="${o.status == 'Cancelled'}">
                                            <span class="badge bg-danger">
                                                <i class="bi bi-x-circle me-1"></i>${o.status}
                                            </span>
                                    </c:when>
                                    <c:otherwise>
                                        <c:if test="${o.status eq 'DRAFT'}">
                                            <span class="badge bg-secondary">${o.status}</span>
                                        </c:if>
                                        <c:if test="${o.status eq 'SUBMITTED'}">
                                            <span class="badge bg-warning">${o.status}</span>
                                        </c:if>
                                        <c:if test="${o.status eq 'PROCESSING'}">
                                            <span class="badge bg-primary">${o.status}</span>
                                        </c:if>
                                        <c:if test="${o.status eq 'APPROVED'}">
                                            <span class="badge bg-success text-white">${o.status}</span>
                                        </c:if>
                                        <c:if test="${o.status eq 'FLAGGED'}">
                                            <span class="badge bg-danger text-white">${o.status}</span>
                                        </c:if>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="px-4 text-muted">
                                <i class="bi bi-calendar3 me-1"></i>${o.createdAt}
                            </td>
                            <td class="px-4 text-center">
                                <a href="${pageContext.request.contextPath}/salesman/order/detail?id=${o.id}"
                                   class="btn btn-sm btn-outline-primary">
                                    <i class="bi bi-eye me-1"></i>View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty orders}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <div class="text-muted">
                                    <i class="bi bi-inbox display-1 d-block mb-3"></i>
                                    <h5>No Orders Found</h5>
                                    <p class="mb-0">Start by creating your first order</p>
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