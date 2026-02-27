<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

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

    <h2 class="mb-4">Import Products</h2>

    <div class="d-flex align-items-center mb-3">
        <%-- search product by name --%>
        <form class="form-inline" action="${pageContext.request.contextPath}" method="get">
            <input type="text" name="name" class="form-control mr-2"
                   placeholder="Search name product" value="${param.name}">
            <button type="submit" class="btn btn-primary mr-2" name="action" value="search">
                Search
            </button>
        </form>

        <%-- link to create new product --%>
        <a href="${pageContext.request.contextPath}/products/add" class="btn btn-primary ml-2">
            Add new products
        </a>

        <%-- import excel --%>
        <form action="${pageContext.request.contextPath}/inventory/import" method="post"
              enctype="multipart/form-data" class="ml-2">

            <label class="btn btn-primary mb-0">
                <input type="hidden" name="action" value="file">
                Import Excel
                <input type="file" name="excelFile" accept=".xls,.xlsx" hidden
                       onchange="this.form.submit()">
            </label>
        </form>

        <%-- save --%>
        <button type="submit" form="productItemsForm" class="btn btn-primary ml-2"
                name="action" value="save">
            Save
        </button>
    </div>


    <%--table list product search--%>
    <%--error if not found any product--%>
    <c:if test="${not empty error}">
        <div class="alert alert-warning">
                ${error}
        </div>
    </c:if>

    <%--table list product when search by name--%>
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
                        <form action="${pageContext.request.contextPath}/inventory/import"
                              method="post">
                                <%--set name to forward--%>
                            <input type="hidden" name="productId"
                                   value="${product.id}">
                            <input type="hidden" name="productName"
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

    <%--table import product items--%>
    <form id="productItemsForm" method="post"
          action="${pageContext.request.contextPath}/inventory/import">
        <div>
            <%--set value for product_items in scope from paged request attribute--%>
            <c:set var="productItem" value="${importItems}"/>
            <div class="table-responsive">
                <%-- if productItem not empty -> display list product item need import --%>
                <c:if test="${not empty productItem}">
                <table class="table table-bordered table-hover table-sm">
                    <thead class="thead-dark text-center">
                    <tr>
                        <th style="width: 60px;">No.</th>
                        <th style="width: 200px;">Product</th>
                        <th>Serial / IMEI</th>
                        <th style="width: 80px;">Unit</th>
                        <th style="width: 140px;">Price</th>
                        <th style="width: 90px;">Delete</th>
                    </tr>
                    </thead>
                        <tbody>
                            <%--loop productItem--%>
                        <c:forEach items="${productItem}" var="item" varStatus="status">
                            <tr>
                                    <%--STT--%>
                                <td class="text-center align-middle">
                                        ${status.index + 1}
                                </td>

                                    <%--name product (item)--%>
                                <td class="align-middle">
                                    <input type="hidden"
                                           name="productId"
                                           value="${item.productId}">
                                        ${item.productName}
                                </td>
                                    <%--serial--%>
                                <td>
                                    <input type="text" name="serial"
                                           class="form-control form-control-sm"
                                           value="${item.serial}"
                                           required>
                                </td>
                                    <%--unit--%>
                                <td
                                        class="text-center align-middle">
                                    1 Item
                                </td>
                                    <%--price--%>
                                    <%--groupingUsed: prevent "," between numbers--%>
                                <td>
                                    <input type="number"
                                           name="price"
                                           class="form-control form-control-sm text-right"
                                           value="<fmt:formatNumber value='${item.importPrice}' groupingUsed='false' />"
                                           oninput="calcTotal()"
                                           required>
                                </td>
                                    <%--delete product item--%>
                                <td
                                        class="text-center align-middle">
                                        <%--index of row in importItems--%>
                                    <a href="${pageContext.request.contextPath}/inventory/import?action=delete&index=${(pageNo - 1) * 10 + status.index}"
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
    <div class="text-dark font-weight-bolder">
        Total payment <span id="totalPayment" class="text-danger">0</span>
    </div>

    <script>
        function calcTotal() {
            let total = 0;
            document.querySelectorAll('input[name="price"]').forEach(input => {
                total += Number(input.value || 0);
            });
            document.getElementById("totalPayment").innerText = total.toLocaleString();
        }
    </script>


    <%-- message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType}">
                ${message}
        </div>
    </c:if>

    <%-- pagination --%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                    <%-- previous page--%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/import?pageNo=${pageNo - 1}&searchName=${param.searchName}">
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
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}&searchName=${param.searchName}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- alway display last page --%>
                        <c:when test="${i == totalPages}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}&searchName=${param.searchName}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display between page--%>
                        <c:when test="${i >= left && i <= right}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}&searchName=${param.searchName}">
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
                       href="${pageContext.request.contextPath}/inventory/import?pageNo=${pageNo + 1}&searchName=${param.searchName}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</main>

</body>

</html>