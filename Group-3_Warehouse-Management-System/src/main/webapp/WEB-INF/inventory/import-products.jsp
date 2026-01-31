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
    <form class="form-inline mb-3" action="${pageContext.request.contextPath}" method="get">

        <input type="text" name="name" class="form-control mr-2" placeholder="Search name product"
               value="${param.name}">

        <button type="submit" class="btn btn-primary mr-2" name="action" value="search">
            Search
        </button>
        <div class="ml-auto">
            <%-- link to create new product--%>
            <a href="${pageContext.request.contextPath}/products/new" class="btn btn-primary mr-2">
                New products
            </a>

            <%-- import product by excel--%>
            <a href="${pageContext.request.contextPath}/products/import-excel"
               class="btn btn-primary mr-2">
                Import excel
            </a>

            <%-- save import product item--%>
            <%-- button link to form have id productItemsForm--%>
            <button type="submit" form="productItemsForm" class="btn btn-primary"
                    name="action" value="save">
                Save
            </button>
        </div>

    </form>

    <%--table list product search--%>
    <%--error if not found any product--%>
    <c:if test="${not empty error}">
        <div class="alert alert-warning">
                ${error}
        </div>
    </c:if>

    <%--table list product--%>

    <c:if test="${not empty products}">
        <table class="table table-bordered table-hover">
            <thead class="thead-dark">
            <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Description</th>
                <th></th>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${products}" var="product">
                <tr>
                    <td>${product.id}</td>
                    <td>${product.name}</td>
                    <td>${product.description}</td>
                        <%--add product to import product item--%>
                    <td>
                        <form
                                action="${pageContext.request.contextPath}/import-products"
                                method="post">
                                <%--set name to forward--%>
                            <input type="hidden" name="product-id"
                                   value="${product.id}">
                            <input type="hidden" name="product-name"
                                   value="${product.name}">
                            <button class="btn btn-success btn-sm" type="submit"
                                    name="action" value="add">
                                Import product
                            </button>
                        </form>
                    </td>
                </tr>
            </c:forEach>
            </tbody>
        </table>
    </c:if>


    <form id="productItemsForm" method="post"
          action="${pageContext.request.contextPath}/import-products">
        <!-- table import product items-->
        <div>
            <%--set value for product_items--%>
            <c:set var="product_items" value="${sessionScope.IMPORT_ITEMS}"/>
            <div class="table-responsive">
                <table class="table table-bordered table-hover table-sm">
                    <thead class="thead-dark text-center">
                    <tr>
                        <th style="width: 60px;">No.</th>
                        <th>Serial / IMEI</th>
                        <th style="width: 200px;">Product</th>
                        <th style="width: 80px;">Unit</th>
                        <th style="width: 140px;">Price</th>
                        <th style="width: 140px;">Total</th>
                        <th style="width: 90px;">Delete</th>
                    </tr>
                    </thead>
                    <c:if test="${not empty product_items}">
                        <tbody>
                            <%--loop product_items--%>
                        <c:forEach items="${product_items}" var="item"
                                   varStatus="status">
                            <tr>
                                    <%--STT--%>
                                <td class="text-center align-middle">
                                        ${status.index + 1}
                                </td>
                                    <%--serial--%>
                                <td>
                                    <input type="text" name="serial"
                                           class="form-control form-control-sm"
                                           required>
                                </td>
                                    <%--name product (item)--%>
                                <td class="align-middle">
                                    <input type="hidden"
                                           name="product-id"
                                           value="${item.id}">
                                        ${item.name}
                                </td>
                                    <%--unit--%>
                                <td
                                        class="text-center align-middle">
                                    1 Item
                                </td>
                                    <%--price--%>
                                <td>
                                    <input type="number"
                                           name="price"
                                           class="form-control form-control-sm text-right"
                                           required>
                                </td>

                                    <%--delete product item--%>
                                <td
                                        class="text-center align-middle">
                                        <%--method get--%>
                                    <a href="${pageContext.request.contextPath}/import-products?action=delete&index=${status.index}"
                                       class="btn btn-danger btn-sm">
                                        Delete
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                        </tbody>
                    </c:if>
                </table>
            </div>
        </div>
    </form>

    <!-- TOTAL -->
    <div class="font-weight-bold mb-3">
        Total payment:
        <span class="text-danger">
            ${totalPayment}
        </span>
    </div>

    <%--  message  --%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType}">
                ${message}
        </div>
    </c:if>

    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                    <%-- previous page --%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/user-list?pageNo=${pageNo - 1}&searchName=${param.searchName}&sortName=${param.sortName}">
                        Previous
                    </a>
                </li>

                    <%-- current page --%>
                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/user-list?pageNo=${i}&searchName=${param.searchName}&sortName=${param.sortName}">
                                ${i}
                        </a>
                    </li>
                </c:forEach>

                    <%-- next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/user-list?pageNo=${pageNo + 1}&searchName=${param.searchName}&sortName=${param.sortName}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</main>

</body>

</html>