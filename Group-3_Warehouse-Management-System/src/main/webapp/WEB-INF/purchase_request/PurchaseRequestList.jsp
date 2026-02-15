<%-- 
    Document   : PurchaseRequestList
    Created on : Feb 6, 2026, 2:06:44 PM
    Author     : PC
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
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
                <div style="overflow-x: auto;">   <!-- Cho phép scroll ngang nếu nhiều role -->

                    <table class="table table-bordered mb-0">
                        <thead class="table-primary text-center">
                            <tr>
                                <th>Request Code</th>
                                <th>Status</th>
                                <th>Created By</th>
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
                                    <td>${pr.createdByName}</td>
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
