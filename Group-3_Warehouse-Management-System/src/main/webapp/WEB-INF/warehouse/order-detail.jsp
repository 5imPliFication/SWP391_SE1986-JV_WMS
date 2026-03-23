<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="currency" uri="http://example.com/functions/currency" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Processing - Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .order-description-block {
            min-height: 40px;
            padding: 1rem;
            border: 1px solid #dee2e6;
            border-radius: 0.5rem;
            background-color: #f8f9fa;
            line-height: 1.5;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <!-- Breadcrumb & Header -->
    <div class="mb-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb bg-transparent p-0 mb-2">
                <li class="breadcrumb-item">
                    <a href="${pageContext.request.contextPath}/warehouse/orders">Order Queue</a>
                </li>
                <li class="breadcrumb-item active">${order.orderCode}</li>
            </ol>
        </nav>
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h2 class="font-weight-bold text-dark mb-1">
                    <i class="fas fa-clipboard-check mr-2"></i>Order Processing
                </h2>
            </div>
            <a href="${pageContext.request.contextPath}/warehouse/orders" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left mr-2"></i>Back to Queue
            </a>
        </div>
    </div>

    <!-- Success/Error Messages -->
    <c:if test="${not empty param.success}">
        <div class="alert alert-success alert-dismissible fade show" role="alert">
            <i class="fas fa-check-circle mr-2"></i>${param.success}
            <button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
        </div>
    </c:if>

    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle mr-2"></i>${param.error}
            <button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Main Order Information Panel -->
    <div class="row mb-4">
        <!-- Left Panel: Order Details -->
        <div class="col-lg-8">
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white border-bottom">
                    <h5 class="mb-0 font-weight-bold">
                        <i class="fas fa-file-invoice text-primary mr-2"></i>Order Information
                    </h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">Order Code</p>
                            <h5 class="text-primary mb-0">
                                <i class="fas fa-hashtag mr-1"></i>${order.orderCode}
                            </h5>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">Status</p>
                            <div>
                                <c:choose>
                                    <c:when test="${order.status == 'SUBMITTED'}">
                                        <span class="badge badge-warning badge-pill px-3 py-2">
                                            <i class="fas fa-paper-plane mr-1"></i>SUBMITTED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'PROCESSING'}">
                                        <span class="badge badge-info badge-pill px-3 py-2">
                                            <i class="fas fa-cog fa-spin mr-1"></i>PROCESSING
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge badge-success badge-pill px-3 py-2">
                                            <i class="fas fa-check-circle mr-1"></i>COMPLETED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <span class="badge badge-danger badge-pill px-3 py-2">
                                            <i class="fas fa-times-circle mr-1"></i>CANCELLED
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary badge-pill px-3 py-2">${order.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <hr class="my-3">

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">
                                <i class="fas fa-user mr-1"></i>Customer Name
                            </p>
                            <h6 class="mb-0">${order.customerName}</h6>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">
                                <i class="fas fa-phone mr-1"></i>Customer Phone
                            </p>
                            <h6 class="mb-0">${order.customerPhone != null ? order.customerPhone : 'N/A'}</h6>
                        </div>
                    </div>

                    <hr class="my-3">

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">
                                <i class="fas fa-calendar-plus mr-1"></i>Submitted Date
                            </p>
                            <h6 class="mb-0">
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, HH:mm"/>
                            </h6>
                        </div>
                        <div class="col-md-6 mb-3">
                            <p class="text-muted mb-1 small font-weight-bold text-uppercase">
                                <i class="fas fa-user-tie mr-1"></i>Submitted By
                            </p>
                            <h6 class="mb-0">
                                <c:choose>
                                    <c:when test="${order.createdBy != null && order.createdBy.fullName != null}">
                                        ${order.createdBy.fullName}
                                    </c:when>
                                    <c:otherwise>
                                        Unknown
                                    </c:otherwise>
                                </c:choose>
                            </h6>
                        </div>
                    </div>

                    <hr class="my-3">
                    <p class="text-muted mb-2 small font-weight-bold text-uppercase">
                        <i class="fas fa-sticky-note mr-1"></i>Order Description
                    </p>
                    <div class="order-description-block">
                        <c:choose>
                            <c:when test="${not empty order.note}">
                                ${order.note}
                            </c:when>
                            <c:otherwise>
                                No description provided.
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <!-- Items Table -->
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-boxes mr-2"></i>Items to Fulfill
                        <span class="badge badge-light text-dark ml-2">${totalItemCount} items</span>
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="thead-light">
                            <tr>
                                <th class="py-3 px-4" width="5%">#</th>
                                <th class="py-3 px-4" width="35%">Product</th>
                                <th class="py-3 px-4 text-right" width="20%">Price at Purchase</th>
                                <th class="py-3 px-4 text-center" width="15%">Quantity</th>
                                <th class="py-3 px-4 text-right" width="25%">Subtotal</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach items="${items}" var="item" varStatus="status">
                                <%-- Calculate subtotal using quantity from OrderItem and price at purchase --%>
                                <c:set var="subtotal" value="${item.priceAtPurchase * item.quantity}"/>
                                <c:set var="total" value="${total + subtotal}"/>
                                <tr>
                                    <td class="px-4 align-middle text-muted">${(itemPage - 1) * itemPageSize + status.index + 1}</td>
                                    <td class="px-4 align-middle">
                                            <%-- Product name from Product entity --%>
                                        <div class="font-weight-bold">${item.product.name}</div>
                                            <%-- Show inventory status based on order state --%>
                                        <c:if test="${order.status == 'PROCESSING' || order.status == 'COMPLETED'}">
                                            <br><small class="text-warning">
                                            <i class="fas fa-exclamation-triangle mr-1"></i>
                                            Stock Reduced (${item.quantity} units consumed)
                                        </small>
                                        </c:if>
                                    </td>
                                    <td class="px-4 align-middle text-right">
                                            ${currency:format(item.priceAtPurchase)} VND
                                            <%-- Current price from ProductItem.currentPrice --%>
                                        <c:if test="${item.priceAtPurchase != item.productItem.currentPrice}">
                                            <br><small class="text-warning">
                                            <i class="fas fa-info-circle"></i> Current:
                                                ${currency:format(item.productItem.currentPrice)} VND
                                        </small>
                                        </c:if>
                                    </td>
                                    <td class="px-4 align-middle text-center">
                                            <%-- Order quantity from OrderItem.quantity --%>
                                        <span class="badge badge-primary badge-pill px-3 py-2">${item.quantity}</span>
                                    </td>
                                    <td class="px-4 align-middle text-right font-weight-bold text-primary">
                                        <fmt:formatNumber value="${subtotal}" type="number" groupingUsed="true"/> VND
                                    </td>
                                </tr>
                            </c:forEach>

                            <c:if test="${empty items}">
                                <tr>
                                    <td colspan="5" class="text-center py-5">
                                        <i class="fas fa-box-open fa-3x text-muted mb-3 d-block"></i>
                                        <h5 class="text-muted">No Items Found</h5>
                                    </td>
                                </tr>
                            </c:if>
                            </tbody>
                        </table>
                    </div>
                </div>

                <c:if test="${totalItemPages > 1}">
                    <nav class="p-3 border-top" aria-label="Order items pagination">
                        <ul class="pagination pagination-sm justify-content-center mb-0">
                            <li class="page-item ${itemPage == 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}&itemPage=${itemPage - 1}"
                                   aria-label="Previous">&laquo;</a>
                            </li>

                            <c:forEach begin="1" end="${totalItemPages}" var="i">
                                <c:if test="${i == 1 || i == totalItemPages || (i >= itemPage - 2 && i <= itemPage + 2)}">
                                    <li class="page-item ${i == itemPage ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}&itemPage=${i}">${i}</a>
                                    </li>
                                </c:if>
                                <c:if test="${i == itemPage - 3 || i == itemPage + 3}">
                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                </c:if>
                            </c:forEach>

                            <li class="page-item ${itemPage == totalItemPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}&itemPage=${itemPage + 1}"
                                   aria-label="Next">&raquo;</a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>

        <!-- Right Panel: Summary & Actions -->
        <div class="col-lg-4">
            <!-- Order Summary -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white border-bottom">
                    <h5 class="mb-0 font-weight-bold">
                        <i class="fas fa-calculator text-success mr-2"></i>Order Summary
                    </h5>
                </div>
                <div class="card-body">
                    <!-- Subtotal -->
                    <div class="d-flex justify-content-between mb-2">
                        <span class="text-muted">Subtotal:</span>
                        <span class="font-weight-bold">
                            ${currency:format(order.total)} VND
                        </span>
                    </div>

                    <hr class="my-3">

                    <!-- Final Total -->
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="h5 mb-0 font-weight-bold">Total:</span>
                        <span class="h4 mb-0 text-primary font-weight-bold">
                            ${currency:format(order.total)} VND
                        </span>
                    </div>

                </div>
            </div>

            <!-- Inventory Note (Important!) -->
            <c:if test="${order.status == 'DRAFT' || order.status == 'SUBMITTED'}">
                <div class="alert alert-warning mb-4" role="alert">
                    <h6 class="alert-heading">
                        <i class="fas fa-info-circle mr-2"></i>Inventory Status
                    </h6>
                    <small>
                        <strong>Warehouse inventory NOT yet consumed.</strong>
                        <br>Product quantities will be automatically reduced when you move this order to "PROCESSING"
                        status.
                    </small>
                </div>
            </c:if>

            <c:if test="${order.status == 'PROCESSING'}">
                <div class="alert alert-success mb-4" role="alert">
                    <h6 class="alert-heading">
                        <i class="fas fa-check-circle mr-2"></i>Inventory Consumed
                    </h6>
                    <small>
                        <i class="fas fa-check mr-1"></i>Warehouse inventory has been reduced by the order quantities.
                        <br><i class="fas fa-undo mr-1"></i>If cancelled, inventory will be restored.
                    </small>
                </div>
            </c:if>

            <c:if test="${order.status == 'COMPLETED'}">
                <div class="alert alert-info mb-4" role="alert">
                    <h6 class="alert-heading">
                        <i class="fas fa-box mr-2"></i>Order Fulfilled
                    </h6>
                    <small>
                        This order has been completed. Warehouse inventory quantity remains reduced.
                    </small>
                </div>
            </c:if>

            <!-- Status Timeline -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white border-bottom">
                    <h5 class="mb-0 font-weight-bold">
                        <i class="fas fa-history text-info mr-2"></i>Order Timeline
                    </h5>
                </div>
                <div class="card-body">
                    <div class="pl-3">
                        <div class="pb-3">
                            <c:set var="step1Active"
                                   value="${order.status == 'SUBMITTED' || order.status == 'PROCESSING' || order.status == 'COMPLETED'}"/>
                            <div class="d-flex align-items-center mb-2">
                                <div class="mr-3">
                                    <c:choose>
                                        <c:when test="${step1Active}">
                                            <i class="fas fa-check-circle text-success fa-2x"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="far fa-circle text-muted fa-2x"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    <small class="text-muted d-block">Step 1</small>
                                    <strong class="${step1Active ? 'text-success' : ''}">Submitted</strong>
                                    <br><small class="text-muted">Stock reserved</small>
                                </div>
                            </div>
                        </div>

                        <div class="pb-3">
                            <c:set var="step2Active"
                                   value="${order.status == 'PROCESSING' || order.status == 'COMPLETED'}"/>
                            <div class="d-flex align-items-center mb-2">
                                <div class="mr-3">
                                    <c:choose>
                                        <c:when test="${step2Active}">
                                            <i class="fas fa-check-circle text-success fa-2x"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="far fa-circle text-muted fa-2x"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    <small class="text-muted d-block">Step 2</small>
                                    <strong class="${step2Active ? 'text-success' : ''}">Processing</strong>
                                    <br><small class="text-muted">Stock reduced from product_items</small>
                                </div>
                            </div>
                        </div>

                        <div>
                            <c:set var="step3Active" value="${order.status == 'COMPLETED'}"/>
                            <div class="d-flex align-items-center">
                                <div class="mr-3">
                                    <c:choose>
                                        <c:when test="${step3Active}">
                                            <i class="fas fa-check-circle text-success fa-2x"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="far fa-circle text-muted fa-2x"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </div>
                                <div>
                                    <small class="text-muted d-block">Step 3</small>
                                    <strong class="${step3Active ? 'text-success' : ''}">Completed</strong>
                                    <br><small class="text-muted">Order fulfilled</small>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="card shadow-sm border-0">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0 font-weight-bold">
                        <i class="fas fa-tasks mr-2"></i>Available Actions
                    </h5>
                </div>
                <div class="card-body p-3">
                    <c:choose>
                        <%-- SUBMITTED: Can start processing or cancel --%>
                        <c:when test="${order.status == 'SUBMITTED'}">
                            <a href="${pageContext.request.contextPath}/export?orderId=${order.id}"
                               class="btn btn-success btn-lg btn-block mb-3">
                                <i class="fas fa-barcode mr-2"></i>Start Processing
                            </a>

                            <div class="card border-danger">
                                <div class="card-body p-3">
                                    <h6 class="text-danger mb-3">
                                        <i class="fas fa-exclamation-triangle mr-2"></i>Cancel Order
                                    </h6>
                                    <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                          method="post"
                                          onsubmit="return confirm('Cancel this order? This cannot be undone.');">
                                        <input type="hidden" name="orderId" value="${order.id}"/>

                                        <div class="form-group mb-3">
                                            <label for="cancelNote" class="font-weight-bold small">
                                                Cancellation Reason *
                                            </label>
                                            <textarea class="form-control form-control-sm"
                                                      id="cancelNote"
                                                      name="note"
                                                      rows="3"
                                                      placeholder="Why is this order being cancelled?"
                                                      required></textarea>
                                        </div>

                                        <button type="submit" class="btn btn-danger btn-block">
                                            <i class="fas fa-times-circle mr-2"></i>Cancel Order
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:when>

                        <%-- PROCESSING: Can complete or cancel --%>
                        <c:when test="${order.status == 'PROCESSING'}">

                            <div class="card border-danger">
                                <div class="card-body p-3">
                                    <h6 class="text-danger mb-3">
                                        <i class="fas fa-exclamation-triangle mr-2"></i>Cancel Order
                                    </h6>
                                    <p class="small text-muted mb-3">
                                        <i class="fas fa-undo mr-1"></i>
                                        Product item quantities will be restored if canceled.
                                    </p>
                                    <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                          method="post"
                                          onsubmit="return confirm('Cancel this order? Stock will be restored to product_items.');">
                                        <input type="hidden" name="orderId" value="${order.id}"/>

                                        <div class="form-group mb-3">
                                            <label for="cancelNoteProcessing" class="font-weight-bold small">
                                                Cancellation Reason *
                                            </label>
                                            <textarea class="form-control form-control-sm"
                                                      id="cancelNoteProcessing"
                                                      name="note"
                                                      rows="3"
                                                      placeholder="Why is this order being cancelled?"
                                                      required></textarea>
                                        </div>

                                        <button type="submit" class="btn btn-danger btn-block">
                                            <i class="fas fa-times-circle mr-2"></i>Cancel Order
                                        </button>
                                    </form>
                                </div>
                            </div>
                        </c:when>

                        <%-- COMPLETED or CANCELLED: No actions available --%>
                        <c:otherwise>
                            <div class="alert alert-info mb-0" role="alert">
                                <i class="fas fa-info-circle mr-2"></i>
                                <c:choose>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <strong>Order Completed</strong><br>
                                        This order has been fulfilled successfully.
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <strong>Order Cancelled</strong><br>
                                        This order has been cancelled.
                                        <c:if test="${not empty order.note && order.note.startsWith('CANCELLED:')}">
                                            <hr class="my-2">
                                            <small class="d-block mt-2">
                                                <strong>Reason:</strong><br>
                                                    ${order.note.substring(10).trim()}
                                            </small>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${order.status == 'DRAFT'}">
                                        <strong>Draft Order</strong><br>
                                        This order is still being prepared.
                                    </c:when>
                                    <c:otherwise>
                                        <strong>Status: ${order.status}</strong><br>
                                        No actions available.
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </c:otherwise>
                    </c:choose>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto-dismiss alerts after 5 seconds
    setTimeout(function () {
        $('.alert-dismissible').fadeOut('slow');
    }, 5000);
</script>
</body>
</html>