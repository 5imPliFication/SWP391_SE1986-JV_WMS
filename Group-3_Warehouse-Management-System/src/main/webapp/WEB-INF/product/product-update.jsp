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

                        <!-- Product Name -->
                        <div class="mb-3">
                            <label class="form-label">Product Name</label>
                            <input type="text"
                                   name="productName"
                                   class="form-control"
                                   value="${product.name}"
                                   placeholder="Automatically generated with format: [Brand] [Model] [Chip] [Ram] [Storage] [Screen]"
                                   readonly>
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

                        <!-- Category -->
                        <div class="mb-3">
                            <label class="form-label">Category</label>
                            <select name="categoryId" class="form-select">
                                <option value="">Select Category</option>
                                <c:forEach items="${categories}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.category.id == c.id}">selected</c:if>>
                                            ${c.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Brand -->
                        <div class="mb-3">
                            <label class="form-label">Brand</label>
                            <select id="brandSelect" name="brandId" class="form-select">
                                <option value="">Select Brand</option>
                                <c:forEach items="${brands}" var="b">
                                    <option value="${b.id}"
                                            <c:if test="${product.brand.id == b.id}">selected</c:if>>
                                            ${b.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Model -->
                        <div class="mb-3">
                            <label class="form-label">Model</label>
                            <select id="modelSelect" name="modelId" class="form-select">
                                <option value="">Select Model</option>
                                <c:forEach items="${models}" var="c">
                                    <option value="${c.id}" data-brand="${c.brand.id}"
                                            <c:if test="${product.model.id == c.id}">selected</c:if>>
                                            ${c.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Chip -->
                        <div class="mb-3">
                            <label class="form-label">Chip</label>
                            <select name="chipId" class="form-select">
                                <option value="">Select Chip</option>
                                <c:forEach items="${chips}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.chip.id == c.id}">selected</c:if>>
                                            ${c.name}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Ram -->
                        <div class="mb-3">
                            <label class="form-label">Ram</label>
                            <select name="ramId" class="form-select">
                                <option value="">Select Ram</option>
                                <c:forEach items="${rams}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.ram.id == c.id}">selected</c:if>>
                                            ${c.size}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Storage -->
                        <div class="mb-3">
                            <label class="form-label">Storage</label>
                            <select name="storageId" class="form-select">
                                <option value="">Select Storage</option>
                                <c:forEach items="${storages}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.storage.id == c.id}">selected</c:if>>
                                            ${c.size}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Screen Size -->
                        <div class="mb-3">
                            <label class="form-label">Screen Size</label>
                            <select name="sizeId" class="form-select">
                                <option value="">Select Screen Size</option>
                                <c:forEach items="${sizes}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.size.id == c.id}">selected</c:if>>
                                            ${c.size}
                                    </option>
                                </c:forEach>
                            </select>
                        </div>

                        <!-- Product Unit -->
                        <div class="mb-3">
                            <label class="form-label">Product Unit</label>
                            <select name="unitId" class="form-select">
                                <option value="">Select Unit</option>
                                <c:forEach items="${units}" var="c">
                                    <option value="${c.id}"
                                            <c:if test="${product.unit.id == c.id}">selected</c:if>>
                                            ${c.name}
                                    </option>
                                </c:forEach>
                            </select>
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
                            <a href="${pageContext.request.contextPath}/products"
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

<script>
    document.getElementById("brandSelect").addEventListener("change", function () {
        let brandId = this.value;
        let options = document.querySelectorAll("#modelSelect option");
        options.forEach(opt => {
            let modelBrand = opt.dataset.brand;
            if (!modelBrand) return;
            if (brandId === "" || modelBrand === brandId) {
                opt.style.display = "";
            } else {
                opt.style.display = "none";
            }
        });
    });

    document.getElementById("imageInput").addEventListener("change", function (event) {
        const file = event.target.files[0];
        if (!file) return;

        const img = document.getElementById("preview");
        img.src = URL.createObjectURL(file);
    });
</script>

</body>
</html>