<%-- 
    Document   : PurchaseRequestList
    Created on : Feb 6, 2026, 2:06:44 PM
    Author     : PC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                <c:if test="${sessionScope.user != null
                              and sessionScope.user.role != null
                              and sessionScope.user.role.active
                              and fn:contains(sessionScope.userPermissions, 'CREATE_PURCHASE_REQUEST')}">
                      <div class="d-flex mb-3">
                          <a href="${pageContext.request.contextPath}/purchase-request/create"
                             class="btn btn-success">
                              + Create Purchase Request
                          </a>
                      </div>
                </c:if>
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
                        <select name="status" class="form-select" 
                                ${isWarehouse ? "disabled" : ""}              
                                >
                            <option value="">All</option>
                            <c:forEach items="${statuses}" var="s">
                                <option value="${s}"
                                        ${param.status == s ? 'selected' : ''}>
                                    ${s}
                                </option>
                            </c:forEach>
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
                                <tr class="${pr.status == 'CANCELLED' ? 'table-secondary text-muted' : ''}">
                                    <td>${pr.requestCode}</td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${pr.status == 'PENDING'}">
                                                <span class="badge bg-warning text-dark">${pr.status}</span>
                                            </c:when>

                                            <c:when test="${pr.status == 'APPROVED'}">
                                                <span class="badge bg-success">${pr.status}</span>
                                            </c:when>

                                            <c:when test="${pr.status == 'REJECTED'}">
                                                <span class="badge bg-danger">${pr.status}</span>
                                            </c:when>

                                            <c:when test="${pr.status == 'CANCELLED'}">
                                                <span class="badge bg-secondary">${pr.status}</span>
                                            </c:when>

                                            <c:when test="${pr.status == 'COMPLETED'}">
                                                <span class="badge bg-primary">${pr.status}</span>
                                            </c:when>

                                            <c:otherwise>
                                                <span class="badge bg-dark">${pr.status}</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <c:if test="${showCreatedBy}">
                                        <td>${pr.createdByName}</td>
                                    </c:if>

                                    <td>${pr.approvedByName}</td>
                                    <td>${pr.createdAt}</td>
                                    <td class="text-center">
                                        <c:choose>
                                            <c:when test="${pr.status == 'CANCELLED'}">
                                                <span class="text-muted fst-italic">No action</span>
                                            </c:when>

                                            <c:otherwise>
                                                <a href="${pageContext.request.contextPath}/purchase-request/detail?id=${pr.id}">
                                                    Detail
                                                </a>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>

                    </table>
                    <c:if test="${totalPages > 1}">
                        <nav class="mt-3">
                            <ul class="pagination justify-content-center">

                                <!-- Previous -->
                                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/purchase-request/list?pageNo=${pageNo - 1}&requestCode=${param.requestCode}&status=${param.status}&createdDate=${param.createdDate}">
                                        Previous
                                    </a>
                                </li>

                                <!-- Page numbers -->
                                <c:forEach begin="1" end="${totalPages}" var="i">
                                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/purchase-request/list?pageNo=${i}&requestCode=${param.requestCode}&status=${param.status}&createdDate=${param.createdDate}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:forEach>

                                <!-- Next -->
                                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                                    <a class="page-link"
                                       href="${pageContext.request.contextPath}/purchase-request/list?pageNo=${pageNo + 1}&requestCode=${param.requestCode}&status=${param.status}&createdDate=${param.createdDate}">
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
