<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap-grid.min.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/dashboard.css">
    <title>Salesman Dashboard</title>
</head>

<div class="container mt-4">
    <div class="card shadow-sm border-0">
        <div class="card-header bg-white border-0 pt-4 pb-3">
            <h3 class="card-title mb-0 text-primary">Daily Tasks</h3>
        </div>

        <div class="card-body">

            <!-- SALESMAN Section -->
            <c:if test="${sessionScope.user.role.name eq 'Salesman'}">
                <div class="border-start border-primary border-4 ps-4 mb-4">
                    <h4 class="mb-3 text-dark">
                        <i class="bi bi-cart-check me-2"></i>Sales
                    </h4>

                    <div class="row mb-3">
                        <div class="col-md-6 mb-3">
                            <div class="card h-100 border-0 shadow-sm">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted text-uppercase small">
                                        Orders Created Today
                                    </h6>
                                    <h3 class="card-title text-primary">${ordersCreatedToday}</h3>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-6 mb-3">
                            <div class="card h-100 border-0 shadow-sm ${pendingOrders > 0 ? 'border-warning' : ''}">
                                <div class="card-body">
                                    <h6 class="card-subtitle mb-2 text-muted text-uppercase small">
                                        Pending Orders
                                    </h6>
                                    <h3 class="card-title ${pendingOrders > 0 ? 'text-warning' : 'text-secondary'}">
                                            ${pendingOrders}
                                    </h3>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="d-flex gap-2 flex-wrap">
                        <a href="salesman/order/create" class="btn btn-primary">
                            <i class="bi bi-plus-circle me-1"></i>Create Order
                        </a>
                        <a href="salesman/orders" class="btn btn-outline-primary">
                            <i class="bi bi-list-ul me-1"></i>View Orders
                        </a>
                    </div>
                </div>
            </c:if>
<%--            clear session--%>
            <c:if test="${sessionScope.user.role.name != 'Salesman'}">
                <div class="text-center py-5">
                    <i class="bi bi-check-circle display-4 text-muted"></i>
                    <p class="mt-3 text-muted">No daily tasks assigned for your role.</p>
                </div>
            </c:if>
        </div>
    </div>
</div>