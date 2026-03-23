<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Export History Detail</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/design-system.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
</head>

<body class="mt-4">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <div class="container-fluid">
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h2>Export History Detail</h2>
            <a href="${pageContext.request.contextPath}/inventory/export-history" class="btn btn-secondary">
                Back to List
            </a>
        </div>

        <%--header--%>
        <c:if test="${not empty order}">
            <div class="card mb-4">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">Export Information (Order ${order.orderCode})</h5>
                </div>
                <div class="card-body">
                    <div class="row mb-2">
                        <div class="col-md-6">
                            <p><strong>Customer:</strong> ${order.customerName}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Total Value:</strong>
                                    ${order.total}
                            </p>
                        </div>
                    </div>

                    <div class="row mb-2">
                        <div class="col-md-6">
                            <p><strong>Salesman:</strong> ${order.salesmanName}
                            </p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Order Date:</strong>
                                    ${order.createdAt}
                            </p>
                        </div>
                    </div>

                    <div class="row mb-2">
                        <div class="col-md-6">
                            <p><strong>Warehouse
                                Staff:</strong> ${order.warehouseStaffName}</p>
                        </div>
                        <div class="col-md-6">
                            <p><strong>Processed Date:</strong>
                                    ${order.processedAt}
                            </p>
                        </div>
                    </div>

                    <div class="row">
                        <div class="col-12">
                            <p><strong>Note:</strong> ${order.note}</p>
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
                        <th style="width: 150px;" class="text-center">Quantity</th>
                        <th style="width: 200px;" class="text-center">Price</th>
                        <th style="width: 200px;" class="text-center">Unit</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty order.items}">
                            <c:forEach items="${order.items}" var="item" varStatus="status">
                                <%-- group-header --%>
                                <tr class="group-header"
                                    style="cursor:pointer;background:#f8f9fa;"
                                    onclick="toggleGroup('${item.id}')">

                                    <td colspan="2" class="font-weight-bold">
                                        <span id="icon-${item.id}">&#9658;</span>
                                            ${item.productName}
                                    </td>
                                    <td class="text-center align-middle">
                                            ${item.quantity}
                                    </td>
                                    <td class="text-center align-middle">
                                            ${item.priceAtPurchase}
                                    </td><td class="text-center align-middle">
                                            ${item.unit}
                                    </td>
                                </tr>

                                <%-- detail rows --%>
                                <c:forEach items="${item.productItems}" var="productItem" varStatus="productStatus">
                                    <tr class="sub-item item-group-${item.id}" style="display:none;">
                                        <td colspan="5">
                                            <div class="d-flex align-items-center">

                                                <!-- STT -->
                                                <div style="width:60px;" class="text-center">
                                                        ${productStatus.index + 1}
                                                </div>

                                                <!-- Serial + button -->
                                                <div class="flex-grow-1">
                                                    <div class="input-group input-group-sm">
                                                        <input type="text" class="form-control"
                                                               value="${productItem.serial}" readonly>

                                                        <div class="input-group-append">
                                                            <a class="btn btn-outline-info"
                                                               href="${pageContext.request.contextPath}/products/items/update?productItemId=${productItem.id}&searchSerial=${productItem.serial}&pageNo=1&isActive=true"
                                                               title="View Detail">
                                                                View
                                                            </a>
                                                        </div>
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
                                <td colspan="4" class="text-center text-muted py-3">
                                    No items found for this export record.
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

<script src="https://code.jquery.com/jquery-3.5.1.slim.min.js"></script>
<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.5.3/dist/js/bootstrap.bundle.min.js"></script>
</body>

</html>
