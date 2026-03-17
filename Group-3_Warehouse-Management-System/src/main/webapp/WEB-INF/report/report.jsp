<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!DOCTYPE html>
<html lang="en">

<head>
    <meta charset="UTF-8">
    <title>Inventory Report</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/bootstrap.min.css">

    <style>
        .main-content {
            padding: 20px;
            max-width: 1200px;
            margin: auto;
        }

        .report-type {
            cursor: pointer;
            padding: 10px 20px;
            border: 1px solid #ccc;
            margin-right: 10px;
            border-radius: 5px;
        }

        .report-type.active {
            background-color: #007bff;
            color: white;
        }
    </style>
</head>

<body>

<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<main class="main-content">

    <h2 class="mb-4">Inventory Report</h2>

    <!-- ===== SELECT YEAR ===== -->
    <form action="${pageContext.request.contextPath}/inventory/report" method="get" class="mb-3">
        <input type="number" name="year" class="form-control"
               placeholder="Select year"
               value="${param.year}">
    </form>

    <div class="mb-3">
        Biểu đồ kết hợp xuất, nhập, tồn (hiển thị đủ 12 tháng trong năm) (Alo Tùng à e)
    </div>

    <!-- ===== TYPE SELECT ===== -->
    <div class="d-flex mb-3">
        <a href="?type=export&year=${param.year}"
           class="report-type ${param.type == 'export' ? 'active' : ''}">
            Export
        </a>

        <a href="?type=import&year=${param.year}"
           class="report-type ${param.type == 'import' ? 'active' : ''}">
            Import
        </a>

        <a href="?type=inventory&year=${param.year}"
           class="report-type ${param.type == 'inventory' ? 'active' : ''}">
            Inventory
        </a>
    </div>

    <!-- ===== CHART PLACEHOLDER ===== -->
    <div class="mb-4">
        <c:choose>
            <c:when test="${not empty chartData}">
                <!-- Sau này bạn render chart bằng JS -->
                <div class="alert alert-info text-center">
                    [Biểu đồ sẽ render tại đây]
                </div>
            </c:when>
            <c:otherwise>
                <div class="text-muted text-center">
                    Chọn loại báo cáo để hiển thị biểu đồ
                </div>
            </c:otherwise>
        </c:choose>
    </div>

    <div class="mb-4">
        Biểu đồ hiển thị đủ 12 tháng trong năm đã chọn,
        nếu không có dữ liệu thì hiển thị 0
    </div>

    <!-- ===== SELECT MONTH ===== -->
    <form action="${pageContext.request.contextPath}/inventory/report" method="get" class="mb-3">

        <input type="hidden" name="type" value="${param.type}">
        <input type="hidden" name="year" value="${param.year}">

        <input type="month" name="month"
               class="form-control"
               value="${param.month}">

    </form>

    <!-- ===== TABLE ===== -->
    <div class="table-responsive">

        <!-- HEADER -->
        <c:set var="tableHeader" scope="request">
            <tr>
                <th style="width: 80px;">STT</th>
                <th>Tên sản phẩm</th>
                <th style="width: 150px;">Số lượng</th>
            </tr>
        </c:set>

        <!-- BODY -->
        <c:set var="tableBody" scope="request">

            <c:choose>

                <c:when test="${not empty reportItems}">
                    <c:forEach items="${reportItems}" var="item" varStatus="loop">

                        <tr>
                            <td class="text-center">
                                    ${loop.index + 1}
                            </td>

                            <td>
                                    ${item.productName}
                            </td>

                            <td class="text-center">
                                    ${item.quantity}
                            </td>
                        </tr>

                    </c:forEach>
                </c:when>

                <c:otherwise>
                    <tr>
                        <td colspan="3" class="text-center text-muted py-3">
                            Chọn tháng để xem dữ liệu
                        </td>
                    </tr>
                </c:otherwise>

            </c:choose>

        </c:set>

        <!-- COMMON TABLE -->
        <jsp:include page="/WEB-INF/common/table.jsp"/>

    </div>

</main>

</body>

</html>