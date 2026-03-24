<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="currency" uri="http://example.com/functions/currency" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .create-accent {
            background: linear-gradient(135deg, #1f6f8b 0%, #144552 100%);
            color: #fff;
            border: 0;
        }

        .product-pill {
            border: 1px dashed #17a2b8;
            border-radius: 0.35rem;
            padding: 0.4rem 0.6rem;
            margin-bottom: 0.45rem;
            background: #f4fbfd;
        }

        .qty-box {
            width: 84px;
            text-align: center;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <c:if test="${not empty param.error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
            <i class="fas fa-exclamation-circle mr-2"></i>${param.error}
            <button type="button" class="close" data-dismiss="alert"><span>&times;</span></button>
        </div>
    </c:if>

    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="font-weight-bold text-dark mb-0">
            <i class="fas fa-plus-circle mr-2 text-info"></i>Create Order
        </h2>
        <a href="${pageContext.request.contextPath}/salesman/orders" class="btn btn-outline-secondary">
            <i class="fas fa-arrow-left mr-2"></i>Back to Orders
        </a>
    </div>

    <div class="alert alert-info border shadow-sm mb-4" role="alert">
        <i class="fas fa-info-circle mr-2"></i>
        Draft data on this page is local only. The order is created only when you click Add order.
    </div>

    <form action="${pageContext.request.contextPath}/salesman/order/create" method="post" id="createOrderForm">
        <input type="hidden" name="orderItems" id="orderItems" value="${initialOrderItems}"/>

        <div class="card shadow-sm mb-4">
            <div class="card-header create-accent">
                <h5 class="mb-0"><i class="fas fa-user mr-2"></i>Customer Information</h5>
            </div>
            <div class="card-body">
                <div class="form-row">
                    <div class="form-group col-md-6">
                        <label for="customerName" class="font-weight-bold">Customer Name</label>
                        <input type="text" class="form-control" id="customerName" name="customerName"
                               value="${param.customerName}" placeholder="Enter customer full name" required>
                    </div>
                    <div class="form-group col-md-6">
                        <label for="customerPhone" class="font-weight-bold">Customer Phone</label>
                        <input type="text" class="form-control" id="customerPhone" name="customerPhone"
                               value="${param.customerPhone}" placeholder="Enter customer phone number">
                    </div>
                </div>
                <div class="form-group mb-0">
                    <label for="note" class="font-weight-bold">Order Description</label>
                    <textarea class="form-control" id="note" name="note" rows="3"
                              placeholder="Enter note">${param.note}</textarea>
                </div>
            </div>
        </div>

        <div class="card shadow-sm mb-4">
            <div class="card-header bg-dark text-white d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="fas fa-box mr-2"></i>Order Items</h5>
                <button type="button" class="btn btn-success btn-sm" data-toggle="modal" data-target="#productModal">
                    <i class="fas fa-plus-circle mr-1"></i>Add Products
                </button>
            </div>
            <div class="card-body p-0">
                <div class="table-responsive">
                    <table class="table table-hover mb-0">
                        <thead class="thead-light">
                        <tr>
                            <th class="px-4 py-3">#</th>
                            <th class="px-4 py-3">Product</th>
                            <th class="px-4 py-3">Unit Price</th>
                            <th class="px-4 py-3 text-center">Quantity</th>
                            <th class="px-4 py-3 text-right">Subtotal</th>
                            <th class="px-4 py-3 text-center">Action</th>
                        </tr>
                        </thead>
                        <tbody id="selectedItemsBody">
                        <tr id="emptyItemsRow">
                            <td colspan="6" class="text-center py-5 text-muted">
                                <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                                <h5>No Items Added Yet</h5>
                                <p class="mb-0">Add at least one product before creating the order.</p>
                            </td>
                        </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <div class="card shadow-sm mb-4">
            <div class="card-body d-flex justify-content-between align-items-center">
                <h5 class="mb-0"><i class="fas fa-receipt mr-2"></i>Total</h5>
                <h4 class="mb-0 text-info font-weight-bold" id="totalAmount">0 VND</h4>
            </div>
        </div>

        <button type="submit" class="btn btn-info btn-lg btn-block shadow-sm">
            <i class="fas fa-check-circle mr-2"></i>Add order
        </button>
    </form>
</main>

<div class="modal fade" id="productModal" tabindex="-1" role="dialog" aria-hidden="true">
    <div class="modal-dialog modal-xl" role="document">
        <div class="modal-content">
            <div class="modal-header create-accent">
                <h5 class="modal-title"><i class="fas fa-shopping-cart mr-2"></i>Select Products</h5>
                <button type="button" class="close text-white" data-dismiss="modal"><span>&times;</span></button>
            </div>
            <div class="modal-body">
                <div class="form-group">
                    <input type="text" class="form-control" id="productSearch" placeholder="Search product name...">
                </div>

                <div class="row" id="productGrid">
                    <c:forEach items="${availableProducts}" var="p">
                        <div class="col-md-4 mb-3 product-card" data-name="${p.name.toLowerCase()}"
                             data-id="${p.id}" data-price="${p.currentPrice}" data-max="${p.totalQuantity}">
                            <div class="card h-100 border-info">
                                <div class="card-body">
                                    <h6 class="font-weight-bold mb-2">${p.name}</h6>
                                    <div class="small text-muted mb-2">Available: ${p.totalQuantity}</div>
                                    <div class="font-weight-bold text-primary mb-3">
                                        ${currency:format(p.currentPrice)} VND
                                    </div>
                                    <div class="input-group">
                                        <input type="number" class="form-control qty-box" min="1" value="1"
                                               max="${p.totalQuantity}" id="qty-${p.id}">
                                        <div class="input-group-append">
                                            <button class="btn btn-info" type="button" onclick="addProduct(${p.id})"
                                                    ${p.totalQuantity <= 0 ? 'disabled' : ''}>
                                                Add
                                            </button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script>
    const selectedItems = {};

    function currencyFormat(value) {
        const number = Number(value || 0);
        return number.toLocaleString('en-US') + ' VND';
    }

    function parseInitialItems() {
        const raw = document.getElementById('orderItems').value || '';
        if (!raw) {
            return;
        }

        raw.split(';').forEach(pair => {
            const p = pair.trim();
            if (!p) {
                return;
            }
            const parts = p.split(':');
            if (parts.length !== 2) {
                return;
            }
            const productId = parseInt(parts[0], 10);
            const qty = parseInt(parts[1], 10);
            if (!productId || !qty || qty < 1) {
                return;
            }

            const card = document.querySelector('.product-card[data-id="' + productId + '"]');
            if (!card) {
                return;
            }

            selectedItems[productId] = {
                id: productId,
                name: card.querySelector('h6').innerText,
                price: Number(card.dataset.price || 0),
                max: Number(card.dataset.max || 0),
                qty: Math.min(qty, Number(card.dataset.max || qty))
            };
        });
    }

    function syncHiddenOrderItems() {
        const serialized = Object.values(selectedItems)
            .filter(item => item.qty > 0)
            .map(item => item.id + ':' + item.qty)
            .join(';');
        document.getElementById('orderItems').value = serialized;
    }

    function renderItemsTable() {
        const body = document.getElementById('selectedItemsBody');
        const rows = [];
        let total = 0;
        let index = 1;

        Object.values(selectedItems).forEach(item => {
            if (item.qty < 1) {
                return;
            }
            const subtotal = item.qty * item.price;
            total += subtotal;

            rows.push(`
                <tr>
                    <td class="px-4 align-middle">${index+1}</td>
                    <td class="px-4 align-middle">
                        <div class="font-weight-bold">${item.name}</div>
                        <div class="small text-muted">Max stock: ${item.max}</div>
                    </td>
                    <td class="px-4 align-middle">${currencyFormat(item.price)}</td>
                    <td class="px-4 align-middle text-center">
                        <div class="d-inline-flex align-items-center">
                            <button type="button" class="btn btn-sm btn-outline-danger" onclick="changeQty(${item.id}, -1)">-</button>
                            <span class="mx-2 font-weight-bold">${item.qty}</span>
                            <button type="button" class="btn btn-sm btn-outline-success" onclick="changeQty(${item.id}, 1)">+</button>
                        </div>
                    </td>
                    <td class="px-4 align-middle text-right font-weight-bold">${currencyFormat(subtotal)}</td>
                    <td class="px-4 align-middle text-center">
                        <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeItem(${item.id})">
                            <i class="fas fa-trash"></i>
                        </button>
                    </td>
                </tr>
            `);
        });

        if (rows.length === 0) {
            body.innerHTML = `
                <tr id="emptyItemsRow">
                    <td colspan="6" class="text-center py-5 text-muted">
                        <i class="fas fa-inbox fa-3x mb-3 d-block"></i>
                        <h5>No Items Added Yet</h5>
                        <p class="mb-0">Add at least one product before creating the order.</p>
                    </td>
                </tr>
            `;
        } else {
            body.innerHTML = rows.join('');
        }

        document.getElementById('totalAmount').innerText = currencyFormat(total);
        syncHiddenOrderItems();
    }

    function addProduct(productId) {
        const card = document.querySelector('.product-card[data-id="' + productId + '"]');
        const qtyInput = document.getElementById('qty-' + productId);
        if (!card || !qtyInput) {
            return;
        }

        const qty = Number(qtyInput.value || 1);
        const max = Number(card.dataset.max || 0);
        if (!qty || qty < 1) {
            alert('Quantity must be at least 1');
            return;
        }

        if (qty > max) {
            alert('Quantity exceeds available stock');
            return;
        }

        if (selectedItems[productId]) {
            selectedItems[productId].qty = Math.min(selectedItems[productId].qty + qty, max);
        } else {
            selectedItems[productId] = {
                id: productId,
                name: card.querySelector('h6').innerText,
                price: Number(card.dataset.price || 0),
                max: max,
                qty: qty
            };
        }

        renderItemsTable();
    }

    function changeQty(productId, delta) {
        if (!selectedItems[productId]) {
            return;
        }

        const next = selectedItems[productId].qty + delta;
        if (next < 1) {
            return;
        }
        if (next > selectedItems[productId].max) {
            alert('Quantity exceeds available stock');
            return;
        }

        selectedItems[productId].qty = next;
        renderItemsTable();
    }

    function removeItem(productId) {
        delete selectedItems[productId];
        renderItemsTable();
    }

    $('#productSearch').on('keyup', function() {
        const query = ($(this).val() || '').toLowerCase();
        document.querySelectorAll('.product-card').forEach(card => {
            const name = card.dataset.name || '';
            card.style.display = name.includes(query) ? '' : 'none';
        });
    });

    document.getElementById('createOrderForm').addEventListener('submit', function(event) {
        syncHiddenOrderItems();
        if (!document.getElementById('orderItems').value) {
            event.preventDefault();
            alert('Please add at least one product item.');
        }
    });

    parseInitialItems();
    renderItemsTable();
</script>
</body>
</html>
