<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
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
    <h2>Product List</h2>

    <%--create new Product--%>
    <a href="${pageContext.request.contextPath}/products/add">
        <button class="btn btn-primary mb-3">Add new product</button>
    </a>
    <br>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/products" method="get"
          class="row g-2 align-items-center mb-3">
        <%--search user by name--%>
        <div class="col-auto">
            <label>
                <input type="text" class="form-control" name="searchName" placeholder="Search by name"
                       value="${param.searchName}">
            </label>
        </div>

        <%--sort by brand name--%>
        <div class="col-auto">
            <label>
                <select name="brandName" class="form-select">
                    <option value="">All Brand</option>
                    <c:forEach var="b" items="${brands}">
                        <option value="${b.name}"
                            ${param.brandName == b.name ? 'selected' : ''}>
                                ${b.name}
                        </option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <%--sort by category name--%>
        <div class="col-auto">
            <label>
                <select name="categoryName" class="form-select">
                    <option value="">All Category</option>
                    <c:forEach var="c" items="${categories}">
                        <option value="${c.name}"
                            ${param.categoryName == c.name ? 'selected' : ''}>
                                ${c.name}
                        </option>
                    </c:forEach>
                </select>
            </label>
        </div>

        <%--sort by status--%>
        <div class="col-auto">
            <label>
                <select name="isActive" class="form-select">
                    <option value="">All Status</option>
                    <option value="true"  ${param.isActive == 'true' ? 'selected' : ''}>Active</option>
                    <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Inactive</option>
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
            <th>Image</th>
            <th>Name</th>
            <th>Description</th>
            <th>Brand</th>
            <th>Category</th>
            <th>Status</th>
            <th>Created Time</th>
            <th>Updated Time</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach items="${products}" var="p">
            <tr>
                <td><img src="${p.imgUrl}" class="product-img" alt="${p.name}"></td>
                <td>
                    <a href="${pageContext.request.contextPath}/products/update?productId=${p.id}">${p.name}</a>
                </td>
                <td>${p.description}</td>
                <td>${p.brand.name}</td>
                <td>${p.category.name}</td>
                <td>${(p.isActive == true) ? 'Active' : 'Inactive'}</td>
                <td>${p.createdAt}</td>
                <td>${p.updatedAt}</td>

            </tr>
        </c:forEach>

        <c:if test="${empty products}">
            <tr>
                <td colspan="8">No data</td>
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
                       href="${pageContext.request.contextPath}/products?pageNo=${pageNo - 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/products?pageNo=${i}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products?pageNo=${pageNo + 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>

