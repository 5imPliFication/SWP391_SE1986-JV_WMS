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
    <h2 class="mb-4 text-danger">Out Of Stock Alert</h2>

    <div class="d-flex align-items-center mb-3">
        <%-- search product by name --%>
        <form class="form-inline" action="${pageContext.request.contextPath}" method="get">
            <input type="text" name="name" class="form-control mr-2"
                   placeholder="Search name product" value="${param.name}">
            <button type="submit" class="btn btn-primary mr-2" name="action" value="search">
                Search
            </button>
        </form>
    </div>
    <div class="container mt-4">
        <table class="table table-bordered table-hover">
            <thead class="table-dark">
            <tr>
                <th>No</th>
                <th>Name</th>
                <th>Stock</th>
                <th>Status</th>
                <th>Details</th>
            </tr>
            </thead>

            <tbody>
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
                                        <span class="badge bg-danger">Out of Stock</span>
                                    </c:when>
                                    <c:when test="${product.totalQuantity <= 10}">
                                        <span class="badge bg-warning text-dark">Low Stock</span>
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
                        <td colspan="5" class="text-center text-muted">
                            No out-of-stock products found.
                        </td>
                    </tr>
                </c:otherwise>
            </c:choose>
            </tbody>
        </table>
    </div>
</main>
</body>

</html>