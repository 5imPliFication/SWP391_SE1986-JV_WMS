<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Inventory Audit List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        table {
            border-collapse: collapse;
            width: 100%;
        }

        th, td {
            padding: 8px;
            border: 1px solid #ccc;
            text-align: left;
        }

        th {
            background-color: #f4f4f4;
        }

        .product-img {
            width: 40px;
            height: 40px;
            object-fit: cover;
            border-radius: 4px;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">
    <h2>Inventory Audit List</h2>

    <%--create new audit--%>
    <a href="${pageContext.request.contextPath}/inventory-audits/add">
        <button class="btn btn-primary mb-3">Create new audit</button>
    </a>
    <br>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/inventory-audits" method="get"
          class="row g-2 align-items-center mb-3">

        <%--search audit by code--%>
        <div class="col-auto">
            <label>
                <input type="text" class="form-control" name="auditCode" placeholder="Search by code"
                       value="${param.auditCode}">
            </label>
        </div>

        <%--sort by status--%>
        <div class="col-auto">
            <label>
                <select name="status" class="form-select">
                    <option value="">All Status</option>
                    <option value="PENDING"  ${param.status == 'PENDING' ? 'selected' : ''}>PENDING</option>
                    <option value="COMPLETED" ${param.status == 'COMPLETED' ? 'selected' : ''}>COMPLETED</option>
                    <option value="CANCELLED" ${param.status == 'CANCELLED' ? 'selected' : ''}>CANCELLED</option>
                </select>
            </label>
        </div>

        <div class="col-auto">
            <button type="submit" class="btn btn-outline-primary">
                Search
            </button>
        </div>
    </form>
    <table>
        <thead>
        <tr>
            <th>Audit Code</th>
            <th>Created By</th>
            <th>Status</th>
            <th>Created At</th>
            <th>Updated At</th>
            <th>Action</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${inventoryAudits}" var="i">
            <tr>
                <td>
                        <%--Click -> Move to Inventory Audit Item List--%>
                    <a href="${pageContext.request.contextPath}/inventory-audits/detail?inventoryAuditId=${i.id}">${i.auditCode}</a>
                </td>
                <td>${i.user.fullName}</td>
                <td>${i.status}</td>
                <td>${i.createdAt}</td>
                <td>${i.updatedAt}</td>

                <td>
                    <c:if test="${i.status == 'PENDING'}">
                        <div class="d-flex justify-content-around">
                            <form action="${pageContext.request.contextPath}/inventory-audits" method="post">
                                <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                <input type="hidden" name="pageNo" value="${pageNo}">
                                <input type="hidden" name="auditCode" value="${param.auditCode}">
                                <input type="hidden" name="status" value="${param.status}">
                                <button type="submit" class="btn btn-link p-0 m-0 align-baseline"
                                        style="text-decoration: none; color: red;">
                                    CANCEL
                                </button>
                            </form>
                            <form action="${pageContext.request.contextPath}/inventory-audits/perform" method="get">
                                <input type="hidden" name="inventoryAuditId" value="${i.id}">
                                <input type="hidden" name="pageNo" value="${pageNo}">
                                <input type="hidden" name="auditCode" value="${param.auditCode}">
                                <input type="hidden" name="status" value="${param.status}">
                                <button type="submit" class="btn btn-link p-0 m-0 align-baseline"
                                        style="text-decoration: none; color: blue;">
                                    PERFORM
                                </button>
                            </form>
                        </div>
                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty inventoryAudits}">
            <tr>
                <td colspan="6">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>

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