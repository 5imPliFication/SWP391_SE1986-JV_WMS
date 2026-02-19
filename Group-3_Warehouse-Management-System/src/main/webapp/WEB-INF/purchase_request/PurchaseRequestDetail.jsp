<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <title>Purchase Request Detail</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css"
              rel="stylesheet">
    </head>

    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content container mt-4" style="max-width: 1100px">

            <h3 class="mb-3">
                Purchase Request Detail
                <span class="badge bg-secondary">${prList.status}</span>
            </h3>

            <!-- ===== HEADER INFO ===== -->
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

            <!-- ===== NOTE ===== -->
            <div class="mb-3">
                <label class="fw-semibold">Note</label>
                <textarea class="form-control" rows="3" readonly>${prList.note}</textarea>
            </div>

            <!-- ===== ITEMS TABLE ===== -->
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="table-primary text-center">
                        <tr>
                            <th>#</th>
                            <th>Product</th>
                            <th>Brand</th>
                            <th>Category</th>
                            <th>Qty</th>
                        </tr>
                    </thead>

                    <tbody>
                        <c:forEach items="${items}" var="i" varStatus="st">
                            <tr>
                                <td class="text-center">${st.index + 1}</td>

                                <td>
                                    <c:choose>
                                        <c:when test="${not empty i.productName}">
                                            ${i.productName}
                                        </c:when>
                                        <c:otherwise>
                                            ${i.productNameFromDB}
                                        </c:otherwise>
                                    </c:choose>
                                </td>

                                <td>${i.brandName}</td>
                                <td>${i.categoryName}</td>
                                <td class="text-center">${i.quantity}</td>
                            </tr>
                        </c:forEach>

                        <c:if test="${empty items}">
                            <tr>
                                <td colspan="5" class="text-center text-muted">
                                    No items
                                </td>
                            </tr>
                        </c:if>
                    </tbody>
                </table>
            </div>

            <!-- ===== ACTION BUTTONS ===== -->
            <div class="mt-3 d-flex gap-2">
                <a href="${pageContext.request.contextPath}/purchase-request/list"
                   class="btn btn-secondary">
                    ← Back
                </a>

                <!-- Delete chỉ khi PENDING & là creator -->
                <c:if test="${prList.status == 'PENDING' && prList.createdBy == user.id}">
                    <a href="${pageContext.request.contextPath}/purchase-request/delete?id=${pr.id}"
                       class="btn btn-danger"
                       onclick="return confirm('Delete this request?')">
                        Delete
                    </a>
                </c:if>
            </div>

        </main>
    </body>
</html>
