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
                <c:when test="${not empty productList}">
                    <c:forEach var="product" items="${productList}" varStatus="loop">
                        <tr>
                            <td>${loop.index + 1}</td>
                            <td>${product.name}</td>
                            <td>${product.stock}</td>

                            <td>
                                <c:choose>
                                    <c:when test="${product.stock == 0}">
                                        <span class="badge bg-danger">Out of Stock</span>
                                    </c:when>
                                    <c:when test="${product.stock <= 5}">
                                        <span class="badge bg-warning text-dark">Low Stock</span>
                                    </c:when>
                                    <c:otherwise>
                                        <span class="badge bg-success">In Stock</span>
                                    </c:otherwise>
                                </c:choose>
                            </td>

                            <td>
                                <a href="product-detail?id=${product.id}"
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