<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update product</title>

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
                    <h4 class="mb-0">Update product</h4>
                </div>

                <div class="card-body">
                    <form action="${pageContext.request.contextPath}/products/update"
                          method="post"
                          enctype="multipart/form-data">

                        <input type="hidden" name="productId" value="${product.id}">
                        <input type="hidden" name="pageNo" value="${param.pageNo}">
                        <input type="hidden" name="searchName" value="${param.searchName}">
                        <input type="hidden" name="brandId" value="${param.brandId}">
                        <input type="hidden" name="categoryId" value="${param.categoryId}">
                        <input type="hidden" name="modelId" value="${param.modelId}">
                        <input type="hidden" name="chipId" value="${param.chipId}">
                        <input type="hidden" name="ramId" value="${param.ramId}">
                        <input type="hidden" name="storageId" value="${param.storageId}">
                        <input type="hidden" name="sizeId" value="${param.sizeId}">
                        <input type="hidden" name="isActive" value="${param.isActive}">

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name:</label>
                            <input type="text" class="form-control"
                                   value="${product.name}" readonly>
                        </div>

                        <!-- Category -->
                        <div class="mb-3">
                            <label class="form-label">Category:</label>
                            <input type="text" class="form-control"
                                   value="${product.category.name}" readonly>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand:</label>
                            <input type="text" class="form-control"
                                   value="${product.brand.name}" readonly>
                        </div>

                        <!-- Model -->
                        <div class="mb-3">
                            <label class="form-label">Model:</label>
                            <input type="text" class="form-control"
                                   value="${product.model.name}" readonly>
                        </div>

                        <!-- Chip -->
                        <div class="mb-3">
                            <label class="form-label">Chip:</label>
                            <input type="text" class="form-control"
                                   value="${product.chip.name}" readonly>
                        </div>

                        <!-- Ram -->
                        <div class="mb-3">
                            <label class="form-label">Ram:</label>
                            <input type="text" class="form-control"
                                   value="${product.ram.size}" readonly>
                        </div>

                        <!-- Storage -->
                        <div class="mb-3">
                            <label class="form-label">Storage:</label>
                            <input type="text" class="form-control"
                                   value="${product.storage.size}" readonly>
                        </div>

                        <!-- Screen Size -->
                        <div class="mb-3">
                            <label class="form-label">Screen Size:</label>
                            <input type="text" class="form-control"
                                   value="${product.size.size}" readonly>
                        </div>

                        <!-- Product Unit -->
                        <div class="mb-3">
                            <label class="form-label">Unit:</label>
                            <input type="text" class="form-control"
                                   value="${product.unit.name}" readonly>
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

                        <!-- Upload Image-->
                        <div class="mb-3">
                            <label class="form-label">Change Image</label>
                            <input type="file"
                                   name="imageFile"
                                   id="imageInput"
                                   class="form-control"
                                   accept="image/*"
                            >
                            <%--Image preview--%>
                            <img id="preview" src="${product.imgUrl}"
                                 style="margin-top:10px; width:120px; display:block; border-radius:6px;">

                        </div>

                        <!-- Current Price -->
                        <div class="mb-3">
                            <label class="form-label">Selling Price</label>
                            <%--format to avoid 1.5E8 (150,000,000) when display in input field--%>
                            <fmt:formatNumber value="${product.currentPrice}" groupingUsed="false" var="priceRaw"/>
                            <input type="number"
                                   name="productCurrentPrice"
                                   class="form-control"
                                   value="${priceRaw}"
                                   required>
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
                            <a href="${pageContext.request.contextPath}/products?pageNo=${param.pageNo}&searchName=${param.searchName}&brandId=${param.brandId}&categoryId=${param.categoryId}&modelId=${param.modelId}&chipId=${param.chipId}&ramId=${param.ramId}&storageId=${param.storageId}&sizeId=${param.sizeId}&isActive=${param.isActive}"
                               class="btn btn-secondary">
                                Back to products list
                            </a>
                            <button type="submit" class="btn btn-primary">
                                Update product
                            </button>
                        </div>

<%--                        <div>--%>
<%--                            <c:if test="${not empty sessionScope.successMessage}">--%>
<%--                                <div class="alert alert-success">--%>
<%--                                        ${sessionScope.successMessage}--%>
<%--                                </div>--%>
<%--                                <c:remove var="successMessage" scope="session"/>--%>
<%--                            </c:if>--%>
<%--                            <c:if test="${not empty sessionScope.errorMessage}">--%>
<%--                                <div class="alert alert-danger">--%>
<%--                                        ${sessionScope.errorMessage}--%>
<%--                                </div>--%>
<%--                                <c:remove var="errorMessage" scope="session"/>--%>
<%--                            </c:if>--%>
<%--                        </div>--%>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script>
    document.getElementById("imageInput").addEventListener("change", function (event) {
        const file = event.target.files[0];
        if (!file) return;

        const img = document.getElementById("preview");
        img.src = URL.createObjectURL(file);
    });
</script>

</body>
</html>