<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Import Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <!-- search product -->
    <form class="form-inline mb-3"
          action="${pageContext.request.contextPath}/products/search"
          method="get">

        <input type="text"
               name="keyword"
               class="form-control mr-2"
               placeholder="Search name product"
               value="${param.keyword}">

        <button type="submit" class="btn btn-primary mr-2">
            Search
        </button>
        <div class="ml-auto">
            <%--        link to create new product--%>
            <a href="${pageContext.request.contextPath}/products/new"
               class="btn btn-primary mr-2">
                New products
            </a>

            <%--      import product by excel--%>
            <a href="${pageContext.request.contextPath}/products/import-excel"
               class="btn btn-primary mr-2">
                Import excel
            </a>

            <%--        save import product--%>
            <button type="submit"
                    form="productForm"
                    class="btn btn-primary">
                Save
            </button>
        </div>

    </form>


    <!-- table -->
    <form id="productForm" action="${pageContext.request.contextPath}/products/save" method="post">

        <table class="table table-bordered">
            <thead class="thead-dark">
            <tr>
                <th>No.</th>
                <th>Serial</th>
                <th>Name product</th>
                <th>Quantity</th>
                <th>Price</th>
                <th>Total</th>
                <th>Delete</th>
            </tr>
            </thead>

            <tbody>
            <c:forEach var="item" items="${products}" varStatus="loop">
                <tr>
                    <td>${loop.index + 1}</td>

                    <td>
                        <input type="text" name="items[${loop.index}].serial" class="form-control"
                               value="${item.serial}" placeholder="Serial/IMEI">
                    </td>

                    <td>${item.productName}</td>

                    <td>
                        <input type="number" name="items[${loop.index}].quantity" class="form-control"
                               value="${item.quantity}">
                    </td>

                    <td>
                        <input type="number" name="items[${loop.index}].price" class="form-control"
                               value="${item.price}">
                    </td>

                    <td>
                            ${item.quantity * item.price}
                    </td>

                    <td>
                        <a href="${pageContext.request.contextPath}/products/delete?id=${item.id}"
                           class="btn btn-danger btn-sm" onclick="return confirm('Delete this item?')">
                            Delete
                        </a>
                    </td>
                </tr>
            </c:forEach>

            <c:if test="${empty products}">
                <tr>
                    <td colspan="7" class="text-center text-muted">
                        No data
                    </td>
                </tr>
            </c:if>
            </tbody>
        </table>
    </form>

    <!-- TOTAL -->
    <div class="font-weight-bold mb-3">
        Total payment:
        <span class="text-danger">
            ${totalPayment}
        </span>
    </div>

    <!-- PAGINATION -->
    <nav aria-label="Page navigation">
        <ul class="pagination">
            <li class="page-item ${currentPage == 1 ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage - 1}">
                    Previous
                </a>
            </li>

            <c:forEach begin="1" end="${totalPages}" var="p">
                <li class="page-item ${p == currentPage ? 'active' : ''}">
                    <a class="page-link" href="?page=${p}">
                            ${p}
                    </a>
                </li>
            </c:forEach>

            <li class="page-item ${currentPage == totalPages ? 'disabled' : ''}">
                <a class="page-link" href="?page=${currentPage + 1}">
                    Next
                </a>
            </li>
        </ul>
    </nav>
</main>

</body>

</html>