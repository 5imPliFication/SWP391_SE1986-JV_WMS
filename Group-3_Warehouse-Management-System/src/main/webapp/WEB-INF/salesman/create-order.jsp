<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Create Order - Laptop Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    <style>
        .product-card {
            border: 1px solid #e2e8f0;
            border-radius: 8px;
            padding: 12px;
            margin-bottom: 10px;
            transition: all 0.2s ease;
            cursor: pointer;
            background: #fff;
        }
        .product-card:hover {
            border-color: #3b82f6;
            box-shadow: 0 2px 8px rgba(59,130,246,0.15);
        }
        .product-search {
            position: sticky;
            top: 0;
            background: #fff;
            z-index: 10;
            padding-bottom: 10px;
        }
        .product-list-container {
            max-height: 65vh;
            overflow-y: auto;
        }
        .cart-item {
            display: flex;
            align-items: center;
            justify-content: space-between;
            padding: 10px 12px;
            border-bottom: 1px solid #f1f5f9;
        }
        .cart-item:last-child {
            border-bottom: none;
        }
        .cart-empty {
            text-align: center;
            padding: 40px 20px;
            color: #94a3b8;
        }
        .qty-btn {
            width: 28px;
            height: 28px;
            border: 1px solid #cbd5e1;
            background: #f8fafc;
            border-radius: 4px;
            display: inline-flex;
            align-items: center;
            justify-content: center;
            cursor: pointer;
            font-weight: bold;
            font-size: 14px;
        }
        .qty-btn:hover {
            background: #e2e8f0;
        }
        .qty-input {
            width: 50px;
            text-align: center;
            border: 1px solid #cbd5e1;
            border-radius: 4px;
            padding: 2px 4px;
            font-size: 14px;
        }
        .stock-badge {
            font-size: 0.75rem;
            padding: 2px 8px;
            border-radius: 12px;
        }
        .cart-section {
            min-height: 200px;
            max-height: 45vh;
            overflow-y: auto;
        }
    </style>
