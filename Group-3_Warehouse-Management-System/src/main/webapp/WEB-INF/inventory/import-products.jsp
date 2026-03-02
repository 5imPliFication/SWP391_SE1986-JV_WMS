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
    <div class="d-flex align-items-center justify-content-between mb-3">

        <div class="d-flex align-items-center">
            <c:if test="${not empty purchaseCode}">
                <div class="d-flex align-items-center mr-3">
                    <label class="mb-0 mr-2 font-weight-bold">Purchase Request:</label>
                    <input type="text" class="form-control form-control-sm"
                           value="${purchaseCode}" readonly
                           style="width: 150px; background-color: #e9ecef;">
                </div>

                <div class="d-flex align-items-center">
                    <label class="mb-0 mr-2 font-weight-bold">Note:</label>
                    <input type="text" class="form-control form-control-sm"
                           value="${purchaseNote}" readonly
                           style="width: 200px; background-color: #e9ecef;">
                </div>
            </c:if>
        </div>

        <div class="d-flex align-items-center">
            <%-- import excel --%>
            <form action="${pageContext.request.contextPath}/inventory/import" method="post"
                  enctype="multipart/form-data" class="mb-0">
                <label class="btn btn-primary ml-2 mb-0">
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
    </div>


    <%--table import product items--%>
    <form id="productItemsForm" method="post"
          action="${pageContext.request.contextPath}/inventory/import">
        <input type="hidden" name="purchaseId" value="${purchaseId}">
        <div>
            <%--set value for product_items in scope from paged request attribute--%>
            <c:set var="productItem" value="${importItems}"/>
            <div class="table-responsive">
                <%-- if productItem not empty -> display list product item need
                    import --%>
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
                    <c:forEach items="${productItem}" var="item"
                               varStatus="status">
                        <tr>
                                <%--STT--%>
                            <td class="text-center align-middle">
                                    ${status.index + 1}
                            </td>

                                <%--name product (item)--%>
                            <td class="align-middle">
                                <input type="hidden" name="productId" value="${item.productId}">
                                    ${item.productName}
                            </td>
                                <%--serial--%>
                            <td>
                                <input type="text" name="serial" class="form-control form-control-sm"
                                       value="${item.serial}"
                                       required>
                            </td>
                                <%--unit--%>
                            <td class="text-center align-middle">
                                1 Item
                            </td>
                                <%--price--%>
                                <%--groupingUsed: prevent "," between numbers--%>
                            <td>
                                <input type="number" name="price"
                                       class="form-control form-control-sm text-right"
                                       value="<fmt:formatNumber value='${item.importPrice}' groupingUsed='false' />"
                                       oninput="calcTotal()"
                                       required>
                            </td>
                                <%--delete product item--%>
                            <td class="text-center align-middle">
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

        <%-- delete value after display --%>
        <c:remove var="message" scope="session"/>
        <c:remove var="messageType" scope="session"/>
    </c:if>

    <%-- pagination --%>
    <c:if test="${totalPages > 1}">
        <nav class="mt-3">
            <ul class="pagination justify-content-center">
                    <%-- previous page--%>
                <li class="page-item ${pageNo == 1 ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/import?pageNo=${pageNo - 1}">
                        Previous
                    </a>
                </li>

                <c:set var="left" value="${pageNo - 2}"/>
                <c:set var="right" value="${pageNo + 2}"/>

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <c:choose>
                        <%-- alway display first page --%>
                        <c:when test="${i == 1}">
                            <li
                                    class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- alway display last page --%>
                        <c:when test="${i == totalPages}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}">
                                        ${i}
                                </a>
                            </li>
                        </c:when>
                        <%-- display between page--%>
                        <c:when test="${i >= left && i <= right}">
                            <li class="page-item ${i == pageNo ? 'active' : ''}">
                                <a class="page-link"
                                   href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}">
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
                <li
                        class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/import?pageNo=${pageNo + 1}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>
</main>

</body>

</html>