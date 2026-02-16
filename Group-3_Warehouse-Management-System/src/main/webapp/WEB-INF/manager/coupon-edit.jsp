<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Edit Coupon - Laptop Warehouse</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <div class="container-fluid">
        <!-- Page Header -->
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2 class="font-weight-bold text-dark">
                <i class="fas fa-edit mr-2"></i>Edit Coupon
            </h2>
            <a href="${pageContext.request.contextPath}/coupons" class="btn btn-secondary">
                <i class="fas fa-arrow-left mr-2"></i>Back to List
            </a>
        </div>

        <!-- Error Messages -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                <i class="fas fa-exclamation-circle mr-2"></i>${param.error}
                <button type="button" class="close" data-dismiss="alert">
                    <span>&times;</span>
                </button>
            </div>
        </c:if>

        <!-- Edit Form -->
        <div class="row">
            <div class="col-lg-8 col-xl-6 mx-auto">
                <!-- Coupon Info Card -->
                <div class="card shadow-sm mb-4 border-info">
                    <div class="card-body bg-light">
                        <div class="row">
                            <div class="col-md-6">
                                <p class="mb-1 text-muted small">Coupon Code</p>
                                <h5 class="font-weight-bold text-primary">${coupon.code}</h5>
                            </div>
                            <div class="col-md-6">
                                <p class="mb-1 text-muted small">Current Usage</p>
                                <h5 class="font-weight-bold">
                                    ${coupon.usedCount} 
                                    <c:if test="${not empty coupon.usageLimit}">
                                        <span class="text-muted">/ ${coupon.usageLimit}</span>
                                    </c:if>
                                    <span class="text-muted small">times</span>
                                </h5>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="card shadow-sm">
                    <div class="card-header bg-primary text-white">
                        <h5 class="mb-0"><i class="fas fa-ticket-alt mr-2"></i>Coupon Details</h5>
                    </div>
                    <div class="card-body">
                        <form method="post" action="${pageContext.request.contextPath}/coupons">
                            <input type="hidden" name="action" value="update">
                            <input type="hidden" name="couponId" value="${coupon.id}">

                            <!-- Coupon Code -->
                            <div class="form-group">
                                <label for="code" class="font-weight-bold">
                                    <i class="fas fa-barcode mr-1"></i>Coupon Code 
                                    <span class="text-danger">*</span>
                                </label>
                                <input type="text" 
                                       class="form-control form-control-lg text-uppercase" 
                                       id="code" 
                                       name="code" 
                                       required 
                                       value="${coupon.code}"
                                       pattern="[A-Z0-9]+" 
                                       title="Use uppercase letters and numbers only">
                                <small class="form-text text-muted">
                                    Use uppercase letters and numbers only (no spaces or special characters)
                                </small>
                            </div>

                            <!-- Description -->
                            <div class="form-group">
                                <label for="description" class="font-weight-bold">
                                    <i class="fas fa-align-left mr-1"></i>Description 
                                    <span class="text-danger">*</span>
                                </label>
                                <textarea class="form-control" 
                                          id="description" 
                                          name="description" 
                                          rows="3"
                                          required>${coupon.description}</textarea>
                                <small class="form-text text-muted">
                                    This will be shown to customers when they apply the coupon
                                </small>
                            </div>

                            <!-- Discount Type and Value -->
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="discountType" class="font-weight-bold">
                                        <i class="fas fa-tag mr-1"></i>Discount Type 
                                        <span class="text-danger">*</span>
                                    </label>
                                    <select class="form-control form-control-lg" 
                                            id="discountType" 
                                            name="discountType" 
                                            required 
                                            onchange="updateDiscountLabel()">
                                        <option value="PERCENTAGE" ${coupon.discountType == 'PERCENTAGE' ? 'selected' : ''}>Percentage (%)</option>
                                        <option value="FIXED" ${coupon.discountType == 'FIXED' ? 'selected' : ''}>Fixed Amount ($)</option>
                                    </select>
                                </div>

                                <div class="form-group col-md-6">
                                    <label for="discountValue" class="font-weight-bold">
                                        <i class="fas ${coupon.discountType == 'PERCENTAGE' ? 'fa-percent' : 'fa-dollar-sign'} mr-1" id="discountIcon"></i>
                                        <span id="discountValueLabel">
                                            Discount Value (${coupon.discountType == 'PERCENTAGE' ? '%' : '$'})
                                        </span>
                                        <span class="text-danger">*</span>
                                    </label>
                                    <input type="number" 
                                           class="form-control form-control-lg" 
                                           id="discountValue" 
                                           name="discountValue" 
                                           required 
                                           step="0.01" 
                                           min="0"
                                           value="${coupon.discountValue}">
                                    <small class="form-text text-muted" id="discountHint">
                                        ${coupon.discountType == 'PERCENTAGE' ? 'Enter percentage value (e.g., 10 for 10% off)' : 'Enter dollar amount (e.g., 50 for $50 off)'}
                                    </small>
                                </div>
                            </div>

                            <!-- Minimum Order Amount -->
                            <div class="form-group">
                                <label for="minOrderAmount" class="font-weight-bold">
                                    <i class="fas fa-shopping-cart mr-1"></i>Minimum Order Amount 
                                    <span class="text-danger">*</span>
                                </label>
                                <div class="input-group input-group-lg">
                                    <div class="input-group-prepend">
                                        <span class="input-group-text">VND</span>
                                    </div>
                                    <input type="number" 
                                           class="form-control" 
                                           id="minOrderAmount" 
                                           name="minOrderAmount" 
                                           required 
                                           step="0.01" 
                                           min="0"
                                           value="${coupon.minOrderAmount}">
                                </div>
                                <small class="form-text text-muted">
                                    Minimum order total required to use this coupon (set to 0 for no minimum)
                                </small>
                            </div>

                            <!-- Valid Until and Usage Limit -->
                            <div class="form-row">
                                <div class="form-group col-md-6">
                                    <label for="validUntil" class="font-weight-bold">
                                        <i class="fas fa-calendar mr-1"></i>Valid Until 
                                        <span class="text-muted">(Optional)</span>
                                    </label>
                                    <input type="date" 
                                           class="form-control" 
                                           id="validUntil" 
                                           name="validUntil"
                                           value="<fmt:formatDate value='${coupon.validUntil}' pattern='yyyy-MM-dd'/>">
                                    <small class="form-text text-muted">
                                        Leave empty for no expiration date
                                    </small>
                                </div>

                                <div class="form-group col-md-6">
                                    <label for="usageLimit" class="font-weight-bold">
                                        <i class="fas fa-sort-numeric-up mr-1"></i>Usage Limit 
                                        <span class="text-muted">(Optional)</span>
                                    </label>
                                    <input type="number" 
                                           class="form-control" 
                                           id="usageLimit" 
                                           name="usageLimit" 
                                           min="1"
                                           value="${coupon.usageLimit}"
                                           placeholder="Unlimited">
                                    <small class="form-text text-muted">
                                        Leave empty for unlimited uses
                                    </small>
                                </div>
                            </div>

                            <hr class="my-4">

                            <!-- Form Actions -->
                            <div class="d-flex justify-content-between">
                                <a href="${pageContext.request.contextPath}/coupons" 
                                   class="btn btn-secondary btn-lg">
                                    <i class="fas fa-times mr-2"></i>Cancel
                                </a>
                                <button type="submit" class="btn btn-primary btn-lg">
                                    <i class="fas fa-save mr-2"></i>Update Coupon
                                </button>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script>
    function updateDiscountLabel() {
        const type = document.getElementById('discountType').value;
        const label = document.getElementById('discountValueLabel');
        const hint = document.getElementById('discountHint');
        const icon = document.getElementById('discountIcon');
        
        if (type === 'PERCENTAGE') {
            label.textContent = 'Discount Value (%)';
            hint.textContent = 'Enter percentage value (e.g., 10 for 10% off)';
            icon.className = 'fas fa-percent mr-1';
        } else {
            label.textContent = 'Discount Value (VND)';
            hint.textContent = 'Enter dollar amount (e.g., 50000 for VND50000 off)';
            icon.className = 'fas fa-dollar-sign mr-1';
        }
    }

    // Auto-uppercase the coupon code input
    document.getElementById('code').addEventListener('input', function(e) {
        this.value = this.value.toUpperCase();
    });
</script>
</main>
</body>
</html>
