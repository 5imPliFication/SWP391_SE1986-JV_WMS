<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
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


        <%--sort by category name--%>
        <div class="col-auto">
            <select name="categoryId" class="form-select">
                <option value="">All Category</option>
                <c:forEach var="c" items="${categories}">
                    <option value="${c.id}"
                        ${param.categoryId == c.id.toString() ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by brand name--%>
        <div class="col-auto">
            <select id="brandSelect" name="brandId" class="form-select">
                <option value="">All Brand</option>
                <c:forEach var="b" items="${brands}">
                    <option value="${b.id}"
                        ${param.brandId == b.id.toString() ? 'selected' : ''}>
                            ${b.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by model name--%>
        <div class="col-auto">
            <select id="modelSelect" name="modelId" class="form-select">
                <option value="">All Model</option>
                <c:forEach var="c" items="${models}">
                    <option value="${c.id}" data-brand="${c.brand.id}"
                        ${param.modelId == c.id.toString() ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by chip name--%>
        <div class="col-auto">
            <select name="chipId" class="form-select">
                <option value="">All Chip</option>
                <c:forEach var="c" items="${chips}">
                    <option value="${c.id}"
                        ${param.chipId == c.id.toString() ? 'selected' : ''}>
                            ${c.name}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by ram size--%>
        <div class="col-auto">
            <select name="ramId" class="form-select">
                <option value="">All Ram</option>
                <c:forEach var="c" items="${rams}">
                    <option value="${c.id}"
                        ${param.ramId == c.id.toString() ? 'selected' : ''}>
                            ${c.size}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by storage size--%>
        <div class="col-auto">
            <select name="storageId" class="form-select">
                <option value="">All Storage</option>
                <c:forEach var="c" items="${storages}">
                    <option value="${c.id}"
                        ${param.storageId == c.id.toString() ? 'selected' : ''}>
                            ${c.size}
                    </option>
                </c:forEach>
            </select>
        </div>

        <%--sort by screen size--%>
        <div class="col-auto">
            <select name="sizeId" class="form-select">
                <option value="">All Screen</option>
                <c:forEach var="c" items="${sizes}">
                    <option value="${c.id}"
                        ${param.sizeId == c.id.toString() ? 'selected' : ''}>
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
            <th>Selling Price</th>
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
                    <img src="${p.imgUrl}" class="product-img" alt=""
                         style="width:60px; height:60px; object-fit:cover;">
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
                <td><fmt:formatNumber value="${p.currentPrice}" type="number" groupingUsed="true"/> đ</td>
                <td>${p.totalQuantity}</td>
                <td>${p.unit.name}</td>
                <td>
                    <jsp:include page="/WEB-INF/common/statusBadge.jsp">
                        <jsp:param name="active" value="${p.isActive}"/>
                    </jsp:include>
                </td>
                <td>
                        <%--Convert LocalDateTime to Date for JSTL formatting--%>
                        <fmt:parseDate value="${p.createdAt}"
                                       pattern="yyyy-MM-dd'T'HH:mm"
                                       var="parsedCreatedDate"
                                       type="both"/>
                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                        value="${parsedCreatedDate}"/>
                </td>
                <td>
                        <%--Convert LocalDateTime to Date for JSTL formatting--%>
                        <fmt:parseDate value="${p.updatedAt}"
                                       pattern="yyyy-MM-dd'T'HH:mm"
                                       var="parsedUpdatedDate"
                                       type="both"/>
                        <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                        value="${parsedUpdatedDate}"/>
                </td>
                <td>
                    <c:if test="${sessionScope.user != null
                                          and sessionScope.user.role != null
                                          and sessionScope.user.role.active
                                          and fn:contains(sessionScope.userPermissions, 'UPDATE_PRODUCT')}">

                        <form method="get" action="${pageContext.request.contextPath}/products/update">
                                <input type="hidden" name="productId" value="${p.id}">
                                <input type="hidden" name="pageNo" value="${param.pageNo}">
                                <input type="hidden" name="searchName" value="${param.searchName}">
                                <input type="hidden" name="brandId" value="${param.brandId}">
                                <input type="hidden" name="categoryId" value="${param.categoryId}">
                                <input type="hidden" name="modelId" value="${param.modelId}">
                                <input type="hidden" name="chipId" value="${param.chipId}">
                                <input type="hidden" name="ramId" value="${param.ramId}">
                                <input type="hidden" name="storageId" value="${param.storageId}">
                                <input type="hidden" name="sizeId" value="${param.sizeId}">
                                <input type="hidden" name="isActive" value="${param.isActive}">
                            <button type="submit" class="btn btn-sm btn-primary">Edit</button>
                        </form>

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

<script>
    document.getElementById("brandSelect").addEventListener("change", function () {
        let brandId = this.value;
        let options = document.querySelectorAll("#modelSelect option");
        options.forEach(opt => {
            let modelBrand = opt.dataset.brand;
            if (!modelBrand) return;
            if (brandId === "" || modelBrand === brandId) {
                opt.style.display = "";
            } else {
                opt.style.display = "none";
            }
        });
    });
</script>

</body>
</html>