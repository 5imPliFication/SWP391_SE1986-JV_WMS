<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
    <title>Update Product: ${product.name}</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/css/design-system.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet"/>
    <style>
        body {
            background-color: #f4f7f6;
        }

        .main-content {
            padding: 20px;
        }

        .section-title {
            font-size: 1rem;
            font-weight: 700;
            color: #334155;
            margin-bottom: 1.5rem;
            border-left: 4px solid #4e73df;
            padding-left: 10px;
        }

        .card {
            border: none;
            border-radius: 12px;
            transition: all 0.3s;
        }

        .form-label {
            font-weight: 500;
            color: #64748b;
            font-size: 0.9rem;
        }

        .form-control, .form-select {
            border-radius: 8px;
            padding: 0.6rem 1rem;
            border: 1px solid #e2e8f0;
        }

        .form-control:focus {
            border-color: #4e73df;
            box-shadow: 0 0 0 3px rgba(78, 115, 223, 0.1);
        }

        .sticky-sidebar {
            position: sticky;
            top: 20px;
        }

        #preview {
            width: 100%;
            height: 250px;
            object-fit: contain;
            border-radius: 12px;
            background: #f8fafc;
            border: 1px solid #e2e8f0;
        }

        .readonly-field {
            background-color: #f8fafc !important;
            color: #64748b;
            font-weight: 600;
        }
    </style>
</head>
<body>
<jsp:include page="/WEB-INF/common/sidebar.jsp"/>

<div class="main-content">
    <form action="${pageContext.request.contextPath}/products/update" method="post" enctype="multipart/form-data">
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

        <div class="container-fluid">
            <h2 class="mb-5">Update Product Details</h2>

            <div class="row g-4">
                <div class="col-xl-9 col-lg-8">

                    <div class="card shadow-sm mb-4">
                        <div class="card-body p-4">
                            <h5 class="section-title">Core Specifications (Read-only)</h5>
                            <div class="row g-3">
                                <div class="col-md-4">
                                    <label class="form-label">Category</label>
                                    <input type="text" class="form-control readonly-field"
                                           value="${product.category.name}" readonly>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Brand</label>
                                    <input type="text" class="form-control readonly-field" value="${product.brand.name}"
                                           readonly>
                                </div>
                                <div class="col-md-4">
                                    <label class="form-label">Model</label>
                                    <input type="text" class="form-control readonly-field" value="${product.model.name}"
                                           readonly>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Chipset</label>
                                    <input type="text" class="form-control readonly-field" value="${product.chip.name}"
                                           readonly>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">RAM</label>
                                    <input type="text" class="form-control readonly-field" value="${product.ram.size}"
                                           readonly>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Storage</label>
                                    <input type="text" class="form-control readonly-field"
                                           value="${product.storage.size}" readonly>
                                </div>
                                <div class="col-md-3">
                                    <label class="form-label">Screen Size</label>
                                    <input type="text" class="form-control readonly-field" value="${product.size.size}"
                                           readonly>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="card shadow-sm">
                        <div class="card-body p-4">
                            <h5 class="section-title">Editable Content</h5>
                            <div class="mb-4">
                                <label class="form-label">Product Full Name</label>
                                <input type="text" class="form-control readonly-field fw-bold text-primary"
                                       value="${product.name}" readonly>
                            </div>
                            <div class="mb-0">
                                <label class="form-label">Product Description</label>
                                <textarea name="productDescription" class="form-control" rows="6"
                                          placeholder="Enter product description..."
                                          required>${product.description}</textarea>
                            </div>
                        </div>
                    </div>
                </div>

                <div class="col-xl-3 col-lg-4">
                    <div class="sticky-sidebar">

                        <div class="card shadow-sm mb-4">
                            <div class="card-body p-4">
                                <h5 class="section-title">Product Visual</h5>
                                <div class="text-center">
                                    <img id="preview" src="${product.imgUrl}" alt="Current Image" class="mb-3">
                                    <input type="file" name="imageFile" id="imageInput" class="form-control"
                                           accept="image/*">
                                    <small class="text-muted d-block mt-2">Leave empty to keep current image</small>
                                </div>
                            </div>
                        </div>

                        <div class="card shadow-sm mb-4">
                            <div class="card-body p-4">
                                <h5 class="section-title">Pricing & Status</h5>

                                <div class="mb-3">
                                    <label class="form-label">Selling Price (USD)</label>
                                    <fmt:formatNumber value="${product.currentPrice}" groupingUsed="false"
                                                      var="priceRaw"/>
                                    <input type="number" name="productCurrentPrice" class="form-control fw-bold"
                                           value="${priceRaw}" required>
                                </div>

                                <div class="mb-3">
                                    <label class="form-label">Display Status</label>
                                    <select name="productIsActive" class="form-select">
                                        <option value="true" ${product.isActive ? 'selected' : ''}>Active</option>
                                        <option value="false" ${!product.isActive ? 'selected' : ''}>Inactive</option>
                                    </select>
                                </div>

                                <div class="mb-0">
                                    <label class="form-label">Unit</label>
                                    <input type="text" class="form-control readonly-field" value="${product.unit.name}"
                                           readonly>
                                </div>
                            </div>
                        </div>

                        <div class="d-grid gap-2">
                            <button type="submit" class="btn btn-primary shadow-sm py-2">Update Product</button>
                            <a href="${pageContext.request.contextPath}/products?pageNo=${param.pageNo}&searchName=${param.searchName}&brandId=${param.brandId}&categoryId=${param.categoryId}&modelId=${param.modelId}&chipId=${param.chipId}&ramId=${param.ramId}&storageId=${param.storageId}&sizeId=${param.sizeId}&isActive=${param.isActive}"
                               class="btn btn-outline-secondary">
                                Cancel
                            </a>
                        </div>

                    </div>
                </div>
            </div>
        </div>
    </form>
</div>

<script>
    // Preview ảnh khi upload mới
    document.getElementById("imageInput").addEventListener("change", function (event) {
        const [file] = event.target.files;
        if (file) {
            const img = document.getElementById("preview");
            img.src = URL.createObjectURL(file);
        }
    });
</script>
</body>
</html>