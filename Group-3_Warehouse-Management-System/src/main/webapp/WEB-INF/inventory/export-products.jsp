<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Export Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <%--    search and filter--%>
    <div class="d-flex justify-content-between align-items-end mb-3">

        <%--        filter date--%>
        <form action="${pageContext.request.contextPath}/inventory/export"
              method="get"
              class="form-row align-items-end">

            <div class="form-group mr-2">
                <label class="small mb-1">From date</label>
                <input type="date"
                       name="fromDate"
                       value="${param.fromDate}"
                       class="form-control form-control-sm">
            </div>

            <div class="form-group mr-2">
                <label class="small mb-1">To date</label>
                <input type="date"
                       name="toDate"
                       value="${param.toDate}"
                       class="form-control form-control-sm">
            </div>

            <div class="form-group mb-0">
                <button type="submit"
                        name="action"
                        value="search"
                        class="btn btn-primary btn-sm">
                    Search
                </button>
            </div>
        </form>

        <%--        export excel--%>
        <div>
            <a href="${pageContext.request.contextPath}/inventory/export?action=excel&fromDate=${param.fromDate}&toDate=${param.toDate}"
               class="btn btn-primary btn-sm">
                Export Excel
            </a>
        </div>

    </div>

    <%--    message--%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType}">
                ${message}
        </div>
    </c:if>

    <%--  table export order  --%>
    <c:if test="${not empty exportOrders}">
        <div class="table-responsive">
            <table class="table table-bordered table-hover table-sm">
                <thead class="thead-dark text-center">
                <tr>
                    <th style="width: 140px;">Order Code</th>
                    <th style="width: 140px;">Date</th>
                    <th>Salesman</th>
                    <th>Customer</th>
                    <th style="width: 120px;">Status</th>
                    <th style="width: 100px;">Details</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${exportOrders}" var="order">
                    <tr>
                        <td class="text-center align-middle">
                                ${order.code}
                        </td>
                        <td class="text-center align-middle">
                                ${order.exportDate}
                        </td>
                        <td class="align-middle">
                                ${order.salesmanName}
                        </td>
                        <td class="align-middle">
                                ${order.customerName}
                        </td>
                        <td class="text-center align-middle">
                                ${order.status}
                        </td>
                        <td class="text-center align-middle">
                            <a href="${pageContext.request.contextPath}/inventory/export?action=detail&id=${order.id}"
                               class="btn btn-info btn-sm">
                                View
                            </a>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </c:if>

<%--    empty message--%>
    <c:if test="${empty exportOrders}">
        <div class="alert alert-warning">
            No export orders found.
        </div>
    </c:if>

<%--   pagination --%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                <!-- previous -->
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/export?pageNo=${pageNo - 1}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                        Previous
                    </a>
                </li>

                <!-- pages -->
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/inventory/import/history?pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                <!-- next -->
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/import/history?pageNo=${pageNo + 1}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>

</main>
</body>
</html>