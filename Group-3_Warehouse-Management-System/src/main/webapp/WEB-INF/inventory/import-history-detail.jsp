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
            <a href="${pageContext.request.contextPath}/inventory/import/history"
               class="btn btn-secondary">
                Back to List
            </a>
        </div>

        <%--header--%>
        <c:if test="${not empty history}">
            <div class="card mb-4">
                <div class="card-header bg-dark text-white">
                    <h5 class="mb-0">Receipt Information</h5>
                </div>
                <div class="card-body">
                    <div class="row">
                        <div class="col-md-3">
                            <p><strong>Receipt Code:</strong> ${history.receiptCode}</p>
                        </div>
                        <div class="col-md-3">
                            <p><strong>Received At:</strong>
                                <fmt:formatDate value="${history.receivedAt}" pattern="dd/MM/yyyy HH:mm:ss"/>
                            </p>
                        </div>
                        <div class="col-md-3">
                            <p><strong>Staff:</strong> ${history.warehouseName}</p>
                        </div>
                        <div class="col-md-3">
                            <p><strong>Total Quantity:</strong> ${history.totalQuantity}</p>
                        </div>
                    </div>
                    <div class="row mt-2">
                        <div class="col-12">
                            <p><strong>Note:</strong> ${history.note}</p>
                        </div>
                    </div>
                </div>
            </div>

            <%--items--%>
            <div class="table-responsive">
                <table class="table table-bordered table-hover">
                    <thead class="thead-light text-center">
                    <tr>
                        <th>Product Name</th>
                        <th style="width: 200px;">Expected Quantity</th>
                        <th style="width: 200px;">Actual Quantity</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:choose>
                        <c:when test="${not empty details}">
                            <c:forEach items="${details}" var="item">
                                <tr>
                                    <td>${item.productName}</td>
                                    <td class="text-center">${item.expectedQuantity}</td>
                                    <td class="text-center">${item.actualQuantity}</td>
                                </tr>
                            </c:forEach>
                        </c:when>
                        <c:otherwise>
                            <tr>
                                <td colspan="3" class="text-center text-muted">No items found for
                                    this receipt.
                                </td>
                            </tr>
                        </c:otherwise>
                    </c:choose>
                    </tbody>
                </table>
            </div>
        </c:if>

        <c:if test="${empty history}">
            <div class="alert alert-warning">
                Receipt not found.
            </div>
        </c:if>
    </div>
</main>
</body>

</html>