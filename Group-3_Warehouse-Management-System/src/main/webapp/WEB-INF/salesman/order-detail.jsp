<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Order Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/order-detail-salesman.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Success/Error Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle mr-2"></i>${param.success}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>
    
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle mr-2"></i>${param.error}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

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
                            <span class="badge badge-secondary badge-pill px-3 py-2">
                                <i class="fas fa-edit mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'SUBMITTED'}">
                            <span class="badge badge-warning badge-pill px-3 py-2">
                                <i class="fas fa-paper-plane mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'PROCESSING'}">
                            <span class="badge badge-info badge-pill px-3 py-2">
                                <i class="fas fa-spinner mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'COMPLETED'}">
                            <span class="badge badge-success badge-pill px-3 py-2">
                                <i class="fas fa-check-circle mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'CANCELLED'}">
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
                        <th class="py-3 px-4">Unit Price</th>
                        <th class="py-3 px-4 text-center">Quantity</th>
                        <th class="py-3 px-4 text-right">Subtotal</th>
                        <c:if test="${order.status == 'DRAFT'}">
                            <th class="py-3 px-4 text-center">Action</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="total" value="0"/>
                    <c:forEach items="${items}" var="i" varStatus="status">
                        <c:set var="subtotal" value="${i.product.price * i.quantity}"/>
                        <c:set var="total" value="${total + subtotal}"/>
                        <tr>
                            <td class="px-4 align-middle">${status.index + 1}</td>
                            <td class="px-4 align-middle font-weight-bold">${i.product.name}</td>
                            <td class="px-4 align-middle">
                                <fmt:formatNumber value="${i.product.price}" type="number" groupingUsed="true"/> VND
                            </td>
                            <td class="px-4 align-middle text-center">
                                <span class="badge badge-info badge-pill">${i.quantity}</span>
                            </td>
                            <td class="px-4 align-middle text-right font-weight-bold text-primary">
                                <fmt:formatNumber value="${subtotal}" type="number" groupingUsed="true"/> VND
                            </td>
                            <c:if test="${order.status == 'DRAFT'}">
                                <td class="px-4 align-middle text-center">
                                    <form action="${pageContext.request.contextPath}/salesman/order/item/remove"
                                          method="post"
                                          style="display:inline;"
                                          onsubmit="return confirm('Remove this item from order?');">
                                        <input type="hidden" name="productId" value="${i.product.id}"/>
                                        <input type="hidden" name="orderId" value="${order.id}"/>
                                        <button type="submit" class="btn btn-sm btn-outline-danger">
                                            <i class="fas fa-trash"></i>
                                        </button>
                                    </form>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty items}">
                        <tr>
                            <td colspan="${order.status == 'DRAFT' ? '6' : '5'}" class="text-center py-5">
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

    <!-- Order Summary -->
    <c:if test="${not empty items}">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h5 class="mb-3"><i class="fas fa-receipt mr-2"></i>Order Summary</h5>

                        <!-- Coupon Section - INTEGRATED -->
                        <c:choose>
                            <c:when test="${not empty order.coupon}">
                                <!-- Coupon Applied -->
                                <div class="coupon-applied-box">
                                    <div class="d-flex justify-content-between align-items-center">
                                        <div>
                                            <div class="mb-2">
                                                <span class="coupon-badge">
                                                    <i class="fas fa-ticket-alt mr-1"></i>${order.coupon.code}
                                                </span>
                                            </div>
                                            <h6 class="mb-1 font-weight-bold text-success">
                                                <i class="fas fa-check-circle mr-1"></i>
                                                <c:choose>
                                                    <c:when test="${order.coupon.discountType == 'PERCENTAGE'}">
                                                        <fmt:formatNumber value="${order.coupon.discountValue}" pattern="#"/>% OFF Applied
                                                    </c:when>
                                                    <c:otherwise>
                                                        <fmt:formatNumber value="${order.coupon.discountValue}" type="number" groupingUsed="true"/> VND OFF Applied
                                                    </c:otherwise>
                                                </c:choose>
                                            </h6>
                                            <small class="text-muted">
                                                <i class="fas fa-info-circle mr-1"></i>${order.coupon.description}
                                            </small>
                                        </div>
                                        <c:if test="${order.status == 'DRAFT'}">
                                            <form action="${pageContext.request.contextPath}/salesman/order/remove-coupon"
                                                  method="post"
                                                  onsubmit="return confirm('Remove this coupon from the order?');">
                                                <input type="hidden" name="orderId" value="${order.id}"/>
                                                <button type="submit" class="btn btn-outline-danger btn-sm">
                                                    <i class="fas fa-times mr-1"></i>Remove
                                                </button>
                                            </form>
                                        </c:if>
                                    </div>
                                </div>
                            </c:when>
                            <c:otherwise>
                                <!-- Apply Coupon Form (Only for DRAFT orders) -->
                                <c:if test="${order.status == 'DRAFT'}">
                                    <div class="form-group">
                                        <label class="font-weight-bold text-muted">
                                            <i class="fas fa-tag mr-1"></i>Discount Coupon
                                        </label>
                                        <form action="${pageContext.request.contextPath}/salesman/order/apply-coupon" method="post">
                                            <input type="hidden" name="orderId" value="${order.id}"/>
                                            <div class="input-group">
                                                <input type="text"
                                                       name="couponCode"
                                                       class="form-control coupon-input-uppercase"
                                                       placeholder="Enter coupon code"
                                                       pattern="[A-Z0-9]+"
                                                       title="Coupon codes contain only uppercase letters and numbers"
                                                       maxlength="50"
                                                       required>
                                                <div class="input-group-append">
                                                    <button class="btn btn-outline-primary" type="submit">
                                                        <i class="fas fa-check mr-1"></i>Apply
                                                    </button>
                                                </div>
                                            </div>
                                            <small class="form-text text-muted">
                                                <i class="fas fa-info-circle mr-1"></i>Enter a valid coupon code to receive a discount
                                            </small>
                                        </form>
                                    </div>
                                </c:if>
                            </c:otherwise>
                        </c:choose>
                    </div>

                    <div class="col-md-4">
                        <div class="card bg-light">
                            <div class="card-body">
                                <div class="d-flex justify-content-between mb-2">
                                    <span class="text-muted">Subtotal:</span>
                                    <span class="font-weight-bold">
                                        <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> VND
                                    </span>
                                </div>
                                
                                <!-- Show discount if coupon applied -->
                                <c:choose>
                                    <c:when test="${not empty order.coupon}">
                                        <div class="d-flex justify-content-between mb-2">
                                            <span class="text-muted">
                                                <i class="fas fa-tag mr-1"></i>Discount:
                                            </span>
                                            <span class="discount-highlight">
                                                -<fmt:formatNumber value="${order.discountAmount}" type="number" groupingUsed="true"/> VND
                                            </span>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between">
                                            <span class="h5 mb-0">Total:</span>
                                            <span class="h4 mb-0 text-success font-weight-bold">
                                                <fmt:formatNumber value="${order.finalTotal}" type="number" groupingUsed="true"/> VND
                                            </span>
                                        </div>
                                        <small class="text-muted d-block mt-2">
                                            <i class="fas fa-save mr-1"></i>You saved <fmt:formatNumber value="${order.discountAmount}" type="number" groupingUsed="true"/> VND!
                                        </small>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="d-flex justify-content-between mb-2">
                                            <span class="text-muted">Discount:</span>
                                            <span class="text-success">0 VND</span>
                                        </div>
                                        <hr>
                                        <div class="d-flex justify-content-between">
                                            <span class="h5 mb-0">Total:</span>
                                            <span class="h4 mb-0 text-primary font-weight-bold">
                                                <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> VND
                                            </span>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

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

        <!-- Action Buttons -->
        <div class="row mb-4">
            <div class="col-md-6">
                <!-- Submit Order Form -->
                <div class="card shadow-sm border-primary h-100">
                    <div class="card-body text-center py-4">
                        <h5 class="mb-3">
                            <i class="fas fa-paper-plane mr-2"></i>Submit Order
                        </h5>

                        <c:choose>
                            <c:when test="${not empty items}">
                                <p class="text-muted mb-4">
                                    Ready to submit? You won't be able to modify afterwards.
                                </p>

                                <form action="${pageContext.request.contextPath}/salesman/order/submit"
                                      method="post"
                                      onsubmit="return confirm('Are you sure you want to submit this order?');">

                                    <input type="hidden" name="orderId" value="${order.id}"/>

                                    <button type="submit" class="btn btn-primary btn-lg btn-block">
                                        <i class="fas fa-paper-plane mr-2"></i>Submit Order
                                    </button>
                                </form>
                            </c:when>

                            <c:otherwise>
                                <p class="text-danger mb-4">
                                    You must add at least one item before submitting the order.
                                </p>

                                <button class="btn btn-secondary btn-lg btn-block" disabled>
                                    <i class="fas fa-paper-plane mr-2"></i>Submit Order
                                </button>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <!-- Cancel Order Form -->
                <div class="card shadow-sm border-danger h-100">
                    <div class="card-body text-center py-4">
                        <h5 class="mb-3">
                            <i class="fas fa-times-circle mr-2"></i>Cancel Order
                        </h5>
                        <p class="text-muted mb-4">
                            Discard this draft order? This action cannot be undone.
                        </p>
                        <form action="${pageContext.request.contextPath}/salesman/order/cancel"
                              method="post"
                              onsubmit="return confirm('Are you sure you want to CANCEL this order? This cannot be undone!');">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <button type="submit" class="btn btn-danger btn-lg btn-block">
                                <i class="fas fa-times-circle mr-2"></i>Cancel Order
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Locked Order Notice -->
    <c:if test="${order.status != 'DRAFT'}">
        <div class="alert alert-info shadow-sm" role="alert">
            <h5 class="alert-heading">
                <i class="fas fa-lock mr-2"></i>Order ${order.status}
            </h5>
            <p class="mb-0">
                <c:choose>
                    <c:when test="${order.status == 'CANCELLED'}">
                        This order has been cancelled and cannot be modified.
                    </c:when>
                    <c:otherwise>
                        This order has been submitted and cannot be modified.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
    </c:if>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto-uppercase coupon input
    document.querySelector('.coupon-input-uppercase')?.addEventListener('input', function(e) {
        this.value = this.value.toUpperCase();
    });
    
    // Auto-dismiss alerts after 5 seconds
    setTimeout(function() {
        const alerts = document.querySelectorAll('.alert-dismissible');
        alerts.forEach(alert => {
            const bsAlert = new bootstrap.Alert(alert);
            bsAlert.close();
        });
    }, 5000);
</script>
</body>
</html>
