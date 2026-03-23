<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Assign Serial Numbers - Export Product</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">
    <style>
        .group-header:hover {
            background-color: #e9ecef !important;
        }

        .sub-item td {
            background-color: #ffffff;
        }
    </style>
</head>

<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>
<main class="main-content">
    <div class="mb-4">
        <div class="d-flex justify-content-between align-items-center">
            <h2 class="mb-0">Export Products</h2>
            <a href="${pageContext.request.contextPath}/warehouse/order/detail?id=${order.id}"
               class="btn btn-outline-secondary">
                <i class="fas fa-arrow-left mr-2"></i>Back to Order
            </a>
        </div>
    </div>

    <%-- message --%>
    <c:if test="${not empty error}">
        <div class="alert alert-danger alert-dismissible fade show" role="alert">
                ${error}
            <button type="button" class="close" data-dismiss="alert" aria-label="Close">
                <span aria-hidden="true">&times;</span>
            </button>
        </div>
    </c:if>

    <div class="card p-3 mb-4 shadow-sm">

        <!-- Row 1 -->
        <div class="row align-items-center mb-3">

            <div class="col-md-6">
                <label class="font-weight-bold mb-1 d-block">Order Code</label>
                <input type="text" class="form-control form-control-sm bg-light d-inline-block"
                       value="${order.orderCode}" readonly>
            </div>

            <!-- Created By -->
            <div class="col-md-3 mb-2 mb-md-0">
                <label class="font-weight-bold mb-1">Created By</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${order.salesmanName}" readonly>
            </div>

            <!-- Created At -->
            <div class="col-md-3 mb-2 mb-md-0">
                <label class="font-weight-bold mb-1">Created At</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${order.createdAt}" readonly>
            </div>

        </div>

        <!-- Row 2 -->
        <div class="row mb-3">

            <!-- Customer Name -->
            <div class="col-md-6">
                <label class="font-weight-bold mb-1">Customer Name</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${order.customerName}" readonly>
            </div>

            <!-- Note -->
            <div class="col-md-6">
                <label class="font-weight-bold mb-1">Note</label>
                <input type="text" class="form-control form-control-sm bg-light"
                       value="${order.note}" readonly>
            </div>

        </div>

        <!-- Row 3 (Button bottom right) -->
        <div class="row">
            <div class="col-md-12 d-flex justify-content-end">
                <button type="submit" form="assignForm"
                        class="btn btn-success btn-sm"
                ${empty order.items ? 'disabled' : '' }>
                    Export
                </button>
            </div>
        </div>

    </div>

    <form id="assignForm" method="post" action="${pageContext.request.contextPath}/export">
        <input type="hidden" name="orderId" value="${order.id}">

        <div class="table-responsive">
            <table class="table table-bordered mb-0">
                <thead class="thead-dark">
                <tr>
                    <th style="width: 60px;" class="text-center">No.</th>
                    <th>Serial Number</th>
                    <th style="width: 100px;" class="text-center">Quantity</th>
                    <th style="width: 150px;" class="text-center">Unit</th>
                </tr>
                </thead>

                <tbody>
                <c:choose>
                    <c:when test="${empty order.items}">
                        <tr>
                            <td colspan="4" class="text-center text-muted py-4">
                                No product items found for this order.
                            </td>
                        </tr>
                    </c:when>
                    <c:otherwise>

                        <c:set var="displayIndex" value="1"/>

                        <c:forEach items="${order.items}" var="item">
                            <!-- Group Header for the Order Item -->
                            <tr class="group-header" style="cursor:pointer;background:#f8f9fa;"
                                onclick="toggleGroup('${item.id}')">

                                <td colspan="2" class="font-weight-bold align-middle">
                                    <span id="icon-${item.id}">&#9658;</span>
                                        ${item.productName}
                                </td>

                                <td class="text-center align-middle font-weight-bold">
                                        ${item.quantity}
                                </td>

                                <td class="text-center align-middle">
                                        ${item.unit != null ? item.unit : 'Pieces'}
                                </td>
                            </tr>

                            <!-- Detail Rows for Serial Input -->
                            <c:forEach begin="1" end="${item.quantity}" var="i">
                                <tr class="sub-item item-group-${item.id}"
                                    style="display:none;">

                                    <td class="text-center align-middle text-muted">
                                            ${displayIndex}
                                        <c:set var="displayIndex" value="${displayIndex + 1}"/>
                                    </td>

                                    <td>
                                        <input type="text" name="serial"
                                               class="form-control form-control-sm serial-input"
                                               placeholder="Input Serial / IMEI" required>
                                    </td>

                                    <td class="text-center align-middle">
                                        1
                                    </td>

                                    <td class="text-center align-middle text-muted">
                                            ${item.unit}
                                    </td>
                                </tr>
                            </c:forEach>
                        </c:forEach>
                    </c:otherwise>
                </c:choose>
                </tbody>
            </table>
        </div>
    </form>
</main>

<script src="${pageContext.request.contextPath}/static/js/bootstrap.bundle.min.js"></script>
<script src="https://unpkg.com/html5-qrcode"></script>
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

    let html5QrCode;
    let currentInput = null;

    // Bắt sự kiện focus vào input
    document.addEventListener("DOMContentLoaded", function () {
        document.querySelectorAll(".serial-input").forEach(input => {
            input.addEventListener("focus", function () {
                currentInput = this;
            });
        });
    });

    function startScan() {
        if (!currentInput) {
            alert("Hãy click vào ô cần nhập trước!");
            return;
        }

        const reader = document.getElementById("reader");
        reader.style.display = "block";

        html5QrCode = new Html5Qrcode("reader");

        html5QrCode.start(
            {facingMode: "environment"},
            {
                fps: 10,
                qrbox: 250
            },
            onScanSuccess
        );
    }

    function onScanSuccess(decodedText) {
        // Fill vào input đang chọn
        if (currentInput) {
            currentInput.value = decodedText;
        }

        // Dừng camera
        html5QrCode.stop().then(() => {
            document.getElementById("reader").style.display = "none";
        });

        // 👉 Auto focus sang ô tiếp theo (xịn hơn)
        moveToNextInput();
    }

    function moveToNextInput() {
        const inputs = Array.from(document.querySelectorAll(".serial-input"));
        const index = inputs.indexOf(currentInput);

        if (index !== -1 && index < inputs.length - 1) {
            inputs[index + 1].focus();
        }
    }
</script>
</body>

</html>