</head>
<body>
    <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

    <main class="main-content">
        <!-- Page Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="fw-bold text-dark">
                <i class="fas fa-cart-plus me-2"></i>Create New Order
            </h2>
            <a href="${pageContext.request.contextPath}/salesman/orders" class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left me-1"></i>Back to Orders
            </a>
        </div>

        <!-- Error Alert -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle me-1"></i>${param.error}
                <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
            </div>
        </c:if>

        <form id="createOrderForm" method="post" action="${pageContext.request.contextPath}/salesman/order/create">
            <input type="hidden" name="orderItems" id="orderItemsInput">

            <div class="row">
                <!-- LEFT: Customer Info + Cart -->
                <div class="col-lg-5">
                    <!-- Customer Info -->
                    <div class="card shadow-sm mb-3">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="fas fa-user me-2"></i>Customer Information</h5>
                        </div>
                        <div class="card-body">
                            <div class="mb-3">
                                <label for="customerName" class="form-label">Customer Name</label>
                                <input type="text" class="form-control" id="customerName" name="customerName"
                                       value="${param.customerName}" placeholder="Walk-in Customer">
                            </div>
                            <div class="mb-3">
                                <label for="customerPhone" class="form-label">Phone Number</label>
                                <input type="text" class="form-control" id="customerPhone" name="customerPhone"
                                       value="${param.customerPhone}" placeholder="Enter phone number">
                            </div>
                            <div class="mb-0">
                                <label for="note" class="form-label">Note</label>
                                <textarea class="form-control" id="note" name="note" rows="2"
                                          placeholder="Order notes...">${param.note}</textarea>
                            </div>
                        </div>
                    </div>

                    <!-- Order Cart -->
                    <div class="card shadow-sm">
                        <div class="card-header bg-white d-flex justify-content-between align-items-center">
                            <h5 class="mb-0"><i class="fas fa-shopping-cart me-2"></i>Order Items</h5>
                            <span class="badge bg-primary" id="cartCount">0</span>
                        </div>
                        <div class="card-body p-0">
                            <div class="cart-section" id="cartSection">
                                <div class="cart-empty" id="cartEmpty">
                                    <i class="fas fa-inbox" style="font-size: 2rem;"></i>
                                    <p class="mt-2 mb-0">No items added yet.<br>Select products from the right panel.</p>
                                </div>
                                <div id="cartItems"></div>
                            </div>
                        </div>
                        <div class="card-footer bg-white">
                            <div class="d-flex justify-content-between mb-2">
                                <strong>Total Items:</strong>
                                <strong id="totalItems">0</strong>
                            </div>
                            <button type="submit" class="btn btn-primary w-100 btn-lg" id="submitBtn" disabled>
                                <i class="fas fa-check me-2"></i>Create Order
                            </button>
                        </div>
                    </div>
                </div>

                <!-- RIGHT: Product List -->
                <div class="col-lg-7">
                    <div class="card shadow-sm">
                        <div class="card-header bg-white">
                            <h5 class="mb-0"><i class="fas fa-boxes me-2"></i>Available Products</h5>
                        </div>
                        <div class="card-body">
                            <!-- Search -->
                            <div class="product-search">
                                <input type="text" class="form-control" id="productSearch"
                                       placeholder="Search products by name or brand...">
                            </div>

                            <!-- Product List -->
                            <div class="product-list-container" id="productListContainer">
                                <c:choose>
                                    <c:when test="${not empty availableProducts}">
                                        <c:forEach items="${availableProducts}" var="p">
                                            <div class="product-card" data-product-id="${p.id}"
                                                 data-product-name="${fn:escapeXml(p.name)}"
                                                 data-product-stock="${p.totalQuantity}"
                                                 data-product-brand="${p.brand != null ? fn:escapeXml(p.brand.name) : ''}"
                                                 onclick="addToCart(${p.id})">
                                                <div class="d-flex justify-content-between align-items-start">
                                                    <div style="flex: 1;">
                                                        <h6 class="mb-1 fw-semibold">${fn:escapeXml(p.name)}</h6>
                                                        <c:if test="${p.brand != null}">
                                                            <small class="text-muted">
                                                                <i class="fas fa-tag me-1"></i>${fn:escapeXml(p.brand.name)}
                                                            </small>
                                                        </c:if>
                                                    </div>
                                                    <div class="text-end">
                                                        <span class="stock-badge ${p.totalQuantity > 0 ? 'bg-success text-white' : 'bg-danger text-white'}">
                                                            Stock: ${p.totalQuantity}
                                                        </span>
                                                    </div>
                                                </div>
                                            </div>
                                        </c:forEach>
                                    </c:when>
                                    <c:otherwise>
                                        <div class="text-center py-5 text-muted">
                                            <i class="fas fa-box-open" style="font-size: 2rem;"></i>
                                            <p class="mt-2 mb-0">No products available in stock.</p>
                                        </div>
                                    </c:otherwise>
                                </c:choose>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </form>
    </main>

    <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
    <script>
        // Cart data: { productId: { name, quantity, stock, brand } }
        const cart = {};

        // Restore cart from initialOrderItems if returning with error
        (function restoreCart() {
            const raw = '${fn:escapeXml(initialOrderItems)}';
            if (!raw || raw.trim() === '') return;
            const pairs = raw.split(';');
            pairs.forEach(function(pair) {
                const trimmed = pair.trim();
                if (!trimmed) return;
                const parts = trimmed.split(':');
                if (parts.length !== 2) return;
                const pid = parseInt(parts[0]);
                const qty = parseInt(parts[1]);
                if (isNaN(pid) || isNaN(qty) || pid <= 0 || qty <= 0) return;

                const card = document.querySelector('.product-card[data-product-id="' + pid + '"]');
                if (card) {
                    cart[pid] = {
                        name: card.getAttribute('data-product-name'),
                        quantity: qty,
                        stock: parseInt(card.getAttribute('data-product-stock')),
                        brand: card.getAttribute('data-product-brand')
                    };
                }
            });
            renderCart();
        })();

        function addToCart(productId) {
            const card = document.querySelector('.product-card[data-product-id="' + productId + '"]');
            if (!card) return;

            const stock = parseInt(card.getAttribute('data-product-stock'));
            const name = card.getAttribute('data-product-name');
            const brand = card.getAttribute('data-product-brand');

            if (stock <= 0) {
                alert('This product is out of stock.');
                return;
            }

            if (cart[productId]) {
                if (cart[productId].quantity >= stock) {
                    alert('Cannot add more. Maximum stock available: ' + stock);
                    return;
                }
                cart[productId].quantity++;
            } else {
                cart[productId] = { name: name, quantity: 1, stock: stock, brand: brand };
            }

            renderCart();
        }

        function removeFromCart(productId) {
            delete cart[productId];
            renderCart();
        }

        function updateQuantity(productId, newQty) {
            newQty = parseInt(newQty);
            if (isNaN(newQty) || newQty <= 0) {
                removeFromCart(productId);
                return;
            }
            if (newQty > cart[productId].stock) {
                alert('Maximum stock available: ' + cart[productId].stock);
                newQty = cart[productId].stock;
            }
            cart[productId].quantity = newQty;
            renderCart();
        }

        function changeQty(productId, delta) {
            if (!cart[productId]) return;
            const newQty = cart[productId].quantity + delta;
            updateQuantity(productId, newQty);
        }

        function renderCart() {
            const cartItemsEl = document.getElementById('cartItems');
            const cartEmptyEl = document.getElementById('cartEmpty');
            const cartCountEl = document.getElementById('cartCount');
            const totalItemsEl = document.getElementById('totalItems');
            const submitBtn = document.getElementById('submitBtn');
            const orderItemsInput = document.getElementById('orderItemsInput');

            const keys = Object.keys(cart);

            if (keys.length === 0) {
                cartEmptyEl.style.display = 'block';
                cartItemsEl.innerHTML = '';
                cartCountEl.textContent = '0';
                totalItemsEl.textContent = '0';
                submitBtn.disabled = true;
                orderItemsInput.value = '';
                return;
            }

            cartEmptyEl.style.display = 'none';
            submitBtn.disabled = false;

            let html = '';
            let totalQty = 0;
            const orderParts = [];

            keys.forEach(function(pid) {
                const item = cart[pid];
                totalQty += item.quantity;
                orderParts.push(pid + ':' + item.quantity);

                html += '<div class="cart-item">'
                    + '  <div style="flex:1;">'
                    + '    <div class="fw-semibold" style="font-size:0.9rem;">' + escapeHtml(item.name) + '</div>'
                    + (item.brand ? '<small class="text-muted">' + escapeHtml(item.brand) + '</small>' : '')
                    + '  </div>'
                    + '  <div class="d-flex align-items-center gap-1 me-2">'
                    + '    <button type="button" class="qty-btn" onclick="changeQty(' + pid + ', -1)">−</button>'
                    + '    <input type="text" class="qty-input" value="' + item.quantity + '" '
                    + '           onchange="updateQuantity(' + pid + ', this.value)">'
                    + '    <button type="button" class="qty-btn" onclick="changeQty(' + pid + ', 1)">+</button>'
                    + '  </div>'
                    + '  <button type="button" class="btn btn-sm btn-outline-danger" onclick="removeFromCart(' + pid + ')">'
                    + '    <i class="fas fa-trash-alt"></i>'
                    + '  </button>'
                    + '</div>';
            });

            cartItemsEl.innerHTML = html;
            cartCountEl.textContent = keys.length;
            totalItemsEl.textContent = totalQty;
            orderItemsInput.value = orderParts.join(';');
        }

        // Product search/filter
        document.getElementById('productSearch').addEventListener('input', function() {
            const query = this.value.toLowerCase();
            document.querySelectorAll('.product-card').forEach(function(card) {
                const name = card.getAttribute('data-product-name').toLowerCase();
                const brand = (card.getAttribute('data-product-brand') || '').toLowerCase();
                card.style.display = (name.includes(query) || brand.includes(query)) ? '' : 'none';
            });
        });

        // Form submit validation
        document.getElementById('createOrderForm').addEventListener('submit', function(e) {
            if (Object.keys(cart).length === 0) {
                e.preventDefault();
                alert('Please add at least one product to the order.');
            }
        });

        function escapeHtml(text) {
            const div = document.createElement('div');
            div.appendChild(document.createTextNode(text));
            return div.innerHTML;
        }
    </script>
</body>
</html>
