<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

    <head>
        <meta charset="UTF-8">
        <title>Import History</title>
        <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    </head>

    <body >
        <jsp:include page="/WEB-INF/common/sidebar.jsp"/>

        <main class="main-content">

            <h2 class="mb-4">Inventory</h2>

            <div class="d-flex align-items-center mb-3">
                <%-- search product by name --%>
                <form class="form-inline" action="${pageContext.request.contextPath}/inventory" method="get">

                    <input type="text" name="name" class="form-control mr-2"
                           placeholder="Search name product" value="${param.name}">

                    <%-- SORT BY QUANTITY --%>
                    <select name="sort" class="form-control mr-2">
                        <option value="">Sort by quantity</option>
                        <option value="asc" ${param.sort == 'asc' ? 'selected' : ''}>Low → High</option>
                        <option value="desc" ${param.sort == 'desc' ? 'selected' : ''}>High → Low</option>
                    </select>

                    <button type="submit" class="btn btn-primary mr-2" name="action" value="search">
                        Search
                    </button>
                </form>
            </div>


            <div class="mt-4">
                <!-- HEADER -->
                <c:set var="tableHeader" scope="request">
                    <tr>
                        <th>No</th>
                        <th>Name</th>
                        <th>Quantity</th>
                        <th>Status</th>
                        <th>Details</th>
                    </tr>
                </c:set>

                <!-- BODY -->
                <c:set var="tableBody" scope="request">

                    <c:choose>

                        <c:when test="${not empty products}">

                            <c:forEach items="${products}" var="product" varStatus="loop">

                                <tr>

                                    <td>${loop.index + 1}</td>

                                    <td>${product.name}</td>

                                    <td>${product.totalQuantity}</td>

                                    <td>

                                        <c:choose>

                                            <c:when test="${product.totalQuantity == 0}">
                                                <span class="badge bg-danger text-white">
                                                    Out of Stock
                                                </span>
                                            </c:when>

                                            <c:when test="${product.totalQuantity <= 10}">
                                                <span class="badge bg-warning text-dark">
                                                    Low Stock
                                                </span>
                                            </c:when>

                                        </c:choose>

                                    </td>

                                    <td>

                                        <a href="${pageContext.request.contextPath}/products/items?productId=${product.id}"
                                           class="btn btn-sm btn-primary">

                                            View

                                        </a>

                                    </td>

                                </tr>

                            </c:forEach>

                        </c:when>

                        <c:otherwise>

                            <tr>
                                <td colspan="5"
                                    class="text-center text-muted">

                                    No out-of-stock products found.

                                </td>
                            </tr>

                        </c:otherwise>

                    </c:choose>

                </c:set>

                <!-- COMMON TABLE -->
                <jsp:include page="/WEB-INF/common/table.jsp"/>

            </div>

            <%--   pagination --%>
            <c:if test="${totalPages > 1}">
                <nav class="mt-3">
                    <ul class="pagination justify-content-center">
                        <%-- previous page--%>
                        <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                            <a class="page-link"
                               href="${pageContext.request.contextPath}/inventory?name=${param.name}&sort=${param.sort}&pageNo=${pageNo - 1}">
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
                                           href="${pageContext.request.contextPath}/inventory?name=${param.name}&sort=${param.sort}&pageNo=${i}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:when>
                                <%-- alway display last page --%>
                                <c:when test="${i == totalPages}">
                                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/inventory?name=${param.name}&sort=${param.sort}&pageNo=${i}">
                                            ${i}
                                        </a>
                                    </li>
                                </c:when>
                                <%-- display between page--%>
                                <c:when test="${i >= left && i <= right}">
                                    <li
                                        class="page-item ${i == pageNo ? 'active' : ''}">
                                        <a class="page-link"
                                           href="${pageContext.request.contextPath}/inventory?name=${param.name}&sort=${param.sort}&pageNo=${i}">
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
                               href="${pageContext.request.contextPath}/inventory?name=${param.name}&sort=${param.sort}&pageNo=${pageNo + 1}">
                                Next
                            </a>
                        </li>
                    </ul>
                </nav>
            </c:if>
        </main>
    </body>

</html>