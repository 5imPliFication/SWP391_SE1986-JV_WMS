<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Order List - Laptop Warehouse</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
        <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css">
    </head>
    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content">
            <!-- Page Header -->
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="fw-bold text-dark">
                    <i class="bi bi-cart-check me-2"></i>My Orders
                </h2>
                <a href="${pageContext.request.contextPath}/salesman/order/create"
                   class="btn btn-primary btn-lg shadow-sm">
                    Create New Order
                </a>
            </div>

            <div class="card shadow-sm mb-4">
                <div class="card-body">
                    <form method="get" action="${pageContext.request.contextPath}/salesman/orders" class="row g-3 align-items-end">
                        <div class="col-md-5">
                            <label for="searchCode" class="form-label">Search</label>
                            <input
                                    type="text"
                                    class="form-control"
                                    id="searchCode"
                                    name="searchCode"
                                    value="${param.searchCode}"
                                    placeholder="Order code, customer name, phone"
                            >
                        </div>

                        <div class="col-md-3">
                            <label for="status" class="form-label">Status</label>
                            <select id="status" name="status" class="form-select">
                                <option value="">All</option>
                                <option value="DRAFT" ${param.status == 'DRAFT' ? 'selected' : ''}>DRAFT</option>
                                <option value="SUBMITTED" ${param.status == 'SUBMITTED' ? 'selected' : ''}>SUBMITTED</option>
                                <option value="PROCESSING" ${param.status == 'PROCESSING' ? 'selected' : ''}>PROCESSING</option>
                                <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                                <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                            </select>
                        </div>

                        <input type="hidden" name="sortBy" value="${sortBy}">
                        <input type="hidden" name="sortDir" value="${sortDir}">

                        <div class="col-md-4 d-flex gap-2">
                            <button type="submit" class="btn btn-primary">
                                <i class="fas fa-search me-1"></i>Search
                            </button>
                            <a href="${pageContext.request.contextPath}/salesman/orders" class="btn btn-outline-secondary">
                                Reset
                            </a>
                        </div>
                    </form>
                </div>
            </div>

            <!-- Table Container -->

            <div class="table-responsive">
                <!-- HEADER -->
                <c:set var="tableHeader" scope="request">
                    <tr>
                        <th class="py-3 px-4" data-sort="orderCode">Order Code</th>
                        <th class="py-3 px-4" data-sort="customerName">Customer</th>
                        <th class="py-3 px-4" data-sort="status">Status</th>
                        <th class="py-3 px-4" data-sort="createdAt">Created At</th>
                        <th class="py-3 text-center">Action</th>
                    </tr>
                </c:set>

                <!-- BODY -->
                <c:set var="tableBody" scope="request">

                    <c:choose>

                        <c:when test="${not empty orders}">

                            <c:forEach items="${orders}" var="o">

                                <tr>

                                    <td class="px-4 fw-semibold text-primary">
                                        ${o.orderCode}
                                    </td>

                                    <td class="px-4">
                                        ${o.customerName}
                                    </td>

                                    <td class="px-4">

                                        <c:choose>

                                            <c:when test="${o.status == 'SUBMITTED'}">
                                                <span class="badge bg-warning text-dark">
                                                    <i class="bi bi-clock-history me-1"></i>${o.status}
                                                </span>
                                            </c:when>

                                            <c:when test="${o.status == 'PROCESSING'}">
                                                <span class="badge bg-info text-white">
                                                    <i class="bi bi-arrow-repeat me-1"></i>${o.status}
                                                </span>
                                            </c:when>

                                            <c:when test="${o.status == 'COMPLETED'}">
                                                <span class="badge bg-success text-white">
                                                    <i class="bi bi-check-circle me-1"></i>${o.status}
                                                </span>
                                            </c:when>

                                            <c:when test="${o.status == 'CANCELLED'}">
                                                <span class="badge bg-danger text-white">
                                                    <i class="bi bi-x-circle me-1"></i>${o.status}
                                                </span>
                                            </c:when>

                                            <c:otherwise>
                                                <c:if test="${o.status eq 'DRAFT'}">
                                                    <span class="badge bg-secondary text-white">
                                                        ${o.status}
                                                    </span>
                                                </c:if>
                                            </c:otherwise>

                                        </c:choose>

                                    </td>

                                    <td class="px-4 text-muted">
                                        <i class="bi bi-calendar3 me-1"></i>${o.createdAt}
                                    </td>

                                    <td class="px-4 text-center">

                                        <a href="${pageContext.request.contextPath}/salesman/order/detail?id=${o.id}"
                                           class="btn btn-sm btn-outline-primary">

                                            <i class="bi bi-eye me-1"></i>View

                                        </a>

                                    </td>

                                </tr>

                            </c:forEach>

                        </c:when>


                        <c:otherwise>

                            <tr>
                                <td colspan="5" class="text-center py-5">

                                    <div class="text-muted">

                                        <i class="bi bi-inbox display-1 d-block mb-3"></i>

                                        <h5>No Orders Found</h5>

                                        <p class="mb-0">
                                            Start by creating your first order
                                        </p>

                                    </div>

                                </td>
                            </tr>

                        </c:otherwise>

                    </c:choose>

                </c:set>

                <!-- COMMON TABLE -->
                <jsp:include page="/WEB-INF/common/table.jsp"/>
            </div>

            <c:set var="queryParams" value="status=${param.status}&searchCode=${param.searchCode}&sortBy=${sortBy}&sortDir=${sortDir}"/>
            <jsp:include page="/WEB-INF/common/pagination.jsp">
                <jsp:param name="pageNo" value="${pageNo}"/>
                <jsp:param name="totalPages" value="${totalPages}"/>
                <jsp:param name="baseUrl" value="${pageContext.request.contextPath}/salesman/orders"/>
                <jsp:param name="queryParams" value="${queryParams}"/>
            </jsp:include>
        </main>

        <script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
        <script src="${pageContext.request.contextPath}/static/js/table-sort.js"></script>
    </body>
</html>