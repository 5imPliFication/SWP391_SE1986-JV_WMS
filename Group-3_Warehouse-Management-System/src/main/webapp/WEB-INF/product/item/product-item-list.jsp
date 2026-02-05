<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product Item List</title>
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
    <h2>Product Item List</h2>

    <%--Product Name (Readonly)--%>
    <div class="col-auto">
        <label>
            <input type="text" class="form-control" name="productName" readonly
                   value="${product.name}"
                   style="background-color: #adb5bd;"
            >
        </label>
    </div>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/products/items" method="get"
          class="row g-2 align-items-center mb-3">
        <%--Hidden productId--%>
        <input type="hidden" name="productId" value="${product.id}"/>

        <%--search by serial number--%>
        <div class="col-auto">
            <label>
                <input type="text" class="form-control" name="searchSerial" placeholder="Search by serial number"
                       value="${param.searchSerial}">
            </label>
        </div>

        <%--sort by status--%>
        <div class="col-auto">
            <label>
                <select name="isActive" class="form-select">
                    <option value="">All Status</option>
                    <option value="true"  ${param.isActive == 'true' ? 'selected' : ''}>Available</option>
                    <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Unavailable</option>
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
            <th>Serial Number</th>
            <th>Imported Price</th>
            <th>Current Price</th>
            <th>Imported At</th>
            <th>Updated At</th>
            <th>Status</th>
            <th>Action</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${productItems}" var="pi">
            <tr>
                <td>${pi.serial}</td>
                <td>
                    <fmt:formatNumber value="${pi.importedPrice}" type="number" groupingUsed="true"/> đ
                </td>
                <td>
                    <fmt:formatNumber value="${pi.currentPrice}" type="number" groupingUsed="true"/> đ
                </td>
                <td>${pi.importedAt}</td>
                <td>${pi.updatedAt}</td>
                <td>${(pi.isActive == true) ? 'Available' : 'Unavailable'}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/products/items/update?productItemId=${pi.id}&pageNo=${pageNo}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                        EDIT
                    </a>
                </td>

            </tr>
        </c:forEach>

        <c:if test="${empty productItems}">
            <tr>
                <td colspan="7">No data</td>
            </tr>
        </c:if>
        </tbody>
    </table>

    <a href="${pageContext.request.contextPath}/products">Back to product list</a>

    <%-- pagination--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products/items?pageNo=${pageNo - 1}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/products/items?pageNo=${i}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products/items?pageNo=${pageNo + 1}&productId=${param.productId}&searchSerial=${param.searchSerial}&isActive=${param.isActive}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>