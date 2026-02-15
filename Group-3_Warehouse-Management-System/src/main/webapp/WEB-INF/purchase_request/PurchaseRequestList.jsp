<%-- 
    Document   : PurchaseRequestList
    Created on : Feb 6, 2026, 2:06:44 PM
    Author     : PC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>

    </head>
    <body>
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>
        <main class="main-content">


            <div class="card-body p-2">
                <div class="d-flex mb-3">
                    <c:if test="${!showCreatedBy}">
                        <a href="${pageContext.request.contextPath}/purchase-request/create"
                           class="btn btn-success">
                            + Create Purchase Request
                        </a>
                    </c:if>
                </div>
                <form method="get"
                      action="${pageContext.request.contextPath}/purchase-request/list"
                      class="row g-3 mb-3 align-items-end">

                    <!-- Request Code -->
                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Request Code</label>
                        <input type="text"
                               name="requestCode"
                               value="${param.requestCode}"
                               class="form-control"
                               placeholder="PR-xxxxx">
                    </div>

                    <!-- Status -->
                    <div class="col-md-2">
                        <label class="form-label fw-semibold">Status</label>
                        <select name="status" class="form-select">
                            <option value="">All</option>
                            <option value="PENDING"  ${param.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                            <option value="APPROVED" ${param.status == 'APPROVED' ? 'selected' : ''}>APPROVED</option>
                            <option value="REJECTED" ${param.status == 'REJECTED' ? 'selected' : ''}>REJECTED</option>
                        </select>
                    </div>

                    <!-- Created Date (1 filter ngày) -->
                    <div class="col-md-3">
                        <label class="form-label fw-semibold">Created Date</label>
                        <input type="date"
                               name="createdDate"
                               value="${param.createdDate}"
                               class="form-control">
                    </div>

                    <!-- Buttons -->
                    <div class="col-md-2 d-flex gap-2">
                        <button type="submit" class="btn btn-primary w-100">
                            Search
                        </button>

                        <a href="${pageContext.request.contextPath}/purchase-request/list"
                           class="btn btn-secondary w-100">
                            Clear
                        </a>
                    </div>
                </form>



                <div style="overflow-x: auto;">   <!-- Cho phép scroll ngang nếu nhiều role -->

                    <table class="table table-bordered mb-0">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Request Code</th>
                                <th>Status</th>

                                <c:if test="${showCreatedBy}">
                                    <th>Created By</th>
                                    </c:if>

                                <th>Approved By</th>
                                <th>Created At</th>
                                <th>Action</th>
                            </tr>
                        </thead>

                        <tbody>
                            <c:forEach items="${purchaseRequests}" var="pr">
                                <tr>
                                    <td>${pr.requestCode}</td>
                                    <td>${pr.status}</td>
                                    <c:if test="${showCreatedBy}">
                                        <td>${pr.createdByName}</td>
                                    </c:if>

                                    <td>${pr.approvedByName}</td>
                                    <td>${pr.createdAt}</td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/purchase-request/detail?id=${pr.id}">
                                            View
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                    <c:if test="${totalPages > 1}">
                        <nav class="mt-3">
                            <ul class="pagination justify-content-center">

                                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/brands?pageNo=${pageNo - 1}">
                                        Previous
                                    </a>
                                </li>

                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/brands?pageNo=${i}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>

                                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/brands?pageNo=${pageNo + 1}">
                                        Next
                                    </a>
                                </li>

                            </ul>
                        </nav>
                    </c:if>

                </div>
            </div>
        </main>
    </body>
</html>
