<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Processing - Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .info-label {
            font-weight: 600;
            color: #6c757d;
            font-size: 0.875rem;
            text-transform: uppercase;
            letter-spacing: 0.5px;
            margin-bottom: 0.25rem;
        }

        .info-value {
            font-size: 1.125rem;
            color: #212529;
            font-weight: 500;
        }

        .section-divider {
            border-top: 2px solid #e9ecef;
            margin: 1.5rem 0;
        }

        .status-timeline {
            position: relative;
            padding-left: 2rem;
        }

        .status-timeline::before {
            content: '';
            position: absolute;
            left: 0.5rem;
            top: 0;
            bottom: 0;
            width: 2px;
            background: #dee2e6;
        }

        .timeline-item {
            position: relative;
            padding-bottom: 1.5rem;
        }

        .timeline-dot {
            position: absolute;
            left: -1.4rem;
            width: 1rem;
            height: 1rem;
            border-radius: 50%;
            border: 2px solid #dee2e6;
            background: white;
        }

        .timeline-dot.active {
            background: #28a745;
            border-color: #28a745;
        }

        .action-card {
            transition: all 0.3s ease;
            cursor: pointer;
        }

        .action-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 0.5rem 1rem rgba(0, 0, 0, 0.15) !important;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Breadcrumb & Header -->
    <div class="mb-4">
        <nav aria-label="breadcrumb">
            <ol class="breadcrumb bg-transparent p-0 mb-2">
                <li class="breadcrumb-item"><a href="${pageContext.request.contextPath}/warehouse/orders">Order
                    Queue</a></li>
                <li class="breadcrumb-item active">${order.orderCode}</li>
            </ol>
        </nav>
        <div class="d-flex justify-content-between align-items-center">
            <div>
                <h2 class="font-weight-bold text-dark mb-1">
                    <i class="fas fa-clipboard-check mr-2"></i>Order Processing
                </h2>
                <p class="text-muted mb-0">Review and process order fulfillment</p>
            </div>
            <a href="${pageContext.request.contextPath}/warehouse/orders" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left mr-2"></i>Back to Queue
            </a>
        </div>
    </div>

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
                            <div class="info-label">Order Code</div>
                            <div class="info-value text-primary">
                                <i class="fas fa-hashtag mr-1"></i>${order.orderCode}
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="info-label">
                                <i class="fas fa-user-tie mr-1"></i>Submitted By
                            </div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${order.createdBy != null}">
                                        ${order.createdBy.fullName != null ? order.createdBy.fullName : 'unknown'}
                                    </c:when>
                                    <c:otherwise>
                                        Unknown
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="info-label">Status</div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${order.status == 'SUBMITTED'}">
                                        <span class="badge badge-warning badge-lg px-3 py-2">
                                            <i class="fas fa-paper-plane mr-1"></i>SUBMITTED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'PROCESSING'}">
                                        <span class="badge badge-info badge-lg px-3 py-2">
                                            <i class="fas fa-cog fa-spin mr-1"></i>PROCESSING
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'COMPLETED'}">
                                        <span class="badge badge-success badge-lg px-3 py-2">
                                            <i class="fas fa-check-circle mr-1"></i>COMPLETED
                                        </span>
                                    </c:when>
                                    <c:when test="${order.status == 'CANCELLED'}">
                                        <span class="badge badge-danger badge-lg px-3 py-2">
                                            <i class="fas fa-times-circle mr-1"></i>CANCELLED
                                        </span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-secondary badge-lg px-3 py-2">${order.status}</span>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <div class="section-divider"></div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <div class="info-label">
                                <i class="fas fa-user mr-1"></i>Customer Name
                            </div>
                            <div class="info-value">${order.customerName}</div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="info-label">
                                <i class="fas fa-phone mr-1"></i>Customer Phone
                            </div>
                            <div class="info-value">${order.customerPhone != null ? order.customerPhone : 'N/A'}</div>
                        </div>
                    </div>

                    <div class="section-divider"></div>

                    <div class="row">
                        <div class="col-md-6 mb-3">
                            <div class="info-label">
                                <i class="fas fa-calendar-plus mr-1"></i>Submitted Date
                            </div>
                            <div class="info-value">
                                <fmt:formatDate value="${order.createdAt}" pattern="dd MMM yyyy, HH:mm"/>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="info-label">
                                <i class="fas fa-user-tie mr-1"></i>Submitted By
                            </div>
                            <div class="info-value">
                                <c:choose>
                                    <c:when test="${order.createdBy != null}">
                                        ${order.createdBy.fullName != null ? order.createdBy.fullName : 'unknown'}
                                    </c:when>
                                    <c:otherwise>
                                        Unknown
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>

                    <c:if test="${order.note != null && !order.note.isEmpty()}">
                        <div class="section-divider"></div>
                        <div class="alert alert-light mb-0" role="alert">
                            <div class="info-label mb-2">
                                <i class="fas fa-sticky-note mr-1"></i>Order Notes
                            </div>
                            <p class="mb-0">${order.note}</p>
                        </div>
                    </c:if>
                </div>
            </div>

            <!-- Items Table -->
            <div class="card shadow-sm">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">
                        <i class="fas fa-boxes mr-2"></i>Items to Fulfill
                        <span class="badge badge-light text-dark ml-2">${items.size()} items</span>
                    </h5>
                </div>
                <div class="card-body p-0">
                    <div class="table-responsive">
                        <table class="table table-hover mb-0">
                            <thead class="thead-light">
                            <tr>
                                <th class="py-3 px-4" width="5%">#</th>
                                <th class="py-3 px-4" width="40%">Product</th>
                                <th class="py-3 px-4 text-right" width="20%">Unit Price</th>
                                <th class="py-3 px-4 text-center" width="15%">Quantity</th>
                                <th class="py-3 px-4 text-right" width="20%">Subtotal</th>
                            </tr>
                            </thead>
                            <tbody>
                            <c:set var="total" value="0"/>
                            <c:forEach items="${items}" var="item" varStatus="status">
                                <c:set var="subtotal" value="${item.product.price * item.quantity}"/>
                                <c:set var="total" value="${total + subtotal}"/>
                                <tr>
                                    <td class="px-4 align-middle text-muted">${status.index + 1}</td>
                                    <td class="px-4 align-middle">
                                        <div class="font-weight-bold">${item.product.name}</div>
                                        <small class="text-muted">SKU: ${item.product.id}</small>
                                    </td>
                                    <td class="px-4 align-middle text-right">
                                        <fmt:formatNumber value="${item.product.price}" type="number"
                                                          groupingUsed="true"/> VND
                                    </td>
                                    <td class="px-4 align-middle text-center">
                                        <span class="badge badge-primary badge-pill px-3 py-2">${item.quantity}</span>
                                    </td>
                                    <td class="px-4 align-middle text-right font-weight-bold">
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
                    <div class="d-flex justify-content-between mb-3">
                <span class="text-muted">
                    <i class="fas fa-tag mr-1"></i>Coupon Applied:
                </span>
                        <span class="${order.coupon != null ? 'text-success font-weight-bold' : 'text-muted'}">
                            ${order.coupon != null ? order.coupon.code : 'None'}
                        </span>
                    </div>

                    <c:if test="${order.coupon != null}">
                        <div class="alert alert-success mb-3" role="alert">
                            <i class="fas fa-tag mr-2"></i>
                            <strong>${order.coupon.code}</strong>: ${order.coupon.description}
                        </div>

                        <div class="d-flex justify-content-between mb-3">
                            <span class="text-muted">Discount:</span>
                            <span class="text-success font-weight-bold">
                            <c:choose>
                                <c:when test="${order.coupon.discountType == 'PERCENTAGE'}">
                                    -${order.coupon.discountValue}%
                                </c:when>
                                <c:otherwise>
                                    -<fmt:formatNumber value="${order.coupon.discountValue}" type="number"
                                                       groupingUsed="true"/> VND
                                </c:otherwise>
                            </c:choose>
                        </span>
                        </div>
                    </c:if>

                    <c:if test="${order.coupon == null}">
                        <c:set var="finalTotal" value="${total}"/>
                    </c:if>

                    <hr class="my-3">

                    <div class="d-flex justify-content-between align-items-center">
                        <span class="h5 mb-0 font-weight-bold">Total:</span>
                        <span class="h4 mb-0 text-primary font-weight-bold">
        <fmt:formatNumber value="${finalTotal}" type="number" groupingUsed="true"/> VND
    </span>
                    </div>
                </div>
            </div>

            <!-- Status Timeline -->
            <div class="card shadow-sm mb-4">
                <div class="card-header bg-white border-bottom">
                    <h5 class="mb-0 font-weight-bold">
                        <i class="fas fa-history text-info mr-2"></i>Order Timeline
                    </h5>
                </div>
                <div class="card-body">
                    <div class="status-timeline">
                        <div class="timeline-item">
                            <div class="timeline-dot ${order.status == 'SUBMITTED' || order.status == 'PROCESSING' || order.status == 'COMPLETED' ? 'active' : ''}"></div>
                            <small class="text-muted d-block">Step 1</small>
                            <strong>Submitted</strong>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-dot ${order.status == 'PROCESSING' || order.status == 'COMPLETED' ? 'active' : ''}"></div>
                            <small class="text-muted d-block">Step 2</small>
                            <strong>Processing</strong>
                        </div>
                        <div class="timeline-item">
                            <div class="timeline-dot ${order.status == 'COMPLETED' ? 'active' : ''}"></div>
                            <small class="text-muted d-block">Step 3</small>
                            <strong>Completed</strong>
                        </div>
                    </div>
                </div>
            </div>

            <!-- Action Buttons -->
            <div class="card shadow-sm border-0">
                <div class="card-header bg-gradient-primary text-white">
                    <h5 class="mb-0 font-weight-bold text-dark">
                        <i class="fas fa-tasks mr-2"></i>Available Actions
                    </h5>
                </div>
                <div class="card-body p-3">
                    <c:choose>
                        <%-- SUBMITTED: Can start processing or cancel --%>
                        <c:when test="${order.status == 'SUBMITTED'}">
                            <form action="${pageContext.request.contextPath}/warehouse/order/process"
                                  method="post"
                                  onsubmit="return confirm('Start processing this order?');"
                                  class="mb-3">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <button type="submit" class="btn btn-primary btn-block btn-lg action-card">
                                    <i class="fas fa-play-circle mr-2"></i>Start Processing
                                </button>
                            </form>

                            <div class="card border-danger">
                                <div class="card-body p-3">
                                    <h6 class="text-danger mb-3">
                                        <i class="fas fa-exclamation-triangle mr-2"></i>Cancel Order
                                    </h6>
                                    <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                          method="post"
                                          onsubmit="return confirm('Are you sure you want to cancel this order? This cannot be undone!');">
                                        <input type="hidden" name="orderId" value="${order.id}"/>

                                        <div class="form-group mb-3">
                                            <label for="cancelNote" class="font-weight-bold small">
                                                <i class="fas fa-comment mr-1"></i>Cancellation Reason *
                                            </label>
                                            <textarea class="form-control form-control-sm"
                                                      id="cancelNote"
                                                      name="note"
                                                      rows="3"
                                                      placeholder="Required: Why is this order being cancelled?"
                                                      required></textarea>
                                            <small class="form-text text-muted">
                                                Provide a clear reason for the cancellation
                                            </small>
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
                            <form action="${pageContext.request.contextPath}/warehouse/order/complete"
                                  method="post"
                                  onsubmit="return confirm('Mark this order as completed?');"
                                  class="mb-3">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <button type="submit" class="btn btn-success btn-block btn-lg action-card">
                                    <i class="fas fa-check-circle mr-2"></i>Mark as Completed
                                </button>
                            </form>

                            <div class="card border-danger">
                                <div class="card-body p-3">
                                    <h6 class="text-danger mb-3">
                                        <i class="fas fa-exclamation-triangle mr-2"></i>Cancel Order
                                    </h6>
                                    <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                          method="post"
                                          onsubmit="return confirm('Are you sure you want to cancel this order? This cannot be undone!');">
                                        <input type="hidden" name="orderId" value="${order.id}"/>

                                        <div class="form-group mb-3">
                                            <label for="cancelNoteProcessing" class="font-weight-bold small">
                                                <i class="fas fa-comment mr-1"></i>Cancellation Reason *
                                            </label>
                                            <textarea class="form-control form-control-sm"
                                                      id="cancelNoteProcessing"
                                                      name="note"
                                                      rows="3"
                                                      placeholder="Required: Why is this order being cancelled?"
                                                      required></textarea>
                                            <small class="form-text text-muted">
                                                Provide a clear reason for the cancellation
                                            </small>
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
                                            <small class="text-muted d-block mt-2">
                                                <strong>Reason:</strong><br>
                                                    ${order.note.substring(10).trim()}
                                            </small>
                                        </c:if>
                                    </c:when>
                                    <c:when test="${order.status == 'DRAFT'}">
                                        <strong>Draft Order</strong><br>
                                        This order is still being prepared by the salesman.
                                    </c:when>
                                    <c:otherwise>
                                        <strong>Status: ${order.status}</strong><br>
                                        No actions available for this order status.
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
</body>
</html>