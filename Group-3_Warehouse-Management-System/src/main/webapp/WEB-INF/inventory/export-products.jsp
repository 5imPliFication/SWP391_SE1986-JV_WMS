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
    <h2 class="mb-4">Export Products</h2>
    <div class="d-flex align-items-center mb-3">
        <form class="form-inline"
              action="${pageContext.request.contextPath}/inventory/export"
              method="get">
            <%--search name customer--%>
            <input type="text"
                   name="name"
                   class="form-control mr-5"
                   placeholder="Search name customer"
                   value="${param.name}">

            <%--    from date--%>
            <span class="mr-2">From Date</span>
            <div class="form-group mr-5">
                <input type="date"
                       name="fromDate"
                       value="${param.fromDate}"
                       class="form-control form-control-sm">
            </div>

            <%--    to date--%>
            <span class="mr-2">To Date</span>
            <div class="form-group mr-2">
                <input type="date"
                       name="toDate"
                       value="${param.toDate}"
                       class="form-control form-control-sm">
            </div>

            <%-- filter by status --%>
            <div class="col-auto">
                <label>
                    <select name="status" class="form-control">
                        <option value="" ${empty status ? 'selected' : ''}>
                            All Status
                        </option>

                        <option value="SUBMITTED" ${status == 'SUBMITTED' ? 'selected' : ''}>
                            SUBMITTED
                        </option>

                        <option value="PROCESSING" ${status == 'PROCESSING' ? 'selected' : ''}>
                            PROCESSING
                        </option>
                    </select>
                </label>
            </div>

            <%--    button search--%>
            <button type="submit"
                    class="btn btn-primary mr-2"
                    name="action"
                    value="search">
                Search
            </button>
        </form>
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
                            <c:choose>
                                <%-- submiteed -> processing --%>
                                <c:when test="${order.status == 'SUBMITTED'}">
                                    <form action="${pageContext.request.contextPath}/inventory/update-status"
                                          method="post" style="display:inline;">
                                        <input type="hidden" name="orderCode" value="${order.code}"/>
                                        <input type="hidden" name="newStatus" value="PROCESSING"/>
                                        <button type="submit" class="btn btn-sm btn-warning">
                                                ${order.status}
                                        </button>
                                    </form>
                                </c:when>

                                <%-- processing -> completed --%>
                                <c:when test="${order.status == 'PROCESSING'}">
                                    <form action="${pageContext.request.contextPath}/inventory/update-status"
                                          method="post" style="display:inline;">

                                        <input type="hidden" name="orderCode" value="${order.code}"/>
                                        <input type="hidden" name="newStatus" value="COMPLETED"/>
                                        <button type="submit" class="btn btn-sm btn-success">
                                                ${order.status}
                                        </button>
                                    </form>
                                </c:when>

                            </c:choose>
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
                    <%-- previous page--%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/export?name=${param.name}&pageNo=${pageNo - 1}&fromDate=${param.fromDate}&toDate=${param.toDate}&status=${param.status}">
                        Previous
                    </a>
                </li>

                <c:set var="left" value="${pageNo - 2}"/>
                <c:set var="right" value="${pageNo + 2}"/>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <%-- alway display first page --%>
                        <c:when test="${i == 1}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export?name=${param.name}&pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}&status=${param.status}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- alway display last page --%>
                        <c:when test="${i == totalPages}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export?name=${param.name}&pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}&status=${param.status}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display between page--%>
                        <c:when test="${i >= left && i <= right}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export?name=${param.name}&pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}&status=${param.status}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display hidden page by ... --%>
                        <c:when test="${i == left - 1 || i == right + 1}">
                            <li class="page-item disabled">
                                <span class="page-link">...</span>
                            </li>
                        </c:when>
                    </c:choose>
                </c:forEach>

                    <%--next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/export?name=${param.name}&pageNo=${pageNo + 1}&fromDate=${param.fromDate}&toDate=${param.toDate}&status=${param.status}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>

</main>
</body>
</html>