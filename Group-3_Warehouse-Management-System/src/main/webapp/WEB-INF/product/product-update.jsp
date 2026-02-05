<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update product</title>

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
                    <h4 class="mb-0">Update product</h4>
                </div>

                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/products/update" method="post">

                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="hidden" name="pageNo" value="${param.pageNo}">
                        <input type="hidden" name="searchName" value="${param.searchName}">
                        <input type="hidden" name="brandName" value="${param.categoryName}">
                        <input type="hidden" name="categoryName" value="${param.categoryName}">
                        <input type="hidden" name="isActive" value="${param.isActive}">

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text"
                                   name="productName"
                                   class="form-control"
                                   value="${product.name}"
                                   placeholder="Enter product name"
                                   required>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <input type="text"
                                   name="productDescription"
                                   class="form-control"
                                   value="${product.description}"
                                   placeholder="Enter description"
                                   required>
                        </div>

                        <!-- Image URL -->
                        <div class="mb-3">
                            <label class="form-label">Image URL</label>
                            <input type="text"
                                   name="imgUrl"
                                   class="form-control"
                                   value="${product.imgUrl}"
                                   placeholder="Enter image url"
                                   required>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand</label>
                            <select name="brandId" class="form-select">
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.id}"
                                            <c:if test="${product.brand.id == b.id}">selected</c:if>>
                                            ${b.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Category -->
                        <div class="mb-3">
                            <label class="form-label">Category</label>
                            <select name="categoryId" class="form-select">
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.category.id == c.id}">selected</c:if>>
                                            ${c.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <%--Status--%>
                        <div class="mb-3">
                            <label class="form-label">Status</label>
                            <select name="productIsActive" class="form-select">
                                <option value="true"  ${product.isActive == 'true' ? 'selected' : ''}>Active</option>
                                <option value="false" ${product.isActive == 'false' ? 'selected' : ''}>Inactive</option>
                            </select>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/products?pageNo=${param.pageNo}&searchName=${param.searchName}&brandName=${param.brandName}&categoryName=${param.categoryName}&isActive=${param.isActive}"
                               class="btn btn-secondary">
                                Back to products list
                            </a>
                            <button type="submit" class="btn btn-primary">
                                Update product
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
