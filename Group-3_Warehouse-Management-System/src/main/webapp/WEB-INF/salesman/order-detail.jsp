<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <title>Order Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="font-weight-bold text-dark">
            <i class="fas fa-file-invoice mr-2"></i>Order Detail
        </h2>
        <a href="${pageContext.request.contextPath}/salesman/orders"
           class="btn btn-secondary">
            <i class="fas fa-arrow-left mr-2"></i>Back to Orders
        </a>
    </div>

    <!-- Order Info Card -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-primary text-white">
            <h5 class="mb-0"><i class="fas fa-info-circle mr-2"></i>Order Information</h5>
        </div>
        <div class="card-body">
            <div class="row">
                <div class="col-md-4 mb-3">
                    <p class="text-muted mb-1">Order Code</p>
                    <h5 class="text-primary font-weight-bold">${order.orderCode}</h5>
                </div>
                <div class="col-md-4 mb-3">
                    <p class="text-muted mb-1">Customer</p>
                    <h5 class="font-weight-bold">${order.customerName}</h5>
                </div>
                <div class="col-md-4 mb-3">
                    <p class="text-muted mb-1">Status</p>
                    <c:choose>
                        <c:when test="${order.status == 'DRAFT'}">
                            <span class="badge badge-warning badge-pill px-3 py-2">
                                <i class="fas fa-edit mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'Pending'}">
                            <span class="badge badge-info badge-pill px-3 py-2">
                                <i class="fas fa-clock mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'Processing'}">
                            <span class="badge badge-primary badge-pill px-3 py-2">
                                <i class="fas fa-spinner mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'Completed'}">
                            <span class="badge badge-success badge-pill px-3 py-2">
                                <i class="fas fa-check-circle mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'Cancelled'}">
                            <span class="badge badge-danger badge-pill px-3 py-2">
                                <i class="fas fa-times-circle mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:otherwise>
                            <span class="badge badge-secondary badge-pill px-3 py-2">${order.status}</span>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>

    <!-- Order Items Table -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white">
            <h5 class="mb-0"><i class="fas fa-box mr-2"></i>Order Items</h5>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4">#</th>
                        <th class="py-3 px-4">Product Name</th>
                        <th class="py-3 px-4 text-right">Quantity</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${items}" var="i" varStatus="status">
                        <tr>
                            <td class="px-4 align-middle">${status.index + 1}</td>
                            <td class="px-4 align-middle font-weight-bold">${i.productId}</td>
                            <td class="px-4 align-middle text-right">
                                <span class="badge badge-info">${i.quantity}</span>
                            </td>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty items}">
                        <tr>
                            <td colspan="3" class="text-center py-5">
                                <div class="text-muted">
                                    <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                    <h5>No Items in Order</h5>
                                    <p class="mb-0">Add items to this order</p>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Add Item Form (Only for DRAFT) -->
    <c:if test="${order.status == 'DRAFT'}">
        <div class="card shadow-sm mb-4">
            <div class="card-header bg-success text-white">
                <h5 class="mb-0"><i class="fas fa-plus-circle mr-2"></i>Add Item to Order</h5>
            </div>
            <div class="card-body">
                <form action="${pageContext.request.contextPath}/salesman/order/item/add" method="post">
                    <input type="hidden" name="orderId" value="${order.id}"/>

                    <div class="form-row">
                        <div class="form-group col-md-6">
                            <label for="productId" class="font-weight-bold">
                                <i class="fas fa-laptop mr-1"></i>Product ID
                            </label>
                            <input type="number"
                                   class="form-control form-control-lg"
                                   id="productId"
                                   name="productId"
                                   placeholder="Enter product ID"
                                   required/>
                            <small class="form-text text-muted">Enter the product ID from inventory</small>
                        </div>

                        <div class="form-group col-md-6">
                            <label for="quantity" class="font-weight-bold">
                                <i class="fas fa-sort-numeric-up mr-1"></i>Quantity
                            </label>
                            <input type="number"
                                   class="form-control form-control-lg"
                                   id="quantity"
                                   name="quantity"
                                   placeholder="Enter quantity"
                                   min="1"
                                   required/>
                            <small class="form-text text-muted">Number of units to order</small>
                        </div>
                    </div>

                    <button type="submit" class="btn btn-success btn-lg btn-block">
                        <i class="fas fa-plus-circle mr-2"></i>Add Item
                    </button>
                </form>
            </div>
        </div>

        <!-- Submit Order Form -->
        <div class="card shadow-sm border-primary">
            <div class="card-body text-center py-4">
                <h5 class="mb-3">Ready to submit this order?</h5>
                <p class="text-muted mb-4">Once submitted, you won't be able to modify the order.</p>
                <form action="${pageContext.request.contextPath}/salesman/order/submit"
                      method="post"
                      onsubmit="return confirm('Are you sure you want to submit this order? You will not be able to edit it afterwards.');">
                    <input type="hidden" name="orderId" value="${order.id}"/>
                    <button type="submit" class="btn btn-primary btn-lg px-5">
                        <i class="fas fa-paper-plane mr-2"></i>Submit Order
                    </button>
                </form>
            </div>
        </div>
    </c:if>

    <!-- Locked Order Notice -->
    <c:if test="${order.status != 'DRAFT'}">
        <div class="alert alert-info shadow-sm" role="alert">
            <h5 class="alert-heading">
                <i class="fas fa-lock mr-2"></i>Order Locked
            </h5>
            <p class="mb-0">This order has been submitted and cannot be modified.</p>
        </div>
    </c:if>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
