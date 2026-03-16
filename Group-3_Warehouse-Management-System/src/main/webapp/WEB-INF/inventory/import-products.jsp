<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Import Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
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

    <div class="card p-3 mb-3 shadow-sm">
        <!-- Row 1 -->
        <div class="row align-items-center mb-2">

            <div class="col-md-2">
                <label class="font-weight-bold mb-1">Purchase Request</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${sessionScope.purchaseCode}" readonly>
            </div>

            <div class="col-md-2">
                <label class="font-weight-bold mb-1">Created By</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${sessionScope.createdBy}" readonly>
            </div>

            <div class="col-md-2">
                <label class="font-weight-bold mb-1">Created At</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${sessionScope.createdAt}" readonly>
            </div>

            <!-- Actions -->
            <div class="col-md-6 d-flex justify-content-end align-items-end">

                <form action="${pageContext.request.contextPath}/inventory/import" method="post"
                      enctype="multipart/form-data" class="mb-0">

                    <input type="hidden" name="action" value="file">

                    <label class="btn btn-outline-primary btn-sm mb-0">
                        Import Excel
                        <input type="file" name="excelFile" accept=".xls,.xlsx" hidden
                               onchange="this.form.submit()">
                    </label>

                </form>

                <button type="submit" form="productItemsForm" class="btn btn-success btn-sm ml-2"
                        name="action" value="save" ${empty importItems ? 'disabled' : '' }>
                    Save
                </button>

            </div>
        </div>

        <!-- Row 2 -->
        <div class="row">
            <div class="col-md-12">
                <label class="font-weight-bold mb-1">Note</label>
                <textarea class="form-control form-control-sm bg-light" rows="2" readonly
                          style="resize:none;">${sessionScope.purchaseNote}</textarea>
            </div>
        </div>
    </div>

    <form id="productItemsForm" method="post"
          action="${pageContext.request.contextPath}/inventory/import">
        <input type="hidden" name="purchaseId" value="${sessionScope.purchaseId}">

        <div class="table-responsive">
            <table class="table table-bordered">
                <thead class="thead-dark">
                <tr>
                    <th style="width: 60px;" class="text-center">No.</th>
                    <th>Serial / IMEI</th>
                    <th style="width: 100px;" class="text-center">Quantity</th>
                    <th style="width: 100px;" class="text-center">Unit</th>
                    <th style="width: 160px;" class="text-center">Price</th>
                    <th style="width: 90px;" class="text-center">Action</th>
                </tr>
                </thead>

                <tbody>

                <c:choose>
                    <c:when test="${empty importItems}">
                        <tr>
                            <td colspan="6" class="text-center text-muted py-3">
                                No product items to import. Please go from Purchase Request
                                or upload an Excel file.
                            </td>
                        </tr>
                    </c:when>

                    <c:otherwise>

                        <c:set var="currentProductId" value="-1"/>
                        <c:set var="displayIndex" value="1"/>

                        <c:forEach items="${importItems}" var="item" varStatus="status">

                            <%-- create group header --%>
                            <c:if test="${currentProductId != item.productId}">
                                <%--save current productID--%>
                                <c:set var="currentProductId" value="${item.productId}"/>

                                <%-- count items in group --%>
                                <%--init count variable=0--%>
                                <c:set var="groupCount" value="0"/>
                                <%-- loop any item in list--%>
                                <c:forEach items="${importItems}" var="tmp">
                                    <%-- if have same product id -> count++--%>
                                    <c:if
                                            test="${tmp.productId == item.productId}">
                                        <c:set var="groupCount"
                                               value="${groupCount + 1}"/>
                                    </c:if>
                                </c:forEach>

                                <tr class="group-header"
                                    style="cursor:pointer;background:#f8f9fa;"
                                    onclick="toggleGroup('${item.productId}')">

                                    <td colspan="2" class="font-weight-bold">
                                        <span id="icon-${item.productId}">&#9658;</span>
                                            ${item.productName}
                                    </td>
                                        <%--quantity--%>
                                    <td class="text-center align-middle">
                                            ${groupCount}
                                    </td>

                                    <td class="text-center align-middle">
                                            ${item.unit}
                                    </td>

                                    <td onclick="event.stopPropagation();">
                                        <input type="number"
                                               class="form-control form-control-sm text-right master-price-input"
                                               value="${item.importPrice}"
                                               oninput="updateGroupPrice('${item.productId}', this.value)">
                                    </td>

                                    <td></td>
                                </tr>

                            </c:if>

                            <%-- detail row --%>
                            <tr class="sub-item item-group-${item.productId}"
                                style="display:none;">

                                <input type="hidden" name="productId"
                                       value="${item.productId}">
                                <input type="hidden" name="rowIndex"
                                       value="${status.index}">

                                <td class="text-center align-middle">
                                        ${displayIndex}
                                    <c:set var="displayIndex" value="${displayIndex + 1}"/>
                                </td>

                                <td>
                                    <input type="text" name="serial"
                                           class="form-control form-control-sm"
                                           value="${item.serial}">
                                </td>

                                <td class="text-center align-middle">
                                    1
                                </td>

                                <td class="text-center align-middle">
                                        ${item.unit}
                                </td>

                                <td class="text-center text-muted">
                                    -
                                    <input type="hidden" name="price"
                                           class="detail-price price-${item.productId}"
                                           value="<c:out value='${item.importPrice}'/>">
                                </td>

                                <td class="text-center align-middle">
                                    <button type="submit" name="delete"
                                            value="${status.index}"
                                            class="btn btn-outline-danger btn-sm"
                                            formaction="${pageContext.request.contextPath}/inventory/import?action=delete">
                                        Delete
                                    </button>
                                </td>

                            </tr>

                        </c:forEach>

                    </c:otherwise>

                </c:choose>

                </tbody>
            </table>
        </div>
    </form>


    <div class="d-flex justify-content-end align-items-center mt-3 p-3 bg-light rounded border">
        <h4 class="mb-0 mr-3">Total payment:</h4>
        <h3 class="mb-0 text-danger font-weight-bold"><span id="totalPayment">0</span> đ</h3>
    </div>

</main>

<script>
    function toggleGroup(productId) {
        let items = document.querySelectorAll('.item-group-' + productId);
        let icon = document.getElementById('icon-' + productId);
        let isHidden = true;
        if (items.length > 0) {
            isHidden = (items[0].style.display === 'none');
        }
        if (isHidden) {
            items.forEach(item => item.style.display = 'table-row');
            if (icon) icon.innerHTML = '&#9660;'; // down triangle
        } else {
            items.forEach(item => item.style.display = 'none');
            if (icon) icon.innerHTML = '&#9658;'; // right triangle
        }
    }

    // autofill price for all item when change price at header
    function updateGroupPrice(productId, price) {
        // get all price input of item con belong to productId
        let detailInputs = document.querySelectorAll('.price-' + productId);

        detailInputs.forEach(input => {
            input.value = price; // set price
        });

        // recalculate total payment after price change
        calcTotal();
    }

    // function to calculate total payment of all import product
    function calcTotal() {
        let total = 0;
        document.querySelectorAll('.detail-price').forEach(input => {
            total += Number(input.value || 0);
        });

        document.getElementById("totalPayment").innerText = total.toLocaleString('en-US');
    }

    // Chạy tính tổng lần đầu khi load trang
    window.onload = calcTotal;
</script>
</body>

</html>