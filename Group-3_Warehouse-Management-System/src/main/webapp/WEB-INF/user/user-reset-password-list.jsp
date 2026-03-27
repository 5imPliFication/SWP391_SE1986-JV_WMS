<%--
  Created by IntelliJ IDEA.
  User: ADMIN
  Date: 1/8/2026
  Time: 9:03 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>User Reset Password List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">

    <h2>List Reset Password Request</h2>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/admin/password-reset"
          method="get"
          class="row g-2 align-items-end mb-3">

        <%-- search user by name --%>
        <div class="col-auto">
            <input type="text"
                   class="form-control"
                   name="searchName"
                   placeholder="Search by name"
                   value="${param.searchName}">
        </div>

        <%-- sort by status --%>
        <div class="col-auto">
            <select name="status" class="form-select">
                <option value="">Status</option>

                <option value="PENDING"
                ${param.status == 'PENDING' ? 'selected' : ''}>
                    PENDING
                </option>

                <option value="APPROVED"
                ${param.status == 'APPROVED' ? 'selected' : ''}>
                    APPROVED
                </option>

                <option value="REJECTED"
                ${param.status == 'REJECTED' ? 'selected' : ''}>
                    REJECTED
                </option>
            </select>
        </div>

        <div class="col-auto">
            <button type="submit" class="btn btn-outline-primary">
                Search
            </button>
        </div>

    </form>

    <c:set var="tableHeader" scope="request">
        <tr>
            <th>No.</th>
            <th>Full Name</th>
            <th>Email</th>
            <th>Request Status</th>
            <th>Request Created Time</th>
            <th>Option</th>
        </tr>
    </c:set>

    <!-- BODY -->
    <c:set var="tableBody" scope="request">

        <c:forEach items="${passwordResetList}" var="p">
            <tr>
                <td>${p.id}</td>
                <td>${p.user.fullName}</td>
                <td>${p.user.email}</td>
                <td>${p.status}</td>
                <td>
                        <%--Convert LocalDateTime to Date for JSTL formatting--%>
                    <fmt:parseDate value="${p.createdAt}"
                                   pattern="yyyy-MM-dd'T'HH:mm"
                                   var="parsedCreatedDate"
                                   type="both"/>
                    <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                    value="${parsedCreatedDate}"/>
                </td>
                <td>
                    <form action="${pageContext.request.contextPath}/admin/password-reset" method="post"
                          class="d-inline">
                        <input type="hidden" name="passwordResetId" value="${p.id}">
                        <input type="hidden" name="userEmail" value="${p.user.email}">

                        <!-- giữ filter để dùng post vẫn còn get param được -->
                        <input type="hidden" name="searchName" value="${param.searchName}">
                        <input type="hidden" name="status" value="${param.status}">
                        <input type="hidden" name="pageNo" value="${param.pageNo}">

                        <c:if test="${sessionScope.user != null
                                              and sessionScope.user.role != null
                                              and sessionScope.user.role.active
                                              and fn:contains(sessionScope.userPermissions, 'UPDATE_PASSWORD_RESET_REQUEST')
                                              and p.status == 'PENDING'}">
                            <button type="submit" name="action" value="Approve" class="btn btn-sm btn-success">
                                Approve
                            </button>
                            <button type="submit" name="action" value="Reject" class="btn btn-sm btn-danger">
                                Reject
                            </button>
                        </c:if>

                    </form>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty passwordResetList}">
            <tr>
                <td colspan="7">No data</td>
            </tr>
        </c:if>

    </c:set>

    <!-- COMMON TABLE -->
    <jsp:include page="/WEB-INF/common/table.jsp"/>

    <%-- pagination--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/admin/password-reset?pageNo=${pageNo - 1}&searchName=${param.searchName}&status=${param.status}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/admin/password-reset?pageNo=${i}&searchName=${param.searchName}&status=${param.status}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/admin/password-reset?pageNo=${pageNo + 1}&searchName=${param.searchName}&status=${param.status}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>

