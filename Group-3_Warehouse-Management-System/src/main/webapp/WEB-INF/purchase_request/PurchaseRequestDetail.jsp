<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Purchase Request Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <c:set var="isManager" value="${user.role.name eq 'Manager'}"/>
        <c:set var="isWarehouse" value="${user.role.name eq 'Warehouse'}"/>
        <c:set var="isPending" value="${prList.status eq 'PENDING'}"/>

        <main class="main-content container mt-4 w-auto" >

            <h3 class="mb-3">
                Purchase Request Detail
                <span class="badge bg-secondary">${prList.status}</span>
            </h3>

            <div class="row mb-3">
                <div class="col-md-4">
                    <label class="fw-semibold">Request Code</label>
                    <input class="form-control" value="${prList.requestCode}" readonly>
                </div>

                <div class="col-md-4">
                    <label class="fw-semibold">Created By</label>
                    <input class="form-control" value="${prList.createdByName}" readonly>
                </div>

                <div class="col-md-4">
                    <label class="fw-semibold">Created At</label>
                    <input class="form-control"
                           value="${prList.createdAt}"
                           readonly>
                </div>
            </div>

            <div class="mb-3">
                <label class="fw-semibold">Note</label>
                <textarea class="form-control" rows="3" readonly>${prList.note}</textarea>
            </div>

            <!-- EDIT BUTTON -->
            <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'UPDATE_PURCHASE_REQUEST')}">
                <c:if test="${!isManager && isPending}">
                    <button class="btn btn-warning mb-3"
                            data-bs-toggle="modal"
                            data-bs-target="#editModal">
                        ✏️ Edit Purchase Request
                    </button>
                </c:if>
            </c:if>

            <!-- ITEMS TABLE -->
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-primary text-center">
                        <tr>
                            <th>Product</th>
                            <th>Brand</th>
                            <th>Category</th>
                            <th>Qty</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach items="${items}" var="i">
                            <tr>
                                <td>${i.productName}</td>
                                <td>${i.brandName}</td>
                                <td>${i.categoryName}</td>
                                <td class="text-center">${i.quantity}</td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty items}">
                            <tr>
                                <td colspan="4" class="text-center text-muted">No items</td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- ACTIONS -->
            <div class="mt-3 d-flex gap-2">
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'UPDATE_PURCHASE_REQUEST')}">
                    <c:if test="${!isManager && isPending}">
                        <form method="post"
                              action="${pageContext.request.contextPath}/purchase-request/detail"
                              onsubmit="return confirm('Cancel this purchase request?')">
                            <input type="hidden" name="id" value="${prList.id}">
                            <input type="hidden" name="action" value="cancel">
                            <button class="btn btn-danger">❌ Cancel</button>
                        </form>
                    </c:if>
                    <c:if test="${isWarehouse}">
                        <%--đặt tên ? vậy, prList nhưng có giá trị là prDetail--%>
                        <c:if test="${prList.status eq 'APPROVED'}">
                            <a href="${pageContext.request.contextPath}/inventory/import?purchaseId=${prList.id}&purchaseCode=${prList.requestCode}&action=import"
                               class="btn btn-primary">
                                Import Product
                            </a>
                        </c:if>
                    </c:if>

                    <c:if test="${isManager && isPending}">
                        <form method="post" action="${pageContext.request.contextPath}/purchase-request/detail">
                            <input type="hidden" name="id" value="${prList.id}">
                            <input type="hidden" name="action" value="approve">
                            <button class="btn btn-success">Approve</button>
                        </form>

                        <form method="post" action="${pageContext.request.contextPath}/purchase-request/detail">
                            <input type="hidden" name="id" value="${prList.id}">
                            <input type="hidden" name="action" value="reject">
                            <button class="btn btn-danger">Reject</button>
                        </form>
                    </c:if>
                </c:if>
                <a href="${pageContext.request.contextPath}/purchase-request/list"
                   class="btn btn-secondary ms-auto">
                    ← Back
                </a>
            </div>

        </main>

        <!-- INCLUDE MODAL -->
        <jsp:include page="/WEB-INF/purchase_request/PurchaseRequestEdit.jsp"/>

        <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>
    </body>
</html>
