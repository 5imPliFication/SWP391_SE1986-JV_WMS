<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html>
<head>
    <title>Product List</title>
    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<div class="main-content">

    <h2>Product List</h2>

    <%--create new Product--%>
    <c:if test="${sessionScope.user != null
                          and sessionScope.user.role != null
                          and sessionScope.user.role.active
                          and fn:contains(sessionScope.userPermissions, 'CREATE_PRODUCT')}">
        <a href="${pageContext.request.contextPath}/products/add">
            <button class="btn btn-primary mb-3">Add new product</button>
        </a>
    </c:if>
    <br>
    <br>
    <%--form submit for search and sort--%>
    <form action="${pageContext.request.contextPath}/products" method="get"
          class="row g-2 align-items-end mb-3">

        <%--search user by name--%>
        <div class="col-auto">
            <input type="text" class="form-control" name="searchName" placeholder="Search by name"
                   value="${param.searchName}">
        </div>

        <%--sort by brand name--%>
        <div class="col-auto">
            <select name="brandName" class="form-select">
                <option value="">All Brand</option>
                <c:forEach var="b" items="${brands}">
                    <option value="${b.name}"
                        ${param.brandName == b.name ? 'selected' : ''}>
                            ${b.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by category name--%>
        <div class="col-auto">
            <select name="categoryName" class="form-select">
                <option value="">All Category</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.name}"
                        ${param.categoryName == c.name ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by model name--%>
        <div class="col-auto">
            <select name="modelName" class="form-select">
                <option value="">All Model</option>
                <c:forEach var="c" items="${models}">
                    <option value="${c.name}"
                        ${param.modelName == c.name ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by chip name--%>
        <div class="col-auto">
            <select name="chipName" class="form-select">
                <option value="">All Model</option>
                <c:forEach var="c" items="${chips}">
                    <option value="${c.name}"
                        ${param.chipName == c.name ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by ram size--%>
        <div class="col-auto">
            <select name="ramSize" class="form-select">
                <option value="">All Ram</option>
                <c:forEach var="c" items="${rams}">
                    <option value="${c.size}"
                        ${param.ramSize == c.size ? 'selected' : ''}>
                            ${c.size}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by storage size--%>
        <div class="col-auto">
            <select name="storageSize" class="form-select">
                <option value="">All Storage</option>
                <c:forEach var="c" items="${storages}">
                    <option value="${c.size}"
                        ${param.storageSize == c.size ? 'selected' : ''}>
                            ${c.size}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by screen size--%>
        <div class="col-auto">
            <select name="screenSize" class="form-select">
                <option value="">All Screen</option>
                <c:forEach var="c" items="${sizes}">
                    <option value="${c.size}"
                        ${param.screenSize == c.size ? 'selected' : ''}>
                            ${c.size}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by status--%>
        <div class="col-auto">
            <select name="isActive" class="form-select">
                <option value="">All Status</option>
                <option value="true"  ${param.isActive == 'true' ? 'selected' : ''}>Active</option>
                <option value="false" ${param.isActive == 'false' ? 'selected' : ''}>Inactive</option>
            </select>
        </div>

        <div class="col-auto">
            <button type="submit" class="btn btn-outline-primary">
                Search
            </button>
        </div>

    </form>

    <!-- HEADER -->
    <c:set var="tableHeader" scope="request">
        <tr>
            <th>Image</th>
            <th>Name</th>
            <th>Description</th>
            <th>Category</th>
            <th>Brand</th>
            <th>Model</th>
            <th>Chip</th>
            <th>Ram</th>
            <th>Storage</th>
            <th>Screen</th>
            <th>Total Quantity</th>
            <th>Unit</th>
            <th>Status</th>
            <th>Created Time</th>
            <th>Updated Time</th>
            <th>Action</th>
        </tr>
    </c:set>

    <!-- BODY -->
    <c:set var="tableBody" scope="request">

        <c:forEach items="${products}" var="p">
            <tr>
                <td>
                    <img src="${p.imgUrl}" class="product-img" alt="${p.name}">
                </td>
                <td>
                    <!-- Click -> Move to Product Item List -->
                    <a href="${pageContext.request.contextPath}/products/items?productId=${p.id}">
                            ${p.name}
                    </a>
                </td>
                <td>${p.description}</td>
                <td>${p.category.name}</td>
                <td>${p.brand.name}</td>
                <td>${p.model.name}</td>
                <td>${p.chip.name}</td>
                <td>${p.ram.size}</td>
                <td>${p.storage.size}</td>
                <td>${p.size.size}</td>
                <td>${p.totalQuantity} item</td>
                <td>${p.unit.name}</td>
                <td>
                    <jsp:include page="/WEB-INF/common/statusBadge.jsp">
                        <jsp:param name="active" value="${p.isActive}"/>
                    </jsp:include>
                </td>
                <td>${p.createdAt}</td>
                <td>${p.updatedAt}</td>
                <td>
                    <c:if test="${sessionScope.user != null
                                          and sessionScope.user.role != null
                                          and sessionScope.user.role.active
                                          and fn:contains(sessionScope.userPermissions, 'UPDATE_PRODUCT')}">

                        <a href="${pageContext.request.contextPath}/products/update?productId=${p.id}&pageNo=${pageNo}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&modelName=${param.modelName}&chipName=${param.chipName}&ramSize=${param.ramSize}&storageSize=${param.storageSize}&screenSize=${param.screenSize}&isActive=${param.isActive}"
                           class="btn btn-warning btn-sm">
                            Edit
                        </a>

                    </c:if>
                </td>
            </tr>
        </c:forEach>

        <c:if test="${empty products}">
            <tr>
                <td colspan="16" class="text-center text-muted">No data</td>
            </tr>
        </c:if>

    </c:set>

    <!-- COMMON TABLE -->
    <jsp:include page="/WEB-INF/common/table.jsp"/>


    <%-- pagination--%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">

                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products?pageNo=${pageNo - 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&modelName=${param.modelName}&chipName=${param.chipName}&ramSize=${param.ramSize}&storageSize=${param.storageSize}&screenSize=${param.screenSize}&isActive=${param.isActive}">
                        Previous
                    </a>
                </li>

                    <%-- current page  --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/products?pageNo=${i}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&modelName=${param.modelName}&chipName=${param.chipName}&ramSize=${param.ramSize}&storageSize=${param.storageSize}&screenSize=${param.screenSize}&isActive=${param.isActive}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/products?pageNo=${pageNo + 1}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&modelName=${param.modelName}&chipName=${param.chipName}&ramSize=${param.ramSize}&storageSize=${param.storageSize}&screenSize=${param.screenSize}&isActive=${param.isActive}">
                        Next
                    </a>
                </li>

            </ul>
        </nav>
    </c:if>
</div>

</body>
</html>