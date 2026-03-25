<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="currency" uri="http://example.com/functions/currency" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Order Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .quantity-controls {
            display: flex;
            align-items: center;
            justify-content: center;
            gap: 10px;
        }

        .quantity-btn {
            width: 32px;
            height: 32px;
            padding: 0;
            display: flex;
            align-items: center;
            justify-content: center;
            border-radius: 50%;
            font-size: 14px;
        }

        .quantity-display {
            min-width: 40px;
            text-align: center;
            font-weight: bold;
            font-size: 16px;
        }

        .product-card {
            transition: all 0.3s ease;
            cursor: pointer;
            border: 2px solid transparent;
        }

        .product-card:hover {
            border-color: #007bff;
            transform: translateY(-2px);
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        }

        .product-card.selected {
            border-color: #28a745;
            background-color: #f0f8f5;
        }

        .product-image {
            width: 100%;
            height: 150px;
            object-fit: cover;
            border-radius: 8px;
        }

        .search-box {
            position: sticky;
            top: 0;
            background: white;
            z-index: 10;
            padding: 15px;
            border-bottom: 1px solid #dee2e6;
        }

        .modal-body {
            max-height: 60vh;
            overflow-y: auto;
        }

        .order-description-block {
            min-height: 30px;
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

    <c:if test="${not empty param.quantityError}">
        <div class="alert alert-warning alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-triangle mr-2"></i>
            <strong>Stock Unavailable:</strong> ${param.quantityError}
            <button type="button" class="close" data-dismiss="alert">
                <span>&times;</span>
            </button>
        </div>
    </c:if>

    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="font-weight-bold text-dark">
            <i class="fas fa-file-invoice mr-2"></i>Order Detail
        </h2>
        <a href="${pageContext.request.contextPath}/salesman/orders" class="btn btn-secondary">
            <i class="fas fa-arrow-left mr-2"></i>Back to Orders
        </a>
    </div>

    <div class="alert alert-light border shadow-sm mb-4" role="alert">
        <i class="fas fa-info-circle text-primary mr-2"></i>
        <c:choose>
            <c:when test="${order.status == 'DRAFT'}">
                Use this page to review the order and manage order items before submitting.
            </c:when>
            <c:otherwise>
                This order has been submitted and is being processed by the warehouse.
            </c:otherwise>
        </c:choose>
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
                    <h5 class="font-weight-bold">${not empty order.customerName ? order.customerName : 'Not set yet'}</h5>
                    <p class="mb-0 text-muted">${not empty order.customerPhone ? order.customerPhone : 'No phone number'}</p>
                </div>
                <div class="col-md-4 mb-3">
                    <p class="text-muted mb-1">Status</p>
                    <c:choose>
                        <c:when test="${order.status == 'DRAFT'}">
                            <span class="badge badge-secondary badge-pill px-3 py-2">
                                <i class="fas fa-file mr-1"></i>DRAFT
                            </span>
                        </c:when>
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

            <c:choose>
                <c:when test="${order.status == 'DRAFT'}">
                    <div class="mt-3 border rounded p-3 bg-light">
                        <h6 class="mb-3"><i class="fas fa-user-edit mr-2"></i>Customer And Note</h6>
                        <form action="${pageContext.request.contextPath}/salesman/order/customer/update" method="post">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <input type="hidden" name="itemPage" value="${itemPage}"/>
                            <input type="hidden" name="productPage" value="${productPage}"/>
                            <input type="hidden" name="returnTo" value="detail"/>

                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="customerName">Customer Name</label>
                                    <input type="text" class="form-control" id="customerName" name="customerName"
                                           value="${order.customerName}" placeholder="Enter customer name">
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="customerPhone">Customer Phone</label>
                                    <input type="text" class="form-control" id="customerPhone" name="customerPhone"
                                           value="${order.customerPhone}" placeholder="Enter customer phone">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="note">Order Description</label>
                                <textarea class="form-control" id="note" name="note" rows="2"
                                          placeholder="Enter note">${order.note}</textarea>
                            </div>

                            <button type="submit" class="btn btn-outline-primary btn-sm">
                                <i class="fas fa-save mr-1"></i>Update
                            </button>
                        </form>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="mt-3">
                        <p class="text-muted mb-2">Order Description</p>
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
                </c:otherwise>
            </c:choose>
        </div>
    </div>

    <!-- Order Items Table -->
    <div class="card shadow-sm mb-4">
        <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
            <h5 class="mb-0">
                <i class="fas fa-box mr-2"></i>Order Items
                <span class="badge badge-light text-dark ml-2">${totalItemCount} items</span>
            </h5>
            <c:if test="${order.status == 'DRAFT'}">
                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#productModal">
                    <i class="fas fa-plus-circle mr-1"></i>Add Products
                </button>
            </c:if>
        </div>
        <div class="card-body p-0">
            <div class="table-responsive">
                <table class="table table-hover table-striped mb-0">
                    <thead class="thead-light">
                    <tr>
                        <th class="py-3 px-4" width="5%">#</th>
                        <th class="py-3 px-4" width="40%">Product Name</th>
                        <th class="py-3 px-4" width="20%">Unit Price</th>
                        <th class="py-3 px-4 text-center" width="20%">Quantity</th>
                        <th class="py-3 px-4 text-right" width="15%">Subtotal</th>
                        <c:if test="${order.status == 'DRAFT'}">
                            <th class="py-3 px-4 text-center" width="5%">Action</th>
                        </c:if>
                    </tr>
                    </thead>
                    <tbody>
                    <c:set var="total" value="0"/>
                    <c:forEach items="${items}" var="i" varStatus="status">
                        <c:set var="subtotal" value="${i.priceAtPurchase * i.quantity}"/>
                        <c:set var="total" value="${total + subtotal}"/>
                        <tr>
                            <td class="px-4 align-middle">${(itemPage - 1) * itemPageSize + status.index + 1}</td>
                            <td class="px-4 align-middle">
                                <div class="font-weight-bold">${i.product.name}</div>
                            </td>
                            <td class="px-4 align-middle">
                                ${currency:format(i.priceAtPurchase)} VND
                            </td>
                            <td class="px-4 align-middle">
                                <c:choose>
                                    <c:when test="${order.status == 'DRAFT'}">
                                        <!-- Quantity Controls -->
                                        <div class="quantity-controls">
                                            <button class="btn btn-sm btn-outline-danger quantity-btn qty-minus"
                                                    data-order-id="${order.id}" data-item-id="${i.id}" data-quantity="${i.quantity}"
                                                ${i.quantity <= 1 ? 'disabled' : ''}>
                                                <i class="fas fa-minus"></i>
                                            </button>
                                            <span class="quantity-display">${i.quantity}</span>
                                            <button class="btn btn-sm btn-outline-success quantity-btn qty-plus"
                                                    data-order-id="${order.id}" data-item-id="${i.id}" data-quantity="${i.quantity}">
                                                <i class="fas fa-plus"></i>
                                            </button>
                                        </div>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge badge-info badge-pill px-3 py-2">${i.quantity}</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>
                            <td class="px-4 align-middle text-right font-weight-bold text-primary">
                                ${currency:format(subtotal)} VND
                            </td>
                            <c:if test="${order.status == 'DRAFT'}">
                                <td class="px-4 align-middle text-center">
                                    <button class="btn btn-sm btn-outline-danger btn-remove-item"
                                            data-order-id="${order.id}" data-item-id="${i.id}">
                                        <i class="fas fa-trash"></i>
                                    </button>
                                </td>
                            </c:if>
                        </tr>
                    </c:forEach>

                    <!-- Empty State -->
                    <c:if test="${empty items}">
                        <tr>
                            <td colspan="${order.status == 'DRAFT' ? '6' : '5'}" class="text-center py-5">
                                <div class="text-muted">
                                    <i class="fas fa-box-open fa-2x mb-3 d-block"></i>
                                    <h5>No Items</h5>
                                </div>
                            </td>
                        </tr>
                    </c:if>
                    </tbody>
                </table>
            </div>

            <c:if test="${totalItemPages > 1}">
                <nav class="p-3 border-top" aria-label="Order items pagination">
                    <ul class="pagination pagination-sm justify-content-center mb-0">
                        <li class="page-item ${itemPage == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&itemPage=${itemPage - 1}">
                                &laquo;
                            </a>
                        </li>

                        <c:forEach begin="1" end="${totalItemPages}" var="i">
                            <c:if test="${i == 1 || i == totalItemPages || (i >= itemPage - 2 && i <= itemPage + 2)}">
                                <li class="page-item ${i == itemPage ? 'active' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&itemPage=${i}">
                                        ${i}
                                    </a>
                                </li>
                            </c:if>
                            <c:if test="${i == itemPage - 3 || i == itemPage + 3}">
                                <li class="page-item disabled"><span class="page-link">...</span></li>
                            </c:if>
                        </c:forEach>

                        <li class="page-item ${itemPage == totalItemPages ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&itemPage=${itemPage + 1}">
                                &raquo;
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </div>
    </div>

    <!-- Order Summary -->
    <c:if test="${not empty items}">
        <div class="card shadow-sm mb-4">
            <div class="card-body">
                <div class="row">
                    <div class="col-md-8">
                        <h5 class="mb-3"><i class="fas fa-receipt mr-2"></i>Order Summary</h5>
                        <p class="text-muted mb-0">Total amount of items in this order.</p>
                    </div>

                    <div class="col-md-4">
                        <div class="card bg-light">
                            <div class="card-body text-center">
                                <p class="text-muted mb-2">Total Amount</p>
                                <h3 class="text-primary font-weight-bold">${currency:format(total)} VND</h3>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:if>

    <!-- Action Buttons (Only for DRAFT) -->
    <c:if test="${order.status == 'DRAFT'}">
        <div class="row mb-4">
            <div class="col-md-6">
                <div class="card shadow-sm border-primary h-100">
                    <div class="card-body text-center py-4">
                        <h5 class="mb-3"><i class="fas fa-paper-plane mr-2"></i>Submit Order</h5>
                        <c:choose>
                            <c:when test="${empty items}">
                                <p class="text-muted mb-4">Please add at least one product before submitting.</p>
                                <button class="btn btn-primary btn-lg" disabled>
                                    <i class="fas fa-paper-plane mr-2"></i>Submit
                                </button>
                            </c:when>
                            <c:otherwise>
                                <p class="text-muted mb-4">Submit this draft order to the warehouse for processing.</p>
                                <form action="${pageContext.request.contextPath}/salesman/order/submit" method="post"
                                      onsubmit="return confirm('Submit this order? You will not be able to modify it afterward.');">
                                    <input type="hidden" name="orderId" value="${order.id}"/>
                                    <button type="submit" class="btn btn-primary btn-lg">
                                        <i class="fas fa-paper-plane mr-2"></i>Submit Order
                                    </button>
                                </form>
                            </c:otherwise>
                        </c:choose>
                    </div>
                </div>
            </div>

            <div class="col-md-6">
                <div class="card shadow-sm border-danger h-100">
                    <div class="card-body text-center py-4">
                        <h5 class="mb-3"><i class="fas fa-times-circle mr-2"></i>Cancel Order</h5>
                        <p class="text-muted mb-4">Discard this draft? This cannot be undone.</p>
                        <form action="${pageContext.request.contextPath}/salesman/order/cancel" method="post"
                              onsubmit="return confirm('Cancel this order? This action cannot be undone.');">
                            <input type="hidden" name="orderId" value="${order.id}"/>
                            <button type="submit" class="btn btn-danger btn-lg">
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
        <div class="alert alert-info shadow-sm">
            <h5 class="alert-heading"><i class="fas fa-lock mr-2"></i>Order ${order.status}</h5>
            <p class="mb-0">
                <c:choose>
                    <c:when test="${order.status == 'CANCELLED'}">
                        This order has been cancelled and cannot be modified.
                    </c:when>
                    <c:otherwise>
                        This order has been submitted and cannot be modified. Track its progress in the warehouse.
                    </c:otherwise>
                </c:choose>
            </p>
        </div>
    </c:if>
</main>

<!-- Product Selection Modal -->
<div class="modal fade" id="productModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <div class="modal-header bg-primary text-white">
                <h5 class="modal-title"><i class="fas fa-shopping-cart mr-2"></i>Select Products</h5>
                <button type="button" class="close text-white" data-dismiss="modal">
                    <span>&times;</span>
                </button>
            </div>

            <!-- Search Box -->
            <div class="search-box">
                <input type="text" id="productSearch" class="form-control form-control-lg"
                       placeholder="🔍 Search products by name...">
            </div>

            <div class="modal-body">
                <div class="row" id="productList">
                    <c:forEach items="${availableProducts}" var="p">
                        <div class="col-md-4 mb-3 product-item"
                             data-name="${p.name.toLowerCase()}"
                             data-product-id="${p.id}"
                             data-price="${p.currentPrice}"
                             data-max="${p.totalQuantity}">
                            <div class="card h-100 border-info ${p.id == param.productId && not empty param.quantityError ? 'border-warning' : ''}">
                                <c:if test="${p.id == param.productId && not empty param.quantityError}">
                                    <div class="card-header bg-warning text-dark p-2">
                                        <small><i class="fas fa-exclamation-triangle mr-1"></i>Adjust quantity below</small>
                                    </div>
                                </c:if>
                                <div class="card-body">
                                    <h6 class="font-weight-bold mb-2">${p.name}</h6>
                                    <div class="small text-muted mb-2">Available: ${p.totalQuantity}</div>
                                    <div class="font-weight-bold text-primary mb-3">
                                        ${currency:format(p.currentPrice)} VND
                                    </div>
                                    <div class="input-group input-group-sm">
                                        <input type="number" class="form-control text-center" min="1" 
                                               value="${p.id == param.productId && not empty param.quantity ? param.quantity : '1'}"
                                               max="${p.totalQuantity}" step="1" id="qty-${p.id}">
                                        <div class="input-group-append">
                                            <c:choose>
                                                <c:when test="${p.totalQuantity <= 0}">
                                                    <button class="btn btn-info" type="button" disabled>
                                                        Add
                                                    </button>
                                                </c:when>
                                                <c:otherwise>
                                                    <button class="btn btn-info btn-add-product" type="button"
                                                            data-order-id="${order.id}" data-product-id="${p.id}">
                                                        Add
                                                    </button>
                                                </c:otherwise>
                                            </c:choose>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>

                <c:if test="${empty availableProducts}">
                    <div class="text-center py-5 text-muted">
                        <i class="fas fa-box-open fa-3x mb-3"></i>
                        <h5>No Products Available</h5>
                    </div>
                </c:if>

                <c:if test="${totalProductPages > 1}">
                    <nav class="mt-3" aria-label="Product modal pagination">
                        <ul class="pagination justify-content-center mb-0">
                            <li class="page-item ${productPage == 1 ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&productPage=${productPage - 1}">
                                    &laquo;
                                </a>
                            </li>

                            <c:forEach begin="1" end="${totalProductPages}" var="i">
                                <c:if test="${i == 1 || i == totalProductPages || (i >= productPage - 2 && i <= productPage + 2)}">
                                    <li class="page-item ${i == productPage ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&productPage=${i}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:if>
                                <c:if test="${i == productPage - 3 || i == productPage + 3}">
                                    <li class="page-item disabled"><span class="page-link">...</span></li>
                                </c:if>
                            </c:forEach>

                            <li class="page-item ${productPage == totalProductPages ? 'disabled' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/salesman/order/detail?id=${order.id}&productPage=${productPage + 1}">
                                    &raquo;
                                </a>
                            </li>
                        </ul>
                    </nav>
                </c:if>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script>
    // Auto-dismiss alerts
    setTimeout(function() {
        $('.alert-dismissible').fadeOut('slow');
    }, 5000);

    const urlParams = new URLSearchParams(window.location.search);
    const returnTo = '${returnTo}';
    if (urlParams.get('openProductModal') === '1') {
        $('#productModal').modal('show');
    }

    // Product search (front-end filtering)
    $('#productSearch').on('keyup', function() {
        const searchTerm = $(this).val().toLowerCase();
        $('.product-item').each(function() {
            const productName = $(this).data('name');
            if (productName.includes(searchTerm)) {
                $(this).show();
            } else {
                $(this).hide();
            }
        });
    });

    // Quantity controls - using data attributes
    $(document).on('click', '.qty-minus', function() {
        const orderId = $(this).data('order-id');
        const itemId = $(this).data('item-id');
        const currentQty = parseInt($(this).data('quantity'));
        const newQty = currentQty - 1;
        if (newQty >= 1) {
            updateQuantity(orderId, itemId, newQty);
        }
    });

    $(document).on('click', '.qty-plus', function() {
        const orderId = $(this).data('order-id');
        const itemId = $(this).data('item-id');
        const currentQty = parseInt($(this).data('quantity'));
        const newQty = currentQty + 1;
        updateQuantity(orderId, itemId, newQty);
    });

    // Remove item button
    $(document).on('click', '.btn-remove-item', function() {
        const orderId = $(this).data('order-id');
        const itemId = $(this).data('item-id');
        removeItem(orderId, itemId);
    });

    // Add product button
    $(document).on('click', '.btn-add-product', function() {
        const orderId = $(this).data('order-id');
        const productId = $(this).data('product-id');
        const qtyInput = $('#qty-' + productId);
        if (qtyInput.length) {
            addProductToOrder(orderId, productId);
        }
    });

    // Add product to order
    function addProductToOrder(orderId, productId) {
        const quantity = parseInt($('#qty-' + productId).val());
        const itemPage = urlParams.get('itemPage') || '${itemPage}';
        const productPage = urlParams.get('productPage') || '${productPage}';

        if (!quantity || quantity < 1) {
            alert('Please enter a valid quantity');
            return;
        }

        // Create form and submit
        const form = $('<form>', {
            method: 'POST',
            action: '${pageContext.request.contextPath}/salesman/order/item/add'
        });

        form.append($('<input>', { type: 'hidden', name: 'orderId', value: orderId }));
        form.append($('<input>', { type: 'hidden', name: 'productId', value: productId }));
        form.append($('<input>', { type: 'hidden', name: 'quantity', value: quantity }));
        form.append($('<input>', { type: 'hidden', name: 'itemPage', value: itemPage }));
        form.append($('<input>', { type: 'hidden', name: 'productPage', value: productPage }));
        form.append($('<input>', { type: 'hidden', name: 'returnTo', value: returnTo }));

        $('body').append(form);
        form.submit();
    }

    // Update quantity
    function updateQuantity(orderId, itemId, newQuantity) {
        const itemPage = urlParams.get('itemPage') || '${itemPage}';
        const productPage = urlParams.get('productPage') || '${productPage}';

        if (newQuantity < 1) {
            alert('Quantity must be at least 1');
            return;
        }

        if (confirm('Update quantity to ' + newQuantity + '?')) {
            const form = $('<form>', {
                method: 'POST',
                action: '${pageContext.request.contextPath}/salesman/order/item/update-quantity'
            });

            form.append($('<input>', { type: 'hidden', name: 'orderId', value: orderId }));
            form.append($('<input>', { type: 'hidden', name: 'itemId', value: itemId }));
            form.append($('<input>', { type: 'hidden', name: 'quantity', value: newQuantity }));
            form.append($('<input>', { type: 'hidden', name: 'itemPage', value: itemPage }));
            form.append($('<input>', { type: 'hidden', name: 'productPage', value: productPage }));
            form.append($('<input>', { type: 'hidden', name: 'returnTo', value: returnTo }));

            $('body').append(form);
            form.submit();
        }
    }

    // Remove item
    function removeItem(orderId, orderItemId) {
        const itemPage = urlParams.get('itemPage') || '${itemPage}';
        const productPage = urlParams.get('productPage') || '${productPage}';

        if (confirm('Remove this item from the order?')) {
            const form = $('<form>', {
                method: 'POST',
                action: '${pageContext.request.contextPath}/salesman/order/item/remove'
            });

            form.append($('<input>', { type: 'hidden', name: 'orderId', value: orderId }));
            form.append($('<input>', { type: 'hidden', name: 'orderItemId', value: orderItemId }));
            form.append($('<input>', { type: 'hidden', name: 'itemPage', value: itemPage }));
            form.append($('<input>', { type: 'hidden', name: 'productPage', value: productPage }));
            form.append($('<input>', { type: 'hidden', name: 'returnTo', value: returnTo }));

            $('body').append(form);
            form.submit();
        }
    }
</script>
</body>
</html>
