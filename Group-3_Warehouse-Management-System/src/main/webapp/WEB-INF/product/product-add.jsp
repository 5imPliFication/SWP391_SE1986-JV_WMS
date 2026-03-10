<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <title>Add new product</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class=" main-content ">

    <div class="row justify-content-center">
        <div class="col-md-6">

            <div class="card shadow-sm">
                <div class="card-header bg-primary text-white">
                    <h4 class="mb-0">Add new product</h4>
                </div>

                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/products/add" method="post">

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text"
                                   name="productName"
                                   class="form-control"
                                   placeholder="Automatically generated with format: [Brand] [Model] [Chip] [Ram] [Storage] [Screen]"
                                   readonly>
                        </div>

                        <!-- Description -->
                        <div class="mb-3">
                            <label class="form-label">Description</label>
                            <input type="text"
                                   name="productDescription"
                                   class="form-control"
                                   placeholder="Enter description"
                                   required>
                        </div>

                        <!-- Image URL -->
                        <div class="mb-3">
                            <label class="form-label">Image URL</label>
                            <input type="text"
                                   name="imgUrl"
                                   class="form-control"
                                   placeholder="Enter image url"
                                   required>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand</label>
                            <select name="brandId" class="form-select">
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.id}">${b.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Category -->
                        <div class="mb-3">
                            <label class="form-label">Category</label>
                            <select name="categoryId" class="form-select">
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Model -->
                        <div class="mb-3">
                            <label class="form-label">Model</label>
                            <select name="modelId" class="form-select">
                                <c:forEach items="${models}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Chip -->
                        <div class="mb-3">
                            <label class="form-label">Chip</label>
                            <select name="chipId" class="form-select">
                                <c:forEach items="${chips}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Ram -->
                        <div class="mb-3">
                            <label class="form-label">Ram</label>
                            <select name="ramId" class="form-select">
                                <c:forEach items="${rams}" var="c">
                                    <option value="${c.id}">${c.size}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Storage -->
                        <div class="mb-3">
                            <label class="form-label">Storage</label>
                            <select name="storageId" class="form-select">
                                <c:forEach items="${storages}" var="c">
                                    <option value="${c.id}">${c.size}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Screen Size -->
                        <div class="mb-3">
                            <label class="form-label">Screen size</label>
                            <select name="sizeId" class="form-select">
                                <c:forEach items="${sizes}" var="c">
                                    <option value="${c.id}">${c.size}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Product Unit -->
                        <div class="mb-3">
                            <label class="form-label">Product Unit</label>
                            <select name="unitId" class="form-select">
                                <c:forEach items="${units}" var="c">
                                    <option value="${c.id}">${c.name}</option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Buttons -->
                        <div class="d-flex justify-content-between">
                            <a href="${pageContext.request.contextPath}/products"
                               class="btn btn-secondary">
                                Back to products list
                            </a>
                            <button type="submit" class="btn btn-primary">
                                Add product
                            </button>
                        </div>

                    </form>

                    <c:if test="${not empty sessionScope.successMessage}">
                        <div class="alert alert-info">
                                ${sessionScope.successMessage}
                        </div>
                        <c:remove var="successMessage" scope="session"/>
                    </c:if>

                    <c:if test="${not empty sessionScope.errorMessage}">
                        <div class="alert alert-info">
                                ${sessionScope.errorMessage}
                        </div>
                        <c:remove var="errorMessage" scope="session"/>
                    </c:if>

                </div>
            </div>
        </div>
    </div>
</div>

</body>
</html>