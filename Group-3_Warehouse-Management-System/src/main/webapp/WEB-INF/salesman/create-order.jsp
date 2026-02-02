<%--
  Created by IntelliJ IDEA.
  User: Admin
  Date: 1/28/2026
  Time: 11:54 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Create Order</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <jsp:include page="/WEB-INF/common/header.jsp"/>

    <!-- Page Header -->
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h2 class="font-weight-bold text-dark">
            <i class="fas fa-plus-circle mr-2"></i>Create New Order
        </h2>
        <a href="${pageContext.request.contextPath}/salesman/orders"
           class="btn btn-secondary">
            <i class="fas fa-arrow-left mr-2"></i>Back to Orders
        </a>
    </div>

    <!-- Create Order Form -->
    <div class="row justify-content-center">
        <div class="col-lg-8">
            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h5 class="mb-0"><i class="fas fa-file-alt mr-2"></i>Order Information</h5>
                </div>
                <div class="card-body p-4">
                    <form action="${pageContext.request.contextPath}/salesman/order/create" method="post">
                        <div class="form-group">
                            <label for="customerName" class="font-weight-bold">
                                <i class="fas fa-user mr-1"></i>Customer Name
                            </label>
                            <input type="text"
                                   class="form-control form-control-lg"
                                   id="customerName"
                                   name="customerName"
                                   placeholder="Enter customer full name"
                                   required/>
                            <small class="form-text text-muted">
                                Enter the full name of the customer placing this order
                            </small>
                            <label for="customerPhone" class="font-weight-bold">
                                <i class="fas fa-user mr-1"></i>Customer Phone
                            </label>
                            <input type="text"
                                   class="form-control form-control-lg"
                                   id="customerPhone"
                                   name="customerPhone"
                                   placeholder="Enter customer phone number"
                                   required/>
                            <small class="form-text text-muted">
                                Customer phone number is required
                            </small>
                            <label for="note" class="font-weight-bold">
                                <i class="fas fa-user mr-1"></i>Note
                            </label>
                            <textarea
                                    class="form-control form-control-lg"
                                    id="note"
                                    name="note"
                                    placeholder="Enter order note"
                                    required
                                    rows="4"
                            ></textarea>
                            <small class="form-text text-muted">
                                Enter note for the order
                            </small>
                        </div>

                        <div class="alert alert-info" role="alert">
                            <i class="fas fa-info-circle mr-2"></i>
                            After creating the order, you'll be able to add products and quantities.
                        </div>

                        <button type="submit" class="btn btn-primary btn-lg btn-block">
                            <i class="fas fa-check-circle mr-2"></i>Create Order
                        </button>
                    </form>
                </div>
            </div>
        </div>
    </div>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
</body>
</html>
