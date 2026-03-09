<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Import Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .main-content { padding: 20px; max-width: 1200px; margin: auto; }

        .group-header { background-color: #d1ecf1 !important; font-weight: bold; }

        .sub-item td:nth-child(2) { padding-left: 30px; color: #6c757d; }

        .master-price-input { border: 2px solid #007bff; font-weight: bold; color: #007bff; }
    </style>
</head>

<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <h2 class="mb-4">Import Products</h2>

    <%-- message --%>
    <c:if test="${not empty message}">
        <div class="alert alert-${messageType} alert-dismissible fade show" role="alert">
            ${message}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <div class="d-flex align-items-center justify-content-between mb-3">
        <div class="d-flex align-items-center">
            <div class="d-flex align-items-center mr-3">
                <label class="mb-0 mr-2 font-weight-bold">Purchase Request:</label>
                <input type="text" class="form-control form-control-sm"
                       value="${sessionScope.purchaseCode}" readonly
                       style="width: 150px; background-color: #e9ecef;">
            </div>
            <div class="d-flex align-items-center">
                <label class="mb-0 mr-2 font-weight-bold">Note:</label>
                <input type="text" class="form-control form-control-sm"
                       value="${sessionScope.purchaseNote}" readonly
                       style="width: 200px; background-color: #e9ecef;">
            </div>
        </div>

        <div class="d-flex align-items-center">
            <form action="${pageContext.request.contextPath}/inventory/import" method="post"
                  enctype="multipart/form-data" class="mb-0">
                <label class="btn btn-primary ml-2 mb-0">
                    <input type="hidden" name="action" value="file">
                    Import Excel
                    <input type="file" name="excelFile" accept=".xls,.xlsx" hidden onchange="this.form.submit()">
                </label>
            </form>
            <button type="submit" form="productItemsForm" class="btn btn-success ml-2" name="action" value="save"
                    ${empty importItems ? 'disabled' : ''}>
                Save Changes
            </button>
        </div>
    </div>

    <form id="productItemsForm" method="post" action="${pageContext.request.contextPath}/inventory/import">
        <input type="hidden" name="purchaseId" value="${sessionScope.purchaseId}">

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead class="thead-dark">
                <tr>
                    <th style="width: 60px;" class="text-center">No.</th>
                    <th style="width: 250px;">Product Name</th>
                    <th>Serial / IMEI</th>
                    <th style="width: 100px;" class="text-center">Quantity</th>
                    <th style="width: 160px;" class="text-right">Unit Price (VND)</th>
                    <th style="width: 90px;" class="text-center">Action</th>
                </tr>
                </thead>
                <tbody>
                <c:choose>
                    <c:when test="${empty importItems}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-3">
                                No product items to import. Please go from Purchase Request or upload an Excel file.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>
                        <%-- Tạo header nhóm theo productId --%>
                        <c:set var="currentProductId" value="-1"/>
                        <c:set var="displayIndex" value="${(startIndex != null ? startIndex : 0) + 1}"/>

                        <c:forEach items="${importItems}" var="item" varStatus="status">
                            <%-- Khi gặp productId mới thì in header nhóm --%>
                            <c:if test="${currentProductId != item.productId}">
                                <c:set var="currentProductId" value="${item.productId}"/>

                                <%-- Đếm số item thuộc productId này để hiển thị Qty --%>
                                <c:set var="groupCount" value="0"/>
                                <c:forEach items="${importItems}" var="tmp">
                                    <c:if test="${tmp.productId == item.productId}">
                                        <c:set var="groupCount" value="${groupCount + 1}"/>
                                    </c:if>
                                </c:forEach>

                                <tr class="group-header">
                                    <td colspan="2" class="align-middle">
                                        ${item.productName}
                                    </td>
                                    <td class="align-middle text-muted"><em></em></td>
                                    <td class="text-center align-middle">
                                        ${groupCount} Item
                                    </td>
                                    <td>
                                        <input type="number"
                                               class="form-control form-control-sm text-right master-price-input"
                                               oninput="updateGroupPrice('${item.productId}', this.value)">
                                    </td>
                                    <td></td>
                                </tr>
                            </c:if>

                            <%-- Row chi tiết --%>
                            <tr class="sub-item">
                                <input type="hidden" name="productId" value="${item.productId}">
                                <td class="text-center align-middle">
                                    ${displayIndex}
                                    <c:set var="displayIndex" value="${displayIndex + 1}"/>
                                </td>
                                <td class="align-middle">
                                    ${item.productName}
                                </td>
                                <td>
                                    <input type="text" name="serial"
                                           class="form-control form-control-sm"
                                           value="${item.serial}"
                                           required>
                                </td>
                                <td class="text-center align-middle">1</td>
                                <td>
                                    <input type="number" readonly
                                           name="price"
                                           class="form-control form-control-sm text-right detail-price price-${item.productId}"
                                           value="<c:out value='${item.importPrice}'/>">
                                </td>
                                <td class="text-center align-middle">
                                    <a href="${pageContext.request.contextPath}/inventory/import?action=delete&index=${(startIndex != null ? startIndex : 0) + status.index}"
                                       class="btn btn-outline-danger btn-sm">
                                        Delete
                                    </a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </form>

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

                <c:forEach begin="1" end="${totalPages}" var="i">
                    <li class="page-item ${i == pageNo ? 'active' : ''}">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/inventory/import?pageNo=${i}">
                            ${i}
                        </a>
                    </li>
                </c:forEach>

                <%--next page--%>
                <li class="page-item ${pageNo == totalPages ? 'disabled' : ''}">
                    <a class="page-link"
                       href="${pageContext.request.contextPath}/inventory/import?pageNo=${pageNo + 1}">
                        Next
                    </a>
                </li>
            </ul>
        </nav>
    </c:if>

    <div class="d-flex justify-content-end align-items-center mt-3 p-3 bg-light rounded border">
        <h4 class="mb-0 mr-3">Total payment:</h4>
        <h3 class="mb-0 text-danger font-weight-bold"><span id="totalPayment">0</span> đ</h3>
    </div>

</main>

<script>
    // Hàm cập nhật giá cho tất cả các item con khi sửa giá ở Header
    function updateGroupPrice(productId, newPrice) {
        // Lấy tất cả các input giá của item con thuộc productId đó
        let detailInputs = document.querySelectorAll('.price-' + productId);

        detailInputs.forEach(input => {
            input.value = newPrice; // Gán giá trị mới
        });

        // Tính lại tổng tiền sau khi giá thay đổi
        calcTotal();
    }

    // Hàm tính tổng tiền toàn bộ phiếu nhập
    function calcTotal() {
        let total = 0;
        // Chỉ lấy các input thuộc class 'detail-price' (các máy con) để cộng, bỏ qua ô nhập Master
        document.querySelectorAll('.detail-price').forEach(input => {
            total += Number(input.value || 0);
        });

        // Format số thành chuỗi có dấu phẩy (vd: 25,000,000)
        document.getElementById("totalPayment").innerText = total.toLocaleString('en-US');
    }

    // Chạy tính tổng lần đầu khi load trang
    window.onload = calcTotal;
</script>
</body>

</html>