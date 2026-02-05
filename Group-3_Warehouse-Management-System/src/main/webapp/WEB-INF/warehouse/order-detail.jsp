<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Detail - Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
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
        <a href="${pageContext.request.contextPath}/warehouse/orders"
           class="btn btn-secondary">
            <i class="fas fa-arrow-left mr-2"></i>Back to Queue
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
                        <c:when test="${order.status == 'SUBMITTED'}">
                            <span class="badge badge-info badge-pill px-3 py-2">
                                <i class="fas fa-paper-plane mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'PROCESSING'}">
                            <span class="badge badge-warning badge-pill px-3 py-2">
                                <i class="fas fa-spinner mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'APPROVED'}">
                            <span class="badge badge-success badge-pill px-3 py-2">
                                <i class="fas fa-check-circle mr-1"></i>${order.status}
                            </span>
                        </c:when>
                        <c:when test="${order.status == 'FLAGGED'}">
                            <span class="badge badge-danger badge-pill px-3 py-2">
                                <i class="fas fa-flag mr-1"></i>${order.status}
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
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="total" value="0" />
                    <c:forEach items="${items}" var="i" varStatus="status">
                        <c:set var="subtotal" value="${i.product.price * i.quantity}" />
                        <c:set var="total" value="${total + subtotal}" />
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
                        </tr>
                    </c:forEach>

                    <c:if test="${empty items}">
                        <tr>
                            <td colspan="5" class="text-center py-5">
                                <div class="text-muted">
                                    <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                    <h5>No Items in Order</h5>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>
        </div>
    </div>

    <!-- Order Total -->
    <c:if test="${not empty items}">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <div class="row justify-content-end">
                    <div class="col-md-4">
                        <div class="card bg-light">
                            <div class="card-body">
                                <div class="d-flex justify-content-between">
                                    <span class="h5 mb-0 font-weight-bold">Order Total:</span>
                                    <span class="h4 mb-0 text-primary font-weight-bold">
                                        <fmt:formatNumber value="${total}" type="number" groupingUsed="true"/> VND
                                    </span>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Action Forms (Only for PROCESSING status) -->
    <c:if test="${order.status == 'PROCESSING'}">
        <div class="row">
            <div class="col-md-6 mb-3">
                <div class="card shadow-sm border-success h-100">
                    <div class="card-header bg-success text-white">
                        <h5 class="mb-0"><i class="fas fa-check-circle mr-2"></i>Mark as Completed</h5>
                    </div>
                    <div class="card-body text-center d-flex flex-column justify-content-center">
                        <p class="mb-4">All items are in stock and ready to fulfill.</p>
                        <form action="${pageContext.request.contextPath}/warehouse/order/complete"
                              method="post"
                              onsubmit="return confirm('Are you sure this order requirement is fulfilled?');">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <button type="submit" class="btn btn-success btn-lg btn-block">
                                <i class="fas fa-check-circle mr-2"></i>Complete Order
                            </button>
                        </form>
                    </div>
                </div>
            </div>

            <div class="col-md-6 mb-3">
                <div class="card shadow-sm border-danger h-100">
                    <div class="card-header bg-danger text-white">
                        <h5 class="mb-0"><i class="fas fa-flag mr-2"></i>Cancel Order</h5>
                    </div>
                    <div class="card-body">
                        <p class="mb-3">Report issues with this order:</p>
                        <form action="${pageContext.request.contextPath}/order/cancel"
                              method="post"
                              onsubmit="return confirm('Are you sure you want to cancel this order?');">
                            <input type="hidden" name="orderId" value="${order.id}"/>

                            <div class="form-group">
                                <label for="note" class="font-weight-bold">
                                    <i class="fas fa-comment mr-1"></i>Reason
                                </label>
                                <textarea class="form-control"
                                          id="note"
                                          name="note"
                                          rows="3"
                                          placeholder="Explain why this order needs attention...">
                                </textarea>
                            </div>

                            <button type="submit" class="btn btn-danger btn-lg btn-block">
                                <i class="fas fa-flag mr-2"></i>Cancel Order
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Order Status Notice -->
    <c:if test="${order.status != 'PROCESSING'}">
        <div class="alert alert-info shadow-sm" role="alert">
            <h5 class="alert-heading">
                <i class="fas fa-info-circle mr-2"></i>Order Status: ${order.status}
            </h5>
            <p class="mb-0">
                <!-- Add these action buttons for different statuses in the warehouse order-detail.jsp -->

                <!-- Action Buttons Based on Status -->
                <c:choose>
                    <%-- SUBMITTED orders can be set to PROCESSING or CANCELLED --%>
                <c:when test="${order.status == 'SUBMITTED'}">
            <div class="row">
                <div class="col-md-6 mb-3">
                    <div class="card shadow-sm border-primary h-100">
                        <div class="card-header bg-primary text-white">
                            <h5 class="mb-0"><i class="fas fa-play-circle mr-2"></i>Start Processing</h5>
                        </div>
                        <div class="card-body text-center d-flex flex-column justify-content-center">
                            <p class="mb-4">Begin processing this order</p>
                            <form action="${pageContext.request.contextPath}/warehouse/order/process"
                                  method="post"
                                  onsubmit="return confirm('Start processing this order?');">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <button type="submit" class="btn btn-primary btn-lg btn-block">
                                    <i class="fas fa-play-circle mr-2"></i>Start Processing
                                </button>
                            </form>
                        </div>
                    </div>
                </div>

                <div class="col-md-6 mb-3">
                    <div class="card shadow-sm border-danger h-100">
                        <div class="card-header bg-danger text-white">
                            <h5 class="mb-0"><i class="fas fa-times-circle mr-2"></i>Cancel Order</h5>
                        </div>
                        <div class="card-body text-center d-flex flex-column justify-content-center">
                            <p class="mb-4">Cancel this order</p>
                            <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                  method="post"
                                  onsubmit="return confirm('Are you sure you want to cancel this order?');">
                                <input type="hidden" name="orderId" value="${order.id}"/>
                                <button type="submit" class="btn btn-danger btn-lg btn-block">
                                    <i class="fas fa-times-circle mr-2"></i>Cancel Order
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            </c:when>

                <%-- PROCESSING orders can be COMPLETED or CANCELLED --%>
            <c:when test="${order.status == 'PROCESSING'}">
                <div class="row">
                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-success h-100">
                            <div class="card-header bg-success text-white">
                                <h5 class="mb-0"><i class="fas fa-check-circle mr-2"></i>Complete Order</h5>
                            </div>
                            <div class="card-body text-center d-flex flex-column justify-content-center">
                                <p class="mb-4">Mark this order as completed</p>
                                <form action="${pageContext.request.contextPath}/warehouse/order/complete"
                                      method="post"
                                      onsubmit="return confirm('Mark this order as completed?');">
                                    <input type="hidden" name="orderId" value="${order.id}"/>
                                    <button type="submit" class="btn btn-success btn-lg btn-block">
                                        <i class="fas fa-check-circle mr-2"></i>Complete Order
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>

                    <div class="col-md-6 mb-3">
                        <div class="card shadow-sm border-danger h-100">
                            <div class="card-header bg-danger text-white">
                                <h5 class="mb-0"><i class="fas fa-times-circle mr-2"></i>Cancel Order</h5>
                            </div>
                            <div class="card-body text-center d-flex flex-column justify-content-center">
                                <p class="mb-4">Cancel this order</p>
                                <form action="${pageContext.request.contextPath}/warehouse/order/cancel"
                                      method="post"
                                      onsubmit="return confirm('Are you sure you want to cancel this order?');">
                                    <input type="hidden" name="orderId" value="${order.id}"/>
                                    <button type="submit" class="btn btn-danger btn-lg btn-block">
                                        <i class="fas fa-times-circle mr-2"></i>Cancel Order
                                    </button>
                                </form>
                            </div>
                        </div>
                    </div>
                </div>
            </c:when>

                <%-- COMPLETED and CANCELLED orders show status only --%>
            <c:otherwise>
                <div class="alert alert-info shadow-sm" role="alert">
                    <h5 class="alert-heading">
                        <i class="fas fa-info-circle mr-2"></i>Order ${order.status}
                    </h5>
                    <p class="mb-0">
                        <c:choose>
                            <c:when test="${order.status == 'COMPLETED'}">
                                This order has been completed successfully.
                            </c:when>
                            <c:when test="${order.status == 'CANCELLED'}">
                                This order has been cancelled.
                            </c:when>
                            <c:when test="${order.status == 'DRAFT'}">
                                This order is still in draft mode.
                            </c:when>
                            <c:otherwise>
                                No actions available for this order status.
                            </c:otherwise>
                        </c:choose>
                    </p>
                </div>
            </c:otherwise>
            </c:choose>
            </p>
        </div>
    </c:if>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>