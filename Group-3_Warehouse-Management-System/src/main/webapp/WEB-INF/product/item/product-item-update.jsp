<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update product item</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body class="d-flex justify-content-center align-items-center vh-100 bg-light">
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="container mt-5 main-content ">
    <div class="row justify-content-center">
        <div class="col-md-6">

            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Update product item</h4>
                </div>

                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/products/items/update" method="post">

                        <input type="hidden" name="productItemId" value="${productItem.id}">
                        <input type="hidden" name="pageNo" value="${param.pageNo}">
                        <input type="hidden" name="searchSerial" value="${param.searchSerial}">
                        <input type="hidden" name="isActive" value="${param.isActive}">

                        <!-- Serial Number -->
                        <div class="mb-3">
                            <label class="form-label">Serial Number</label>
                            <input type="text"
                                   name="productItemSerial"
                                   class="form-control"
                                   value="${productItem.serial}"
                                   style="background-color: #adb5bd;"
                                   readonly>
                        </div>

                        <!-- Imported Price -->
                        <div class="mb-3">
                            <label class="form-label">Imported Price</label>
                            <input type="text"
                                   name="productItemImportedPrice"
                                   class="form-control"
                                   value="<fmt:formatNumber value='${productItem.importedPrice}' type='number' groupingUsed='true' /> đ"
                                   style="background-color: #adb5bd;"
                                   readonly>
                        </div>

                        <!-- Current Price -->
                        <div class="mb-3">
                            <label class="form-label">Current Price</label>
                            <input type="number"
                                   name="productItemCurrentPrice"
                                   class="form-control"
                                   value="${productItem.currentPrice}"
                                   placeholder="Input current price"
                                   required>
                        </div>

                        <%--Status--%>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select name="productItemIsActive" class="form-select">
                                <option value="true"  ${productItem.isActive == 'true' ? 'selected' : ''}>Available
                                </option>
                                <option value="false" ${productItem.isActive == 'false' ? 'selected' : ''}>Unavailable
                                </option>
                            </select>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/products/items?productId=${productItem.productId}&pageNo=${param.pageNo}&searchSerial=${param.searchSerial}&isActive=${param.isActive}"
                               class="btn btn-secondary">
                                Back to product item list
                            </a>

                            <button type="submit" class="btn btn-primary">
                                Update Item
                            </button>
                        </div>

                        <div>
                            <c:if test="${not empty sessionScope.successMessage}">
                                <div class="alert alert-success">
                                        ${sessionScope.successMessage}
                                </div>
                                <c:remove var="successMessage" scope="session"/>
                            </c:if>
                            <c:if test="${not empty sessionScope.errorMessage}">
                                <div class="alert alert-danger">
                                        ${sessionScope.errorMessage}
                                </div>
                                <c:remove var="errorMessage" scope="session"/>
                            </c:if>
                        </div>

                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>