<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Create New Audit</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">
    <h2>Create New Inventory Audit</h2>
    <div class="d-flex align-items-center mb-3">

        <%--form submit for search and sort--%>
        <form action="${pageContext.request.contextPath}/inventory-audits/add" method="get"
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
    </div>


    <%--table list product search--%>
    <table class="table table-bordered table-hover">
        <thead class="thead-dark">
        <tr>
            <th>ID</th>
            <th>Name</th>
            <th>Description</th>
            <th>Brand</th>
            <th>Category</th>
            <th></th>
        </tr>
        </thead>
        <tbody>
        <c:forEach items="${products}" var="product">
            <tr>
                <td>${product.id}</td>
                <td>${product.name}</td>
                <td>${product.description}</td>
                <td>${product.brand.name}</td>
                <td>${product.category.name}</td>
                    <%--add product to import product item--%>
                <td>
                    <form action="${pageContext.request.contextPath}/inventory-audits/add" method="post">
                        <input type="hidden" name="productId"
                               value="${product.id}">
                        <button class="btn btn-success btn-sm" type="submit"
                                name="action" value="add">
                            Add for audit
                        </button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>

    <c:if test="${empty products}">
        <tr>
            <td colspan="6">No data</td>
        </tr>
    </c:if>

    <c:if test="${not empty sessionScope.auditProductExistMessage}">
        <div class="alert alert-warning">
                ${sessionScope.auditProductExistMessage}
        </div>
        <c:remove var="auditProductExistMessage" scope="session"/>
    </c:if>

    <%-- pagination--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory-audits/add?pageNo=${pageNo - 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/inventory-audits/add?pageNo=${i}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory-audits/add?pageNo=${pageNo + 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>


    <%--Table for list product need audit--%>
    <form method="post" action="${pageContext.request.contextPath}/inventory-audits/add">
        <div>
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-sm">
                    <thead class="thead-dark text-center">
                    <tr>
                        <th>No.</th>
                        <th>Product</th>
                        <th>Brand</th>
                        <th>Category</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach items="${tmpInventoryAuditItems}" var="item" varStatus="status">
                        <tr>
                            <td class="text-center align-middle">
                                    ${status.index + 1}
                            </td>
                            <td class="align-middle">${item.product.name}</td>
                            <td class="align-middle">${item.product.brand.name}</td>
                            <td class="align-middle">${item.product.category.name}</td>

                                <%--Button delete product item from list need audit--%>
                            <td class="text-center align-middle">
                                <form method="POST" action="${pageContext.request.contextPath}/inventory-audits/add">
                                    <input type="hidden" name="productId" value="${item.product.id}">
                                    <input type="hidden" name="action" value="delete">
                                    <button type="submit" class="btn btn-link text-danger">Delete</button>
                                </form>
                            </td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
                <c:if test="${empty tmpInventoryAuditItems}">
                    <tr>
                        <td colspan="5">No data</td>
                    </tr>
                </c:if>

                <c:if test="${not empty sessionScope.auditCreatedSuccessMessage}">
                    <div class="alert alert-success">
                            ${sessionScope.auditCreatedSuccessMessage}
                    </div>
                    <c:remove var="auditCreatedSuccessMessage" scope="session"/>
                </c:if>

                <c:if test="${not empty sessionScope.auditNoProductMessage}">
                    <div class="alert alert-warning">
                            ${sessionScope.auditNoProductMessage}
                    </div>
                    <c:remove var="auditNoProductMessage" scope="session"/>
                </c:if>

            </div>
        </div>
    </form>

    <div class="d-flex justify-content-between mt-4">
        <!-- Back button -->
        <a href="${pageContext.request.contextPath}/inventory-audits"
           class="btn btn-secondary">
            ← Back
        </a>

        <!-- Create audit button -->
        <form action="${pageContext.request.contextPath}/inventory-audits/add"
              method="post">
            <button type="submit" class="btn btn-primary" value="create" name="action">
                Create
            </button>
        </form>

    </div>

</main>
</body>
</html>