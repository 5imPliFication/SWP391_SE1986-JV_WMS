<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Import History Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Import History Detail</h2>
            <a href="${pageContext.request.contextPath}/inventory/import-history"
               class="btn btn-secondary">
                Back to List
            </a>
        </div>

        <%--header--%>
        <c:if test="${not empty goodsReceipt}">
            <div class="card mb-4">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">Receipt Information</h5>
                </div>
                <div class="card-body">
                    <div class="row mb-2">
                        <div class="col-12">
                            <p><strong>Receipt Code:</strong> ${goodsReceipt.receiptCode}</p>
                        </div>
                    </div>

                    <div class="row mb-2">
                        <div class="col-md-6">
                            <p><strong>Requestor:</strong> ${goodsReceipt.requestorName}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Request Date:</strong>
                                <fmt:parseDate value="${goodsReceipt.requestDate}"
                                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedRequestDate"
                                               type="both"/>
                                <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                value="${parsedRequestDate}"/>
                            </p>
                        </div>
                    </div>

                    <div class="row mb-2">
                        <div class="col-md-6">
                            <p><strong>Warehouse:</strong> ${goodsReceipt.warehouseName}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Received Date:</strong>
                                <fmt:parseDate value="${goodsReceipt.receivedAt}"
                                               pattern="yyyy-MM-dd'T'HH:mm" var="parsedReceivedAt"
                                               type="both"/>
                                <fmt:formatDate pattern="dd/MM/yyyy HH:mm"
                                                value="${parsedReceivedAt}"/>
                            </p>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <p><strong>Note:</strong> ${goodsReceipt.note}</p>
                        </div>
                    </div>
                </div>
            </div>

            <%--items--%>
            <div class="table-responsive">
                <table class="table table-bordered">
                    <thead class="thead-dark">
                    <tr>
                        <th style="width: 60px;" class="text-center">No.</th>
                        <th>Product Name / Serial</th>
                        <th style="width: 200px;" class="text-center">Expected Quantity</th>
                        <th style="width: 200px;" class="text-center">Actual Quantity</th>
                        <th style="width: 150px;" class="text-center">Unit</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty goodsReceiptItems}">
                            <c:forEach items="${goodsReceiptItems}" var="item"
                                       varStatus="status">
                                <%-- group-header --%>
                                <tr class="group-header"
                                    style="cursor:pointer;background:#f8f9fa;"
                                    onclick="toggleGroup('${item.id}')">

                                    <td colspan="2" class="font-weight-bold">
                                        <span id="icon-${item.id}">&#9658;</span>
                                            ${item.productName}
                                    </td>
                                    <td class="text-center align-middle">
                                            ${item.expectedQuantity}
                                    </td>
                                    <td class="text-center align-middle">
                                            ${item.actualQuantity}
                                    </td>
                                    <td class="text-center align-middle">${item.unitName}</td>
                                </tr>

                                <%-- detail rows --%>
                                <c:forEach items="${item.productItems}"
                                           var="productItem" varStatus="productStatus">
                                    <tr class="sub-item item-group-${item.id}" style="display:none;">
                                        <td colspan="5">
                                            <div class="d-flex align-items-center gap-2">
            <span style="width:50px;" class="text-center">
                    ${productStatus.index + 1}
            </span>
                                                <div class="input-group input-group-sm">
                                                    <input type="text" class="form-control"
                                                           value="${productItem.serial}" readonly>
                                                    <div class="input-group-append">
                                                        <a class="btn btn-outline-info"
                                                           href="${pageContext.request.contextPath}/products/items/update?productItemId=${productItem.id}"
                                                           title="View Detail">
                                                            View
                                                        </a>
                                                    </div>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="5" class="text-center text-muted py-3">
                                    No items found for this receipt.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </c:if>
    </div>
</main>

<!-- Script for toggleGroup -->
<script>
    function toggleGroup(itemId) {
        let items = document.querySelectorAll('.item-group-' + itemId);
        let icon = document.getElementById('icon-' + itemId);
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
</script>

<!-- Scripts for Bootstrap collapse -->
<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>