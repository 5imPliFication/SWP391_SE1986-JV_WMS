<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="currency" uri="http://example.com/functions/currency" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Coupon Management - Laptop Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <div class="container">
        <!-- Page Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="font-weight-bold text-dark">
                <i class="fas fa-ticket-alt mr-2"></i>Coupon Management
            </h2>
            <a href="${pageContext.request.contextPath}/coupons/create" class="btn btn-primary">
                <i class="fas fa-plus mr-2"></i>Create New Coupon
            </a>
        </div>

        <!-- Success/Error Messages -->
        <c:if test="${param.success == 'true'}">
            <div class="alert alert-success alert-dismissible fade show" role="alert">
                <i class="fas fa-check-circle mr-2"></i>Operation completed successfully!
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

        <!-- Coupons Table -->
        <div class="card shadow-sm">
            <div class="card-header bg-primary text-white">
                <h5 class="mb-0"><i class="fas fa-list mr-2"></i>All Coupons</h5>
            </div>
            <div class="card-body p-0">
                <c:choose>
                    <c:when test="${empty coupons}">
                        <!-- Empty State -->
                        <div class="text-center py-5">
                            <i class="fas fa-inbox fa-4x text-muted mb-3"></i>
                            <h4 class="text-muted">No coupons yet</h4>
                            <p class="text-muted mb-4">Create your first coupon to get started</p>
                            <a href="${pageContext.request.contextPath}/coupons/create" class="btn btn-primary">
                                <i class="fas fa-plus mr-2"></i>Create Coupon
                            </a>
                        </div>
                    </c:when>
                    <c:otherwise>
                        <div class="table-responsive">
                            <table class="table table-hover table-striped mb-0">
                                <thead class="thead-light">
                                <tr>
                                    <th class="py-3 px-4">Code</th>
                                    <th class="py-3 px-4">Type</th>
                                    <th class="py-3 px-4">Discount</th>
                                    <th class="py-3 px-4">Min. Order</th>
                                    <th class="py-3 px-4">Valid Until</th>
                                    <th class="py-3 px-4">Usage</th>
                                    <th class="py-3 px-4 text-center">Status</th>
                                    <th class="py-3 px-4 text-center">Actions</th>
                                </tr>
                                </thead>
                                <tbody>
                                <c:forEach var="coupon" items="${coupons}">
                                    <tr>
                                        <td class="px-4 align-middle">
                                            <strong class="text-primary">${coupon.code}</strong>
                                        </td>
                                        <td class="px-4 align-middle">
                                            <c:choose>
                                                <c:when test="${coupon.discountType == 'PERCENTAGE'}">
                                                    <span class="badge badge-info">Percentage</span>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="badge badge-warning">Fixed Amount</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-4 align-middle font-weight-bold">
                                            <c:choose>
                                                <c:when test="${coupon.discountType == 'PERCENTAGE'}">
                                                    <fmt:formatNumber value="${coupon.discountValue}" pattern="#"/>%
                                                </c:when>
                                                <c:otherwise>
                                                    ${currency:format(coupon.discountValue)} VND
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-4 align-middle">
                                            ${currency:format(coupon.minOrderAmount)} VND
                                        </td>
                                        <td class="px-4 align-middle">
                                            <c:choose>
                                                <c:when test="${not empty coupon.validUntil}">
                                                    <fmt:formatDate value="${coupon.validUntil}"
                                                                    pattern="MMM dd, yyyy"/>
                                                </c:when>
                                                <c:otherwise>
                                                    <span class="text-muted">No expiry</span>
                                                </c:otherwise>
                                            </c:choose>
                                        </td>
                                        <td class="px-4 align-middle">
                                                <span class="badge badge-secondary">
                                                    ${coupon.usedCount}
                                                    <c:if test="${not empty coupon.usageLimit}">
                                                        / ${coupon.usageLimit}
                                                    </c:if>
                                                </span>
                                        </td>
                                        <td class="px-4 align-middle">
                                            <div class="custom-control custom-switch">
                                                <input type="checkbox"
                                                       class="custom-control-input"
                                                       id="switch${coupon.id}"
                                                    ${coupon.isActive ? 'checked' : ''}
                                                       onchange="toggleStatus(${coupon.id}, this.checked)">
                                                <label class="custom-control-label" for="switch${coupon.id}">
                                                        ${coupon.isActive ? 'Active' : 'Inactive'}
                                                </label>
                                            </div>
                                        </td>
                                        <td class="px-4 align-middle text-center">
                                            <div class="btn-group btn-group-sm" role="group">
                                                <a href="${pageContext.request.contextPath}/coupons/edit?id=${coupon.id}"
                                                   class="btn btn-outline-primary"
                                                   data-toggle="tooltip"
                                                   title="Edit">
                                                    <i class="fas fa-edit"></i>
                                                </a>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                                </tbody>
                            </table>
                        </div>
                    </c:otherwise>
                </c:choose>
            </div>
        </div>
    </div>


    <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
    <script>
        let deleteTargetId = null;

        // Initialize tooltips
        $(function () {
            $('[data-toggle="tooltip"]').tooltip();
        });

        // Handle toggle status with event delegation
        $(document).on('change', '.coupon-status-switch', function () {
            const couponId = $(this).data('coupon-id');
            const isActive = this.checked;
            const switchElement = this;
            const label = $(this).next('.custom-control-label');

            // Disable the switch while processing
            $(switchElement).prop('disabled', true);

            toggleStatus(couponId, isActive, switchElement, label);
        });

        function toggleStatus(couponId, isActive, switchElement, label) {
            const form = document.createElement('form');
            form.method = 'post';
            form.action = '${pageContext.request.contextPath}/coupons';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'toggle';

            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'couponId';
            idInput.value = couponId;

            const statusInput = document.createElement('input');
            statusInput.type = 'hidden';
            statusInput.name = 'isActive';
            statusInput.value = isActive;

            form.appendChild(actionInput);
            form.appendChild(idInput);
            form.appendChild(statusInput);
            document.body.appendChild(form);
            form.submit();
        }

        function confirmDelete(couponId, couponCode) {
            deleteTargetId = couponId;
            document.getElementById('deleteCouponCode').textContent = couponCode;
            $('#deleteModal').modal('show');
        }

        function deleteCoupon() {
            if (!deleteTargetId) return;

            const form = document.createElement('form');
            form.method = 'post';
            form.action = '${pageContext.request.contextPath}/coupons';

            const actionInput = document.createElement('input');
            actionInput.type = 'hidden';
            actionInput.name = 'action';
            actionInput.value = 'delete';

            const idInput = document.createElement('input');
            idInput.type = 'hidden';
            idInput.name = 'couponId';
            idInput.value = deleteTargetId;

            form.appendChild(actionInput);
            form.appendChild(idInput);
            document.body.appendChild(form);
            form.submit();
        }
    </script>
</main>
</body>
</html>
