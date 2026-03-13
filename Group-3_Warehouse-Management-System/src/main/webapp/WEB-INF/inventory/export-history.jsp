<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Export History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <h2 class="mb-4">Export History</h2>

    <div class="d-flex align-items-center mb-3">
        <form class="form-inline" action="${pageContext.request.contextPath}/inventory/export-history" method="get">
            <%--search by order code--%>
            <input type="text" name="searchCode" class="form-control mr-5"
                   placeholder="Search order code" value="${searchCode}">

            <%-- from date--%>
            <span class="mr-2">From Date</span>
            <div class="form-group mr-5">
                <input type="date" name="fromDate" value="${fromDate}"
                       class="form-control form-control-sm">
            </div>

            <%-- to date--%>
            <span class="mr-2">To Date</span>
            <div class="form-group mr-2">
                <input type="date" name="toDate" value="${toDate}"
                       class="form-control form-control-sm">
            </div>

            <%-- button search--%>
            <button type="submit" class="btn btn-primary mr-2">
                Search
            </button>
        </form>
    </div>

    <%-- message --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <%-- table --%>
    <div class="table-responsive">

        <!-- HEADER -->
        <c:set var="tableHeader" scope="request">
            <tr>
                <th class="text-left">Order Code</th>
                <th>Customer</th>
                <th>Processed Date</th>
                <th>Warehouse Staff</th>
                <th style="width: 100px;">Details</th>
            </tr>
        </c:set>

        <!-- BODY -->
        <c:set var="tableBody" scope="request">
            <c:choose>
                <c:when test="${not empty orders}">
                    <c:forEach items="${orders}" var="order">
                        <tr>
                            <td class="text-left align-middle fw-bold">
                                    ${order.orderCode}
                            </td>
                            <td class="align-middle">
                                    ${order.customerName}
                            </td>
                            <td class="text-center align-middle">
                                <fmt:formatDate pattern="dd/MM/yyyy HH:mm" value="${order.processedAt}"/>
                            </td>
                            <td class="align-middle">
                                    ${order.processedBy != null ? order.processedBy.fullName : '-'}
                            </td>
                            <td class="text-center align-middle">
                                <a href="${pageContext.request.contextPath}/inventory/export-history/detail?orderId=${order.id}"
                                   class="btn btn-info btn-sm">
                                    View
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </c:when>
                <c:otherwise>
                    <tr>
                        <td colspan="6" class="text-center text-muted py-3">
                            No export history found.
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
        </c:set>
        <!-- COMMON TABLE -->
        <jsp:include page="/WEB-INF/common/table.jsp"/>
    </div>


    <%-- pagination --%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                <%-- previous page--%>
                <li class="page-item ${pageNo <= 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/export-history?pageNo=${pageNo - 1}&searchCode=${searchCode}&fromDate=${fromDate}&toDate=${toDate}">
                        Previous
                    </a>
                </li>

                <c:set var="left" value="${pageNo - 2}"/>
                <c:set var="right" value="${pageNo + 2}"/>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <%-- always display first page --%>
                        <c:when test="${i == 1}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export-history?pageNo=${i}&searchCode=${searchCode}&fromDate=${fromDate}&toDate=${toDate}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- always display last page --%>
                        <c:when test="${i == totalPages}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export-history?pageNo=${i}&searchCode=${searchCode}&fromDate=${fromDate}&toDate=${toDate}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display between page--%>
                        <c:when test="${i >= left && i <= right}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/export-history?pageNo=${i}&searchCode=${searchCode}&fromDate=${fromDate}&toDate=${toDate}">
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
                <li class="page-item ${pageNo >= totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/export-history?pageNo=${pageNo + 1}&searchCode=${searchCode}&fromDate=${fromDate}&toDate=${toDate}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>

</main>
</body>

</html>
