<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inventory Audit List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">

    <h2>Inventory Audit List</h2>

    <%--create new audit--%>
    <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'CREATE_AUDIT')}">
        <a href="${pageContext.request.contextPath}/inventory-audits/add">
            <button class="btn btn-primary mb-3">Create new audit</button>
        </a>
    </c:if>


    <br>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/inventory-audits" method="get"
          class="d-flex">

        <%--search audit by code--%>
        <div class="mr-4">
            <label>
                <input type="text" class="form-control" name="auditCode" placeholder="Search by code"
                       value="${param.auditCode}">
            </label>
        </div>

        <%--sort by status--%>
        <div class="mr-4">
            <label>
                <select name="status" class="form-select">
                    <option value="">All Status</option>
                    <option value="PENDING"  ${param.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                    <option value="SUBMITTED" ${param.status == 'SUBMITTED' ? 'selected' : ''}>SUBMITTED</option>
                    <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                    <option value="REJECTED" ${param.status == 'REJECTED' ? 'REJECTED' : ''}>REJECTED</option>
                    <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                </select>
            </label>
        </div>

        <div class="">
            <button type="submit" class="btn btn-outline-primary">
                Search
            </button>
        </div>
    </form>
    <!-- HEADER -->
    <c:set var="tableHeader" scope="request">
        <tr>
            <th>Audit Code</th>
            <th>Created By</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Updated At</th>
            <th>Action</th>
        </tr>
    </c:set>

    <!-- BODY -->
    <c:set var="tableBody" scope="request">

        <c:forEach items="${inventoryAudits}" var="i">
            <tr>

                <td>
                    <!-- Click -> Move to Inventory Audit Item List -->
                    <a href="${pageContext.request.contextPath}/inventory-audits/detail?inventoryAuditId=${i.id}">
                            ${i.auditCode}
                    </a>
                </td>

                <td>${i.user.fullName}</td>

                <td>${i.status}</td>

                <td>${i.createdAt}</td>

                <td>${i.updatedAt}</td>

                <td>

                    <div class="d-flex justify-content-center gap-2">
                        <c:if test="${i.status == 'PENDING'}">

                            <c:if test="${sessionScope.user != null
                                                  and sessionScope.user.role != null
                                                  and sessionScope.user.role.active
                                                  and fn:contains(sessionScope.userPermissions, 'CANCEL_AUDIT')}">
                                <form action="${pageContext.request.contextPath}/inventory-audits" method="post">
                                    <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                    <input type="hidden" name="pageNo" value="${pageNo}">
                                    <input type="hidden" name="auditCode" value="${param.auditCode}">
                                    <input type="hidden" name="status" value="${param.status}">
                                    <button type="submit" name="btnAction" value="CANCEL"
                                            class="btn btn-danger">
                                        CANCEL
                                    </button>
                                </form>
                            </c:if>

                            <c:if test="${sessionScope.user != null
                                                  and sessionScope.user.role != null
                                                  and sessionScope.user.role.active
                                                  and fn:contains(sessionScope.userPermissions, 'PERFORM_AUDIT')}">
                                <form action="${pageContext.request.contextPath}/inventory-audits/perform" method="get">
                                    <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                    <input type="hidden" name="pageNo" value="${pageNo}">
                                    <input type="hidden" name="auditCode" value="${param.auditCode}">
                                    <input type="hidden" name="status" value="${param.status}">
                                    <button type="submit"
                                            class="btn btn-primary">
                                        PERFORM
                                    </button>
                                </form>
                            </c:if>
                        </c:if>

                        <c:if test="${i.status == 'SUBMITTED'}">
                            <c:if test="${sessionScope.user != null
                                                  and sessionScope.user.role != null
                                                  and sessionScope.user.role.active
                                                  and fn:contains(sessionScope.userPermissions, 'CANCEL_AUDIT')}">

                                <form action="${pageContext.request.contextPath}/inventory-audits" method="post">
                                    <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                    <input type="hidden" name="pageNo" value="${pageNo}">
                                    <input type="hidden" name="auditCode" value="${param.auditCode}">
                                    <input type="hidden" name="status" value="${param.status}">
                                    <button type="submit" name="btnAction" value="REJECT"
                                            class="btn btn-warning">
                                        REJECT
                                    </button>
                                </form>

                                <form action="${pageContext.request.contextPath}/inventory-audits" method="post">
                                    <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                    <input type="hidden" name="pageNo" value="${pageNo}">
                                    <input type="hidden" name="auditCode" value="${param.auditCode}">
                                    <input type="hidden" name="status" value="${param.status}">
                                    <button type="submit" name="btnAction" value="APPROVE"
                                            class="btn btn-success">
                                        APPROVE
                                    </button>
                                </form>
                            </c:if>
                        </c:if>
                    </div>
                </td>

            </tr>
        </c:forEach>


        <c:if test="${empty inventoryAudits}">
            <tr>
                <td colspan="6" class="text-center text-muted">
                    No data
                </td>
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
                       href="${pageContext.request.contextPath}/inventory-audits?pageNo=${pageNo - 1}&auditCode=${param.auditCode}&status=${param.status}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/inventory-audits?pageNo=${i}&auditCode=${param.auditCode}&status=${param.status}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory-audits?pageNo=${pageNo + 1}&auditCode=${param.auditCode}&status=${param.status}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>