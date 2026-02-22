<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Import History</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <h3 class="mb-4">Import History</h3>

    <%-- filter --%>
    <div class="d-flex justify-content-between align-items-end mb-3">
        <form action="${pageContext.request.contextPath}/import-history" method="get"
              class="form-row align-items-end">
            <div class="form-group mr-2">
                <label class="small mb-1">From date</label>
                <input type="date" name="fromDate" value="${param.fromDate}"
                       class="form-control form-control-sm">
            </div>

            <div class="form-group mr-2">
                <label class="small mb-1">To date</label>
                <input type="date" name="toDate" value="${param.toDate}"
                       class="form-control form-control-sm">
            </div>

            <div class="form-group mb-0">
                <button type="submit" class="btn btn-primary btn-sm">Search</button>
            </div>
        </form>

        <%-- export history by excel --%>
        <div>
            <a href="${pageContext.request.contextPath}/import-history?action=excel&fromDate=${param.fromDate}&toDate=${param.toDate}"
               class="btn btn-success btn-sm">
                Export Excel
            </a>
        </div>
    </div>

    <%-- message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
                ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <%-- table --%>
    <div class="table-responsive">
        <table class="table table-bordered table-hover table-sm">
            <thead class="thead-dark text-center">
            <tr>
                <th style="width: 150px;">Receipt Code</th>
                <th style="width: 160px;">Received Date</th>
                <th>Warehouse Staff</th>
                <th style="width: 120px;">Total Qty</th>
                <th>Note</th>
                <th style="width: 100px;">Details</th>
            </tr>
            </thead>
            <tbody>
            <c:choose>
                <c:when test="${not empty importHistories}">
                    <c:forEach items="${importHistories}" var="history">
                        <tr>
                            <td class="text-center align-middle font-weight-bold">
                                    ${history.receiptCode}
                            </td>
                            <td class="text-center align-middle">
                                    ${history.receivedAt}
                            </td>
                            <td class="align-middle">
                                    ${history.staffName}
                            </td>
                            <td class="text-center align-middle">
                                    ${history.totalQuantity}
                            </td>
                            <td class="align-middle">
                                    ${history.note}
                            </td>
                            <td class="text-center align-middle">
                                <a href="${pageContext.request.contextPath}/import-history?action=detail&id=${history.id}"
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
                            No import history found.
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>

    <%-- pagination --%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                    <%-- previous page--%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/import-history?pageNo=${pageNo - 1}&fromDate=${param.fromDate}&toDate=${param.toDate}">
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
                                   href="${pageContext.request.contextPath}/import-history?pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- alway display last page --%>
                        <c:when test="${i == totalPages}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/import-history?pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display between page--%>
                        <c:when test="${i >= left && i <= right}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/import-history?pageNo=${i}&fromDate=${param.fromDate}&toDate=${param.toDate}">
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
                       href="${pageContext.request.contextPath}/import-history?pageNo=${pageNo + 1}&fromDate=${param.fromDate}&toDate=${param.toDate}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>

</main>
</body>

</html>