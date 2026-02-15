<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 2/5/2026
  Time: 8:56 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/dashboard.css">
    <title>Warehouse Keeper Dashboard</title>
</head>
<body>
<div class="container mt-4">
    <div class="card shadow-sm border-0">
        <div class="card-header bg-white border-0 pt-4 pb-3">
            <h3 class="card-title mb-0 text-primary">Daily Tasks</h3>
        </div>
        <!-- STOREKEEPER Section -->
        <c:if test="${sessionScope.user.role.name == 'Warehouse'}">
            <div class="border-start border-success border-4 ps-4">
                <h4 class="mb-3 text-dark">
                    <i class="bi bi-box-seam me-2"></i>Inventory
                </h4>

                <div class="row mb-3">
                    <div class="col-md-6 mb-3">
                        <div class="card h-100 border-0 shadow-sm ${lowStockCount > 0 ? 'border-danger' : ''}">
                            <div class="card-body">
                                <h6 class="card-subtitle mb-2 text-muted text-uppercase small">
                                    Low Stock Items
                                </h6>
                                <h3 class="card-title ${lowStockCount > 0 ? 'text-danger' : 'text-secondary'}">
                                        ${lowStockCount}
                                </h3>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-6 mb-3">
                        <div class="card h-100 border-0 shadow-sm ${restockCount > 0 ? 'border-warning' : ''}">
                            <div class="card-body">
                                <h6 class="card-subtitle mb-2 text-muted text-uppercase small">
                                    Products Needing Restock
                                </h6>
                                <h3 class="card-title ${restockCount > 0 ? 'text-warning' : 'text-secondary'}">
                                        ${restockCount}
                                </h3>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="d-flex gap-2 flex-wrap">
                    <a href="add-product" class="btn btn-success">
                        <i class="bi bi-plus-circle me-1"></i>Add Product
                    </a>
                    <a href="inventory" class="btn btn-outline-success">
                        <i class="bi bi-clipboard-data me-1"></i>View Inventory
                    </a>
                </div>
            </div>
        </c:if>

        <c:if test="${sessionScope.user.role.name != 'Warehouse'}">
            <div class="text-center py-5">
                <i class="bi bi-check-circle display-4 text-muted"></i>
                <p class="mt-3 text-muted">No daily tasks assigned for your role.</p>
            </div>
        </c:if>
    </div>
</div>
</body>
</html>
